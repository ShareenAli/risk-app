package risk;

import risk.game.settings.SettingsController;
import risk.game.tournament.settings.TournamentSettingsController;
import risk.map.MapController;
import risk.support.ActivityController;
import risk.support.GameManager;

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
    private ActionListener newGameLs, newMapLs, editMapLs, loadGameLs, tournamentLs;

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
        this.riskView.bindNewGameListener(this.newGameLs);
        this.riskView.bindNewMapListener(this.newMapLs);
        this.riskView.bindEditMapListener(this.editMapLs);
        this.riskView.bindLoadGameListener(this.loadGameLs);
        this.riskView.bindTournamentListener(this.tournamentLs);
    }

    /**
     * It initializes the useful listeners for the view.
     */
    private void initListeners() {
        this.newGameLs = (ActionEvent e) -> RiskApp.ChangeActivityController(new SettingsController());

        this.editMapLs = (ActionEvent e) -> {
            HashMap<String, Object> headers = new HashMap<>();
            headers.put(RiskApp.MapIntent.KEY_EDIT, true);
            RiskApp.ChangeActivityController(this, new MapController(), headers, false);
        };

        this.newMapLs = (ActionEvent e) -> RiskApp.ChangeActivityController(new MapController());

        this.loadGameLs = (ActionEvent e) -> this.loadGame();

        this.tournamentLs = (ActionEvent e) -> RiskApp.ChangeActivityController(new TournamentSettingsController());
    }

    private void loadGame() {
        GameManager manager = new GameManager();
        manager.loadGame(this);
    }
}
