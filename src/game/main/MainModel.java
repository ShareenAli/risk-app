package game.main;

import entity.Continent;
import entity.Country;
import entity.Player;

import java.util.*;

/**
 * This model class carries all the data structures for the application
 *
 * @author Dhaval
 */

@SuppressWarnings("deprecation")
public class MainModel extends Observable {
    public static final String CHANGE_ARMY = "main:army";
    private ArrayList<String> playerNames = new ArrayList<>();
    private HashMap<String, Player> players = new HashMap<>();
    private HashMap<String, Country> countries = new HashMap<>();
    private HashMap<String, Continent> continents = new HashMap<>();
    private int armiesToAssign;
    private double armiesAvailableToAssign = -1;

    /**
     * Constructor used to extract the data from the map
     */
    public MainModel() {
    }

    /**
     * To initialize all the players playing the game
     *
     * @param playerList list of players
     */
    public void setPlayers(ArrayList<Player> playerList) {
        for (Player player : playerList) {
            this.players.put(player.getName(), player);
            this.playerNames.add(player.getName());
        }
    }

    public void setMapContent(ArrayList<Country> countries, ArrayList<Continent> continents) {
        for (Continent continent : continents) {
            this.continents.put(continent.getName(), continent);
        }
        for (Country country : countries) {
            this.countries.put(country.getName(), country);
            Continent continent = this.continents.get(country.getContinent());
            continent.addCountry();
            this.continents.put(continent.getName(), continent);
        }
    }

    public ArrayList<String> getPlayerNames() {
        return this.playerNames;
    }

    /**
     * Returns the hashmap of the players
     * @return players
     */
    public HashMap<String, Player> getPlayers() {
        return this.players;
    }

    /**
     * Fetch the Player object by feeding in a name
     *
     * @param name name of the player
     * @return player player object representing the player
     */
    public Player getPlayer(String name) {
        return this.players.get(name);
    }

    /**
     * It is used to get the List of countries
     *
     * @return countries List of all the countries
     */
    public HashMap<String, Country> getCountries() {
        return this.countries;
    }

    public String[] getDominationRow(Player player) {
        String[] row = new String[4];
        HashMap<String, Integer> continentsConquered = new HashMap<>();
        int noOfContinents = 0, noOfArmies = 0, noOfCountries = 0;

        for (Map.Entry<String, Integer> countryEntry : player.getCountries().entrySet()) {
            noOfCountries++;
            noOfArmies += countryEntry.getValue();

            String continent = this.countries.get(countryEntry.getKey()).getContinent();
            int countryCount = continentsConquered.getOrDefault(continent, 0);
            countryCount++;
            noOfContinents += (this.continents.get(continent).getCountryCount() == countryCount) ? 1 : 0;
            continentsConquered.put(continent, countryCount);
        }

        double percent = ((double) noOfCountries / (double) this.countries.size()) * 100;
        percent = Math.round(percent * 100) / 100.0;

        row[0] = player.getName();
        row[1] = String.valueOf(noOfContinents);
        row[2] = String.valueOf(noOfArmies);
        row[3] = String.valueOf(percent).concat("%");

        return row;
    }

    public HashMap<String, String[]> getDominationTable() {
        HashMap<String, String[]> outcome = new HashMap<>();
        for (Map.Entry<String, Player> playerEntry : this.players.entrySet()) {
            String name = playerEntry.getKey();

            Player player = playerEntry.getValue();
            outcome.put(name, getDominationRow(player));
        }

        return outcome;
    }

    /**
     * It is used to get all the armies available to assign
     *
     * @return armiesAvailableToAssign returns the number of armies to assign
     */
    public double getArmiesAvailableToAssign() {
        return armiesAvailableToAssign;
    }

    /**
     * Update the state of the player and notify all the observers
     *
     * @param name name of the player
     * @param player object to update
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

        setChanged();
        notifyObservers(CHANGE_ARMY);
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
        } else if (this.armiesAvailableToAssign == -1) {
            this.setArmiesToAssign();
        } else {
            player.reinforcementPhase(countryName, armiesToAdd);
            this.armiesAvailableToAssign -= armiesToAdd;
        }
    }

    /**
     * It is used to validate and perform the fortification phase operations
     *
     * @param playerName Name of the player
     * @param sourceCountryName Name of the source country
     * @param targetCountryName Name of the target country
     * @param armiesToTransfer Number of armies to transfer
     */
    public void fortificationPhase(String playerName, String sourceCountryName, String targetCountryName, int armiesToTransfer) {
        Player player = this.players.get(playerName);
        ArrayList<String> originCountries = new ArrayList<>();

        boolean result = checkForLink(originCountries, sourceCountryName, targetCountryName);

        if (result)
            player.fortificationPhase(sourceCountryName, targetCountryName, armiesToTransfer);
        else
            System.out.println("Countries are not connected, Cannot perform the transfer!");
    }

    /**
     * It checks the Source country is interconnected with the target country to which armies are to be transferred
     *
     * @param originCountries List of all the source Countries in recursion stack
     * @param sourceCountryName Name of the Source country from which the armies are to be transferred
     * @param targetCountryName Name of the target country from which the armies are to be transferred
     * @return result A boolean value to return whether the countries are interconnected
     */
    public boolean checkForLink(ArrayList<String> originCountries, String sourceCountryName, String targetCountryName) {
        Country country = this.countries.get(sourceCountryName);
        ArrayList<String> neighbours = new ArrayList<>();
        boolean compare;
        originCountries.add(sourceCountryName);

        neighbours = country.getNeighbours();

        for (String neighbour : neighbours) {
            if (neighbour.equals(targetCountryName))
                return true;
        }

        for (String neighbour : neighbours) {
            compare = true;

            for (String originCountry : originCountries) {
                if (neighbour.equals(originCountry))
                    compare = false;
            }

            if (compare)
                return checkForLink(originCountries, neighbour, targetCountryName);
        }
        return false;
    }

    /**
     * Calculate the number of armies to assign in the reinforcement phase
     */
    private void setArmiesToAssign() {
        int countriesConquered = this.countries.size();
        this.armiesAvailableToAssign = Math.floor((float) countriesConquered / 3) < 3
            ? 3 : Math.floor((float) countriesConquered / 3);
    }

    /**
     * It is used to reset the counter back to default for a fresh computation
     */
    public void resetArmyCounter() {
        armiesAvailableToAssign = -1;
    }
}
