package game.main;

import entity.Player;
import support.ActivityController;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MainController extends ActivityController {
    private MainView view;
    private MainModel model = new MainModel();

    public MainController(ArrayList<Player> players) {
        this.view = new MainView();
        this.model.setPlayers(players);
    }

    /**
     * It initializes the view with custom values and listeners.
     */
    @Override
    protected void prepareUi() {
        startupPhase();
        this.frame.setContentPane(this.view.$$$getRootComponent$$$());
        this.attachObservers();
    }

    /**
     * Perform the startup Phase operations
     * Assign the countries to the players
     * Assign the armies to the assigned countries
     */
    private void startupPhase() {
        this.model.assignCountry();
        this.model.assignArmies();
    }

    /**
     * Attach the observers for the model
     */
    private void attachObservers() {
        this.model.addObserver(this.view);
    }
}
