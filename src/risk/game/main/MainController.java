package risk.game.main;

import entity.Continent;
import entity.Country;
import entity.Player;
import risk.RiskApp;
import risk.game.main.dialog.NoOfArmiesDialog;
import risk.game.main.logs.LogsController;
import risk.game.main.phases.PhaseController;
import risk.game.main.phases.PhaseModel;
import risk.game.main.world.WorldController;
import risk.support.ActivityController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * The controller for the main view
 *
 * @author shareenali, iamdc003
 * @version 0.2
 */

public class MainController extends ActivityController {
    private MainView view;
    private MainModel model = new MainModel();
    private PhaseController phaseController;
    private LogsController logsController;
    private WorldController worldController;
    private ActionListener buttonCountryLs, buttonChangePhaseLs;
    private String fortSource, fortTarget;
    private String attackSourceCountry, attackTargetCountry, attackerName, defendantName;

    public MainController() {
        this.view = new MainView();
    }

    /**
     * Setup values when the controller is loaded into the game
     *
     * @param data data to get from the previous controller
     */
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
        this.initListeners();
        this.prepControllers();
        this.view.prepareView(this.phaseController.getRootPanel(), this.logsController.getRootPanel(),
                this.worldController.getRootPanel());
        this.attachObservers();
    }

    /**
     * Prepare the child controllers for the views inside
     */
    private void prepControllers() {
        this.prepPhaseController();
        this.prepLogsController();
        this.prepWorldController();
    }

    /**
     * Prepare the phase controller
     */
    private void prepPhaseController() {
        this.phaseController = new PhaseController();
        this.phaseController.initializeValues(this.buttonChangePhaseLs);
    }

    /**
     * Prepare the logs controller
     */
    private void prepLogsController() {
        this.logsController = new LogsController();
        this.logsController.initializeValues();
    }

    /**
     * Prepare the world controller
     */
    private void prepWorldController() {
        this.worldController = new WorldController();
    }

    /**
     * Initialize the view listeners
     */
    private void initListeners() {
        this.buttonCountryLs = (ActionEvent e) -> {
            switch (this.phaseController.activePhase()) {
                case PhaseModel.PHASE_REINFORCEMENT:
                    this.doReinforcementPhase(e.getActionCommand(), false);
                    break;
                case PhaseModel.PHASE_FORTIFICATION:
                    this.doFortificationPhase(e.getActionCommand(), false, 0);
                    break;
            }
        };

        this.buttonChangePhaseLs = (ActionEvent e) -> this.changePhase();

    }

    /**
     * Performs the reinforcement phase when triggered from the UI
     *
     * @param command          action command that contains the owner and name of the country
     * @param isComputerPlayer identifier to check the type of player
     */
    private void doReinforcementPhase(String command, boolean isComputerPlayer) {
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

        NoOfArmiesDialog dialog = new NoOfArmiesDialog();
        dialog.setNoOfArmies(reinforcementArmies);
        int armiesAssigned = isComputerPlayer ? (new Random()).nextInt(reinforcementArmies + 1)
                : dialog.showUi(country);

        if (armiesAssigned == 0)
            return;

        this.model.reinforcementPhase(owner, country, armiesAssigned);
        this.logsController.log(owner + " reinforced " + country + " with " + armiesAssigned + " armies ");
    }

    /**
     * Checking attack feasibility
     *
     * @param owner        Name of the owner of the country
     * @param attackSource The attacking country
     * @param attackTarget The country being attacked
     * @return True or false for feasibility
     */
    private boolean isAttackPossible(String owner, String attackSource, String attackTarget) {
        Player player = this.model.getPlayer(owner);
        boolean feasible = this.model.checkIfAttackFeasible(player, attackSource, attackTarget);

        if (!feasible) {
            JOptionPane.showMessageDialog(new JFrame(), this.attackSourceCountry + " and " + attackTarget +
                    " are not neighbours!", "Invalid Move!", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        boolean minArmyCriteria = this.model.checkMinArmiesForAttack(player, attackSource);

        if (!minArmyCriteria) {
            JOptionPane.showMessageDialog(new JFrame(), this.attackSourceCountry + "does not " +
                    "have sufficient armies!", "Invalid Move!", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    /**
     * Perform the attack phase
     *
     * @param command          action command that contains the owner and name of the country
     * @param isComputerPlayer identifier to check the type of player
     * @param allOutMode       States whether user has opted for alloutmode or not
     */
    private void doAttackPhase(String command, boolean isComputerPlayer, boolean allOutMode) {
        String owner = command.split(":")[0];
        String country = command.split(":")[1];

        if (this.attackSourceCountry == null) {
            this.attackerName = owner;
            this.attackSourceCountry = country;
            this.worldController.selectCountry(country);
            return;
        }

        this.defendantName = owner;
        this.attackTargetCountry = country;

        if (owner.equalsIgnoreCase(this.phaseController.activePlayer())) {
            JOptionPane.showMessageDialog(new JFrame(), "You can't attack your own country",
                    "Wrong move!", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean feasible = isAttackPossible(attackerName, this.attackSourceCountry, this.attackTargetCountry);

        if (!feasible)
            return;

        Player attacker = this.model.getPlayer(this.attackerName);
        Player defendant = this.model.getPlayer(this.defendantName);

        boolean outcome = this.model.attackPhase(this.attackerName, this.defendantName, this.attackSourceCountry, this.attackTargetCountry, allOutMode);
        // if won --> attackerName moves the armies to the newly conquered country, update the players countries and armies, award a card

        if (!outcome) {
            this.model.updateEntitiesAfterAttack(defendant, attacker, attackTargetCountry);

            HashMap<String, Player> players = this.model.getPlayers();

            if (players.size() == 1) {
                this.logsController.log(attacker.getName() + " HAS WON THE GAME!!");
                return;
            } else {
                int armiesInSourceCountry = attacker.getArmiesInCountry(attackSourceCountry);

                NoOfArmiesDialog dialog = new NoOfArmiesDialog();
                dialog.setNoOfArmies(armiesInSourceCountry);

                int armiesMoved = isComputerPlayer ? (new Random()).nextInt(armiesInSourceCountry)
                        : dialog.showUi(attackSourceCountry);

                this.model.processPostAttackPhase(attacker, this.attackSourceCountry, this.attackTargetCountry, armiesMoved);

                this.logsController.log(attacker.getName() + " won the battle");
            }
        } else
            this.logsController.log(attacker.getName() + " lost the battle");
    }

    /**
     * Checks whether the fortification phase is possible or not.
     *
     * @param owner owner of the country
     * @return boolean true if it is possible
     */
    private boolean isFortificationPossible(String owner) {
        if (!owner.equalsIgnoreCase(this.phaseController.activePlayer())) {
            JOptionPane.showMessageDialog(new JFrame(), "You can't move army to other player's country",
                    "Wrong move!", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (this.fortSource != null && this.fortTarget != null) {
            JOptionPane.showMessageDialog(new JFrame(), "You're out of moves for fortification phase",
                    "No more moves allowed!", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    /**
     * Performs the fortification phase based on the UI actions
     *
     * @param command          action commands that contains owner and name of the country.
     * @param isComputerPlayer identifier to check the type of player
     * @param armiesMoved      number of armies to move from one country to another
     */
    private void doFortificationPhase(String command, boolean isComputerPlayer, int armiesMoved) {
        String owner = command.split(":")[0];
        String country = command.split(":")[1];

        if (!isFortificationPossible(owner))
            return;

        if (this.fortSource == null) {
            this.fortSource = country;
            this.worldController.selectCountry(country);
            return;
        }

        boolean isConnected = this.model.checkForLink(new ArrayList<>(), this.fortSource, country);
        if (!isConnected) {
            JOptionPane.showMessageDialog(new JFrame(), this.fortSource + " and " + country +
                    " are not connected!", "No more moves allowed!", JOptionPane.ERROR_MESSAGE);
            return;
        }

        this.fortTarget = country;

        if (!isComputerPlayer)
            armiesMoved = selectFortificationArmies(owner);

        if (armiesMoved == 0) {
            this.fortTarget = this.fortSource = null;
            return;
        }

        this.model.fortificationPhase(owner, this.fortSource, this.fortTarget, armiesMoved);
        this.logsController.log(owner + " transferred " + armiesMoved + " armies from " + this.fortSource + " to " + this.fortTarget);
    }

    /**
     * Displays popup to select number of armies to transfer
     *
     * @param owner owner of the country
     * @return Integer number of armies to transfer
     */
    private int selectFortificationArmies(String owner) {
        NoOfArmiesDialog dialog = new NoOfArmiesDialog();
        int armies = this.model.getPlayer(owner).getArmiesInCountry(this.fortSource) - 1;
        dialog.setNoOfArmies(armies);
        return dialog.showUi(this.fortSource + " to " + this.fortTarget);
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

    /**
     * Start the game initially
     */
    private void startGame() {
        this.startupPhase();

        this.phaseController.changePlayer();
        this.changePhase();
    }

    /**
     * Changes the phase from one to another.
     * Automatically changes the players when the last phase is changed.
     */
    private void changePhase() {
        this.fortSource = this.fortTarget = null;
        this.phaseController.changePhase();
        this.model.resetArmiesToAssign(this.phaseController.activePlayer());


        this.onPhaseChanged();
    }

    /**
     * Called when the phase has been changed.
     */
    private void onPhaseChanged() {
        switch (this.phaseController.activePhase()) {
            case PhaseModel.PHASE_REINFORCEMENT:

                automateReinforcementPhase();
                break;
            case PhaseModel.PHASE_FORTIFICATION:
                automateFortificationPhase();
                break;
        }
    }

    /**
     * It automates the reinforcement phase when it's computer player
     */
    private void automateReinforcementPhase() {
        Player player = this.model.getPlayer(this.phaseController.activePlayer());

        if (player.getType() == Player.TYPE_HUMAN)
            return;

        ArrayList<String> list = new ArrayList<>(player.getCountries().keySet());
        String countryName = list.get((new Random()).nextInt(list.size()));

        while (this.model.getArmiesAvailableToAssign() != 0) {
            this.doReinforcementPhase(player.getName() + ":" + countryName, true);
        }

        this.changePhase();
    }

    /**
     * It automates the fortification phase when it's computer player
     */
    private void automateFortificationPhase() {
        Player player = this.model.getPlayer(this.phaseController.activePlayer());

        if (player.getType() == Player.TYPE_HUMAN)
            return;

        ArrayList<String> list = new ArrayList<>(player.getCountries().keySet());
        String countryName = list.get((new Random()).nextInt(list.size()));

        if (player.getArmiesInCountry(countryName) == 1) {
            this.logsController.log(player.getName() + " skipped the fortification phase!");
            this.changePhase();
            return;
        }

        this.doFortificationPhase(player.getName() + ":" + countryName, true, 0);

        do {
            countryName = list.get((new Random()).nextInt(list.size()));
            if (this.model.checkForLink(new ArrayList<>(), this.fortSource, countryName))
                break;
        } while (!countryName.equalsIgnoreCase(this.fortSource));

        int armiesToMove = (new Random()).nextInt(player.getArmiesInCountry(this.fortSource) - 1);
        this.doFortificationPhase(player.getName() + ":" + countryName, true, armiesToMove);

        this.changePhase();
    }
}
