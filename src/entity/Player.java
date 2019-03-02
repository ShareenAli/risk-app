package entity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This holds the player entity and the runtime entity collected for player It holds
 * the countries he conquered and it can be updated anytime. It also holds list
 * of continents the he has conquered.
 * @author shareenali
 * @version 0.1
 */

public class Player {
    /** A human player. It can be used as player type */
    public static final int TYPE_HUMAN = 0;
    /** A computer player. It can be used as player type */
    public static final int TYPE_COMPUTER = 1;

    private String name;
    private int type;
    private HashMap<String, Integer> countries = new HashMap<>();
    private ArrayList<String> cards = new ArrayList<>();

    /**
     * It initializes the runtime player
     * @param name name of the player
     * @param type type of the player
     */
    public Player(String name, int type) {
        this.name = name;
        this.type = type;
    }

    /**
     * It returns name of the player
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * It returns type of the player
     * @return type
     */
    public int getType() {
        return type;
    }
}
