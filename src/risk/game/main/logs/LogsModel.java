package risk.game.main.logs;

import java.util.Observable;

/**
 * Model that drives the view
 * @author shareenali
 * @version 0.1
 */
@SuppressWarnings("deprecation")
class LogsModel extends Observable {
    static final String ADD_LOG = "add:log";

    private String log;

    /**
     * It's an empty blank constructor
     */
    LogsModel() { }

    /**
     * It dispatches the logs to the view
     * @param log step of the game to display
     */
    void recordLog(String log) {
        this.log = log;
        setChanged();
        notifyObservers(ADD_LOG);
    }

    /**
     * Returns the recent log to attach
     * @return log as a string
     */
    String getLog() {
        return log;
    }
}
