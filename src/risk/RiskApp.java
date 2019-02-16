package risk;

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
        controller.initUi();
        controller.displayUi();
    }
}
