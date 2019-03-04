package game.settings;

import game.main.MainController;
import risk.RiskApp;
import support.ActivityController;
import support.DisplayFileChooser;

import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

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
    private DisplayFileChooser fileChooser = new DisplayFileChooser("~");

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

        this.buttonStartLs = (ActionEvent e) -> {
            this.view.collectData();

            RiskApp.ChangeActivityController(new MainController(SettingsModel.getInstance().getPlayers()));
        };

        this.buttonMapLs = (ActionEvent e) -> {
            this.fileChooser.updateExtension("map");
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
}
