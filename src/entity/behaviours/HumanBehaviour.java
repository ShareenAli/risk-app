package entity.behaviours;

import entity.Player;
import risk.game.main.MainModel;

import java.util.ArrayList;

public class HumanBehaviour implements PlayerBehaviour {
    Player player;
    MainModel model;

    @Override
    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public String reinforcementPhase(String countryName, int armiesToAdd) {
        return null;
    }

    @Override
    public ArrayList<String> fortificationPhase(String sourceCountryName, String targetCountryName, int armiesToTransfer) {
        return null;
    }


    @Override
    public Player attack(Player target, String targetCountry, String sourceCountry, ArrayList<Integer> attackerDices, ArrayList<Integer> defenderDices) {
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
}
