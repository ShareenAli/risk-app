package testgame.main;

import entity.Country;
import entity.Player;
import game.main.MainModel;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static junit.framework.TestCase.assertEquals;

public class TestMainModel {
    private MainModel mainModel = new MainModel();

    @Before
    public void before() {
        ArrayList<Player> players = new ArrayList<>();
        players.add(new Player("dhaval", 0));
        players.add(new Player("shareen", 1));

        this.mainModel.setPlayers(players);
        ArrayList<Country> countries = new ArrayList<>();
        countries.add(new Country("India", "Asia", 1, 1));
        countries.add(new Country("India", "Asia", 1, 1));
        countries.add(new Country("India", "Asia", 1, 1));
        countries.add(new Country("India", "Asia", 1, 1));
        countries.add(new Country("India", "Asia", 1, 1));
        this.mainModel.setMapContent(countries, new ArrayList<>());
    }

    /**
     * Test Case method for calculation of reinforcement armies
     */
    @Test
    public void reinforcementArmies() {
        double armiesToAssign;
        armiesToAssign = Math.floor(5.0 / 3) < 3 ? 3 : Math.floor(5.0 / 3);
        this.mainModel.reinforcementPhase(this.mainModel.getPlayer("dhaval").getName(), this.mainModel.getCountries().get("India").getName(), 4);
        double armiesAvailableToAssign = this.mainModel.getArmiesAvailableToAssign();
        assertEquals(armiesAvailableToAssign, armiesToAssign);
    }
}
