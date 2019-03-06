package testgame.main;

import entity.Country;
import entity.Player;
import game.main.MainModel;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

/**
 * This test class verifies main model functionality
 *
 * @author Dhaval, Jasmeet
 */
public class TestMainModel {
    private MainModel mainModel = new MainModel();

    @Before
    public void before() {
        ArrayList<Player> players = new ArrayList<>();
        players.add(new Player("dhaval", 0));
        players.add(new Player("shareen", 1));

        this.mainModel.setPlayers(players);
        ArrayList<Country> countries = new ArrayList<>();
        Country country1 = new Country("India", "Asia", 1, 1);
        Country country2 = new Country("Russia", "Asia", 1, 1);
        Country country3 = new Country("China", "Asia", 1, 1);
        Country country4 = new Country("Pakistan", "Asia", 1, 1);
        Country country5 = new Country("Mongolia", "Asia", 1, 1);
        Country country6 = new Country("Bengal", "Asia", 1, 1);

        country1.addNeighbour(country2.getName());
        country2.addNeighbour(country1.getName());
        country3.addNeighbour(country4.getName());
        country4.addNeighbour(country3.getName());
        country4.addNeighbour(country5.getName());
        country5.addNeighbour(country4.getName());
        country6.addNeighbour(country5.getName());
        country5.addNeighbour(country6.getName());

        countries.add(country1);
        countries.add(country2);
        countries.add(country3);
        countries.add(country4);
        countries.add(country5);
        countries.add(country6);

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
/**
     * Test Case method for verification of fortification phase
     */
    @Test
    public void fortificationPhase() {
        boolean resultActual = true;
        
        this.mainModel.fortificationPhase(this.mainModel.getPlayer("dhaval").getName(), this.mainModel.getCountries().get("China").getName(), this.mainModel.getCountries().get("Bengal").getName(), 3);
//        assertEquals(resultActual, this.mainModel.result);
    }


    /**
     * Test Case method for verification of countries if connected
     * in fortification phase
     */
    @Test
    public void testForContriesLinkedInFortificationPhase() {
//        assertTrue(this.mainModel.checkForLink(countries,this.mainModel.getCountries().get("China").getName(), this.mainModel.getCountries().get("Bengal").getName());
    }
    
}
