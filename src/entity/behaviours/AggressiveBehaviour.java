package entity.behaviours;

import entity.Country;
import entity.Player;
import risk.game.main.MainModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class AggressiveBehaviour implements PlayerBehaviour, Serializable {
    private Player player;
    private MainModel model;
    int gameNo = 1;

    @Override
    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public String reinforcementPhase(String countryName, int armiesToAdd) {
        countryName = fetchStrongestCountry();

        this.player.addArmies(countryName, armiesToAdd);
        return countryName;
    }

    @Override
    public ArrayList<String> fortificationPhase(String sourceCountryName, String targetCountryName, int armiesToTransfer) {
        ArrayList<String> countries = new ArrayList<>(this.player.getCountries().keySet());

        targetCountryName = fetchStrongestCountry();

        do {
            sourceCountryName = countries.get((new Random()).nextInt(countries.size()));

            if(sourceCountryName.equalsIgnoreCase(targetCountryName))
                continue;

            if (this.model.checkForLink(new ArrayList<>(), sourceCountryName, targetCountryName))
                break;
        } while (targetCountryName.equalsIgnoreCase(sourceCountryName));

        armiesToTransfer = this.player.getArmiesInCountry(sourceCountryName) - 1;

        this.player.addArmies(targetCountryName, armiesToTransfer);
        this.player.removeArmies(sourceCountryName, armiesToTransfer);

        countries.clear();
        countries.add(sourceCountryName);
        countries.add(targetCountryName);
        countries.add(String.valueOf(armiesToTransfer));

        return countries;
    }

    @Override
    @SuppressWarnings("Duplicates")
    public ArrayList<Player> attack(Player target, String targetCountry, String sourceCountry, ArrayList<Integer> attackerDices, ArrayList<Integer> defenderDices) {
        int attackerArmies = this.player.getArmiesInCountry(sourceCountry);
        int defenderArmies = target.getArmiesInCountry(targetCountry);
        attackerDices.sort((Integer o1, Integer o2) -> o2 - o1);
        defenderDices.sort((Integer o1, Integer o2) -> o2 - o1);

        for (int i = 0; i < defenderDices.size(); i++) {
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
        this.player.setArmies(sourceCountry, attackerArmies);

        ArrayList<Player> defenders = new ArrayList<>();
        defenders.add(target);

        return defenders;
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public void setModel(MainModel model) {
        this.model = model;
    }

    private Player getPlayer(String country) {
        HashMap<String, Player> players = this.model.getPlayers();

        for (Map.Entry<String, Player> entry : players.entrySet()) {
            String targetCountry = "";
            HashMap<String, Integer> conqueredCountries = entry.getValue().getCountries();
            if (conqueredCountries.containsKey(country)) {
                performAttack(this.player, this.player, country, targetCountry);
                return entry.getValue();

            }
        }
        return null;
    }

    private String fetchStrongestCountry() {
        ArrayList<String> countries = new ArrayList<>(this.player.getCountries().keySet());
        String strongest = "";
        int maxArmies = 1;

        for (String country : countries) {
            int armies = this.player.getArmiesInCountry(country);

            if (maxArmies < armies) {
                maxArmies = armies;
                strongest = country;
            }
        }
        return strongest;
    }

    @SuppressWarnings("Duplicates")
    public void performAttack(Player attacker, Player defender, String sourceCountry, String targetCountry) {
        ArrayList<Integer> attackerDices = new ArrayList<>();
        ArrayList<Integer> defenderDices = new ArrayList<>();

        int attackerArmies = attacker.getArmiesInCountry(sourceCountry);
        int defenderArmies = defender.getArmiesInCountry(targetCountry);
        determineDiceRolls(attacker, defender, sourceCountry, targetCountry);

        if (attacker.getNoOfDiceRolls() < 2)
            return;

        for (int i = 0; i < attacker.getNoOfDiceRolls(); i++) {
            int roll = new Random().nextInt(6) + 1;
            attackerDices.add(roll);
        }
        for (int i = 0; i < defender.getNoOfDiceRolls(); i++) {
            int roll = new Random().nextInt(6) + 1;
            defenderDices.add(roll);
        }

        attackerDices.sort((Integer o1, Integer o2) -> o2 - o1);
        defenderDices.sort((Integer o1, Integer o2) -> o2 - o1);

        for (int i = 0; i < defenderDices.size(); i++) {
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

        HashMap<Integer, ArrayList<Integer>> attackerDiceRolls = attacker.getAttackerDices();
        if (attackerDiceRolls.get(gameNo) == null) {
            attackerDiceRolls.put(gameNo, attackerDices);
        } else {
            ArrayList<Integer> dices = attackerDiceRolls.get(gameNo);
            dices.addAll(attackerDices);
            attackerDiceRolls.put(gameNo, dices);
        }
        attacker.setAttackerDices(attackerDiceRolls);

        HashMap<Integer, ArrayList<Integer>> defenderDiceRolls = attacker.getDefenderDices();
        if (defenderDiceRolls.get(gameNo) == null) {
            defenderDiceRolls.put(gameNo, defenderDices);
        } else {
            ArrayList<Integer> dices = defenderDiceRolls.get(gameNo);
            dices.addAll(defenderDices);
            defenderDiceRolls.put(gameNo, dices);
        }
        attacker.setDefenderDices(defenderDiceRolls);

        defender.setArmies(targetCountry, defenderArmies);
        attacker.setArmies(sourceCountry, attackerArmies);

        if (attackerArmies > 1 && defenderArmies > 0)
            performAttack(attacker, defender, sourceCountry, targetCountry);

        HashMap<String, Integer> conqueredCountries = this.player.getCountries();
        HashMap<String, Player> defenders = new HashMap<>();
        gameNo = 1;

        for (Map.Entry<String, Integer> entry : conqueredCountries.entrySet()) {
            String country = entry.getKey();
            HashMap<String, Country> countries = this.model.getCountries();
            Country eachCountry = countries.get(country);

            if (this.player.getArmiesInCountry(country) > 1) {
                for (String neighbour : eachCountry.getNeighbours()) {
                    if (!conqueredCountries.containsKey(neighbour)) {
                        sourceCountry = country;
                        targetCountry = neighbour;
                        defender = getPlayer(targetCountry);

                        if (defender == null)
                            continue;

                        performAttack(this.player, defender, sourceCountry, targetCountry);
                        ArrayList<String> defenderModifiedCountries = defender.getModifiedCountries();
                        ArrayList<String> attackerModifiedCountries = this.player.getModifiedCountries();
                        attackerModifiedCountries.add(sourceCountry);
                        defenderModifiedCountries.add(targetCountry);
                        this.player.setModifiedCountries(attackerModifiedCountries);
                        defender.setModifiedCountries(defenderModifiedCountries);
                        defenders.put(defender.getName(), defender);
                        gameNo++;
                    }
                }
            }
        }
    }

    public void determineDiceRolls(Player attacker, Player defender, String sourceCountry, String targetCountry) {
        int attackerArmies = attacker.getArmiesInCountry(sourceCountry);
        int defenderArmies = defender.getArmiesInCountry(targetCountry);

        int maxDiceRollsAttacker = (attackerArmies >= 3) ? 3 : attackerArmies;
        int maxDiceRollsDefender = (defenderArmies >= 2) ? 2 : defenderArmies;
        maxDiceRollsDefender = (maxDiceRollsAttacker == maxDiceRollsDefender) ? maxDiceRollsAttacker - 1 : maxDiceRollsDefender;

        attacker.setNoOfDiceRolls(maxDiceRollsAttacker);
        defender.setNoOfDiceRolls(maxDiceRollsDefender);
    }
}
