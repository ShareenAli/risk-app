package risk.support;

import risk.game.main.MainController;
import risk.game.main.MainModel;
import risk.game.main.phases.PhaseModel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class GameManager {
    private DisplayFileChooser fileChooser = new DisplayFileChooser("/Users/ndkcha/Documents/university/app/risk-games");
    private MainController controller;

    public void initializeController(MainController controller) {
        this.controller = controller;
    }

    public void saveGame() {
        MainModel mainModel = this.controller.getModel();
        PhaseModel phaseModel = this.controller.getPhaseModel();
        String name = fileChooser.saveFile();
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
}
