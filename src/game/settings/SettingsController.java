package game.settings;

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

public class SettingsController implements ActivityController {
    private SettingsView view;
    private JFrame frame = new JFrame();
    private ActionListener comboNoPlayersLs, buttonStartLs;

    /**
     * It initializes the controller
     */
    public SettingsController() {
        this.view = new SettingsView();
    }

    /**
     * It initializes the view, and wraps it to a frame.
     */
    public void initUi() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - this.frame.getWidth()) / 2;
        int y = (screenSize.height - this.frame.getHeight()) / 2;
        this.frame.setLocation(x, y);

        this.frame.setContentPane(this.view.$$$getRootComponent$$$());
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.prepareView();
    }

    /**
     * Initializes the view with custom values and listeners
     */
    private void prepareView() {
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
        };
    }

    /**
     * It displays the view to standard output device
     */
    public void displayUi() {
        this.frame.pack();
        this.frame.setVisible(true);
    }
}
