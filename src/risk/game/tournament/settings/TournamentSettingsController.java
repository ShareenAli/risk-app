package risk.game.tournament.settings;

import risk.RiskApp;
import risk.game.main.MainController;
import risk.support.ActivityController;
import risk.support.DisplayFileChooser;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class TournamentSettingsController extends ActivityController {
    private TournamentSettingsView view;
    private ActionListener comboNoPlayersLs, buttonStartLs;
    private DisplayFileChooser fileChooser = new DisplayFileChooser("/Users/ndkcha/Documents/university/app/risk-maps");

    public TournamentSettingsController() {
        this.view = new TournamentSettingsView();
    }

    public void setupValues() {
        this.view.initializeValues();
    }

    @Override
    protected void prepareUi() {
        this.frame.setContentPane(this.view.$$$getRootComponent$$$());
        this.initListeners();
        this.bindListeners();
    }

    /**
     * Initialize the action listeners for UI
     */
    private void initListeners() {
        this.comboNoPlayersLs = (ActionEvent e) -> {
            this.view.createPlayerInfoPanels();
            this.frame.pack();
        };

        this.buttonStartLs = (ActionEvent e) -> this.startTournament();
    }

    private void startTournament() {
        this.view.collectData();
        TournamentSettingsModel model = TournamentSettingsModel.getInstance();

        for (int i = 0; i < model.getNoOfMaps(); i++) {
            fileChooser.setTitle("Map " + (i + 1));
            fileChooser.updateExtension("map");
            File map = fileChooser.openFile();
            fileChooser.updateExtension("bmp");
            File bmp = fileChooser.openFile();
            if (map == null || bmp == null) {
                i--;
                continue;
            }
            model.addBmp(bmp);
            model.addMap(map);
        }

        this.view.disableStartButton();
        this.view.initTable();

        for (int i = 0; i < model.getNoOfMaps(); i++) {
            for (int j = 0; j < model.getNoOfGames(); j++) {
                model = TournamentSettingsModel.getInstance();
                this.view.collectData();
                model.setMapFile(model.fetchMap(i));
                model.setBmpFile(model.fetchBmp(i));
                System.out.println(model.processFiles());

                MainController controller = new MainController();

                HashMap<String, Object> headers = new HashMap<>();
                headers.put(RiskApp.MainIntent.KEY_PLAYERS, model.getPlayers());
                headers.put(RiskApp.MainIntent.KEY_CONTINENT, model.getContinents());
                headers.put(RiskApp.MainIntent.KEY_COUNTRIES, model.getCountries());
                headers.put(RiskApp.MainIntent.kEY_BMP, model.getBmpFile());
                headers.put(RiskApp.MainIntent.KEY_TOURNAMENT, true);

                RiskApp.ChangeActivityController(this, controller, headers, false);
                String winner = controller.getWinner();
                model.putLogs(String.valueOf(i).concat(String.valueOf(j)), controller.getLogs());
                this.view.addGame(i, j, (winner == null) ? "draw" : winner);

                System.out.println("Done with start game : " + controller.getWinner());
            }
        }
    }

    /**
     * Bind the appropriate listeners into the UI
     */
    private void bindListeners() {
        this.view.bindComboNoPlayersListeners(this.comboNoPlayersLs);
        this.view.bindButtonStartListeners(this.buttonStartLs);
    }
}
