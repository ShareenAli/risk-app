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
        HashMap<String, String[]> outcome = new HashMap<>();
        for (Map.Entry<String, Player> playerEntry : this.players.entrySet()) {
            outcome.put(playerEntry.getKey(), getDominationRow(playerEntry.getValue()));
        }

        return outcome;
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
     * Update the state of the player and notify all the observers
     *
     * @param name   name of the player
     * @param player object to update
     */
    private void updatePlayer(String name, Player player) {
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
        int controlValue = checkControlValueArmies(player);
        armiesToAdd += controlValue;
        player.reinforcementPhase(countryName, armiesToAdd);
        this.armiesAvailableToAssign -= armiesToAdd;
        this.updatePlayer(player.getName(), player);
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
     * Attack phase to perform post attack operations in case an attack is successful
     *
     * @param attackerName  Name of the attacker
     * @param defendantName Name of the defendant
     * @param sourceCountry Name of the attacking country
     * @param targetCountry Name of the defending country
     * @param allOutMode    States whether user has opted for alloutmode
     */
    public boolean attackPhase(String attackerName, String defendantName, String sourceCountry, String targetCountry, boolean allOutMode) {
        Player attacker = this.players.get(attackerName);
        Player defendant = this.players.get(defendantName);
        boolean outcome = false;

        determineNoOfDiceRolls(sourceCountry, attacker, true);
        determineNoOfDiceRolls(targetCountry, defendant, false);

        defendant = attacker.executeAttack(attacker, defendant, sourceCountry, targetCountry);

        if (allOutMode) {
            if (attacker.getArmiesInCountry(sourceCountry) > 1) {
                outcome = attackPhase(attackerName, defendantName, sourceCountry, targetCountry, allOutMode);
                defendant.setVictory(outcome);
            } else {
                return outcome;
            }
        }

        this.players.put(attacker.getName(), attacker);
        this.players.put(defendant.getName(), defendant);
        this.updatePlayer(attacker.getName(), attacker);
        this.updatePlayer(defendant.getName(), defendant);
        return defendant.isVictory();
    }

    /**
     * Perform necessary updates on the entities in the game
     *
     * @param defendant Player object of the defendant
     * @param attacker Player object of the attacker
     * @param targetCountry Name of the target country
     */
    public void updateEntitiesAfterAttack(Player defendant, Player attacker, String targetCountry) {

        if (!defendant.isVictory()) {
            HashMap<String, Integer> defendantCountries = defendant.getCountries();
            Country country = this.countries.get(targetCountry);
            ArrayList<String> attackerCards = attacker.getCards();
            ArrayList<String> defendantCards = defendant.getCards();

            defendant.removeCountry(targetCountry);
            attacker.assignCountry(targetCountry);

            if (defendantCountries.size() == 0) {
                this.playerNames.remove(defendant.getName());
                this.players.remove(defendant.getName());

                if (defendantCards.size() > 0) {
                    for (String card : defendantCards) {
                        attackerCards.add(card);
                    }
                }
            }
            attackerCards.add(country.getCardType());
            attacker.setCards(attackerCards);
        }

        if (this.players.containsKey(defendant.getName()))
            this.players.put(defendant.getName(), defendant);

        this.players.put(attacker.getName(), attacker);
        this.updatePlayer(attacker.getName(), attacker);
        this.updatePlayer(defendant.getName(), defendant);
    }

    /**
     * Process the post attack phase operations
     *
     * @param attacker         Player object of the attacker
     * @param sourceCountry    Country of the attacker
     * @param targetCountry    Country of the defender
     * @param armiesToTransfer No of armies to Transfer to the conquered countries
     */
    public void processPostAttackPhase(Player attacker, String sourceCountry, String targetCountry, int armiesToTransfer) {
        attacker.postAttackPhase(sourceCountry, targetCountry, armiesToTransfer);
        this.players.put(attacker.getName(), attacker);
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

        for (String country : countries) {
            Country c = this.countries.get(country);
            if (c.getContinent().equalsIgnoreCase(continent))
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

    /**
     * Checks if the attack is possible or not
     *
     * @param attackingCountry Name of the attacking country
     * @param defendingCountry Name of the defending country
     * @return boolean true if attack is feasible
     */
    public boolean checkIfAttackFeasible(Player player, String attackingCountry, String defendingCountry) {
        Country attackingCountryObject = this.countries.get(attackingCountry);

        ArrayList<String> neighbours = attackingCountryObject.getNeighbours();

        if (neighbours.contains(defendingCountry))
            return true;
        else
            return false;
    }

    /**
     * Check the minimum armies required to perform the attack
     *
     * @param player  Player object
     * @param country Country object
     * @return boolean true if armies meet the min. army criteria
     */
    public boolean checkMinArmiesForAttack(Player player, String country) {
        int armies = player.getArmiesInCountry(country);

        if (armies < 2)
            return false;
        else
            return true;
    }

    /**
     * Determine the number of dice rolls for each player
     *
     * @param country Country object
     * @param player  Player object
     */
    public void determineNoOfDiceRolls(String country, Player player, boolean attacking) {
        int armies = player.getArmiesInCountry(country);

        if (attacking)
            player.setNoOfDiceRolls((armies >= 3) ? 3 : 2);
        else
            player.setNoOfDiceRolls((armies >= 3) ? 3 : 2);

        this.players.put(player.getName(), player);
    }


    /**
     * Initially assigns one card to every country
     */
    public void assignInitialCards(){
        int cardNumber, max=2, min=0;
        Country tempCountry;
        String cardType = null;
        for(Map.Entry<String, Country> country : this.countries.entrySet()){
            cardNumber = new Random().nextInt(3);
            switch(cardNumber){
                case 0:
                    cardType = "Infantry";
                    break;
                case 1:
                    cardType = "Cavalry";
                    break;
                case 2:
                    cardType = "Artillery";
                    break;
            }

            tempCountry = country.getValue();
            tempCountry.setCardType(cardType);
            this.countries.put(country.getKey(), tempCountry);
        }
    }


    /**
     * Calculates and adds the armies when the cards are availed
     */
    public void addArmiesOnCardsAvail(Player player){
        int availCount = player.getCardsAvailCount();
        int updatedCount = availCount + 1;
        player.setCardsAvailCount(updatedCount);
        this.armiesAvailableToAssign += updatedCount * 5;
        ArrayList<String> cards = player.getCards();
        int cardListSize = cards.size();
        for (int i = cardListSize - 1; i > cardListSize - 4; i--)
            cards.remove(i);
        player.setCards(cards);
    }
}
