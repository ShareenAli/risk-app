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
	ArrayList<Territory> territoriesIn;

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
	
	/**
     * It returns the latest control value of the continent
     * When a player rules over the entire continent,
     *  he is given the no of armies equal to this value in the reinforcement phase.
     * @return controlValue
     */
    public int getControlValue() {
        return controlValue;
    }
	
	/**
     * It returns the territories held in this continent
     * A continent must hold at least one territory.
     * @return listOfTerritories
     */
    public ArrayList<Territory> getTerritoriesIn() {
		return territoriesIn;
	}
	
	/**
     * It sets the list of territories currently belonging to the continent
     * A continent must hold at least one territory.
	 * @param territoriesIn list of territories in the continent
     */
	public void setTerritoriesIn(ArrayList<Territory> territoriesIn) {
		this.territoriesIn = territoriesIn;
	}
}
