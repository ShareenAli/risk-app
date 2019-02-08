package entity;

/**
 * This entity class holds all the information about the continent.
 * It also includes the methods to manipulate the information of the continent
 * @author shareenali
 * @version 0.0.1
 */

public class Continent {
    private String name;
    private int controlValue;

    /**
     * It holds the continent data.
     * @param name name of the continent
     * @param controlValue control value of the continent
     */
    public Continent(String name, int controlValue) {
        this.name = name;
        this.controlValue = controlValue;
    }

    /**
     * It returns the name of the continent
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * It returns the latest control value of the continent
     * When a player rules over the entire continent,
     *  he is given the no of armies equal to this value in the reinforcement phase.
     * @return controlValue
     */
    public int getControlValue() {
        return controlValue;
    }
}
