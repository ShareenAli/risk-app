package map;

import entity.Continent;
import entity.Country;

import java.io.File;
import java.util.*;

/**
 * Model the drives the map view
 * @author shareenali farhan
 * @version 0.1
 */
@SuppressWarnings("deprecation")
public class MapModel extends Observable {
    static final String UPDATE_MAP = "update:map";
    static final String UPDATE_CONTINENTS = "update:continents";
    static final String UPDATE_ALL = "update:all";
    static final String UPDATE_COUNTRIES = "update:countries";
    private HashMap<String, Country> countries = new HashMap<>();
    private HashMap<String, Continent> continents = new HashMap<>();
    private File bmpFile;

    public String author, imageFileName, scrollType, mapFileName;
    public boolean wrap, warn;

    /**
     * It initializes the map model
     */
    public MapModel() {
        wrap = false;
        warn = false;
        this.mapFileName = "";
        this.author = "shareenali";
        this.imageFileName = "";
        this.scrollType = "";
    }

    /**
     * Sets the bit map file for reference
     * @param file image file
     */
    void setBmpFile(File file) {
        this.imageFileName = file.getName();
        this.bmpFile = file;
        setChanged();
        notifyObservers(UPDATE_MAP);
    }

    /**
     * Notifies all the views regardless of changes.
     * Used when the map is loaded from external resource.
     */
    void hardNotifyView() {
        setChanged();
        notifyObservers(UPDATE_ALL);
    }

    /**
     * Get the image file
     * @return file
     */
    File getBmpFile() {
        return bmpFile;
    }

    /**
     * Recursively delete the continent
     * @param name name of the continent
     */
    void deleteContinent(String name) {
        this.continents.remove(name);

        ArrayList<String> countries = new ArrayList<>();
        for (Map.Entry<String, Country> countryEntry : this.countries.entrySet()) {
            if (countryEntry.getValue().getContinent().equalsIgnoreCase(name))
                countries.add(countryEntry.getKey());
        }

        for (String country : countries) {
            this.countries.remove(country);
        }

        setChanged();
        notifyObservers(UPDATE_ALL);
    }

    /**
     * Saves the country without notifying observers
     * @param country country to update
     */
    void saveCountryWithoutNotify(Country country) {
        this.countries.put(country.getName(), country);
    }

    /**
     * Saves the country
     * @param country country to update
     */
    public void saveCountry(Country country) {
        this.countries.put(country.getName(), country);
        setChanged();
        notifyObservers(UPDATE_COUNTRIES);
    }

    /**
     * Deletes the country
     * @param country country to delete
     */
    void deleteCountry(Country country) {
        for (Map.Entry<String, Country> countryEntry : this.countries.entrySet()) {
            Country c = countryEntry.getValue();
            int index = c.getNeighbours().indexOf(country.getName());
            if (index != -1)
                c.removeNeighbour(country.getName());
            this.saveCountryWithoutNotify(c);
        }
        this.countries.remove(country.getName());
        setChanged();
        notifyObservers(UPDATE_COUNTRIES);
    }

    /**
     * Saves the continent
     * @param continent country to save
     */
    public void saveContinent(Continent continent) {
        this.continents.put(continent.getName(), continent);
        setChanged();
        notifyObservers(UPDATE_CONTINENTS);
    }

    /**
     * Saves the continent without notifying observers
     * @param continent country to save
     */
    void saveContinentWithoutNotify(Continent continent) {
        this.continents.put(continent.getName(), continent);
    }

    /**
     * Get the continent
     * @param name name of continent
     * @return continent
     */
    Continent getContinent(String name) {
        return this.continents.get(name);
    }

    /**
     * Get the country
     * @param name name of the country
     * @return country
     */
    Country getCountry(String name) {
        return this.countries.get(name);
    }

    /**
     * Get all the continents
     * @return hash map of continents
     */
    public HashMap<String, Continent> getContinents() {
        return this.continents;
    }

    /**
     * Get all the country
     * @return hash map of countries
     */
    public HashMap<String, Country> getCountries() {
        return this.countries;
    }

    /**
     * Get the countries in continent
     * @param continentName name of the continent
     * @return list of countries
     */
    public List<Country> getCountriesInContinent(String continentName) {
        List<Country> countryDataList = new ArrayList<>();

        for (Map.Entry<String, Country> countryDataEntry : this.countries.entrySet()) {
            Country country = countryDataEntry.getValue();
            if (country.getContinent().equalsIgnoreCase(continentName))
                countryDataList.add(country);
        }

        return countryDataList;
    }

    /**
     * Checks if the country exists
     * @param country name of the country
     * @return true if it does
     */
    boolean doesCountryExist(String country) {
        return this.getCountries().containsKey(country);
    }
}
