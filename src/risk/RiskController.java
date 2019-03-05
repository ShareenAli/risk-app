package risk;

import game.settings.SettingsController;
import map.MapController;
import support.ActivityController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

/**
 * The Main Controller of the game.
 * It is the entry point controller of the game.
 * This controller initializes the view that is displayed when game is launched.
 * @author shareenali
 * @version 0.1
 */
class RiskController extends ActivityController {
    private RiskView riskView;
    private ActionListener newGameListener, newMapListener, editMapListener;

    /**
     * It initializes the controller.
     * As part of the controller initialization, follow steps are executed,
     *  - initialize the view
     *  - bind important variables to the view.
     */
    RiskController() {
        this.riskView = new RiskView();

        this.initListeners();
    }

    /**
     * It initializes the user defined view component.
     * It binds the listeners to the view.
     */
    @Override
    protected void prepareUi() {
        this.frame.setContentPane(this.riskView.$$$getRootComponent$$$());
        this.bindListeners();
    }

    /**
     * It binds the useful listeners to the view.
     */
    private void bindListeners() {
        this.riskView.bindNewGameListener(this.newGameListener);
        this.riskView.bindNewMapListener(this.newMapListener);
        this.riskView.bindEditMapListener(this.editMapListener);
    }

    /**
     * It initializes the useful listeners for the view.
     */
    private void initListeners() {
        this.newGameListener = (ActionEvent e) -> RiskApp.ChangeActivityController(new SettingsController());

        this.editMapListener = (ActionEvent e) -> {
            HashMap<String, Object> headers = new HashMap<>();
            headers.put(RiskApp.MapIntent.KEY_EDIT, true);
            RiskApp.ChangeActivityController(this, new MapController(), headers, false);
        };

        this.newMapListener = (ActionEvent e) -> RiskApp.ChangeActivityController(new MapController());
    }
}
