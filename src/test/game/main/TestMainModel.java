package test.game.main;

import entity.Continent;
import entity.Country;
import entity.Player;
import risk.game.main.MainModel;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

/**
 * This test class verifies main model functionality
 *
 * @author iamdc003, jasmeet, shareenali
 * @version 0.2
 */
public class TestMainModel {
    private MainModel mainModel = new MainModel();

    @Before
    public void before() {
        ArrayList<Player> players = new ArrayList<>();
        ArrayList<Continent> continents = new ArrayList<>();
        Continent continent = new Continent("Asia", 5);
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

        continents.add(continent);

        this.mainModel.setMapContent(countries, continents);
    }

    /**
     * Test Case method for calculation of reinforcement armies
     */
    @Test
    public void mareinforcementArmies() {
        int armiesToAssign, controlValue = 0;
        armiesToAssign = (int) (int) Math.round(Math.floor((float) 3 / 3) < 3
                ? 3 : Math.floor((float) 3 / 3));

        armiesToAssign += controlValue;
        this.mainModel.resetArmiesToAssign(this.mainModel.getPlayer("dhaval").getName());
        int armiesAvailableToAssign = this.mainModel.getArmiesAvailableToAssign();
        assertEquals(armiesAvailableToAssign, armiesToAssign);
    }

    /**
     * Test Case method for calculation of reinforcement armies after
     */
    @Test
    public void calculateReinforcementArmies() {
        this.mainModel.getPlayer("shareen").setArmies("Russia", 1);
        this.mainModel.getPlayer("shareen").setArmies("Pakistan", 1);
        this.mainModel.getPlayer("shareen").setArmies("Bengal", 1);
        this.mainModel.getPlayer("dhaval").setArmies("India", 1);
        this.mainModel.getPlayer("dhaval").setArmies("China", 1);
        this.mainModel.getPlayer("dhaval").setArmies("Mongolia", 1);
        int updatedArmies = 4;
        this.mainModel.reinforcementPhase(this.mainModel.getPlayer("dhaval").getName(), this.mainModel.getCountries().get("India").getName(), 3);
        assertEquals(this.mainModel.getPlayer("dhaval").getArmiesInCountry("India"), updatedArmies);
    }

    /**
     * Check link between countries
     */
    @Test
    public void checkConnectionFort() {
        this.mainModel.getPlayer("shareen").setArmies("Russia", 1);
        this.mainModel.getPlayer("shareen").setArmies("Pakistan", 1);
        this.mainModel.getPlayer("shareen").setArmies("Bengal", 1);
        this.mainModel.getPlayer("dhaval").setArmies("India", 1);
        this.mainModel.getPlayer("dhaval").setArmies("China", 1);
        this.mainModel.getPlayer("dhaval").setArmies("Mongolia", 1);
        boolean resultExcpected = true;
        boolean resultActual = this.mainModel.checkForLink(new ArrayList<>(), "India", "Russia");
        assertEquals(resultActual, resultExcpected);
    }


    @Test
    public void checkDomination() {
        this.mainModel.getPlayer("shareen").setArmies("Russia", 1);
        this.mainModel.getPlayer("shareen").setArmies("Pakistan", 1);
        this.mainModel.getPlayer("shareen").setArmies("Bengal", 1);
        this.mainModel.getPlayer("dhaval").setArmies("India", 1);
        this.mainModel.getPlayer("dhaval").setArmies("China", 1);
        this.mainModel.getPlayer("dhaval").setArmies("Mongolia", 1);
        String dom = "50.0%";
        String[] result = this.mainModel.getDominationRow(this.mainModel.getPlayer("dhaval"));
        assertEquals(result[3], dom);
    }

    @Test
    public void checkCountryAssignment() {
        this.mainModel.assignCountry();
        assertEquals(this.mainModel.getPlayer("dhaval").getCountries().size(), 3);
    }

    @Test
    public void checkAssignArmies() {
        this.mainModel.getPlayer("shareen").setArmies("Russia", 1);
        this.mainModel.getPlayer("shareen").setArmies("Pakistan", 1);
        this.mainModel.getPlayer("shareen").setArmies("Bengal", 1);
        this.mainModel.getPlayer("dhaval").setArmies("India", 1);
        this.mainModel.getPlayer("dhaval").setArmies("China", 1);
        this.mainModel.getPlayer("dhaval").setArmies("Mongolia", 1);
        this.mainModel.assignArmies();
        int total = this.mainModel.getPlayer("dhaval").getArmiesInCountry("India") + this.mainModel.getPlayer("dhaval").getArmiesInCountry("China") + this.mainModel.getPlayer("dhaval").getArmiesInCountry("Mongolia");
        assertEquals(total, 43);
    }
}
