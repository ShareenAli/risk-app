package risk.game.settings;

import entity.Continent;
import entity.Country;
import entity.Player;
import risk.game.main.MainModel;

import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * It holds the model for the data to be collected.
 *
 * @author shareenali
 */

public class SettingsModel {
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
    public static SettingsModel getInstance() {
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
    public void setBmpFile(File file) {
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

                    temp = line.split(",");
                    Country country = new Country(temp[0].trim(), temp[3].trim(), Double.parseDouble(temp[1].trim()),
                        Double.parseDouble(temp[2].trim()));

                    for (int i = 4; i < temp.length - 1; i++) {
                        country.addNeighbour(temp[i]);
                    }

                    this.countries.add(country);
                }
            }
            this.assignCardsToCountry();
            return true;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return false;
        }
    }

    /**
     * Assign the cards to the countries
     */
    private void assignCardsToCountry() {
        ArrayList<Country> countryList = new ArrayList<>();
        int totalCountries = this.countries.size();
        int assignCard[] = new int[3];
        assignCard[0] = totalCountries / 3; // infantry
        assignCard[1] = totalCountries / 3; // cavalry
        assignCard[2] = totalCountries / 3; // artillery
        Random random = new Random();

        for (int i = 0; i < totalCountries; i++) {
            int s = random.nextInt(3);
            if (s == 3)
                s--;

            Country data = this.countries.get(i);
            switch (s) {
                case 0:
                    if (assignCard[0] == 0) {
                        if (assignCard[1] != 0) {
                            data.setCard(MainModel.CARD_TYPE_CAVALRY);
                            assignCard[1]--;
                            if (assignCard[1] < 0)
                                assignCard[1] = 0;
                        } else {
                            data.setCard(MainModel.CARD_TYPE_ARTILLERY);
                            assignCard[2]--;
                            if (assignCard[2] < 0)
                                assignCard[2] = 0;
                        }
                    } else {
                        data.setCard(MainModel.CARD_TYPE_INFANTRY);
                        assignCard[0]--;
                    }
                    break;
                case 1:
                    if (assignCard[1] == 0) {
                        if (assignCard[0] != 0) {
                            data.setCard(MainModel.CARD_TYPE_INFANTRY);
                            assignCard[0]--;
                            if (assignCard[0] < 0)
                                assignCard[0] = 0;
                        } else {
                            data.setCard(MainModel.CARD_TYPE_ARTILLERY);
                            assignCard[2]--;
                            if (assignCard[2] < 0)
                                assignCard[2] = 0;
                        }
                    } else {
                        data.setCard(MainModel.CARD_TYPE_CAVALRY);
                        assignCard[1]--;
                    }
                    break;
                case 2:
                    if (assignCard[2] == 0) {
                        if (assignCard[1] != 0) {
                            data.setCard(MainModel.CARD_TYPE_CAVALRY);
                            assignCard[1]--;
                            if (assignCard[1] < 0)
                                assignCard[1] = 0;
                        } else {
                            data.setCard(MainModel.CARD_TYPE_INFANTRY);
                            assignCard[0]--;
                            if (assignCard[0] < 0)
                                assignCard[0] = 0;
                        }
                    } else {
                        data.setCard(MainModel.CARD_TYPE_ARTILLERY);
                        assignCard[2]--;
                    }
                    break;
            }

            countryList.add(data);
        }

        this.countries = countryList;
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
