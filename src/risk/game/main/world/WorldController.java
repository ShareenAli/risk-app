package risk.game.main.world;

import entity.Country;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

/**
 * Controller that handles the world view
 * @author shareenali
 * @version 0.2
 */
public class WorldController {
    private WorldView view;

    /**
     * The default controller that initializes the view and model
     */
    public WorldController() {
        this.view = new WorldView();
    }

    /**
     * Configure the User Interface when the data has been loaded into the main model
     * @param bmpFile image file
     * @param countries list of countries
     * @param buttonCountryLs listener when the countries are clicked
     */
    public void configureView(File bmpFile, ArrayList<Country> countries, ActionListener buttonCountryLs) {
        this.view.prepUi(bmpFile);
        this.view.loadCountries(countries, buttonCountryLs);
    }

    /**
     * Selects a country.
     *
     * @param country name of the country
     */
    public void selectCountry(String country) {
        this.view.selectCountry(country);
    }

    /**
     * Fetches the root panel
     * @return panel to append the view into
     */
    public JPanel getRootPanel() {
        return (JPanel) this.view.$$$getRootComponent$$$();
    }

    /**
     * Gets the view object
     * @return view
     */
    public WorldView getView() {
        return this.view;
    }
}
