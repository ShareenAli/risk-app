package game.main.phases;

import entity.Player;
import game.main.MainModel;

import java.util.Observable;

public class PhaseModel extends Observable {
    private Player player;
    private MainModel mainModel;

    public void updatePlayer(String name, Player player) {
        setChanged();
        notifyObservers(player);
    }
}
