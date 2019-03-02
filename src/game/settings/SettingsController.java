package game.settings;

import game.main.MainController;
import risk.RiskApp;
import support.ActivityController;

import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * It initializes the game settings.
 * When the new games is about to start, it has to go through this phase
 *      - in order to initialize the important parameters for game.
 * @author shareenali
 * @version 0.1
 */
public class SettingsController extends ActivityController {
    private SettingsView view;
    private ActionListener comboNoPlayersLs, buttonStartLs;

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
        this.view.initializeValues();
        this.initListeners();
        this.bindListeners();
    }

    /**
     * Bind the appropriate listeners into the UI
     */
    private void bindListeners() {
        this.view.bindComboNoPlayersListeners(this.comboNoPlayersLs);
        this.view.bindButtonStartListeners(this.buttonStartLs);
    }

    /**
     * Initialize the action listeners for UI
     */
    private void initListeners() {
        this.comboNoPlayersLs = (ActionEvent e) -> {
            this.view.createPlayerInfoPanels();
            this.frame.pack();
        };

        this.buttonStartLs = (ActionEvent e) -> {
            this.view.collectData();
            RiskApp.ChangeActivityController(new MainController(SettingsModel.getInstance().getPlayers()));
        };
    }
}
