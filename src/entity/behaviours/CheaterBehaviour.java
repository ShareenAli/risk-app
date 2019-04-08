package entity.behaviours;

import entity.Country;
import entity.Player;
import risk.game.main.MainModel;

import java.util.ArrayList;
import java.util.HashMap;

public class CheaterBehaviour implements PlayerBehaviour {
    private Player player;
    private MainModel model;

    @Override
    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public String reinforcementPhase(String countryName, int armiesToAdd) {
        ArrayList<String> countries = new ArrayList<>(this.player.getCountries().keySet());

        for (String country : countries) {
            this.player.addArmies(country, this.player.getArmiesInCountry(country));
        }

        this.player.clearModifiedCountries();
        this.player.setModifiedCountries(countries);
        return countryName;
    }

    @Override
    public ArrayList<String> fortificationPhase(String sourceCountryName, String targetCountryName, int armiesToTransfer) {
        ArrayList<String> modifiedCountries = new ArrayList<>();
        ArrayList<String> conqueredCountries = new ArrayList<>(this.player.getCountries().keySet());
        HashMap<String, Country> countries = this.model.getCountries();

        for (String country : conqueredCountries) {
            boolean flag = false;
            Country eachCountry = countries.get(country);
            ArrayList<String> neighbours = eachCountry.getNeighbours();

            for (String neighbour : neighbours) {
                if (!conqueredCountries.contains(neighbour)) {
                    flag = true;
                    targetCountryName = country;
                    break;
                }
            }

            if (flag) {
                armiesToTransfer = this.player.getArmiesInCountry(sourceCountryName);
                this.player.addArmies(targetCountryName, armiesToTransfer);
                modifiedCountries.add(targetCountryName);
            }
        }

        return modifiedCountries;
    }

    @Override
    public ArrayList<Player> attack(Player target, String targetCountry, String sourceCountry, ArrayList<Integer> attackerDices, ArrayList<Integer> defenderDices) {
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
