package game.main.phases;

import javax.swing.JPanel;
import java.awt.*;
import java.util.ArrayList;

public class PhaseController {
    private PhaseView view;
    private PhaseModel model;

    public PhaseController() {
        this.view = new PhaseView();
        this.model = new PhaseModel();
    }

    public void initializeValues() {
        this.view.initializeValues();

        this.model.addObserver(this.view);
    }

    public void setupValues(ArrayList<String> players, ArrayList<Color> colors) {
        this.model.setPlayers(players, colors);
        this.view.addPlayers(players);
    }

    public void changePhase() {
        this.model.nextPhase();
    }

    public int activePhase() {
        return this.model.getActivePhase();
    }

    public String activePlayer() {
        return this.model.getActivePlayer();
    }

    public void changePlayer() {
        this.model.nextPlayer();
    }

    public JPanel getRootPanel() {
        return (JPanel) this.view.$$$getRootComponent$$$();
    }

    public PhaseView getView() {
        return this.view;
    }
}
