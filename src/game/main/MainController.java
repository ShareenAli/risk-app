package game.main;

import entity.Continent;
import entity.Country;
import entity.Player;
import game.main.dialog.ReinforcementDialog;
import game.main.logs.LogsController;
import game.main.phases.PhaseController;
import game.main.phases.PhaseModel;
import game.main.world.WorldController;
import risk.RiskApp;
import support.ActivityController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class MainController extends ActivityController {
    private MainView view;
    private MainModel model = new MainModel();
    private PhaseController phaseController;
    private LogsController logsController;
    private WorldController worldController;
    private ActionListener buttonCountryLs;

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

        this.phaseController.setupValues(this.model.getPlayerNames(), this.model.getPlayerColors());
        this.worldController.configureView(bmpFile, countries, this.buttonCountryLs);

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
        this.initCountryClickListeners();
    }

    private void prepControllers() {
        this.prepPhaseController();
        this.prepLogsController();
        this.prepWorldController();
    }

    private void prepPhaseController() {
        this.phaseController = new PhaseController();
        this.phaseController.initializeValues();
    }

    private void prepLogsController() {
        this.logsController = new LogsController();
        this.logsController.initializeValues();
    }

    private void prepWorldController() {
        this.worldController = new WorldController();
        this.worldController.initializeValues();
    }

    private void initCountryClickListeners() {
        this.buttonCountryLs = (ActionEvent e) -> {
            switch (this.phaseController.activePhase()) {
                case PhaseModel.PHASE_REINFORCEMENT:
                    this.doReinforcementPhase(e.getActionCommand());
                    break;
            }

            System.out.println(e.getActionCommand());
        };
    }

    private void doReinforcementPhase(String command) {
        String owner = command.split(":")[0];
        String country = command.split(":")[1];
        int reinforcementArmies = this.model.getArmiesAvailableToAssign();

        if (!owner.equalsIgnoreCase(this.phaseController.activePlayer())) {
            JOptionPane.showMessageDialog(new JFrame(), "You can't reinforce other player's country",
                "Wrong move!", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (reinforcementArmies == 0) {
            JOptionPane.showMessageDialog(new JFrame(), "You don't have enough armies to reinforce",
                "Reinforcement Phase", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        ReinforcementDialog dialog = new ReinforcementDialog();
        dialog.setNoOfArmies(reinforcementArmies);
        int armiesAssigned = dialog.showUi(country);

        this.model.reinforcementPhase(owner, country, armiesAssigned);
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
        this.changePhase();
    }

    private void changePhase() {
        this.model.resetArmiesToAssign();
        this.phaseController.changePhase();
    }
}
