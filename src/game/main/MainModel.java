package game.main;

import entity.Continent;
import entity.Country;
import entity.Player;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Scanner;

@SuppressWarnings("deprecation")
public class MainModel extends Observable {
    private ArrayList<String> playerNames = new ArrayList<>();
    private HashMap<String, Player> players = new HashMap<>();
    private HashMap<String, Country> countries = new HashMap<>();
    private HashMap<String, Continent> continents = new HashMap<>();
    private Country country;
    private FileReader fileReader;
    private Scanner scanner;

    public MainModel() {
        readMapFile();
    }

    public void setPlayers(ArrayList<Player> playerList) {
        for (Player player : playerList) {
            this.players.put(player.getName(), player);
        }
    }

    public Player getPlayer(String name) {
        return this.players.get(name);
    }

    public void updatePlayer(String name, Player player) {
        this.players.put(name, player);
        setChanged();
        notifyObservers(player);
    }

    public void readMapFile() {
        int continentFlag = 0, territoryFlag = 0;
        try {
            fileReader = new FileReader("D:\\Courses\\Soen 6441\\Project\\maps\\Empire of Alexander.map");
            scanner = new Scanner(fileReader);


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
                    this.continents.put(temp[0].trim(), new Continent(temp[0].trim(), Integer.parseInt(temp[1])));
                }

                if (territoryFlag == 1) {
                    if (line.equals("[Territories]"))
                        line = scanner.nextLine();

                    temp = line.split(",");
                    country = new Country(temp[0].trim(), temp[3].trim(), Double.parseDouble(temp[1].trim()), Double.parseDouble(temp[2].trim()));

                    for (int i = 4; i < temp.length - 1; i++) {
                        country.addNeighbour(temp[i]);
                    }

                    this.countries.put(temp[0], country);
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
