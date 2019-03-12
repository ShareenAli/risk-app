package entity;

import java.util.ArrayList;

/**
 * This entity class holds all the information about the continent.
 * It also includes the methods to manipulate the information of the continent
 * @author shareenali, farhan
 * @version 0.1
 */

public class Continent {
    private String name;
    private int controlValue;
    private int countryCount;

    /**
     * It holds the continent data.
     * @param name name of the continent
     * @param controlValue control value of the continent
     */
    public Continent(String name, int controlValue) {
        this.name = name;
        this.controlValue = controlValue;
        this.countryCount = 0;
    }

    /**
     * Adds the country to the counter
     */
    public void addCountry() {
        this.countryCount++;
    }

    /**
     * Get the number of countries in continent
     * @return count of the countries
     */
    public int getCountryCount() {
        return countryCount;
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
