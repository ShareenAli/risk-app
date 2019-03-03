package game.main.phases;

import javax.swing.*;

public class PhaseController {
    private PhaseView view;
    private PhaseModel model = new PhaseModel();

    public PhaseController() {
        this.view = new PhaseView();
    }

    public JPanel getRootPanel() {
        return (JPanel) this.view.$$$getRootComponent$$$();
    }


}
