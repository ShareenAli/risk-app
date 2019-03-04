package risk;

import support.ActivityController;

import java.util.HashMap;

/**
 * The Main Application Class.
 * It is the entry point of the game.
 * This class initializes the main controller that displays the first interface of the game.
 * @author shareenali
 * @version 0.1
 */

public class RiskApp {
    public static void main(String[] args) {
        RiskController controller = new RiskController();
        ChangeActivityController(controller);

        System.out.println("End of RiskApp.Main");
    }

    static void ChangeActivityController(ActivityController controller) {
        controller.initUi();
        controller.setupValues();
        controller.displayUi();
    }

    public static void ChangeActivityController(ActivityController source, ActivityController target, boolean hideSource) {
        if (hideSource)
            source.destroy();

        target.initUi();
        target.setupValues();
        target.displayUi();
    }

    public static void ChangeActivityController(ActivityController controller, HashMap<String, Object> data) {
        controller.initUi();
        controller.setupValues(data);
        controller.displayUi();
    }

    public static void ChangeActivityController(ActivityController source, ActivityController target,
                                                HashMap<String, Object> data, boolean hideSource) {
        if (hideSource)
            source.destroy();

        target.initUi();
        target.setupValues(data);
        target.displayUi();
    }

    public static abstract class MainIntent {
        public static final String KEY_PLAYERS = "players";
        public static final String KEY_COUNTRIES = "countries";
        public static final String KEY_CONTINENT = "continents";
        public static final String kEY_BMP = "bmp";
    }
}
