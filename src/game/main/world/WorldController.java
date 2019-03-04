package game.main.world;

import javax.swing.*;

public class WorldController {
    private WorldView view;
    private WorldModel model;

    public WorldController() {
        this.view = new WorldView();
        this.model = new WorldModel();
    }

    public void initializeValues() {
        this.model.addObserver(this.view);
    }

    public JPanel getRootPanel() {
        return (JPanel) this.view.$$$getRootComponent$$$();
    }

    public WorldView getView() {
        return this.view;
    }
}
