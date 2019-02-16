package game.settings;

import javax.swing.JFrame;

/**
 * It initializes the game settings.
 * When the new games is about to start, it has to go through this phase
 *      - in order to initialize the important parameters for game.
 * @author shareenali
 * @version 0.1
 */

public class SettingsController {
    private SettingsView settingsView;
    private JFrame settingsFrame = new JFrame();

    /**
     * It initializes the controller
     */
    public SettingsController() {
        this.settingsView = new SettingsView();
    }

    /**
     * It initializes the view, and wraps it to a frame.
     */
    public void initializeUi() {
        this.settingsFrame.setContentPane(this.settingsView.$$$getRootComponent$$$());
        this.settingsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * It displays the view to standard output device
     */
    public void displayUi() {
        this.settingsFrame.pack();
        this.settingsFrame.setVisible(true);
    }
}
