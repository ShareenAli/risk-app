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
    private FileReader fileReader;
    private Scanner scanner;
    private int armiesToAssign;

    /**
     * Constructor used to extract the data from the map
     */
    public MainModel() {
        readMapFile();
    }

    /**
     * To initialize all the players playing the game
     *
     * @param playerList
     */
    public void setPlayers(ArrayList<Player> playerList) {
        for (Player player : playerList) {
            this.players.put(player.getName(), player);
            this.playerNames.add(player.getName());
        }
    }

    /**
     * Fetch the Player object by feeding in a name
     *
     * @param name
     * @return
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
        notifyObservers(player);
    }

    /**
     * Assign Country to each player in the game
     */
    public void assignCountry() {
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
    public void assignArmies() {
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
     * Parse the map file into the data structures of the application
     */
    public void readMapFile() {
        int continentFlag = 0, territoryFlag = 0;
        try {
            fileReader = new FileReader("D:\\Courses\\Soen 6441\\Project\\maps\\Empire of Alexander.map");
            scanner = new Scanner(fileReader);

            while (scanner.hasNext()) {
                String line;
                String temp[];

                line = scanner.nextLine();

                switch (line) {
                    case "[Continents]":
                        continentFlag = 1;
                        break;

                    case "[Territories]":
                        territoryFlag = 1;
                        continentFlag = 0;
                        break;
                }

                if (line.equals("") || line.equals(" "))
                    continue;

                if (continentFlag == 1) {
                    if (line.equals("[Continents]"))
                        line = scanner.nextLine();

                    temp = line.split("=");
                    this.continents.put(temp[0].trim(), new Continent(temp[0].trim(), Integer.parseInt(temp[1])));
                }

                if (territoryFlag == 1) {
                    if (line.equals("[Territories]"))
                        line = scanner.nextLine();

                    temp = line.split(",");
                    country = new Country(temp[0].trim(), temp[3].trim(), Double.parseDouble(temp[1].trim()), Double.parseDouble(temp[2].trim()));

                    for (int i = 4; i < temp.length - 1; i++) {
                        country.addNeighbour(temp[i]);
                    }

                    this.countries.put(temp[0], country);
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
