package game.main;

import entity.Player;
import game.main.phases.PhaseController;
import support.ActivityController;

import java.util.ArrayList;

public class MainController extends ActivityController {
    private MainView view;
    private MainModel model = new MainModel();
    private PhaseController phaseController;

    public MainController(ArrayList<Player> players) {
        this.view = new MainView();
        this.model.setPlayers(players);
    }

    /**
     * It initializes the view with custom values and listeners.
     */
    @Override
    protected void prepareUi() {
        this.frame.setContentPane(this.view.$$$getRootComponent$$$());
        this.prepPhaseController();
        this.attachObservers();

        this.startGame();
    }

    private void prepPhaseController() {
        this.phaseController = new PhaseController();
        this.view.prepareView(this.phaseController.getRootPanel());
        this.phaseController.initializeValues(this.model.getPlayerNames());
    }

    private void attachObservers() {
        this.model.addObserver(this.view);
        this.model.addObserver(this.phaseController.getView());
    }

    private void startGame() {
        this.phaseController.changePhase();
    }
}
