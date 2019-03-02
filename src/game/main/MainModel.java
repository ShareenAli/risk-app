package game.main;

import entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;

@SuppressWarnings("deprecation")
public class MainModel extends Observable {
    private HashMap<String, Player> players = new HashMap<>();

    public MainModel() { }

    public void setPlayers(ArrayList<Player> playerList) {
        for (Player player : playerList) {
            this.players.put(player.getName(), player);
        }
    }

    public Player getPlayer(String name) {
        return this.players.get(name);
    }

    public void updatePlayer(String name, Player player) {
        this.players.put(name, player);
        setChanged();
        notifyObservers(player);
    }
}
