package risk.game.main.logs;

import javax.swing.*;
import javax.swing.JPanel;
import java.util.ArrayList;

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

    /**
     * Set the view
     * @param logs new logs
     */
    public void setView(JList logs) {
        this.view.setListLog(logs);
    }

    /**
     * Get the logs
     * @return list of logs
     */
    public ArrayList<String> getLogs() {
        return this.model.getLogs();
    }

    /**
     * Dispatch the logs again
     * @param logs list of logs
     */
    public void setLogs(ArrayList<String> logs) {
        for (String log : logs) {
            this.model.recordLog(log);
        }
    }
}
