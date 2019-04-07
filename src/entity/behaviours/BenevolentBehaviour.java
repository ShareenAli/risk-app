package entity.behaviours;

import entity.Player;
import risk.game.main.MainModel;

import java.util.ArrayList;
import java.util.Random;

public class BenevolentBehaviour implements PlayerBehaviour {
    Player player;
    MainModel model;

    @Override
    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public String reinforcementPhase(String countryName, int armiesToAdd) {
        countryName = fetchWeakestCountry();

        return countryName;
    }

    @Override
    public ArrayList<String> fortificationPhase(String sourceCountryName, String targetCountryName, int armiesToTransfer) {
        ArrayList<String> countries = new ArrayList<>();
        sourceCountryName = fetchStrongestCountry();

        targetCountryName = fetchWeakestCountry();
//            if (this.model.checkForLink(new ArrayList<>(), sourceCountryName, targetCountryName))
//                break;


        armiesToTransfer = this.player.getArmiesInCountry(sourceCountryName) - 1;

        countries.clear();
        countries.add(sourceCountryName);
        countries.add(targetCountryName);
        countries.add(String.valueOf(armiesToTransfer));

        return countries;
    }

    @Override
    public Player attack(Player target, String targetCountry, String sourceCountry, ArrayList<Integer> attackerDices, ArrayList<Integer> defenderDices) {
        return target;
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public void setModel(MainModel model) {
        this.model = model;
    }

    private String fetchWeakestCountry() {
        ArrayList<String> countries = new ArrayList<>(this.player.getCountries().keySet());
        String weakest = "";
        int minArmies = 50;

        for (String country : countries) {
            int armies = this.player.getArmiesInCountry(country);

            if (minArmies > armies) {
                minArmies = armies;
                weakest = country;
            }
        }
        return weakest;
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
}
