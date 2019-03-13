package test.map;

import entity.Continent;
import entity.Country;
import org.junit.Before;
import org.junit.Test;
import risk.map.MapModel;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;

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

    @Test
    public void deleteCountry() {
        Country countryToDelete = this.model.getCountries().get("China");
        this.model.deleteCountry(countryToDelete);

        assertFalse(this.model.getCountries().containsKey("China"));
        assertEquals(this.model.getCountries().get("India").getNeighbours().indexOf("China"), -1);
    }

    @Test
    public void deleteContinent() {
        this.model.deleteContinent("Asia");

        assertFalse(this.model.getCountries().containsKey("China"));
        assertFalse(this.model.getCountries().containsKey("India"));
    }
}
