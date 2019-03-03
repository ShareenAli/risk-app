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
        assignCountries();
        assignArmies();
        this.frame.setContentPane(this.view.$$$getRootComponent$$$());
        this.attachObservers();
    }

    /**
     * The function assigns the countries to the players
     */
    private void assignCountries() {
        this.model.assignCountry();
    }

    /**
     * The function assigns the armies to the assigned countries to the players
     */
    private void assignArmies() {
        this.model.assignArmies();
    }

    /**
     * Attach the observers for the model
     */
    private void attachObservers() {
        this.model.addObserver(this.view);
    }
}
