package entity.behaviours;

import entity.Player;
import risk.game.main.MainModel;

import java.util.ArrayList;

public interface PlayerBehaviour {

    void setPlayer(Player player);

    String reinforcementPhase(String countryName, int armiesToAdd);

    ArrayList<String> fortificationPhase(String sourceCountryName, String targetCountryName, int armiesToTransfer);

    Player attack(Player target, String targetCountry, String sourceCountry, ArrayList<Integer> attackerDices, ArrayList<Integer> defenderDices);

    Player getPlayer();

    void setModel(MainModel model);
}
