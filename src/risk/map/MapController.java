package risk.map;

import entity.Continent;
import entity.Country;
import risk.map.country.CountryDialog;
import risk.RiskApp;
import risk.support.ActivityController;
import risk.support.DisplayFileChooser;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.util.*;

/**
 * Controller for the map editor
 * @author shareenali farhan
 * @version 0.1
 */
public class MapController extends ActivityController {
    private MapView view;
    private MapModel model;
    private ActionListener buttonChangeMapLs, buttonAddContinentLs, buttonUpdateContinentsLs;
    private ActionListener buttonDeleteContinentLs, buttonCountryLs, buttonSaveLs;
    private ListSelectionListener comboContinentLs;
    private MouseListener mouseListener;
    private String errorMessage;

    /**
     * It initializes the view and model
     */
    public MapController() {
        this.view = new MapView();
        this.model = new MapModel();
        this.errorMessage = "";
    }

    /**
     * Prepares the UI
     */
    @Override
    protected void prepareUi() {
        this.view.prepUi();
        this.frame.setContentPane(this.view.$$$getRootComponent$$$());
        this.initListeners();

        this.model.addObserver(this.view);
        this.bindListeners();
    }

    /**
     * Setup values for tests
     * @param countries list of countries
     * @param continents list of continents
     */
    public void setupValues(HashMap<String, Country> countries, HashMap<String, Continent> continents) {
        this.model.setValues(countries, continents);
    }

    /**
     * Sets values from the previous controller
     * @param data values from the previous controller
     */
    public void setupValues(HashMap<String, Object> data) {
        boolean isEditMode = (boolean) data.getOrDefault(RiskApp.MapIntent.KEY_EDIT, false);

        if (isEditMode) {
            DisplayFileChooser fileChooser = new DisplayFileChooser("/Users/ndkcha/Documents/university/app/risk-maps");
            fileChooser.updateExtension("risk/map");
            File file = fileChooser.openFile();

            if (file == null) {
                this.frame.dispose();
                return;
            }

            fileChooser.updateExtension("bmp");
            File image = fileChooser.openFile();

            if (image == null) {
                this.frame.dispose();
                return;
            }

            this.model.setBmpFile(image);
            this.frame.pack();
            this.frame.setLocationRelativeTo(null);

            this.loadExistingMap(file);

            this.model.hardNotifyView();
        }
    }

    /**
     * Binds the listeners the view
     */
    private void bindListeners() {
        this.view.bindChangeMapListener(this.buttonChangeMapLs);
        this.view.bindContinentButtonsListeners(this.buttonAddContinentLs, this.buttonUpdateContinentsLs,
            this.buttonDeleteContinentLs);
        this.view.bindContinentComboListener(this.comboContinentLs);
        this.view.bindMouseListener(this.mouseListener);
        this.view.bindCountryListener(this.buttonCountryLs);
        this.view.bindSaveListener(this.buttonSaveLs);
    }

    /**
     * initializes the action listeners
     */
    private void initListeners() {
        this.buttonChangeMapLs = (ActionEvent e) -> this.onMapChanged();
        this.buttonAddContinentLs = (ActionEvent e) -> this.onContinentAdded();
        this.buttonUpdateContinentsLs = (ActionEvent e) -> this.onContinentUpdated();
        this.buttonDeleteContinentLs = (ActionEvent e) -> this.onContinentDeleted();
        this.comboContinentLs = (ListSelectionEvent e) -> this.onContinentSelected();
        this.buttonCountryLs = (ActionEvent e) -> this.openCountryDialog(e.getActionCommand());
        this.buttonSaveLs = (ActionEvent e) -> this.prepareToSave();
        this.initMouseListener();
    }

    /**
     * Called when the continent is selected
     */
    private void onContinentSelected() {
        String name = this.view.selectedContinent();
        if (name == null)
            return;
        Continent continent = this.model.getContinent(name);
        this.view.editContinent(continent);
    }

    /**
     * Called when the continent is deleted
     */
    private void onContinentDeleted() {
        this.view.clearContinentSelection();
        Continent continent = this.view.constructContinentDetails();
        this.model.deleteContinent(continent.getName());
    }

    /**
     * Called when continent is added
     */
    private void onContinentAdded() {
        Continent continent = this.view.constructContinentDetails();
        this.model.saveContinent(continent);
    }

    /**
     * Called when continent is updated
     */
    private void onContinentUpdated() {
        this.view.clearContinentSelection();
        Continent continent = this.view.constructContinentDetails();
        this.model.saveContinent(continent);
    }

    /**
     * Called when the map file is changed
     */
    private void onMapChanged() {
        DisplayFileChooser fileChooser = new DisplayFileChooser("/Users/ndkcha/Documents/university/app/risk-maps");
        fileChooser.updateExtension("bmp");
        File bmp = fileChooser.openFile();

        this.model.setBmpFile(bmp);
        this.frame.pack();

        this.frame.setLocationRelativeTo(null);
    }

    /**
     * Open the country dialog
     * @param command action command that holds name of the country
     */
    private void openCountryDialog(String command) {
        Country country = this.model.getCountry(command);
        Country old = this.model.getCountry(command);

        CountryDialog dialog = new CountryDialog();
        dialog.setupData(country.getName(), country.getLatitude(), country.getLongitude());
        dialog.allocateContinents(this.model.getContinents().keySet(), country.getContinent());
        dialog.allocateCountries(this.model.getCountries().keySet(), country.getNeighbours());
        int result = dialog.showUi();

        if (result == -1)
            return;

        if (dialog.isToDelete()) {
            this.model.deleteCountry(country);
            return;
        }

        ArrayList<String> neighbours = dialog.getNeighbours();
        country.setValues(country.getName(), dialog.getContinentName(), dialog.getCountryLatitude(),
            dialog.getCountryLongitude());

        if (old != null) {
            for (String old_n : old.getNeighbours()) {
                if (neighbours.indexOf(old_n) == -1) {
                    Country c = this.model.getCountry(old_n);
                    c.removeNeighbour(country.getName());
                    this.model.saveCountryWithoutNotify(c);
                }
            }
        }

        country.resetNeighbours();
        for (String neighbour : neighbours) {
            country.addNeighbour(neighbour);
            Country c = this.model.getCountry(neighbour);
            c.addNeighbour(country.getName());
            this.model.saveCountryWithoutNotify(c);
        }
        this.model.saveCountry(country);
    }

    /**
     * Check for the errors on the map
     * @return true of there is any error
     */
    private boolean checkForErrors() {
        boolean noNeighbours, noContinent, noCountryInContinent, ghostNeighbours, subConnectedGraph;

        noContinent = this.validateNoContinent();
        noNeighbours = this.validateNoNeighbours();
        noCountryInContinent = this.validateNoCountryInContinent();
        ghostNeighbours = this.validateGhostNeighboursNolink();
        subConnectedGraph = this.isErrorInSubConnectedGraph();

        return noNeighbours || noContinent || noCountryInContinent
            || ghostNeighbours || subConnectedGraph;
    }

    /**
     * Check if the map is sub connected graph
     * @return true if it is not
     */
    public boolean isErrorInSubConnectedGraph() {
        for (Map.Entry<String, Continent> continentDataEntry : this.model.getContinents().entrySet()) {
            boolean isSubConnectedGraph = false;

            List<Country> countries = this.model.getCountriesInContinent(continentDataEntry.getKey());
            List<String> countryNames = this.countriesToName(countries);

            if (countries.size() < 2)
                continue;

            for (Country countryData : countries) {
                isSubConnectedGraph = false;

                for (String neighbour : countryData.getNeighbours()) {
                    if (countryNames.indexOf(neighbour) != -1) {
                        isSubConnectedGraph = true;
                        break;
                    }
                }

                if (!isSubConnectedGraph)
                    break;
            }

            if (!isSubConnectedGraph) {
                this.errorMessage = this.errorMessage.concat("\n" + continentDataEntry.getKey() +
                    " is not a sub-connected graph");
                return true;
            }
        }

        return false;
    }

    /**
     * Convert the country data list to country names list
     * @param countryDataList the country data list
     * @return the country names list
     */
    private List<String> countriesToName(List<Country> countryDataList) {
        List<String> names = new ArrayList<>();
        for (Country data : countryDataList) {
            names.add(data.getName());
        }

        return names;
    }

    /**
     * Prepare to save the game
     */
    private void prepareToSave() {
        DisplayFileChooser fileChooser = new DisplayFileChooser("/Users/ndkcha/Documents/university/app/risk-maps");
        fileChooser.updateExtension("risk/map");

        String fileName = fileChooser.saveFile();

        this.saveMap(fileName);
    }

    /**
     * Save the map
     * @param name name of the map file.
     */
    private void saveMap(String name) {
        this.errorMessage = "";
        if (this.checkForErrors()) {
            JOptionPane.showMessageDialog(new JFrame(), this.errorMessage,
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (FileWriter fileWriter = new FileWriter(name + ".map")) {
            BufferedWriter writer = new BufferedWriter(fileWriter);

            writer.write("[Map]\n");

            if (this.model.author != null)
                writer.write("author=" + this.model.author + "\n");

            String fileName = this.model.imageFileName;
            writer.write("image=" + fileName + "\n");

            writer.write(
                "wrap=" + (this.model.wrap ? "yes" : "no") + "\n");

            if (this.model.scrollType != null)
                writer.write("scroll=" + this.model.scrollType + "\n");

            writer.write(
                "warn=" + (this.model.warn ? "yes" : "no") + "\n");

            writer.write("\n");
            writer.write("[Continents]\n");

            for (Map.Entry<String, Continent> continentDataEntry : this.model.getContinents().entrySet()) {
                Continent data = continentDataEntry.getValue();
                writer.write(data.getName() + "=" + data.getControlValue()
                    + "\n");
            }

            writer.write("\n");
            writer.write("[Territories]\n");

            for (Map.Entry<String, Country> countryDataEntry : this.model.getCountries().entrySet()) {
                Country data = countryDataEntry.getValue();
                String temp = data.getName() + "," + data.getLatitude() + "," + data.getLongitude() + ","
                    + data.getContinent();

                for (String neighbour : data.getNeighbours()) {
                    temp = temp.concat("," + neighbour);
                }

                writer.write(temp + "\n");
            }

            writer.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * Initialize the mouse listeners
     */
    private void initMouseListener() {
        MapController controller = this;
        this.mouseListener = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (controller.model.getContinents().size() == 0)
                    return;

                CountryDialog dialog = new CountryDialog();
                dialog.setupData("", e.getX(), e.getY());
                dialog.allocateContinents(controller.model.getContinents().keySet(), null);
                dialog.allocateCountries(controller.model.getCountries().keySet(), new ArrayList<>());
                int result = dialog.showUi();

                if (result == -1)
                    return;

                Country country = new Country(dialog.getCountryName(), dialog.getContinentName(),
                    e.getX(), e.getY());
                ArrayList<String> neighbours = dialog.getNeighbours();
                for (String neighbour : neighbours) {
                    country.addNeighbour(neighbour);
                    Country c = controller.model.getCountry(neighbour);
                    c.addNeighbour(country.getName());
                    controller.model.saveCountryWithoutNotify(c);
                }

                controller.model.saveCountry(country);
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        };
    }

    /**
     * Check if the country is part of no continent
     * @return true if it not
     */
    public boolean validateNoContinent() {
        boolean noContinent = false;

        for (Map.Entry<String, Country> countryDataEntry : this.model.getCountries().entrySet()) {
            Country countryData = countryDataEntry.getValue();
            if (countryData.getContinent().length() == 0) {
                noContinent = true;
                this.errorMessage = this.errorMessage.concat("\n"
                    + countryData.getName() + " is part of no continents");
            }
        }

        return noContinent;
    }

    /**
     * Refactoring 1 This Method checks if country has neighbours.
     *
     * @return true If there are any errors.
     */
    public boolean validateNoNeighbours() {
        boolean noNeighbours = false;

        for (Map.Entry<String, Country> countryDataEntry : this.model.getCountries().entrySet()) {
            Country countryData = countryDataEntry.getValue();
            if (countryData.getNeighbours().size() == 0) {
                noNeighbours = true;
                this.errorMessage = this.errorMessage.concat(
                    "\n" + countryData.getName() + " has no neighbour");
            }
        }

        return noNeighbours;
    }

    /**
     * Refactoring 1 This Method checks if continent has country/countries
     * inside.
     *
     * @return true If there are any errors.
     */
    public boolean validateNoCountryInContinent() {
        boolean noCountryInContinent = false;

        for (Map.Entry<String, Continent> continentDataEntry : this.model.getContinents().entrySet()) {
            Continent data = continentDataEntry.getValue();

            List<Country> countries = this.model.getCountriesInContinent(data.getName());
            if (countries.size() == 0) {
                noCountryInContinent = true;
                this.errorMessage = this.errorMessage.concat("\n" + data.getName() + " has no country inside");
            }
        }

        return noCountryInContinent;
    }

    /**
     * Refactoring 1 This Method checks if the country does not exist but is a
     * neighbour of any country. and has no link to other continents.
     *
     * @return true If there are any errors.
     */
    public boolean validateGhostNeighboursNolink() {
        boolean ghostNeighbours = false;
        boolean noLink = false;

        for (Map.Entry<String, Continent> continentDataEntry : this.model.getContinents().entrySet()) {
            Continent data = continentDataEntry.getValue();
            boolean hasLink = false;

            List<Country> countries = this.model.getCountriesInContinent(data.getName());

            for (Country country : countries) {
                for (String neighbour : country.getNeighbours()) {
                    if (!this.model.doesCountryExist(neighbour)) {
                        ghostNeighbours = true;
                        this.errorMessage = this.errorMessage
                            .concat("\n" + neighbour
                                + " doesn't exist, but is a neighbour of "
                                + country.getName());
                    } else {
                        if (!this.model.getCountry(neighbour).getContinent()
                            .equalsIgnoreCase(data.getName()))
                            hasLink = true;
                    }
                }
            }

            if (!hasLink) {
                noLink = true;
                this.errorMessage = this.errorMessage
                    .concat("\n" + data.getName() + " has no link to any other continent");
            }
        }

        return ghostNeighbours || noLink;
    }

    /**
     * Loads existing map into the UI
     * @param mapFile map file
     * @return true if there are errors
     */
    public boolean loadExistingMap(File mapFile) {
        boolean invalidFormatError = false;
        this.errorMessage = "";
        try {
            String existingSegment = "";
            Scanner mapScanner = new Scanner(mapFile);

            while (mapScanner.hasNextLine()) {
                String incoming = mapScanner.nextLine();
                if (incoming.length() == 0)
                    continue;
                if (incoming.startsWith("[")) {
                    // start a segment
                    existingSegment = incoming;
                    continue;
                }
                if (existingSegment.equalsIgnoreCase("[map]")) {
                    String[] contents = incoming.split("=");
                    this.addToMapData(contents[0], contents[1]);
                }
                if (existingSegment.equalsIgnoreCase("[continents]")) {
                    Continent data = this.addContinent(incoming);
                    if (data == null) {
                        invalidFormatError = true;
                        continue;
                    }
                    this.model.saveContinentWithoutNotify(data);
                }
                if (existingSegment.equalsIgnoreCase("[territories]")) {
                    Country data = this.addCountry(incoming);
                    if (data == null) {
                        invalidFormatError = true;
                        continue;
                    }
                    this.model.saveCountryWithoutNotify(data);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        boolean otherErrors = this.checkForErrors();

        if (otherErrors || invalidFormatError) {
            JOptionPane.showMessageDialog(new JFrame(), this.errorMessage,
                "Error", JOptionPane.ERROR_MESSAGE);
        }

        return otherErrors || invalidFormatError;
    }

    /**
     * Parse the map data into object
     * @param field field of the data
     * @param value value of the data
     */
    private void addToMapData(String field, String value) {
        if (field.equalsIgnoreCase("image"))
            this.model.imageFileName = value;
        if (field.equalsIgnoreCase("wrap"))
            this.model.wrap = value.equalsIgnoreCase("yes");
        if (field.equalsIgnoreCase("scroll"))
            this.model.scrollType = value;
        if (field.equalsIgnoreCase("author"))
            this.model.author = value;
        if (field.equalsIgnoreCase("warn"))
            this.model.warn = value.equalsIgnoreCase("yes");
    }

    /**
     * Parse the continent data
     * @param incoming raw string
     * @return continent
     */
    private Continent addContinent(String incoming) {
        String[] contents = incoming.split("=");
        if (contents.length != 2) {
            this.errorMessage = this.errorMessage
                .concat("\n" + "Invalid format of the file");
        }
        return contents.length == 2
            ? new Continent(contents[0], Integer.parseInt(contents[1]))
            : null;
    }

    /**
     * Parse the country data
     * @param incoming raw string
     * @return country
     */
    private Country addCountry(String incoming) {
        String content[] = incoming.split(",");
        if (content.length < 4) {
            this.errorMessage = this.errorMessage
                .concat("\n" + "Invalid format of the file");
            return null;
        }
        Country data = new Country(content[0], content[3], Double.parseDouble(content[1]),
            Double.parseDouble(content[2]));
        for (int i = 4; i < content.length; i++) {
            data.addNeighbour(content[i]);
        }
        return data;
    }
}
