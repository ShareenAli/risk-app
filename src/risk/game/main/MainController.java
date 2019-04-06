package risk.game.main;

import entity.Continent;
import entity.Country;
import entity.Player;
import risk.RiskApp;
import risk.game.main.dialog.CardDialog;
import risk.game.main.dialog.NoOfArmiesDialog;
import risk.game.main.logs.LogsController;
import risk.game.main.phases.PhaseController;
import risk.game.main.phases.PhaseModel;
import risk.game.main.world.WorldController;
import risk.support.ActivityController;
import risk.support.GameManager;

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
    private ActionListener buttonCountryLs, buttonChangePhaseLs, buttonSaveGameLs;
    private String fortSource, fortTarget;
    private String attackSource, attackTarget, attackerName = null, defenderName = null;

    public MainController() {
        this.view = new MainView();
    }

    public MainModel getModel() {
        return this.model;
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
        this.model.setBmpFile(bmpFile);

        this.phaseController.setupValues(this.model.getPlayerNames(), this.model.getPlayerColors());
        this.worldController.configureView(bmpFile, countries, this.buttonCountryLs);

        boolean isLoadedGame = (boolean) data.getOrDefault(RiskApp.MainIntent.KEY_LOAD_FLAG, false);

        if (isLoadedGame) {
            int phase = (int) data.get(RiskApp.MainIntent.KEY_PHASE);
            int playerIdx = (int) data.get(RiskApp.MainIntent.KEY_PLAYER_IDX);
            ArrayList<String> logs = (ArrayList<String>) data.get(RiskApp.MainIntent.KEY_LOGS);

            this.phaseController.setLoadGameValues(phase, playerIdx);
            this.logsController.setLogs(logs);
            this.model.changeWorldView();

            this.changePhase();
            return;
        }

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

    public PhaseModel getPhaseModel() {
        return this.phaseController.getModel();
    }

    public ArrayList<String> getLogs() {
        return this.logsController.getLogs();
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
        this.phaseController.initializeValues(this.buttonChangePhaseLs, this.buttonSaveGameLs);
    }

    /**
     * Prepare the logs controller
     */
    public void prepLogsController() {
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
                case PhaseModel.PHASE_ATTACK:
                    this.doAttackPhase(e.getActionCommand(), false);
                    break;
            }
        };

        this.buttonChangePhaseLs = (ActionEvent e) -> this.changePhase();
        this.buttonSaveGameLs = (ActionEvent e) -> this.saveGame();
    }

    private void saveGame() {
        GameManager manager = new GameManager();
        manager.initializeController(this);
        manager.saveGame();
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
     * Start the attack phase
     *
     * @param command          action command that contains the owner and name of the country
     * @param isComputerPlayer identifier to check the type of player
     */
    private void doAttackPhase(String command, boolean isComputerPlayer) {
        String owner = command.split(":")[0];
        String country = command.split(":")[1];

        if (this.attackerName == null) {
            if (!owner.equalsIgnoreCase(this.phaseController.activePlayer())) {
                if (isComputerPlayer) {
                    this.logsController.log(this.attackerName + " chose not to attack!");
                    return;
                }
                JOptionPane.showMessageDialog(new JFrame(), "You have to choose your own country to attack",
                        "Wrong move!", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (this.model.getPlayer(owner).getArmiesInCountry(country) < 2) {
                if (isComputerPlayer) {
                    this.logsController.log(this.attackerName + " chose not to attack!");
                    return;
                }
                JOptionPane.showMessageDialog(new JFrame(), "You don't have enough armies to attack",
                        "Wrong move!", JOptionPane.ERROR_MESSAGE);
                return;
            }
            this.attackerName = owner;
            this.attackSource = country;
            this.worldController.selectCountry(country);
            return;
        }

        if (this.attackSource.equalsIgnoreCase(country)) {
            this.resetAttackValues();
            this.model.changeWorldView();
            return;
        }

        if (owner.equalsIgnoreCase(this.phaseController.activePlayer())) {
            if (isComputerPlayer) {
                this.logsController.log(this.attackerName + " chose not to attack!");
                return;
            }
            JOptionPane.showMessageDialog(new JFrame(), "You can not attack your own country!",
                    "Wrong move!", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!this.model.checkForLink(new ArrayList<>(), this.attackSource, country)) {
            if (isComputerPlayer) {
                this.logsController.log(this.attackerName + " chose not to attack!");
                return;
            }
            JOptionPane.showMessageDialog(new JFrame(), this.attackSource + " is not connected to " + country,
                    "Wrong move!", JOptionPane.ERROR_MESSAGE);
            return;
        }

        this.defenderName = owner;
        this.attackTarget = country;

        boolean isAllOutMode = true;

        if (!isComputerPlayer) {
            // ask for all out mode?
            int isAllOut = JOptionPane.showConfirmDialog(null, "Would you like to attack with all out mode?",
                    "Attack Phase", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
            isAllOutMode = (isAllOut == JOptionPane.YES_OPTION);
        }

        performAttack(isAllOutMode, isComputerPlayer);

        this.resetAttackValues();
        this.model.changeWorldView();
    }

    /**
     * Perform the attack
     *
     * @param isAllOutMode Attacker can select if they want an all out mode before attacking
     */
    public void performAttack(boolean isAllOutMode, boolean isComputer) {
        Player attacker = this.model.getPlayer(this.attackerName);
        Player defender = this.model.getPlayer(this.defenderName);

        ArrayList<Integer> attackerDices = new ArrayList<>();
        ArrayList<Integer> defenderDices = new ArrayList<>();

        int attackerArmies = attacker.getArmiesInCountry(this.attackSource);
        determineDiceRolls(attacker, defender);

        if (attacker.getNoOfDiceRolls() < 2)
            return;

        for (int i = 0; i < attacker.getNoOfDiceRolls(); i++) {
            int roll = new Random().nextInt(6) + 1;
            attackerDices.add(roll);
            this.logsController.log(attacker.getName() + " rolled " + roll + " to attack");
        }
        for (int i = 0; i < defender.getNoOfDiceRolls(); i++) {
            int roll = new Random().nextInt(6) + 1;
            defenderDices.add(roll);
            this.logsController.log(defender.getName() + " rolled " + roll + " to defend");
        }

        defender = attacker.attack(defender, this.attackTarget, this.attackSource, attackerDices, defenderDices);

        if (defender.getArmiesInCountry(this.attackTarget) == 0) {
            postAttackOperations(defender);

            int armiesUsed = attackerArmies - attacker.getArmiesInCountry(this.attackSource);
            int armiesToMove = attacker.getNoOfDiceRolls() - armiesUsed;
            int differenceInArmies = attacker.getArmiesInCountry(this.attackSource) - armiesToMove;
            if (!isComputer && differenceInArmies > 1) {
                NoOfArmiesDialog noOfArmiesDialog = new NoOfArmiesDialog();
                noOfArmiesDialog.setNoOfArmies(differenceInArmies);
                int result = noOfArmiesDialog.showUi("No. of armies to move");
                armiesToMove += result;
                differenceInArmies -= result;
            }

            if (differenceInArmies < 1) {
                armiesToMove--;
                differenceInArmies++;
            }
            attacker.setArmies(this.attackTarget, armiesToMove);
            attacker.setArmies(this.attackSource, differenceInArmies);

            if (this.model.getCard(this.attackTarget) != null) {
                attacker.addCard(this.model.getCard(this.attackTarget));
                this.model.useCard(this.attackTarget);
            }

            attacker.setNoOfDiceRolls(0);
            defender.setNoOfDiceRolls(0);

            this.logsController.log(attacker.getName() + " has won " + this.attackTarget + " from " + defender.getName());
            this.model.updatePlayer(defender.getName(), defender);
            this.model.updatePlayer(attacker.getName(), attacker);
            return;
        } else if (attacker.getArmiesInCountry(this.attackSource) == 1) {
            this.logsController.log(defender.getName() + " has defended " + this.attackTarget);
            this.model.updatePlayer(defender.getName(), defender);
            this.model.updatePlayer(attacker.getName(), attacker);
            return;
        } else
            this.logsController.log("Round ended without results");

        if (isAllOutMode)
            this.performAttack(true, isComputer);

        if (this.model.hasPlayerWon(attacker)) {
            JOptionPane.showMessageDialog(new JFrame(), attacker.getName() + " has won the game!",
                    "Yeyy!", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
    }

    /**
     * Determines the dice rolls for attacker and defender
     *
     * @param attacker Player object of the attacker
     * @param defender Player object of the defender
     */
    public void determineDiceRolls(Player attacker, Player defender) {
        int attackerArmies = attacker.getArmiesInCountry(this.attackSource);
        int defenderArmies = defender.getArmiesInCountry(this.attackTarget);

        int maxDiceRollsAttacker = (attackerArmies >= 3) ? 3 : attackerArmies;
        int maxDiceRollsDefender = (defenderArmies >= 2) ? 2 : defenderArmies;
        maxDiceRollsDefender = (maxDiceRollsAttacker == maxDiceRollsDefender) ? maxDiceRollsAttacker - 1 : maxDiceRollsDefender;

        attacker.setNoOfDiceRolls(maxDiceRollsAttacker);
        defender.setNoOfDiceRolls(maxDiceRollsDefender);
    }

    /**
     * Removes the player if they do not have any countries in possession
     *
     * @param defender Player object
     */
    public void postAttackOperations(Player defender) {
        defender.removeCountry(this.attackTarget);
        HashMap<String, Integer> countries = defender.getCountries();

        if (countries.size() == 0)
            this.model.removePlayer(defender.getName());
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
            if (isComputerPlayer) {
                this.logsController.log(owner + " skipped the fortification phase!");
                return;
            }
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
        this.resetAttackValues();
        this.phaseController.changePhase();

        this.onPhaseChanged();
    }

    /**
     * Called when the phase has been changed.
     */
    private void onPhaseChanged() {
        switch (this.phaseController.activePhase()) {
            case PhaseModel.PHASE_REINFORCEMENT:
                this.startCardPhase();
                this.model.resetArmiesToAssign(this.phaseController.activePlayer());
                this.automateReinforcementPhase();
                break;
            case PhaseModel.PHASE_FORTIFICATION:
                this.automateFortificationPhase();
                break;
            case PhaseModel.PHASE_ATTACK:
                this.automateAttackPhase();
                break;
        }
    }

    /**
     * Start the card phase for the player
     */
    private void startCardPhase() {
        Player player = this.model.getPlayer(this.phaseController.activePlayer());
        if (player.getType() == Player.TYPE_COMPUTER)
            return;

        ArrayList<String> cards = player.getCards();

        if (cards.size() == 0)
            return;

        CardDialog dialog = new CardDialog();
        dialog.setupCards(cards);

        int result = dialog.showUi();

        ArrayList<String> selectedCards = dialog.getSelectedCards();

        if (cards.size() == 5 && selectedCards.size() < 3)
            this.startCardPhase();


        String cardType = performExchange(selectedCards, player);
        if (!cardType.equalsIgnoreCase("")) {
            if (cardType.equalsIgnoreCase("distinct"))
                this.logsController.log(player.getName() + " exchanged three different cards for armies!");
            else
                this.logsController.log(player.getName() + " exchanged three " + cardType + " cards for armies!");

            this.model.updatePlayerWithoutNotify(player.getName(), player);
        }
    }

    /**
     * Perform the card exchange for additional armies
     *
     * @param selectedCards The cards selected by the user
     * @param player Player object
     * @return String returns the card type
     */
    public String performExchange(ArrayList<String> selectedCards, Player player) {
        if (selectedCards.size() > 2) {
            String cardType1 = selectedCards.get(0), cardType2 = selectedCards.get(1), cardType3 = selectedCards.get(2);
            if (cardType1.equalsIgnoreCase(cardType2) && cardType2.equalsIgnoreCase(cardType3)) {
                player.useSameCard(cardType1);
                return cardType1;

            }
            if (!cardType1.equalsIgnoreCase(cardType2) && !cardType2.equalsIgnoreCase(cardType3)) {
                player.useDistinctCards();
                return "distinct";

            }
        }
        return "";
    }

    /**
     * It automates the reinforcement phase when it's computer player
     */
    private void automateReinforcementPhase() {
        Player player = this.model.getPlayer(this.phaseController.activePlayer());

        if (player.getType() == Player.TYPE_HUMAN)
            return;

        ArrayList<String> countries = new ArrayList<>(player.getCountries().keySet());
        String countryName = countries.get((new Random()).nextInt(countries.size()));

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

        ArrayList<String> countries = new ArrayList<>(player.getCountries().keySet());
        String countryName = countries.get((new Random()).nextInt(countries.size()));

        if (player.getArmiesInCountry(countryName) == 1) {
            this.logsController.log(player.getName() + " skipped the fortification phase!");
            this.changePhase();
            return;
        }

        this.doFortificationPhase(player.getName() + ":" + countryName, true, 0);

        do {
            countryName = countries.get((new Random()).nextInt(countries.size()));
            if (this.model.checkForLink(new ArrayList<>(), this.fortSource, countryName))
                break;
        } while (!countryName.equalsIgnoreCase(this.fortSource));

        int armiesToMove = (new Random()).nextInt(player.getArmiesInCountry(this.fortSource) - 1);
        this.doFortificationPhase(player.getName() + ":" + countryName, true, armiesToMove);

        this.changePhase();
    }

    /**
     * Automate the attack phase for a computer player
     */
    private void automateAttackPhase() {
        Player player = this.model.getPlayer(this.phaseController.activePlayer());

        if (player.getType() == Player.TYPE_HUMAN)
            return;

        ArrayList<String> countryList = new ArrayList<>(player.getCountries().keySet());
        String countryName = countryList.get((new Random()).nextInt(countryList.size()));

        if (player.getArmiesInCountry(countryName) == 1) {
            this.logsController.log(player.getName() + " chose not to attack!");
            this.changePhase();
            return;
        }

        this.doAttackPhase(player.getName() + ":" + countryName, true);

        ArrayList<String> countries = new ArrayList<>(this.model.getCountries().keySet());
        String anotherName = countries.get((new Random()).nextInt(countryList.size()));

        if (!this.model.checkForLink(new ArrayList<>(), this.fortSource, anotherName)) {
            this.logsController.log(player.getName() + " chose not to attack!");
            this.changePhase();
            return;
        }

        this.doAttackPhase(player.getName() + ":" + anotherName, true);

        this.changePhase();
    }

    /**
     * It resets the values associated with the attack phase
     */
    private void resetAttackValues() {
        this.attackSource = this.attackTarget = this.attackerName = this.defenderName = null;
    }

    /**
     * Set the attacking country
     *
     * @param attackSource Name of the attacking country
     */
    public void setAttackSource(String attackSource) {
        this.attackSource = attackSource;
    }

    /**
     * Set the attack target
     *
     * @param attackTarget Name of the target country
     */
    public void setAttackTarget(String attackTarget) {
        this.attackTarget = attackTarget;
    }

    /**
     * Set the model object
     *
     * @param model Model object for data manipulation
     */
    public void setModel(MainModel model) {
        this.model = model;
    }

    /**
     * Set the Name of the attacker
     *
     * @param attackerName Name of the attacker
     */
    public void setAttackerName(String attackerName) {
        this.attackerName = attackerName;
    }

    /**
     * Set the Name of the defender
     *
     * @param defenderName Name of the defender
     */
    public void setDefenderName(String defenderName) {
        this.defenderName = defenderName;
    }

}
