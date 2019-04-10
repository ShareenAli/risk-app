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
import java.util.Map;
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
    private boolean isGameEnded = false;
    private String winner = null;

    public MainController() {
        this.view = new MainView();
    }

    public MainModel getModel() {
        return this.model;
    }

    public String getWinner() {
        return winner;
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
        super.isTournament = (boolean) data.getOrDefault(RiskApp.MainIntent.KEY_TOURNAMENT, false);

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
        int armiesAssigned;

        if (!owner.equalsIgnoreCase(this.phaseController.activePlayer())) {
            JOptionPane.showMessageDialog(new JFrame(), "You can't reinforce other player's country",
                    "Wrong move!", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (reinforcementArmies <= 0 && !isComputerPlayer) {
            JOptionPane.showMessageDialog(new JFrame(), "You don't have enough armies to reinforce",
                    "Reinforcement Phase", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        if (!isComputerPlayer) {
            NoOfArmiesDialog dialog = new NoOfArmiesDialog();
            dialog.setNoOfArmies(reinforcementArmies);
            armiesAssigned = dialog.showUi(country);
        } else
            armiesAssigned = reinforcementArmies;


        if (armiesAssigned <= 0)
            return;

        country = this.model.reinforcementPhase(owner, country, armiesAssigned);
        if (this.model.getPlayer(owner).getType() == 3) {
            ArrayList<String> modifiedCountries = this.model.getPlayer(owner).getModifiedCountries();

            for (String eachCountry : modifiedCountries) {
                int armies = this.model.getPlayer(owner).getArmiesInCountry(eachCountry);
                this.logsController.log(owner + " reinforced " + eachCountry + " with " + armies * 2 + " armies ");
            }
        } else
            this.logsController.log(owner + " reinforced " + country + " with " + armiesAssigned + " armies ");
    }

    /**
     * Start the attack phase
     *
     * @param command          action command that contains the owner and name of the country
     * @param isComputerPlayer identifier to check the type of player
     */
    @SuppressWarnings("Duplicates")
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

        if (this.model.getPlayer(attackerName).getType() == 2 || this.model.getPlayer(this.attackerName).getType() == 3) {
            HashMap<String, Integer> conqueredCountries = this.model.getPlayer(this.attackerName).getCountries();
            HashMap<String, Integer> conqueredCountriesCopy = new HashMap<>();
            conqueredCountriesCopy.putAll(conqueredCountries);
            HashMap<String, Country> countries = this.model.getCountries();

            if (this.model.getPlayer(this.attackerName).getType() == 3)
                isAllOutMode = false;

            for (Map.Entry<String, Integer> entry : conqueredCountriesCopy.entrySet()) {
                String countryName = entry.getKey().trim();
                Country eachCountry = countries.get(countryName);

                for (String neighbour : eachCountry.getNeighbours()) {
                    if (!conqueredCountriesCopy.containsKey(neighbour.trim())) {
                        this.defenderName = getPlayer(neighbour.trim());

                        if (this.defenderName == null)
                            continue;

                        this.attackTarget = neighbour.trim();
                        this.attackSource = countryName;
                        if (this.model.getPlayer(this.attackerName).getArmiesInCountry(countryName) >= 2) {
                            performAttack(isAllOutMode, isComputerPlayer);
                            this.model.changeWorldView();
                        }
                    }
                }
            }
        } else {
            performAttack(isAllOutMode, isComputerPlayer);
        }

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
        ArrayList<Player> defenders;

        ArrayList<Integer> attackerDices = new ArrayList<>();
        ArrayList<Integer> defenderDices = new ArrayList<>();

        int attackerArmies = attacker.getArmiesInCountry(this.attackSource);

        determineDiceRolls(attacker, defender);

        if (attacker.getType() != 4 && attacker.getType() != 3) {
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
        }

        defenders = attacker.attack(this.model, defender, this.attackTarget, this.attackSource, attackerDices, defenderDices);

        if (defenders == null) {
            this.logsController.log(attacker.getName() + " chose not to attack ");
            this.model.updatePlayer(attacker.getName(), attacker);
            return;
        } else if (defenders.get(0).getArmiesInCountry(this.attackTarget) == 0 && attacker.getType() != 3 && attacker.getType() != 4) {

//            int armiesUsed = attackerArmies - attacker.getArmiesInCountry(this.attackSource);
//            int armiesToMove = maxAttacker - armiesUsed;
//            int differenceInArmies = attacker.getArmiesInCountry(this.attackSource) - armiesToMove - 1;
//            if (!isComputer && differenceInArmies > 1) {
//                NoOfArmiesDialog noOfArmiesDialog = new NoOfArmiesDialog();
//                noOfArmiesDialog.setNoOfArmies(differenceInArmies);
//                int result = noOfArmiesDialog.showUi("No. of armies to move");
//                armiesToMove += result;
//                differenceInArmies -= result;
//            }
//
//            if (differenceInArmies < 1) {
//                armiesToMove--;
//                differenceInArmies++;
//            }

            int armiesToMove = attacker.getArmiesInCountry(this.attackSource) - defender.getNoOfDiceRolls();
            int armies = attacker.getArmiesInCountry(this.attackSource);
            attacker.setArmies(this.attackTarget, defender.getNoOfDiceRolls());
            armies -= defender.getNoOfDiceRolls();

            if (!isComputer && armiesToMove > 1) {
                NoOfArmiesDialog noOfArmiesDialog = new NoOfArmiesDialog();
                noOfArmiesDialog.setNoOfArmies(armiesToMove - 1);
                int result = noOfArmiesDialog.showUi("No. of armies to move");
                int targetArmies = attacker.getArmiesInCountry(this.attackTarget);
                attacker.setArmies(this.attackTarget, targetArmies + result);
                armies -= result;
            }

            attacker.setArmies(this.attackSource, armies);

            postAttackOperations(defenders.get(0));

            if (this.model.getCard(this.attackTarget) != null) {
                attacker.addCard(this.model.getCard(this.attackTarget));
                this.model.useCard(this.attackTarget);
            }

            attacker.setNoOfDiceRolls(0);
            defenders.get(0).setNoOfDiceRolls(0);

            this.logsController.log(attacker.getName() + " has won " + this.attackTarget + " from " + defenders.get(0).getName());
            this.model.updatePlayer(defenders.get(0).getName(), defenders.get(0));
            this.model.updatePlayer(attacker.getName(), attacker);
            return;
        } else if (attacker.getType() == 3) {
            attacker.setNoOfDiceRolls(0);
            defenders.get(0).setNoOfDiceRolls(0);

            int defenderArmies = defender.getArmiesInCountry(this.attackTarget);
            postAttackOperations(defenders.get(0));

            attacker.setArmies(this.attackTarget, attackerArmies);
            attacker.setArmies(this.attackSource, defenderArmies);
            this.logsController.log(attacker.getName() + " has won " + this.attackTarget + " from " + defenders.get(0).getName());
            this.model.updatePlayer(defenders.get(0).getName(), defenders.get(0));
            this.model.updatePlayer(attacker.getName(), attacker);
        } else if (attacker.getArmiesInCountry(this.attackSource) == 1) {
            this.logsController.log(defenders.get(0).getName() + " has defended " + this.attackTarget);
            this.model.updatePlayer(defenders.get(0).getName(), defenders.get(0));
            this.model.updatePlayer(attacker.getName(), attacker);
            return;
        } else
            this.logsController.log("Round ended without results");

        if (isAllOutMode)
            this.performAttack(true, isComputer);

        if (!this.isGameEnded && this.model.hasPlayerWon(attacker)) {
            if (!super.isTournament) {
                JOptionPane.showMessageDialog(new JFrame(), attacker.getName() + " has won the game!",
                    "Yeyy!", JOptionPane.INFORMATION_MESSAGE);
            }
            this.isGameEnded = true;
            this.winner = attacker.getName();
        }
    }

    /**
     * Determines the dice rolls for attacker and defender
     *
     * @param attacker Player object of the attacker
     * @param defender Player object of the defender
     */
    public int determineDiceRolls(Player attacker, Player defender) {
        int attackerArmies = attacker.getArmiesInCountry(this.attackSource);
        int defenderArmies = defender.getArmiesInCountry(this.attackTarget);

        int maxDiceRollsAttacker = (attackerArmies >= 3) ? 3 : attackerArmies;
        int maxDiceRollsDefender = (defenderArmies >= 2) ? 2 : defenderArmies;
        maxDiceRollsDefender = (maxDiceRollsAttacker == maxDiceRollsDefender) ? maxDiceRollsAttacker - 1 : maxDiceRollsDefender;

        attacker.setNoOfDiceRolls(maxDiceRollsAttacker);
        defender.setNoOfDiceRolls(maxDiceRollsDefender);

        return maxDiceRollsAttacker;
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
        if (!isConnected && this.model.getPlayer(owner).getType() != 3 && this.model.getPlayer(owner).getType() != 2) {
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

        ArrayList<String> countries = this.model.fortificationPhase(owner, this.fortSource, this.fortTarget, armiesMoved);
        if (this.model.getPlayer(owner).getType() != 3) {
            if (!countries.get(2).equalsIgnoreCase(String.valueOf(0)))
                this.logsController.log(owner + " transferred " + countries.get(2) + " armies from " + countries.get(0) + " to " + countries.get(1));
        } else {
            for (String eachCountry : countries) {
                int armies = this.model.getPlayer(owner).getArmiesInCountry(eachCountry);
                this.logsController.log(owner + " fortified " + eachCountry + " with " + armies * 2);
            }
        }
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
        if (this.isGameEnded)
            return;

        if (this.model.isEveryoneOutOfTurns()) {
            if (!super.isTournament) {
                JOptionPane.showMessageDialog(new JFrame(), "No one won the game!",
                    "Draw!", JOptionPane.INFORMATION_MESSAGE);
            }
            this.isGameEnded = true;
            return;
        }

        switch (this.phaseController.activePhase()) {
            case PhaseModel.PHASE_REINFORCEMENT:
                this.startCardPhase();
                this.model.resetArmiesToAssign(this.phaseController.activePlayer());
                this.automateReinforcementPhase();
                break;
            case PhaseModel.PHASE_FORTIFICATION:
                Player player = this.model.getPlayer(this.phaseController.activePlayer());
                player.takeTurn();
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
        if (player.getType() != Player.TYPE_HUMAN)
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
     * @param player        Player object
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

        while (this.model.getArmiesAvailableToAssign() > 0) {
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
        ArrayList<String> countries = this.model.getPotentialCountriesForAttack(countryList);
        int trials = countries.size();

        if (player.getArmiesInCountry(countryName) == 1) {
            this.logsController.log(player.getName() + " chose not to attack!");
            this.changePhase();
            return;
        }

        this.doAttackPhase(player.getName() + ":" + countryName, true);

        String anotherName;
        do {
            trials--;
            int randomIdx = new Random().nextInt(countries.size());
            anotherName = countries.get(randomIdx);
            if (this.model.checkForLink(new ArrayList<>(), this.attackSource, anotherName))
                break;
        } while (trials != 0);

        if (!this.model.checkForLink(new ArrayList<>(), this.attackSource, anotherName)) {
            this.logsController.log(player.getName() + " chose not to attack!");
            this.model.changeWorldView();
            this.changePhase();
            return;
        }

        this.doAttackPhase(this.model.getPlayerNameFromCountry(anotherName) + ":" + anotherName,
                true);

        if (this.model.hasPlayerWon(this.model.getPlayer(this.phaseController.activePlayer())))
            return;

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

    /**
     * Saves the game state data into a file called Saved_Game_Data
     *
      */
    public void saveGameState(){ this.model.saveGameState("Saved_Game_Data", this.phaseController, this.logsController); }

    /**
     * Loads the saved game state data from a file called Saved_Game_Data
     *
     */
    public void loadGameState(){ this.model.saveGameState("Saved_Game_Data", this.phaseController, this.logsController); }

    /** Fetch the owner of the country by given country name
     *
     * @param country Name of the country
     * @return String Name of the owner
     */
    private String getPlayer(String country) {
        HashMap<String, Player> players = this.model.getPlayers();

        for (Map.Entry<String, Player> entry : players.entrySet()) {
            HashMap<String, Integer> conqueredCountries = entry.getValue().getCountries();
            if (conqueredCountries.containsKey(country))
                return entry.getValue().getName();
        }
        return null;
    }

}
