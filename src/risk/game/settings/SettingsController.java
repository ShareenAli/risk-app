package risk.game.settings;

import risk.game.main.MainController;
import risk.RiskApp;
import risk.support.ActivityController;
import risk.support.DisplayFileChooser;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;

/**
 * It initializes the game settings.
 * When the new games is about to start, it has to go through this phase
 *      - in order to initialize the important parameters for game.
 * @author shareenali
 * @version 0.1
 */
public class SettingsController extends ActivityController {
    private SettingsView view;
    private ActionListener comboNoPlayersLs, buttonStartLs, buttonMapLs, buttonBmpLs;
    private DisplayFileChooser fileChooser = new DisplayFileChooser("/Users/ndkcha/Documents/university/app/risk-maps");

    /**
     * It initializes the controller
     */
    public SettingsController() {
        this.view = new SettingsView();
    }

    /**
     * It initializes the view with custom values and listeners
     */
    @Override
    protected void prepareUi() {
        this.frame.setContentPane(this.view.$$$getRootComponent$$$());
        this.initListeners();
        this.bindListeners();
    }

    public void setupValues() {
        this.view.initializeValues();
    }

    /**
     * Bind the appropriate listeners into the UI
     */
    private void bindListeners() {
        this.view.bindComboNoPlayersListeners(this.comboNoPlayersLs);
        this.view.bindButtonStartListeners(this.buttonStartLs);
        this.view.bindMapButtonsListeners(this.buttonMapLs, this.buttonBmpLs);
    }

    /**
     * Initialize the action listeners for UI
     */
    private void initListeners() {
        this.comboNoPlayersLs = (ActionEvent e) -> {
            this.view.createPlayerInfoPanels();
            this.frame.pack();
        };

        this.buttonStartLs = (ActionEvent e) -> startGame();

        this.buttonMapLs = (ActionEvent e) -> {
            this.fileChooser.updateExtension("risk/map");
            File file = this.fileChooser.openFile();

            if (file != null) {
                SettingsModel.getInstance().setMapFile(file);
                this.view.updateMapFileName(file.getName());
            }
        };

        this.buttonBmpLs = (ActionEvent e) -> {
            this.fileChooser.updateExtension("bmp");
            File file = this.fileChooser.openFile();

            if (file != null) {
                SettingsModel.getInstance().setBmpFile(file);
                this.view.updateBmpFileName(file.getName());
            }
        };
    }

    /**
     * Starts the game
     */
    private void startGame() {
        SettingsModel model = SettingsModel.getInstance();

        this.view.collectData();
        boolean success = model.processFiles();

        if (!success) {
            JOptionPane.showMessageDialog(new JFrame(), "Failed to load the data. Please try again.",
                "Error!", JOptionPane.ERROR_MESSAGE);
            return;
        }

        HashMap<String, Object> headers = new HashMap<>();
        headers.put(RiskApp.MainIntent.KEY_PLAYERS, model.getPlayers());
        headers.put(RiskApp.MainIntent.KEY_CONTINENT, model.getContinents());
        headers.put(RiskApp.MainIntent.KEY_COUNTRIES, model.getCountries());
        headers.put(RiskApp.MainIntent.kEY_BMP, model.getBmpFile());

        RiskApp.ChangeActivityController(this, new MainController(), headers, true);

        System.out.println("Done with Start Game");
    }
}
