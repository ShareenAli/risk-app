package test.map;

import entity.Continent;
import entity.Country;
import org.junit.Before;
import org.junit.Test;
import risk.map.MapController;

import java.util.HashMap;

import static junit.framework.TestCase.assertTrue;

/**
 * This test class verifies map controller functionality
 *
 * @author iamdc003, shareenali
 * @version 0.2
 */

public class TestMapController {
    private HashMap<String, Country> countries = new HashMap<>();
    private HashMap<String, Continent> continents = new HashMap<>();
    private MapController mapController = new MapController();

    @Before
    public void before() {
        Country india = new Country("India", "Asia", 44, 23);
        Country china = new Country("China", "Asia", 42, 12);
        Country sudan = new Country("Sudan", "Africa", 22, 42);
        india.addNeighbour("Sudan");
        china.addNeighbour("Sudan");
        sudan.addNeighbour("India");
        sudan.addNeighbour("China");
        this.countries.put(india.getName(), india);
        this.countries.put(china.getName(), china);
        this.countries.put(sudan.getName(), sudan);
        this.continents.put("Asia", new Continent("Asia", 3));
        this.continents.put("Africa", new Continent("Africa", 3));
    }

    @Test
    public void isSubConnectedGraph() {
        this.mapController.setupValues(this.countries, this.continents);
        assertTrue(this.mapController.isErrorInSubConnectedGraph());
    }

    @Test
    public void isNoContinent() {
        Country chili = new Country("Chili", "", 33, 12);
        this.countries.put(chili.getName(), chili);
        this.mapController.setupValues(this.countries, this.continents);
        assertTrue(this.mapController.validateNoContinent());
    }

    @Test
    public void isNoNeighbours() {
        Country chili = new Country("Chili", "North America", 24, 14);
        this.countries.put(chili.getName(), chili);
        this.mapController.setupValues(this.countries, this.continents);
        assertTrue(this.mapController.validateNoNeighbours());
    }

    @Test
    public void isNoCountryInContinent() {
        Continent continent = new Continent("Europe", 3);
        this.continents.put(continent.getName(), continent);
        this.mapController.setupValues(this.countries, this.continents);
        assertTrue(this.mapController.validateNoCountryInContinent());
    }

    @Test
    public void isGhostNeighbours() {
        Country kenya = new Country("Kenya", "Africa", 32, 16);
        kenya.addNeighbour("Zimbabwe");
        this.countries.put(kenya.getName(), kenya);
        this.mapController.setupValues(this.countries, this.continents);
        assertTrue(this.mapController.validateGhostNeighboursNolink());
    }
}
