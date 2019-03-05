package game.main.phases;

import java.awt.*;
import java.util.ArrayList;
import java.util.Observable;

@SuppressWarnings("deprecation")
public class PhaseModel extends Observable {
    public static final int PHASE_REINFORCEMENT = 0;
    public static final int PHASE_ATTACK = 1;
    public static final int PHASE_FORTIFICATION = 2;

    public static final String CHANGE_PHASE = "change:phase";
    public static final String CHANGE_PLAYER = "change:player";

    private String[] phaseNames = {"Reinforcement", "Attack", "Fortification"};
    private int phase = -1;
    private int playerIndex = -1;
    private int totalPlayers;
    private ArrayList<String> players;
    private ArrayList<Color> colors;

    PhaseModel() { }

    void setPlayers(ArrayList<String> players, ArrayList<Color> colors) {
        this.totalPlayers = players.size();
        this.players = players;
        this.colors = colors;
    }

    void nextPhase() {
        this.phase++;

        if (this.phase == 3) {
            this.phase = 0;
            this.nextPlayer();
        }

        setChanged();
        notifyObservers(CHANGE_PHASE);
    }

    void nextPlayer() {
        this.playerIndex = (this.playerIndex < this.totalPlayers - 1) ? this.playerIndex + 1 : 0;
        setChanged();
        notifyObservers(CHANGE_PLAYER);
    }

    String getActivePlayer() {
        return this.players.get(this.playerIndex);
    }

    Color getActiveColor() {
        return this.colors.get(this.playerIndex);
    }

    int getActivePhase() {
        return this.phase;
    }

    String getActivePhaseName() {
        return this.phaseNames[this.phase];
    }
}
