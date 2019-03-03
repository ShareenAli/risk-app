package game.main.phases;

import support.ActivityController;

import javax.swing.JPanel;

public class PhaseController extends ActivityController {
    private PhaseView view;
    private PhaseModel model = new PhaseModel();

    public PhaseController() {
        this.view = new PhaseView();
    }

    public JPanel getRootPanel() {
        return (JPanel) this.view.$$$getRootComponent$$$();
    }

    @Override
    protected void prepareUi() {

    }

    private void attachObservers(){
        this.model.addObserver(this.view);
    }



}
