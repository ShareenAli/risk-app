package game.main;

import entity.Player;
import game.main.logs.LogsController;
import game.main.phases.PhaseController;
import support.ActivityController;

import java.util.ArrayList;

public class MainController extends ActivityController {
    private MainView view;
    private MainModel model = new MainModel();
    private PhaseController phaseController;
    private LogsController logsController;

    public MainController(ArrayList<Player> players) {
        this.view = new MainView();
        this.model.setPlayers(players);
    }

    /**
     * It initializes the view with custom values and listeners.
     */
    @Override
    protected void prepareUi() {
        startupPhase();
        this.frame.setContentPane(this.view.$$$getRootComponent$$$());
        this.prepControllers();
        this.view.prepareView(this.phaseController.getRootPanel(), this.logsController.getRootPanel());
        this.attachObservers();

        this.startGame();
    }

    private void prepControllers() {
        this.prepPhaseController();
        this.prepLogsController();
    }

    private void prepPhaseController() {
        this.phaseController = new PhaseController();
        this.phaseController.initializeValues(this.model.getPlayerNames());
    }

    private void prepLogsController() {
        this.logsController = new LogsController();
        this.logsController.initializeValues();
    }

    /**
     * Perform the startup Phase operations
     * Assign the countries to the players
     * Assign the armies to the assigned countries
     */
    private void startupPhase() {
        this.model.assignCountry();
        this.model.assignArmies();
    }

    /**
     * Attach the observers for the model
     */
    private void attachObservers() {
        this.model.addObserver(this.view);
        this.model.addObserver(this.phaseController.getView());
        this.model.addObserver(this.logsController.getView());
    }

    private void startGame() {
        this.phaseController.changePlayer();
        this.phaseController.changePhase();
    }
}
