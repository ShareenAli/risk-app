package game.main;

import entity.Player;
import support.ActivityController;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MainController implements ActivityController {
    private MainView view;
    private JFrame frame = new JFrame();
    private MainModel model = new MainModel();

    public MainController(ArrayList<Player> players) {
        this.view = new MainView();
        this.model.setPlayers(players);
    }

    /**
     * It initializes the view, and wraps it to a frame.
     */
    public void initUi() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - this.frame.getWidth()) / 2;
        int y = (screenSize.height - this.frame.getHeight()) / 2;
        this.frame.setLocation(x, y);

        this.frame.setContentPane(this.view.$$$getRootComponent$$$());
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.prepareView();
    }

    /**
     * Initializes the view with custom values and listeners
     */
    private void prepareView() {

        this.attachObservers();
    }

    private void attachObservers() {
        this.model.addObserver(this.view);
    }

    /**
     * It displays the view to standard output device
     */
    public void displayUi() {
        this.frame.pack();
        this.frame.setVisible(true);
    }
}
