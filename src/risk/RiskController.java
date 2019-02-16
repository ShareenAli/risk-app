package risk;

import javax.swing.JFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The Main Controller of the game.
 * It is the entry point controller of the game.
 * This controller initializes the view that is displayed when game is launched.
 * @author shareenali
 * @version 0.1
 */

class RiskController {
    private RiskView riskView;
    private ActionListener newGameListener;
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
    void initUi() {
        this.frame.setContentPane(this.riskView.$$$getRootComponent$$$());
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.bindListeners();
    }

    /**
     * It displays view to the standard output device.
     */
    void displayUi() {
        this.frame.pack();
        this.frame.setVisible(true);
    }

    /**
     * It binds the useful listeners to the view.
     */
    private void bindListeners() {
        this.riskView.bindNewGameListener(this.newGameListener);
    }

    /**
     * It initializes the useful listeners for the view.
     */
    private void initListeners() {
        this.newGameListener = (ActionEvent e) -> {
            System.out.println("Naya game start hoga!");
        };
    }
}
