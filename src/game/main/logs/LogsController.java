package game.main.logs;

import javax.swing.JPanel;

public class LogsController {
    private LogsView view;
    private LogsModel model;

    public LogsController() {
        this.view = new LogsView();
        this.model = new LogsModel();
    }

    public void initializeValues() {
        this.model.addObserver(this.view);
    }

    public void log(String log) {
        this.model.recordLog(log);
    }

    public JPanel getRootPanel() {
        return (JPanel) this.view.$$$getRootComponent$$$();
    }

    public LogsView getView() {
        return this.view;
    }
}
