package support;

import javax.swing.*;
import java.awt.*;

public abstract class ActivityController {
    protected JFrame frame = new JFrame();
    protected abstract void prepareUi();

    /**
     * It initializes the view, and wraps it to a frame.
     */
    public void initUi() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - this.frame.getWidth()) / 2;
        int y = (screenSize.height - this.frame.getHeight()) / 2;
        this.frame.setLocation(x, y);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.prepareUi();
    }

    /**
     * It displays view to the standard output device.
     */
    public void displayUi() {
        this.frame.pack();
        this.frame.setVisible(true);
    }
}
