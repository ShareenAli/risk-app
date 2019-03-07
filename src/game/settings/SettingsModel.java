package game.settings;

import entity.Continent;
import entity.Country;
import entity.Player;

import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * It holds the model for the data to be collected.
 *
 * @author shareenali
 */

class SettingsModel {
    private static SettingsModel instance;
    private File mapFile, bmpFile;
    private ArrayList<Player> players = new ArrayList<>();
    private ArrayList<Country> countries = new ArrayList<>();
    private ArrayList<Continent> continents = new ArrayList<>();

    /**
     * Gets the singleton reference to the class
     *
     * @return instance of the model
     */
    static SettingsModel getInstance() {
        if (instance == null)
            instance = new SettingsModel();
        return instance;
    }

    /**
     * Sets the file that hold map details
     *
     * @param file map file
     */
    void setMapFile(File file) {
        this.mapFile = file;
    }

    /**
     * Sets the file that has image of the map
     *
     * @param file bmp file
     */
    void setBmpFile(File file) {
        this.bmpFile = file;
    }

    /**
     * private controller for the singleton class
     */
    private SettingsModel() {
    }

    /**
     * It clears the list of all the players recorded before.
     * It is used to re-initialize the list of players, in case of any errors in previous list
     */
    void clearPlayers() {
        this.players.clear();
    }

    /**
     * It adds a player to the list.
     *
     * @param name  name of the player
     * @param type  index of the player type combo box.
     * @param color color that the player is assigned
     */
    void addPlayer(String name, int type, String color) {
        this.players.add(new Player(name, type, this.hex2Rgb(color)));
    }

    /**
     * It returns list of players
     *
     * @return players
     */
    ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * It returns the list of countries fetched from the map file
     *
     * @return array list of countries
     */
    public ArrayList<Country> getCountries() {
        return countries;
    }

    /**
     * It returns thie list of continents fetched from the map file
     *
     * @return array list of countries
     */
    public ArrayList<Continent> getContinents() {
        return continents;
    }

    /**
     * The bitmap image file used for map
     *
     * @return image file
     */
    public File getBmpFile() {
        return bmpFile;
    }

    /**
     * Process the map file into the internal data structures
     *
     * @return checkpoint if the files are processed or not.
     */
    boolean processFiles() {
        if (this.mapFile == null)
            return false;

        int continentFlag = 0, territoryFlag = 0;
        try {
            Scanner scanner = new Scanner(new FileReader(this.mapFile));

            while (scanner.hasNext()) {
                String line;
                String temp[];

                line = scanner.nextLine();

                switch (line) {
                    case "[Continents]":
                        continentFlag = 1;
                        break;

                    case "[Territories]":
                        territoryFlag = 1;
                        continentFlag = 0;
                        break;
                }

                if (line.equals("") || line.equals(" "))
                    continue;

                if (continentFlag == 1) {
                    if (line.equals("[Continents]"))
                        line = scanner.nextLine();

                    temp = line.split("=");
                    this.continents.add(new Continent(temp[0].trim(), Integer.parseInt(temp[1])));

                }

                if (territoryFlag == 1) {
                    if (line.equals("[Territories]"))
                        line = scanner.nextLine();

                    int continentIndex = 0;
                    temp = line.split(",");
                    Country country = new Country(temp[0].trim(), temp[3].trim(), Double.parseDouble(temp[1].trim()), Double.parseDouble(temp[2].trim()));

                    for (Continent continent : continents) {
                        if (continent.getName().equals(temp[3].trim())) {
                            continentIndex = this.continents.indexOf(continent);
                        }
                    }
                    Continent continent = this.continents.get(continentIndex);
                    if (continent.getTerritoriesIn() != null) {
                        ArrayList<Country> territoriesIn = continent.getTerritoriesIn();
                        territoriesIn.add(country);
                    } else {
                        ArrayList<Country> territoriesIn = new ArrayList<Country>();
                        territoriesIn.add(country);
                        continent.setTerritoriesIn(territoriesIn);
                    }

                    for (int i = 4; i < temp.length - 1; i++) {
                        country.addNeighbour(temp[i]);
                    }

                    this.countries.add(country);
                }
            }
            return true;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return false;
        }
    }

    /**
     * Covert the hex to rgb
     * @param colorStr the hex code
     * @return the color object
     */
    private Color hex2Rgb(String colorStr) {
        return new Color(Integer.valueOf(colorStr.substring(1, 3), 16),
                Integer.valueOf(colorStr.substring(3, 5), 16),
                Integer.valueOf(colorStr.substring(5, 7), 16));
    }
}
