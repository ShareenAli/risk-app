package game.settings;

import javax.swing.JFrame;

public class SettingsController {
    private SettingsView settingsView;
    private JFrame settingsFrame = new JFrame();

    public SettingsController() {
        this.settingsView = new SettingsView();

        this.initListeners();
    }

    public void initializeUi() {
        this.settingsFrame.setContentPane(this.settingsView.$$$getRootComponent$$$());
        this.settingsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.bindListeners();
    }

    public void displayUi() {
        this.settingsFrame.pack();
        this.settingsFrame.setVisible(true);
    }

    private void bindListeners() {

    }

    private void initListeners() {

    }
}
