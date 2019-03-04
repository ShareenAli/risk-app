package entity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This holds the player entity and the runtime entity collected for player It holds
 * the countries he conquered and it can be updated anytime. It also holds list
 * of continents the he has conquered.
 *
 * @author shareenali
 * @version 0.1
 */

public class Player {
    public static final String CHANGE_PLAYER = "change:player";
    /**
     * A human player. It can be used as player type
     */
    public static final int TYPE_HUMAN = 0;
    /**
     * A computer player. It can be used as player type
     */
    public static final int TYPE_COMPUTER = 1;

    private String name;
    private int type;
    private HashMap<String, Integer> countries = new HashMap<>();
    private ArrayList<String> cards = new ArrayList<>();

    /**
     * It initializes the runtime player
     *
     * @param name name of the player
     * @param type type of the player
     */
    public Player(String name, int type) {
        this.name = name;
        this.type = type;
    }

    /**
     * It returns name of the player
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * It returns type of the player
     *
     * @return type
     */
    public int getType() {
        return type;
    }

    /**
     * It returns the countries conquered by the player
     *
     * @return
     */
    public HashMap<String, Integer> getCountries() {
        return countries;
    }

    /**
     * Assign the country to the player
     *
     * @param country
     */
    public void assignCountry(String country) {
        this.countries.put(country, 1);
    }

    /**
     * Assign armies to the countries
     *
     * @param countryName
     * @param armies
     */
    public void setArmies(String countryName, int armies) {
        this.countries.put(countryName, armies);
    }

    /**
     * Add the armies to a country
     *
     * @param countryName
     * @param armiesToAdd
     */
    public void addArmies(String countryName, int armiesToAdd) {
        int armies = this.countries.get(countryName);
        armies += armiesToAdd;
        this.countries.put(countryName, armies);
    }

    /**
     * Remove the armies from a country
     *
     * @param countryName Name of the country
     * @param armiesToRemove Number of armies to remove from the country
     */
    public void removeArmies(String countryName, int armiesToRemove) {
        int armies = this.countries.get(countryName);
        armies-= armiesToRemove;
        this.countries.put(countryName, armies);
    }

    /**
     * Update the data structures for reinforcement phase
     */
    public void reinforcementPhase(String countryName, int armiesToAdd) {
        this.addArmies(countryName, armiesToAdd);
    }

    /**
     * Perform the fortification phase on the data structures
     *
     * @param sourceCountryName Move the armies from the desired country
     * @param targetCountryName Move the armies to the desired country
     * @param armiesToTransfer Number of armies to be transferred from a country to another
     */
    public void fortificationPhase(String sourceCountryName, String targetCountryName, int armiesToTransfer) {
        this.addArmies(targetCountryName, armiesToTransfer);
        this.removeArmies(sourceCountryName, armiesToTransfer);
    }
}
