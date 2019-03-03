package game.main.phases;

import javax.swing.JPanel;
import java.util.ArrayList;

public class PhaseController {
    private PhaseView view;
    private PhaseModel model;

    public PhaseController() {
        this.view = new PhaseView();
    }

    public void initializeValues(ArrayList<String> players) {
        this.model = new PhaseModel(players);
        this.view.initializeValues();
        this.view.addPlayers(players);

        this.model.addObserver(this.view);
    }

    public void changePhase() {
        this.model.nextPhase();
    }

    public JPanel getRootPanel() {
        return (JPanel) this.view.$$$getRootComponent$$$();
    }

    public PhaseView getView() {
        return this.view;
    }
}
