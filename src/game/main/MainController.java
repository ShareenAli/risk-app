package game.main;

import entity.Continent;
import entity.Country;
import entity.Player;
import game.main.logs.LogsController;
import game.main.phases.PhaseController;
import game.main.world.WorldController;
import risk.RiskApp;
import support.ActivityController;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class MainController extends ActivityController {
    private MainView view;
    private MainModel model = new MainModel();
    private PhaseController phaseController;
    private LogsController logsController;
    private WorldController worldController;

    public MainController() {
        this.view = new MainView();
    }

    @SuppressWarnings("unchecked")
    public void setupValues(HashMap<String, Object> data) {
        ArrayList<Player> players = (ArrayList<Player>) data.get(RiskApp.MainIntent.KEY_PLAYERS);
        ArrayList<Country> countries = (ArrayList<Country>) data.get(RiskApp.MainIntent.KEY_COUNTRIES);
        ArrayList<Continent> continents = (ArrayList<Continent>) data.get(RiskApp.MainIntent.KEY_CONTINENT);
        File bmpFile = (File) data.get(RiskApp.MainIntent.kEY_BMP);

        this.model.setPlayers(players);
        this.model.setMapContent(countries, continents);

        this.worldController.configureView(bmpFile, countries);

        this.startGame();
    }

    /**
     * It initializes the view with custom values and listeners.
     */
    @Override
    protected void prepareUi() {
        this.frame.setContentPane(this.view.$$$getRootComponent$$$());
        this.prepControllers();
        this.view.prepareView(this.phaseController.getRootPanel(), this.logsController.getRootPanel(),
            this.worldController.getRootPanel());
        this.attachObservers();
    }

    private void prepControllers() {
        this.prepPhaseController();
        this.prepLogsController();
        this.prepWorldController();
    }

    private void prepPhaseController() {
        this.phaseController = new PhaseController();
        this.phaseController.initializeValues(this.model.getPlayerNames());
    }

    private void prepLogsController() {
        this.logsController = new LogsController();
        this.logsController.initializeValues();
    }

    private void prepWorldController() {
        this.worldController = new WorldController();
        this.worldController.initializeValues();
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
        this.model.addObserver(this.worldController.getView());
    }

    private void startGame() {
        this.startupPhase();

        this.phaseController.changePlayer();
        this.phaseController.changePhase();
    }
}
