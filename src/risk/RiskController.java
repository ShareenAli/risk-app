package risk;

import game.settings.SettingsController;
import support.ActivityController;

import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The Main Controller of the game.
 * It is the entry point controller of the game.
 * This controller initializes the view that is displayed when game is launched.
 * @author shareenali
 * @version 0.1
 */

class RiskController implements ActivityController {
    private RiskView riskView;
    private ActionListener newGameListener, newMapListener, editMapListener;
    private JFrame frame = new JFrame();

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
     * It initializes the view component.
     * It wraps the entire view into a frame and prepares to display
     */
    public void initUi() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - this.frame.getWidth()) / 2;
        int y = (screenSize.height - this.frame.getHeight()) / 2;
        this.frame.setLocation(x, y);

        this.frame.setContentPane(this.riskView.$$$getRootComponent$$$());
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.bindListeners();
    }

    /**
     * It displays view to the standard output device.
     */
    public void displayUi() {
        this.frame.pack();
        this.frame.setVisible(true);
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
            System.out.println("Dillo me tum apni betabiya le ke chal rhe ho");
        };

        this.newMapListener = (ActionEvent e) -> {
            System.out.println("toh zinda ho tum");
        };
    }
}
