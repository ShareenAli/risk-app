package entity;

import entity.behaviours.BenevolentBehaviour;
import entity.behaviours.HumanBehaviour;
import entity.behaviours.PlayerBehaviour;
import entity.behaviours.RandomBehaviour;
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
    public static final int TYPE_RANDOM = 1;
    public static final int TYPE_AGGRESSIVE = 2;
    public static final int TYPE_CHEATER = 3;
    public static final int TYPE_BENEVOLENT = 4;

    private String name;
    private int type;
    private Color color;
    private int NoOfDiceRolls;
    private ArrayList<String> cards = new ArrayList<>();
    private int cardsUsedCount = 0;
    private boolean cardHasBeenUsed = false;
    private HashMap<String, Integer> countries = new HashMap<>();
    private PlayerBehaviour strategy;

    /**
     * Removes one Infantry, Artillery and Cavalry cards
     */
    public void useDistinctCards() {
        this.cards.remove(MainModel.CARD_TYPE_ARTILLERY);
        this.cards.remove(MainModel.CARD_TYPE_CAVALRY);
        this.cards.remove(MainModel.CARD_TYPE_INFANTRY);

        this.useCard();
    }

    /**
     * Use the same kind of card
     *
     * @param type type of card
     */
    public void useSameCard(String type) {
        this.cards.remove(type);
        this.cards.remove(type);
        this.cards.remove(type);

        this.useCard();
    }

    /**
     * Use the card
     */
    private void useCard() {
        this.cardHasBeenUsed = true;
        this.cardsUsedCount++;
    }

    /**
     * Check if the card has been used
     *
     * @return true if it has been
     */
    public boolean hasCardBeenUsed() {
        return this.cardHasBeenUsed;
    }

    /**
     * Reset the flag for the card has been used
     */
    public void resetCardUsed() {
        this.cardHasBeenUsed = false;
    }

    /**
     * Get the times the cards have been used
     *
     * @return the count
     */
    public int getCardsUsedCount() {
        return this.cardsUsedCount;
    }


    /**
     * Gets a list of cards owned by a player
     *
     * @return list of cards
     */
    public ArrayList<String> getCards() {
        return cards;
    }

    /**
     * Set a behaviour strategy for a player
     *
     * @return PlayerBehaviour return strategy for a player
     */
    public PlayerBehaviour getStrategy() {
        return strategy;
    }

    public void setStrategy(int strategy) {
        switch (strategy) {
            case TYPE_RANDOM:
                this.strategy = new RandomBehaviour();
                break;
            case TYPE_AGGRESSIVE:
                break;
            case TYPE_CHEATER:
                break;
            case TYPE_BENEVOLENT:
                this.strategy = new BenevolentBehaviour();
                break;

            case TYPE_HUMAN:
                this.strategy = new HumanBehaviour();
                break;
        }
    }

    /**
     * Add the card
     *
     * @param card type of card
     */
    public void addCard(String card) {
        this.cards.add(card);
    }

    /**
     * Sets the value of cards owned by a player
     *
     * @param cards list of cards
     */
    public void setCards(ArrayList<String> cards) {
        this.cards = cards;
    }

    /**
     * It initializes the player
     *
     * @param name name of the player
     * @param type type of the player
     */
    public Player(String name, int type) {
        this.name = name;
        this.type = type;
        this.NoOfDiceRolls = 0;
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

        setStrategy(this.type);
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
    public String reinforcementPhase(String countryName, int armiesToAdd) {
        if (this.type != 0) {
            this.strategy.setPlayer(this);
            countryName = this.strategy.reinforcementPhase(countryName, armiesToAdd);
        }

        this.addArmies(countryName, armiesToAdd);
        return countryName;
    }

    /**
     * Perform the fortification phase on the data structures
     *
     * @param sourceCountryName Move the armies from the desired country
     * @param targetCountryName Move the armies to the desired country
     * @param armiesToTransfer  Number of armies to be transferred from a country to another
     */
    public void fortificationPhase(String sourceCountryName, String targetCountryName, int armiesToTransfer) {
        ArrayList<String> countries;
        if (this.type != 0) {
            this.strategy.setPlayer(this);
            countries = this.strategy.fortificationPhase(sourceCountryName, targetCountryName, armiesToTransfer);
            this.addArmies(countries.get(1), Integer.parseInt(countries.get(2)));
            this.removeArmies(countries.get(0), Integer.parseInt(countries.get(2)));
            return;
        }

        this.addArmies(targetCountryName, armiesToTransfer);
        this.removeArmies(sourceCountryName, armiesToTransfer);
    }

    /**
     * Get number of armies present in the country
     *
     * @param country Country object
     * @return Integer Number of armies in the country
     */
    public int getArmiesInCountry(String country) {
        return this.countries.get(country);
    }

    public String notifyString() {
        return MainModel.UPDATE_PLAYER + ":" + this.name;
    }

    /**
     * Remove the country from player's conquered countries
     *
     * @param countryName Name of the country
     */
    public void removeCountry(String countryName) {
        this.countries.remove(countryName);
    }

    /**
     * Get the Number of dice rolls for the attack phase
     *
     * @return Integer Returns the number of dice rolls determined for the player
     */
    public int getNoOfDiceRolls() {
        return NoOfDiceRolls;
    }

    /**
     * Set the value for the dice rolls
     *
     * @param noOfDiceRolls Value of dice rolls for attack phase
     */
    public void setNoOfDiceRolls(int noOfDiceRolls) {
        NoOfDiceRolls = noOfDiceRolls;
    }

    /**
     * Attack for attack phase
     *
     * @param target        Target player of the attack
     * @param targetCountry Target country of the attack
     * @param sourceCountry Source country of the attack
     * @param attackerDices Dice rolls of the attacker
     * @param defenderDices Dice rolls of the defender
     * @return Player player object of the defendant
     */
    public Player attack(Player target, String targetCountry, String sourceCountry, ArrayList<Integer> attackerDices,
                         ArrayList<Integer> defenderDices) {
        this.strategy.setPlayer(this);
        Player defender = this.strategy.attack(target, targetCountry, sourceCountry, attackerDices, defenderDices);

        Player attacker = this.strategy.getPlayer();

        int attackerArmies = attacker.getArmiesInCountry(sourceCountry);
        int defenderArmies = defender.getArmiesInCountry(targetCountry);

        target.setArmies(targetCountry, defenderArmies);
        this.setArmies(sourceCountry, attackerArmies);


        return target;
    }
}
