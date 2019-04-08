package entity.behaviours;

import entity.Country;
import entity.Player;
import risk.game.main.MainModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class BenevolentBehaviour implements PlayerBehaviour {
    private Player player;
    private MainModel model;

    @Override
    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public String reinforcementPhase(String countryName, int armiesToAdd) {
        countryName = fetchWeakestCountry();

        this.player.addArmies(countryName, armiesToAdd);
        return countryName;
    }

    @Override
    public ArrayList<String> fortificationPhase(String sourceCountryName, String targetCountryName, int armiesToTransfer) {
        ArrayList<String> countries = new ArrayList<>(this.player.getCountries().keySet());

        targetCountryName = fetchWeakestCountry();

        sourceCountryName = fetchStrongestCountry(targetCountryName);

        if (sourceCountryName == null || sourceCountryName.equalsIgnoreCase("")) {
            sourceCountryName = countries.get((new Random()).nextInt(countries.size()));
            armiesToTransfer = 0;
        } else
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
    public ArrayList<Player> attack(Player target, String targetCountry, String sourceCountry, ArrayList<Integer> attackerDices, ArrayList<Integer> defenderDices) {
        return null;
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
        int minArmies = 99;

        for (String country : countries) {
            int armies = this.player.getArmiesInCountry(country);

            if (minArmies > armies) {
                minArmies = armies;
                weakest = country;
            }
        }
        return weakest;
    }

    private String fetchStrongestCountry(String weakestCountry) {
        HashMap<String, Integer> conqueredCountries = this.player.getCountries();
        HashMap<String, Country> countries = this.model.getCountries();
        Country country = countries.get(weakestCountry);

        String strongest = "";
        int maxArmies = 1;

        for (String neighbour : country.getNeighbours()) {
            if (conqueredCountries.containsKey(neighbour)) {
                int armies = this.player.getArmiesInCountry(neighbour);

                if (maxArmies < armies) {
                    maxArmies = armies;
                    strongest = neighbour;
                }
            }


        }
        return strongest;
    }
}
