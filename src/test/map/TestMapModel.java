package test.map;

import entity.Continent;
import entity.Country;
import org.junit.Before;
import org.junit.Test;
import risk.map.MapModel;

import static junit.framework.TestCase.*;

/**
 * This test class verifies map model functionality
 *
 * @author iamdc003, shareenali
 * @version 0.2
 */

public class TestMapModel {
    private MapModel model = new MapModel();

    @Before
    public void before() {
        this.model.saveContinent(new Continent("Asia", 4));
        this.model.saveContinent(new Continent("Africa", 6));

        Country india = new Country("India", "Asia", 34, 44);
        india.addNeighbour("China");
        india.addNeighbour("Sudan");
        this.model.saveCountry(india);
        Country china = new Country("China", "Asia", 14, 44);
        china.addNeighbour("India");
        china.addNeighbour("Kenya");
        this.model.saveCountry(china);
        Country sudan = new Country("Sudan", "Africa", 34, 84);
        sudan.addNeighbour("India");
        sudan.addNeighbour("Kenya");
        this.model.saveCountry(sudan);
        Country kenya = new Country("Kenya", "Africa", 34, 54);
        kenya.addNeighbour("Sudan");
        kenya.addNeighbour("China");
        this.model.saveCountry(kenya);
    }

    /**
     * Test case method to delete a country
     */
    @Test
    public void testDeleteCountry() {
        Country countryToDelete = this.model.getCountries().get("China");
        this.model.deleteCountry(countryToDelete);

        assertFalse(this.model.getCountries().containsKey("China"));
        assertEquals(this.model.getCountries().get("India").getNeighbours().indexOf("China"), -1);
    }

    /**
     * Test case method to delete a continent
     */
    @Test
    public void testDeleteContinent() {
        this.model.deleteContinent("Asia");

        assertFalse(this.model.getCountries().containsKey("China"));
        assertFalse(this.model.getCountries().containsKey("India"));
    }

    /**
     * Test case method to verify the presence of country in map
     */
    @Test
    public void testCountryExist(){
        assertTrue(this.model.getCountries().containsKey("India"));
    }

    /**
     * Test case method to add a country
     */
    @Test
    public void testAddCountry(){
        Country russia = new Country("Russia", "Asia", 28, 51);
        russia.addNeighbour("China");
        this.model.saveCountry(russia);
        assertTrue(this.model.getCountries().containsKey("Russia"));
    }

    /**
     * Test case method to add a continent
     */
    @Test
    public void testAddContinent(){
        this.model.saveContinent(new Continent("Australia", 2));
        Country newzealand = new Country("New Zealand", "Australia", 28, 51);
        newzealand.addNeighbour("China");
        this.model.saveCountry(newzealand);
        assertTrue(this.model.getContinents().containsKey("Australia"));
    }


}
