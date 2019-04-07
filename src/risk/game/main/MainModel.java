package risk.game.main;

import entity.Continent;
import entity.Country;
import entity.Player;

import java.awt.*;
import java.util.*;

/**
 * This model class carries all the data structures for the application
 *
 * @author imdc003, shareenali
 * @version 0.1
 */

@SuppressWarnings("deprecation")
public class MainModel extends Observable {
    public static final String CARD_TYPE_INFANTRY = "Infantry";
    public static final String CARD_TYPE_CAVALRY = "Cavalry";
    public static final String CARD_TYPE_ARTILLERY = "Artillery";

    public static final String CHANGE_ARMY = "main:army";
    public static final String UPDATE_PLAYER = "update:player";

    private ArrayList<String> playerNames = new ArrayList<>();
    private HashMap<String, Player> players = new HashMap<>();
    private HashMap<String, Country> countries = new HashMap<>();
    private HashMap<String, Continent> continents = new HashMap<>();
    private int armiesToAssign;
    private int armiesAvailableToAssign = -1;

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

    /**
     * Removes the player from the game
     *
     * @param playerName Name of the player
     */
    public void removePlayer(String playerName) {
        this.players.remove(playerName);
        this.playerNames.remove(playerName);
    }

    /**
     * Set the content from the map
     *
     * @param countries  list of countries
     * @param continents list of continents
     */
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

    /**
     * Get the name of players
     *
     * @return list of players with just their names
     */
    public ArrayList<String> getPlayerNames() {
        return this.playerNames;
    }

    /**
     * Get the colors of the players
     *
     * @return list of players with just their colors
     */
    ArrayList<Color> getPlayerColors() {
        ArrayList<Color> colors = new ArrayList<>();
        for (Map.Entry<String, Player> playerEntry : this.players.entrySet()) {
            colors.add(playerEntry.getValue().getColor());
        }

        return colors;
    }

    /**
     * Returns the hashmap of the players
     *
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

    /**
     * Get the card
     *
     * @param country name of the country
     * @return type of card
     */
    public String getCard(String country) {
        return this.countries.get(country).getCardType();
    }

    /**
     * Use the card
     *
     * @param country name of the country
     */
    public void useCard(String country) {
        Country countryName = this.countries.get(country);
        countryName.useCard();
        this.countries.put(countryName.getName(), countryName);
    }

    /**
     * Get the row for the domination table.
     * It will display name, no of continents conquered, no of armies the player posses
     * and percent of the map conquered.
     *
     * @param player player to get the data for
     * @return domination row
     */
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

    /**
     * Get the domination table
     * It will display name, no of continents conquered, no of armies the player posses
     * and percent of the map conquered.
     *
     * @return the table
     */
    public HashMap<String, String[]> getDominationTable() {
        HashMap<String, String[]> dominationTable = new HashMap<>();
        for (Map.Entry<String, Player> playerEntry : this.players.entrySet()) {
            dominationTable.put(playerEntry.getKey(), getDominationRow(playerEntry.getValue()));
        }

        return dominationTable;
    }

    /**
     * It is used to get all the armies available to assign for reinforcement phase
     *
     * @return armiesAvailableToAssign returns the number of armies to assign
     */
    public int getArmiesAvailableToAssign() {
        return this.armiesAvailableToAssign;
    }

    /**
     * notify all the observers
     */
    void changeWorldView() {
        setChanged();
        notifyObservers(MainModel.CHANGE_ARMY);
    }

    /**
     * Checks if the player has won or not
     *
     * @param player player object
     * @return true if won
     */
    public boolean hasPlayerWon(Player player) {
        int totalCountries = this.countries.size();
        int playerCountries = player.getCountries().size();

        double conquerLimit = ((double) totalCountries) * (3.0 / 4.0);

        return (playerCountries >= conquerLimit);
    }

    /**
     * Update the state of the player
     *
     * @param name   name of the player
     * @param player object to update
     */
    void updatePlayerWithoutNotify(String name, Player player) {
        this.players.put(name, player);
    }

    /**
     * Update the state of the player and notify all the observers
     *
     * @param name   name of the player
     * @param player object to update
     */
    void updatePlayer(String name, Player player) {
        this.players.put(name, player);
        setChanged();
        notifyObservers(player.notifyString());
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
            for (String eachPlayer : playerNames) {
                Player player = this.players.get(eachPlayer);
                HashMap<String, Integer> countriesConquered = player.getCountries();

                Object[] countries = player.getCountries().keySet().toArray();

                int randomCountryIndex = random.nextInt(countries.length);
                String randomCountry = (String) countries[randomCountryIndex];
                int noOfArmies = countriesConquered.get(randomCountry);

                player.setArmies(randomCountry, ++noOfArmies);
                this.players.put(eachPlayer, player);
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
    public String reinforcementPhase(String playerName, String countryName, int armiesToAdd) {
        Player player = this.players.get(playerName);
        int controlValue = checkControlValueArmies(player);
        armiesToAdd += controlValue;
        countryName = player.reinforcementPhase(countryName, armiesToAdd);
        this.armiesAvailableToAssign -= armiesToAdd;
        this.updatePlayer(player.getName(), player);
        return countryName;
    }

    /**
     * It is used to validate and perform the fortification phase operations
     *
     * @param playerName        Name of the player
     * @param sourceCountryName Name of the source country
     * @param targetCountryName Name of the target country
     * @param armiesToTransfer  Number of armies to transfer
     */
    void fortificationPhase(String playerName, String sourceCountryName, String targetCountryName, int armiesToTransfer) {
        Player player = this.players.get(playerName);
        player.fortificationPhase(sourceCountryName, targetCountryName, armiesToTransfer);
        this.updatePlayer(player.getName(), player);
    }

    /**
     * It checks the Source country is interconnected with the target country to which armies are to be transferred
     *
     * @param originCountries   List of all the source Countries in recursion stack
     * @param sourceCountryName Name of the Source country from which the armies are to be transferred
     * @param targetCountryName Name of the target country from which the armies are to be transferred
     * @return result A boolean value to return whether the countries are interconnected
     */
    public boolean checkForLink(ArrayList<String> originCountries, String sourceCountryName, String targetCountryName) {
        Country country = this.countries.get(sourceCountryName);
        if (country == null)
            return false;
        ArrayList<String> neighbours = country.getNeighbours();
        boolean compare;
        originCountries.add(sourceCountryName);

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
    public void resetArmiesToAssign(String playerName) {
        Player player = this.getPlayer(playerName);
        int countriesConquered = player.getCountries().size();
        this.armiesAvailableToAssign = (int) Math.round(Math.floor((float) countriesConquered / 3) < 3
                ? 3 : Math.floor((float) countriesConquered / 3));
        this.armiesAvailableToAssign += this.checkControlValueArmies(player);
        if (player.hasCardBeenUsed()) {
            int cardsArmies = player.getCardsUsedCount() * 5;
            this.armiesAvailableToAssign += cardsArmies;
            player.resetCardUsed();
        }
        this.players.put(player.getName(), player);
    }

    /**
     * Count list of countries in continent from given sets
     *
     * @param continent name of the continent
     * @param countries list of countries to loop in
     * @return the count
     */
    private int countCountriesInContinent(String continent, Set<String> countries) {
        int count = 0;

        for (String eachCountry : countries) {
            Country country = this.countries.get(eachCountry);
            if (country.getContinent().equalsIgnoreCase(continent))
                count++;
        }

        return count;
    }

    /**
     * Count list of countries in the continent
     *
     * @param continent name of the continent
     * @return the count
     */
    private int countCountriesInContinent(String continent) {
        int count = 0;

        for (Map.Entry<String, Country> countryEntry : this.countries.entrySet()) {
            Country country = countryEntry.getValue();
            if (country.getContinent().equalsIgnoreCase(continent))
                count++;
        }

        return count;
    }

    /**
     * Get the armies to get based on continents conquered
     *
     * @param player object players
     * @return armies to add
     */
    private int checkControlValueArmies(Player player) {
        int cv = 0;
        for (Map.Entry<String, Continent> entry : this.continents.entrySet()) {
            Continent continent = entry.getValue();
            int noOfCountriesInside = this.countCountriesInContinent(continent.getName());
            int noOfCountriesPlayer = this.countCountriesInContinent(continent.getName(), player.getCountries().keySet());

            if (noOfCountriesInside == noOfCountriesPlayer)
                cv += continent.getControlValue();
        }
        return cv;
    }

    ArrayList<String> getPotentialCountriesForAttack(ArrayList<String> discardCountries) {
        ArrayList<String> finalList = new ArrayList<>();

        for (String country : this.getCountries().keySet()) {
            if (!discardCountries.contains(country)) {
                finalList.add(country);
            }
        }

        return finalList;
    }

    String getPlayerNameFromCountry(String country) {
        for (Map.Entry<String, Player> entry : this.players.entrySet()) {
            if (entry.getValue().getCountries().containsKey(country)) {
                return entry.getKey();
            }
        }

        return null;
    }

    public HashMap<String, Continent> getContinents() {
        return this.continents;
    }
}
