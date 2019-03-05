package entity;

import java.util.ArrayList;

/**
 * This entity class holds all the information about the country in the map.
 * It also includes the methods to manipulate the information of the country.
 * @author shareenali, farhan
 * @version 0.1
 */

public class Country {
    private String name, continent, cardType;
    private double latitude, longitude; // x and y of the coordinate system  respectively
    private ArrayList<String> neighbours;

    /**
     * It initializes the data for the country.
     * The constructor can be used in both the parts, Map and Game.
     * @param name name of the country
     * @param continent continent in which the country is
     * @param latitude x coordinate of the country
     * @param longitude y coordinate of the country
     */
    public Country(String name, String continent, double latitude, double longitude) {
        this.neighbours = new ArrayList<>();
        this.cardType = null;
        this.name = name;
        this.continent = continent;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Set up values of the country after the country is initialized. It is
     * mainly used to update the existing country's details.
     * @param name of the country
     * @param continent The continent in which the country is
     * @param latitude The X coordinate of the country
     * @param longitude The Y coordinate of the country
     */
    public void setValues(String name, String continent, double latitude, double longitude) {
        this.name = name;
        this.continent = continent;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    /**
     * Use the card assigned to the country.
     * Once the card is used, it should never be used again.
     * So the card type will be null
     */
    public void useCard() {
        this.cardType = null;
    }

    /**
     * Sets type of the card assigned to the country
     * @param type type of card
     */
    public void setCardType(String type) {
        this.cardType = type;
    }

    /**
     * Remove a neighbour for a country
     * @param name name of the neighbouring country to remove
     */
    public void removeNeighbour(String name) {
        if (this.neighbours.indexOf(name) != -1)
            this.neighbours.remove(name);
    }

    /**
     * Add a neighbour to the country
     * @param name name of the neighbouring country
     */
    public void addNeighbour(String name) {
        if (this.neighbours.indexOf(name) == -1)
            this.neighbours.add(name);
    }
    
    /**
     * Adds a list of neighbors to the country
     * @return neighbours of the country
     */
    public void setNeighbours(ArrayList<String> neighbours) {
    	this.neighbours = neighbours;
    }

    /**
     * It returns name of the country
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * It returns the continent in which the country is
     * @return continent of the country
     */
    public String getContinent() {
        return continent;
    }

    /**
     * It returns the card type the country is assigned to
     * @return type of the card
     */
    public String getCardType() {
        return cardType;
    }

    /**
     * It returns the x coordinate of the country
     * @return latitude of the country
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * It returns the y coordinate of the country
     * @return longitude of the country
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * It returns the list of neighbours of the country
     * @return neighbours of the country
     */
    public ArrayList<String> getNeighbours() {
        return neighbours;
    }

    public void resetNeighbours() {
        this.neighbours.clear();
    }
}
