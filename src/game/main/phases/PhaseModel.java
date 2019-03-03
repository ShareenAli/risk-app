package game.main.phases;

import java.util.ArrayList;
import java.util.Observable;

@SuppressWarnings("deprecation")
public class PhaseModel extends Observable {
    public static final int PHASE_CARD = 0;
    public static final int PHASE_REINFORCEMENT = 1;
    public static final int PHASE_ATTACK = 2;
    public static final int PHASE_FORTIFICATION = 3;

    public static final String CHANGE_PHASE = "change:phase";
    public static final String CHANGE_PLAYER = "change:player";

    private String[] phaseNames = {"Card", "Reinforcement", "Attack", "Fortification"};
    private int phase = -1;
    private int playerIndex = 0;
    private int totalPlayers;
    private ArrayList<String> players;

    PhaseModel(ArrayList<String> players) {
        this.totalPlayers = players.size();
        this.players = players;
    }

    void nextPhase() {
        this.phase++;

        if (this.phase == 4) {
            this.phase = 0;
            this.nextPlayer();
        }

        setChanged();
        notifyObservers(CHANGE_PHASE);
    }

    void nextPlayer() {
        this.playerIndex = (this.playerIndex < this.totalPlayers - 1) ? this.totalPlayers + 1 : 0;
        setChanged();
        notifyObservers(CHANGE_PLAYER);
    }

    public String getActivePlayer() {
        return this.players.get(this.playerIndex);
    }

    public int getActivePhase() {
        return this.phase;
    }

    public String getActivePhaseName() {
        return this.phaseNames[this.phase];
    }
}
