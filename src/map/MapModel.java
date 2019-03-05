package map;

import entity.Continent;
import entity.Country;

import java.io.File;
import java.util.*;

@SuppressWarnings("deprecation")
class MapModel extends Observable {
    static final String UPDATE_MAP = "update:map";
    static final String UPDATE_CONTINENTS = "update:continents";
    static final String UPDATE_ALL = "update:all";
    static final String UPDATE_COUNTRIES = "update:countries";
    private HashMap<String, Country> countries = new HashMap<>();
    private HashMap<String, Continent> continents = new HashMap<>();
    private File bmpFile;

    public String author, imageFileName, scrollType, mapFileName;
    public boolean wrap, warn;

    MapModel() {
        wrap = false;
        warn = false;
        this.mapFileName = "";
        this.author = "shareenali";
        this.imageFileName = "";
        this.scrollType = "";
    }

    void setBmpFile(File file) {
        this.imageFileName = file.getName();
        this.bmpFile = file;
        setChanged();
        notifyObservers(UPDATE_MAP);
    }

    void hardNotifyView() {
        setChanged();
        notifyObservers(UPDATE_ALL);
    }

    File getBmpFile() {
        return bmpFile;
    }

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

    void saveCountryWithoutNotify(Country country) {
        this.countries.put(country.getName(), country);
    }

    void saveCountry(Country country) {
        this.countries.put(country.getName(), country);
        setChanged();
        notifyObservers(UPDATE_COUNTRIES);
    }

    void saveContinent(Continent continent) {
        this.continents.put(continent.getName(), continent);
        setChanged();
        notifyObservers(UPDATE_CONTINENTS);
    }

    void saveContinentWithoutNotify(Continent continent) {
        this.continents.put(continent.getName(), continent);
    }

    Continent getContinent(String name) {
        return this.continents.get(name);
    }

    Country getCountry(String name) {
        return this.countries.get(name);
    }

    public HashMap<String, Continent> getContinents() {
        return this.continents;
    }

    public HashMap<String, Country> getCountries() {
        return this.countries;
    }

    public List<Country> getCountriesInContinent(String continentName) {
        List<Country> countryDataList = new ArrayList<>();

        for (Map.Entry<String, Country> countryDataEntry : this.countries.entrySet()) {
            Country country = countryDataEntry.getValue();
            if (country.getContinent().equalsIgnoreCase(continentName))
                countryDataList.add(country);
        }

        return countryDataList;
    }

    boolean doesCountryExist(String country) {
        return this.getCountries().containsKey(country);
    }
}
