package game.main;

import entity.Continent;
import entity.Country;
import entity.Player;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * This model class carries all the data structures for the application
 *
 * @author Dhaval
 */

@SuppressWarnings("deprecation")
public class MainModel extends Observable {
    private ArrayList<String> playerNames = new ArrayList<>();
    private HashMap<String, Player> players = new HashMap<>();
    private HashMap<String, Country> countries = new HashMap<>();
    private HashMap<String, Continent> continents = new HashMap<>();
    private Country country;
    private int armiesToAssign;
    private double armiesAvailableToAssign = -1;

    /**
     * Constructor used to extract the data from the map
     */
    MainModel() { }

    /**
     * To initialize all the players playing the game
     *
     * @param playerList list of players
     */
    void setPlayers(ArrayList<Player> playerList) {
        for (Player player : playerList) {
            this.players.put(player.getName(), player);
            this.playerNames.add(player.getName());
        }
    }

    void setMapContent(ArrayList<Country> countries, ArrayList<Continent> continents) {
        for (Country country : countries) {
            this.countries.put(country.getName(), country);
        }
        for (Continent continent : continents) {
            this.continents.put(continent.getName(), continent);
        }
    }

    ArrayList<String> getPlayerNames() {
        return this.playerNames;
    }

    /**
     * Fetch the Player object by feeding in a name
     *
     * @param name name of the player
     * @return player
     */
    public Player getPlayer(String name) {
        return this.players.get(name);
    }

    /**
     * Update the state of the player and notify all the observers
     *
     * @param name
     * @param player
     */
    public void updatePlayer(String name, Player player) {
        this.players.put(name, player);
        setChanged();
        notifyObservers(Player.CHANGE_PLAYER);
    }

    /**
     * Assign Country to each player in the game
     */
    void assignCountry() {
        int playerIndex = 0;

        for (Map.Entry<String, Country> entry : countries.entrySet()) {
            String playerName = this.playerNames.get(playerIndex);
            Player player = this.players.get(playerName);
            player.assignCountry(entry.getKey());
            this.players.put(playerName, player);
            playerIndex++;
            if (playerIndex == this.playerNames.size())
                playerIndex = 0;
        }
    }

    /**
     * Assign armies to the countries owned by the player
     */
    void assignArmies() {
        int noOfPlayers = this.playerNames.size();
        Random random = new Random();

        switch (noOfPlayers) {
            case 2:
                armiesToAssign = 40;
                break;
            case 3:
                armiesToAssign = 35;
                break;
            case 4:
                armiesToAssign = 30;
                break;
            case 5:
                armiesToAssign = 25;
                break;
            case 6:
                armiesToAssign = 20;
                break;
        }

        for (int i = 0; i < armiesToAssign; i++) {
            for (String thePlayer : playerNames) {
                Player player = this.players.get(thePlayer);
                HashMap<String, Integer> countriesConquered = player.getCountries();

                Object[] entries = player.getCountries().keySet().toArray();

                int randomCountryIndex = random.nextInt(entries.length);
                String randomCountry = (String) entries[randomCountryIndex];
                int noOfArmies = countriesConquered.get(randomCountry);

                player.setArmies(randomCountry, ++noOfArmies);
                this.players.put(thePlayer, player);
            }
        }
    }

    /**
     * Reinforcement phase to assign the armies to the country selected by the player
     *
     * @param playerName  Name of the player
     * @param countryName Name of the country to which armies are to be assigned
     * @param armiesToAdd Number of armies to be assigned
     */
    public void reinforcementPhase(String playerName, String countryName, int armiesToAdd) {
        Player player = this.players.get(playerName);

        if (this.armiesAvailableToAssign == 0) {
            System.out.println("NO ARMIES AVAILABLE TO ASSIGN");
        } else if (this.armiesAvailableToAssign == -1) {
            this.setArmiesToAssign();
        } else {
            player.reinforcementPhase(countryName, armiesToAdd);
            this.armiesAvailableToAssign -= armiesToAdd;
            System.out.println("ARMIES ASSIGNED SUCESSFULLY");
        }
    }

    /**
     * Calculate the number of armies to assign in the reinforcement phase
     */
    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
    public void setArmiesToAssign() {
        int countriesConquered = this.countries.size();
        this.armiesAvailableToAssign = Math.floor(countriesConquered / 3) < 3 ? 3 : Math.floor(countriesConquered / 3);
    }

    /**
     * It is used to reset the counter back to default for a fresh computation
     */
    public void resetArmyCounter() {
        armiesAvailableToAssign = -1;
    }
}
