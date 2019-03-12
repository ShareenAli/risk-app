package risk.game.main.logs;

import javax.swing.JPanel;

/**
 * Handles the logs view
 * @author shareenali
 * @version 0.1
 */
public class LogsController {
    private LogsView view;
    private LogsModel model;

    /**
     * It initializes the view and model
     */
    public LogsController() {
        this.view = new LogsView();
        this.model = new LogsModel();
    }

    /**
     * It initializes the values
     */
    public void initializeValues() {
        this.model.addObserver(this.view);
    }

    /**
     * Add into the log
     * @param log content
     */
    public void log(String log) {
        this.model.recordLog(log);
    }

    /**
     * Get the root panel
     * @return root panel
     */
    public JPanel getRootPanel() {
        return (JPanel) this.view.$$$getRootComponent$$$();
    }

    /**
     * Get the view
     * @return view
     */
    public LogsView getView() {
        return this.view;
    }
}
