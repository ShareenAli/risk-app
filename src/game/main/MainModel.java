package game.main;

import entity.Continent;
import entity.Country;
import entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;

@SuppressWarnings("deprecation")
public class MainModel extends Observable {
    private ArrayList<String> playerNames = new ArrayList<>();
    private HashMap<String, Player> players = new HashMap<>();
    private HashMap<String, Country> countries = new HashMap<>();
    private HashMap<String, Continent> continents = new HashMap<>();

    MainModel() { }

    void setPlayers(ArrayList<Player> playerList) {
        for (Player player : playerList) {
            this.players.put(player.getName(), player);
            this.playerNames.add(player.getName());
        }
    }

    ArrayList<String> getPlayerNames() {
        return this.playerNames;
    }

    public Player getPlayer(String name) {
        return this.players.get(name);
    }

    public void updatePlayer(String name, Player player) {
        this.players.put(name, player);
        setChanged();
        notifyObservers(Player.CHANGE_PLAYER);
    }
}
