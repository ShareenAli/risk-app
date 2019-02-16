package risk;

import javax.swing.JFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RiskController {
    private RiskView riskView;
    private ActionListener newGameListener;
    private JFrame frame = new JFrame();

    RiskController() {
        this.riskView = new RiskView();

        this.initListeners();
    }

    void initUi() {
        this.frame.setContentPane(this.riskView.$$$getRootComponent$$$());
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.bindListeners();
    }

    void displayUi() {
        this.frame.pack();
        this.frame.setVisible(true);
    }

    private void bindListeners() {
        this.riskView.bindNewGameListener(this.newGameListener);
    }

    private void initListeners() {
        this.newGameListener = (ActionEvent e) -> {
            System.out.println("Naya game start hoga!");
        };
    }
}
