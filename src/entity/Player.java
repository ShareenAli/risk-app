package entity;

import risk.game.main.MainModel;

import java.awt.*;
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
    private Color color;
    private HashMap<String, Integer> countries = new HashMap<>();


    /**
     * Gets a list of cards owned by a player
     *
     * @return list of cards
     */
    public ArrayList<String> getCards() {
        return cards;
    }

    /**
     * Sets the value of cards owned by a player
     *
     * @param cards
     */
    public void setCards(ArrayList<String> cards) {
        this.cards = cards;
    }

    private ArrayList<String> cards = new ArrayList<>();

    /**
     * It initializes the player
     *
     * @param name name of the player
     * @param type type of the player
     */
    public Player(String name, int type) {
        this.name = name;
        this.type = type;
    }

    /**
     * It initializes the player with color
     *
     * @param name  name of the player
     * @param type  type of the player
     * @param color color that the player is assigned to
     */
    public Player(String name, int type, Color color) {
        this.name = name;
        this.type = type;
        this.color = color;
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
     * Get the color of the player
     *
     * @return color object
     */
    public Color getColor() {
        return color;
    }

    /**
     * It returns the countries conquered by the player
     *
     * @return hash map of the countries
     */
    public HashMap<String, Integer> getCountries() {
        return this.countries;
    }

    /**
     * Assign the country to the player
     *
     * @param country name of the country
     */
    public void assignCountry(String country) {
        this.countries.put(country, 1);
    }

    /**
     * Assign armies to the countries
     * It will replace the number of armies.
     *
     * @param countryName name of the countries
     * @param armies      armies to assign to
     */
    public void setArmies(String countryName, int armies) {
        this.countries.put(countryName, armies);
    }

    /**
     * Add the armies to the countries.
     * It will add armies to the existing armies.
     *
     * @param countryName name of the countries
     * @param armiesToAdd armies to append
     */
    private void addArmies(String countryName, int armiesToAdd) {
        int armies = this.countries.get(countryName);
        armies += armiesToAdd;
        this.countries.put(countryName, armies);
    }

    /**
     * Remove the armies from a country
     *
     * @param countryName    Name of the country
     * @param armiesToRemove Number of armies to remove from the country
     */
    private void removeArmies(String countryName, int armiesToRemove) {
        int armies = this.countries.get(countryName);
        armies -= armiesToRemove;
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
     * @param armiesToTransfer  Number of armies to be transferred from a country to another
     */
    public void fortificationPhase(String sourceCountryName, String targetCountryName, int armiesToTransfer) {
        this.addArmies(targetCountryName, armiesToTransfer);
        this.removeArmies(sourceCountryName, armiesToTransfer);
    }

    /**
     * Perform the attack phase on data structures
     *
     * @param sourceCountry
     * @param targetCountry
     * @param armiesToTransfer
     */
    public void attackPhase(String sourceCountry, String targetCountry, int armiesToTransfer) {
        this.addArmies(targetCountry, armiesToTransfer);
        this.removeArmies(sourceCountry, armiesToTransfer);
    }

    public int getArmiesInCountry(String country) {
        return this.countries.get(country);
    }

    public String notifyString() {
        return MainModel.UPDATE_PLAYER + ":" + this.name;
    }

    public Integer removeCountry(String countryName) {
        Integer armies = this.countries.remove(countryName);
        return armies;
    }
}
