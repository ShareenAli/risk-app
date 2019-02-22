package game.settings;

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

public class SettingsController {
    private SettingsView view;
    private JFrame frame = new JFrame();
    private ActionListener comboNoPlayersLs;

    /**
     * It initializes the controller
     */
    public SettingsController() {
        this.view = new SettingsView();
    }

    /**
     * It initializes the view, and wraps it to a frame.
     */
    public void initializeUi() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - this.frame.getWidth()) / 2;
        int y = (screenSize.height - this.frame.getHeight()) / 2;
        this.frame.setLocation(x, y);

        this.frame.setContentPane(this.view.$$$getRootComponent$$$());
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.prepareView();
    }

    private void prepareView() {
        this.view.initializeValues();
        this.initComboListeners();
        this.bindListeners();
    }

    private void bindListeners() {

    }

    private void initComboListeners() {
        this.comboNoPlayersLs = (ActionEvent e) -> this.view.createPlayerInfoPanels();
    }

    /**
     * It displays the view to standard output device
     */
    public void displayUi() {
        this.frame.pack();
        this.frame.setVisible(true);
    }
}
