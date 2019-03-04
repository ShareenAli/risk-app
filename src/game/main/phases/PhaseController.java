package game.main.phases;

import javax.swing.JPanel;
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

    public void setupValues(ArrayList<String> players) {
        this.model.setPlayers(players);
        this.view.addPlayers(players);
    }

    public void changePhase() {
        this.model.nextPhase();
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
