package game.settings;

import entity.Player;

import java.util.ArrayList;

/**
 * It holds the model for the data to be collected.
 *
 * @author shareenali
 */

class SettingsModel {
    private static SettingsModel instance;
    private ArrayList<Player> players = new ArrayList<>();

    /**
     * Gets the singleton reference to the class
     * @return instance of the model
     */
    static SettingsModel getInstance() {
        if (instance == null)
            instance = new SettingsModel();
        return instance;
    }

    /**
     * private controller for the singleton class
     */
    private SettingsModel() { }

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
     * @param name name of the player
     * @param type index of the player type combo box.
     */
    void addPlayer(String name, int type) {
        this.players.add(new Player(name, type));
    }

    /**
     * It returns list of players
     *
     * @return players
     */
    ArrayList<Player> getPlayers() {
        return players;
    }
}
