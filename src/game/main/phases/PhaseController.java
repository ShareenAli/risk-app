package game.main.phases;

import javax.swing.JPanel;

public class PhaseController {
    private PhaseView view;

    public PhaseController() {
        this.view = new PhaseView();
    }

    public JPanel getRootPanel() {
        return (JPanel) this.view.$$$getRootComponent$$$();
    }
}
