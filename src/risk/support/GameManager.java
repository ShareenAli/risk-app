package risk.support;

import entity.Continent;
import entity.Country;
import entity.Player;
import risk.RiskApp;
import risk.game.main.MainController;
import risk.game.main.MainModel;
import risk.game.main.phases.PhaseModel;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameManager {
    private DisplayFileChooser fileChooser = new DisplayFileChooser("/Users/ndkcha/Documents/university/app/risk-games");
    private MainController controller;

    public void initializeController(MainController controller) {
        this.controller = controller;
    }

    public void saveGame() {
        MainModel mainModel = this.controller.getModel();
        PhaseModel phaseModel = this.controller.getPhaseModel();
        String name = this.fileChooser.saveFile();
        if (name == null)
            return;
        name = name.endsWith(".sali") ? name : name + ".sali";

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(name);
            ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream);

            outputStream.writeObject(mainModel.getBmpFile());
            outputStream.writeObject(mainModel.getContinents());
            outputStream.writeObject(mainModel.getCountries());
            outputStream.writeObject(mainModel.getPlayers());
            outputStream.writeObject(phaseModel.getPhase());
            outputStream.writeObject(phaseModel.getPlayerIndex());
            outputStream.writeObject(this.controller.getLogs());

            outputStream.close();
            fileOutputStream.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public void loadGame(ActivityController source) {
        File file = this.fileChooser.openFile();

        if (file == null)
            return;

        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            ObjectInputStream inputStream = new ObjectInputStream(fileInputStream);

            File bmpFile = (File) inputStream.readObject();
            HashMap<String, Continent> continents = (HashMap<String, Continent>) inputStream.readObject();
            HashMap<String, Country> countries = (HashMap<String, Country>) inputStream.readObject();
            HashMap<String, Player> players = (HashMap<String, Player>) inputStream.readObject();
            int phase = (int) inputStream.readObject();
            int playerIndex = (int) inputStream.readObject();
            ArrayList<String> logs = (ArrayList<String>) inputStream.readObject();

            HashMap<String, Object> headers = new HashMap<>();
            headers.put(RiskApp.MainIntent.KEY_PLAYERS, this.convertPlayers(players));
            headers.put(RiskApp.MainIntent.KEY_CONTINENT, this.convertContinents(continents));
            headers.put(RiskApp.MainIntent.KEY_COUNTRIES, this.convertCountries(countries));
            headers.put(RiskApp.MainIntent.kEY_BMP, bmpFile);
            headers.put(RiskApp.MainIntent.KEY_PHASE, phase);
            headers.put(RiskApp.MainIntent.KEY_PLAYER_IDX, playerIndex);
            headers.put(RiskApp.MainIntent.KEY_LOGS, logs);
            headers.put(RiskApp.MainIntent.KEY_LOAD_FLAG, true);

            RiskApp.ChangeActivityController(source, new MainController(), headers, true);

            inputStream.close();
            fileInputStream.close();
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    private ArrayList<Continent> convertContinents(HashMap<String, Continent> map) {
        ArrayList<Continent> list = new ArrayList<>();
        for (Map.Entry<String, Continent> entry : map.entrySet()) {
            list.add(entry.getValue());
        }
        return list;
    }

    private ArrayList<Country> convertCountries(HashMap<String, Country> map) {
        ArrayList<Country> list = new ArrayList<>();
        for (Map.Entry<String, Country> entry : map.entrySet()) {
            list.add(entry.getValue());
        }
        return list;
    }

    private ArrayList<Player> convertPlayers(HashMap<String, Player> map) {
        ArrayList<Player> list = new ArrayList<>();
        for (Map.Entry<String, Player> entry : map.entrySet()) {
            list.add(entry.getValue());
        }

        return list;
    }
}
