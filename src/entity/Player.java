package entity;

import risk.game.main.MainModel;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    private int NoOfDiceRolls;
    private ArrayList<String> cards = new ArrayList<>();
    private int cardsAvailCount = 0, cardsUsedCount = 0;
    private boolean hasPlayedBefore = false, cardHasBeenUsed = false;
    private HashMap<String, Integer> countries = new HashMap<>();
    private boolean victory;

    /**
     * Removes one Infantry, Artillery and Cavalry cards
     */
    public void useDistinctCards () {
        this.cards.remove(MainModel.CARD_TYPE_ARTILLERY);
        this.cards.remove(MainModel.CARD_TYPE_CAVALRY);
        this.cards.remove(MainModel.CARD_TYPE_INFANTRY);

        this.useCard();
    }

    /**
     * Use the same kind of card
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
     * Add the card
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
        this.victory = false;
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
        hasPlayedBefore = true;
    }

    /**
     * Perform the attack phase on data structures
     *
     * @param sourceCountry    Name of the attacking country
     * @param targetCountry    Name of the country being attacked
     * @param armiesToTransfer Number of armies transferred by the player from source to target country
     */
    public void postAttackPhase(String sourceCountry, String targetCountry, int armiesToTransfer) {
        this.addArmies(targetCountry, armiesToTransfer - 1);
        this.removeArmies(sourceCountry, armiesToTransfer);
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

    public Player attack(Player target, String targetCountry, String sourceCountry, ArrayList<Integer> attackerDices,
                       ArrayList<Integer> defenderDices) {
        int attackerArmies = this.getArmiesInCountry(sourceCountry);
        int defenderArmies = target.getArmiesInCountry(targetCountry);
        attackerDices.sort((Integer o1, Integer o2) -> o2 - o1);
        defenderDices.sort((Integer o1, Integer o2) -> o2 - o1);

        for (int i = 0; i < defenderDices.size(); i++) {
            System.out.println(attackerDices.get(i) + ", " + defenderDices.get(i));
            if (attackerDices.get(i) > defenderDices.get(i)) {
                defenderArmies--;

                if (defenderArmies == 0)
                    break;
            } else {
                attackerArmies--;

                if (attackerArmies == 1)
                    break;
            }
        }

        target.setArmies(targetCountry, defenderArmies);
        this.setArmies(sourceCountry, attackerArmies);

        return target;
    }

    /**

     * Execution of the attack
     *
     * @param attacker     Player object of attacker
     * @param defendant    Player object of defendant
     * @param attackSource Country of the attacker
     * @param attackTarget Country of the defender
     * @return boolean Gives back Victory or Failure
     */
    public Player executeAttack(Player attacker, Player defendant, String attackSource, String attackTarget) {
        ArrayList<Integer> attackerDiceRolls = new ArrayList<>();
        ArrayList<Integer> defendantDiceRolls = new ArrayList<>();
        int i = 0;

        while (i < 2) {
            attackerDiceRolls.add(rollDice());
            defendantDiceRolls.add(rollDice());
            i++;
        }

        Collections.sort(attackerDiceRolls, Collections.reverseOrder());
        Collections.sort(defendantDiceRolls, Collections.reverseOrder());

        boolean outcome = processAttackOutcome(attackerDiceRolls, defendantDiceRolls, attacker, defendant, attackSource, attackTarget);

        if (outcome)
            defendant.victory = false;
        else
            defendant.victory = true;

        return defendant;
    }

    /**
     * Process the dice rolls and produce an outcome
     *
     * @param attackerDiceRolls  Dice Rolls of the attacker
     * @param defendantDiceRolls Dice Rolls of the defender
     * @param attacker           Player object of the attacker
     * @param defendant          Player object of the defender
     * @param attackSource       Country of the attacker
     * @param attackTarget       Country of the defender
     * @return boolean Produces the outcome of battle
     */
    private boolean processAttackOutcome(ArrayList<Integer> attackerDiceRolls, ArrayList<Integer> defendantDiceRolls, Player attacker, Player defendant, String attackSource, String attackTarget) {
        int attackCount = 0, defendCount = 0;
        int attackerArmies = attacker.getArmiesInCountry(attackSource);
        int defendantArmies = defendant.getArmiesInCountry(attackTarget);

        for (int j = 0; j < 2; j++) {
            if (attackerArmies == 1)
                return false;

            if (attackerDiceRolls.get(j) > defendantDiceRolls.get(j)) {
                defendantArmies--;
                attackCount++;
            } else if (attackerDiceRolls.get(j) < defendantDiceRolls.get(j)) {
                attackerArmies--;
                defendCount++;
            } else {
                attackerArmies--;
                defendCount++;
            }
        }

        attacker.setArmies(attackSource, attackerArmies);
        defendant.setArmies(attackTarget, defendantArmies);

        if (attackCount > defendCount) {
            if (defendantArmies == 0)
                return true;
            else
                return false;
        } else if (attackCount < defendCount) {
            return false;
        } else {
            if (defendantArmies == 1) {
                if (attacker.getNoOfDiceRolls() == 3)
                    return true;
                else
                    return false;
            } else
                return false;
        }
    }

    /**
     * Roll the Dice for attack
     *
     * @return Integer Return the number on the dice rolled
     */
    private int rollDice() {
        int diceRoll = (int) (Math.random() * 6 + 1);
        return diceRoll;
    }

    /**
     * To get the value of victory variable
     *
     * @return Boolean True if defender won the battle
     */
    public boolean isVictory() {
        return victory;
    }

    /**
     * To set whether defender has won the battle
     *
     * @param victory True if defender won the battle
     */
    public void setVictory(boolean victory) {
        this.victory = victory;
    }

    /**
     * Checks if the player has exchanged the card before
     *
     * @return Boolean True if player has done an exchange
     */
    public boolean playedBefore() { return hasPlayedBefore; }



    /**
     * Retruns the number of times current player has availed his/her cards


     */
    public int getCardsAvailCount(){ return cardsAvailCount; }



    /**
     * Sets the number of times current player has availed his/her cards
     * @param availCount Updated number of times player has availed his/her cards

     */
    public void setCardsAvailCount (int availCount){ cardsAvailCount = availCount; }

    /**
     * Checks if player have Infantry card
     *
     * @return true if player has infantry card
     */
    public boolean haveInfantryCard () {
        for (String card : this.cards) {
            if (card.equals(MainModel.CARD_TYPE_INFANTRY)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if player have Cavalry Card
     *
     * @return true if player have Cavalry Card otherwise false
     */
    public boolean haveCavalryCard () {
        for (String card : this.cards) {
            if (card.equals(MainModel.CARD_TYPE_CAVALRY)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if player have Artillery Card
     *
     * @return true if player have Artillery Card otherwise false
     */
    public boolean haveArtilleryCard () {
        for (String card : this.cards) {
            if (card.equals(MainModel.CARD_TYPE_ARTILLERY)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if player have Infantry, Artillery and Cavalry Cards
     *
     * @return true if player have Infantry, Artillery and Cavalry Cards
     * otherwise false
     */
    public boolean haveDistinctCards () {
        return (this.haveInfantryCard() && this.haveArtilleryCard() && this.haveCavalryCard());
    }

    /**
     * Checks if player have three Artillery cards
     *
     * @return true if player have three Artillery cards otherwise false
     */
    public boolean haveThreeArtilleryCards () {
        int artillery = 0;
        for (String card : this.cards) {
            if (card.equals(MainModel.CARD_TYPE_ARTILLERY)) {
                artillery++;
            }
        }
        return (artillery == 3);
    }

    /**
     * Checks if player have three Cavalry cards
     *
     * @return true if player have three Cavalry cards otherwise false
     */
    public boolean haveThreeCavalryCards () {
        int cavalry = 0;
        for (String card : this.cards) {
            if (card.equals(MainModel.CARD_TYPE_CAVALRY)) {
                cavalry++;
            }
        }
        return (cavalry == 3);
    }

    /**
     * Checks if player have three Infantry cards
     *
     * @return true if player have three Infantry Cards otherwise false
     */
    public boolean haveThreeInfantryCards () {
        int infantry = 0;
        for (String card : this.cards) {
            if (card.equals(MainModel.CARD_TYPE_INFANTRY)) {
                infantry++;
            }
        }
        return (infantry == 3);
    }

    /**
     * Checks if player have either three Cavalry, Artillery or Infantry cards
     *
     * @return true if player have either three Cavalry, Artillery or Infantry
     * cards otherwise false
     */
    public boolean haveThreeSameTypeCards () {
        return (this.haveThreeCavalryCards() || this.haveThreeArtilleryCards() || this.haveThreeInfantryCards());
    }

}
