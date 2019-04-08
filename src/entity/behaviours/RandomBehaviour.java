package entity.behaviours;

import entity.Player;
import risk.game.main.MainModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class RandomBehaviour implements PlayerBehaviour {
    private Player player;
    private MainModel model;

    @Override
    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public String reinforcementPhase(String countryName, int armiesToAdd) {
        ArrayList<String> countries = new ArrayList<>(this.player.getCountries().keySet());
        countryName = countries.get((new Random()).nextInt(countries.size()));

        this.player.addArmies(countryName, armiesToAdd);
        return countryName;
    }

    @Override
    public ArrayList<String> fortificationPhase(String sourceCountryName, String targetCountryName, int armiesToTransfer) {
        ArrayList<String> countries = new ArrayList<>(this.player.getCountries().keySet());

        do {
            sourceCountryName = countries.get((new Random()).nextInt(countries.size()));
            armiesToTransfer = this.player.getArmiesInCountry(sourceCountryName) - 1;

            if (armiesToTransfer > 0)
                break;
        } while (true);

        do {
            targetCountryName = countries.get((new Random()).nextInt(countries.size()));
            if (this.model.checkForLink(new ArrayList<>(), sourceCountryName, targetCountryName))
                break;
        } while (targetCountryName.equalsIgnoreCase(sourceCountryName));

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
}
