package risk.game.tournament.settings;

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

public class TournamentSettingsModel {
    private static TournamentSettingsModel settingsModel;
    private int noOfMaps, noOfGames;

    private ArrayList<File> mapFiles = new ArrayList<>();
    private ArrayList<File> bmpFiles = new ArrayList<>();
    private ArrayList<Player> players = new ArrayList<>();
    private File mapFile, bmpFile;
    private ArrayList<Country> countries = new ArrayList<>();
    private ArrayList<Continent> continents = new ArrayList<>();

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

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public File getMapFile() {
        return mapFile;
    }

    public File getBmpFile() {
        return bmpFile;
    }

    public ArrayList<Country> getCountries() {
        return countries;
    }

    public ArrayList<Continent> getContinents() {
        return continents;
    }

    /**
     * Gets the singleton reference to the class
     *
     * @return instance of the model
     */
    public static TournamentSettingsModel getInstance() {
        if (settingsModel == null)
            settingsModel = new TournamentSettingsModel();
        return settingsModel;
    }

    public void setNoOfMapsAndGames(int noOfGames, int noOfMaps) {
        this.noOfGames = noOfGames;
        this.noOfMaps = noOfMaps;
    }

    public int getNoOfMaps() {
        return noOfMaps;
    }

    public int getNoOfGames() {
        return noOfGames;
    }

    void addMap(File map) {
        this.mapFiles.add(map);
    }

    File fetchMap(int index) {
        return this.mapFiles.get(index);
    }

    void addBmp(File bmp) {
        this.bmpFiles.add(bmp);
    }

    File fetchBmp(int index) {
        return this.bmpFiles.get(index);
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
    void addPlayer(String name, int type, String color, int noOfTurns) {
        this.players.add(new Player(name, type, this.hex2Rgb(color), noOfTurns));
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

    /**
     * Process the map file into the internal data structures
     *
     * @return checkpoint if the files are processed or not.
     */
    boolean processFiles() {
        if (this.mapFile == null)
            return false;
        this.countries.clear();
        this.continents.clear();

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
}
