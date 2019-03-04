package game.main.world;

import entity.Country;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

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

    public void configureView(File bmpFile, ArrayList<Country> countries, ActionListener buttonCountryLs) {
        this.view.prepUi(bmpFile);
        this.view.loadCountries(countries, buttonCountryLs);
    }

    public JPanel getRootPanel() {
        return (JPanel) this.view.$$$getRootComponent$$$();
    }

    public WorldView getView() {
        return this.view;
    }
}
