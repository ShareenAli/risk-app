package risk.map;

import entity.Continent;
import entity.Country;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

@SuppressWarnings("deprecation")
public class MapView implements Observer {
    private JLayeredPane layeredPane;
    private JPanel panelMain;
    private JPanel panelContent;
    private JPanel panelContinent;
    private JList listContinents;
    private JScrollPane scrollContinents;
    private JPanel panelContinentName;
    private JLabel labelContinentName;
    private JTextField textContinentName;
    private JPanel panelContinentCv;
    private JLabel labelContinentCv;
    private JTextField textContinentCv;
    private JPanel panelContinentActions;
    private JButton buttonAddContinent;
    private JButton buttonUpdateContinent;
    private JButton buttonDeleteContinent;
    private JPanel panelMap;
    private JPanel panelMapLoader;
    private JLabel labelMap;
    private JPanel panelMapName;
    private JPanel panelMapButton;
    private JButton buttonChangeMap;
    private JButton buttonSave;

    private JLabel imageLabel;
    private DefaultListModel<String> modelContinents = new DefaultListModel<>();
    private HashMap<String, JButton> countries = new HashMap<>();
    private ActionListener buttonCountryLs;

    /**
     * Prepare the UI
     */
    void prepUi() {
        this.layeredPane = new JLayeredPane();

        this.panelMain.add(this.layeredPane);
    }

    /**
     * Bind action listener to change map
     *
     * @param listener listener to change map
     */
    void bindChangeMapListener(ActionListener listener) {
        this.buttonChangeMap.addActionListener(listener);
    }

    /**
     * Bind action listeners to continent buttons
     *
     * @param add    add continent
     * @param update update continent
     * @param delete delete continent
     */
    void bindContinentButtonsListeners(ActionListener add, ActionListener update, ActionListener delete) {
        this.buttonAddContinent.addActionListener(add);
        this.buttonUpdateContinent.addActionListener(update);
        this.buttonDeleteContinent.addActionListener(delete);
    }

    /**
     * Bind the continent combo listeners
     *
     * @param listener listener for the combo
     */
    void bindContinentComboListener(ListSelectionListener listener) {
        this.listContinents.addListSelectionListener(listener);
    }

    /**
     * Bind the mouse actions
     *
     * @param listener mouse listener
     */
    void bindMouseListener(MouseListener listener) {
        this.layeredPane.addMouseListener(listener);
    }

    /**
     * Bind country listener
     *
     * @param listener click listener
     */
    void bindCountryListener(ActionListener listener) {
        this.buttonCountryLs = listener;
    }

    /**
     * Bind save listener
     *
     * @param listener click listener
     */
    void bindSaveListener(ActionListener listener) {
        this.buttonSave.addActionListener(listener);
    }

    /**
     * Notifies changes
     *
     * @param o   model class
     * @param arg notifier action
     */
    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof String) {
            String content[] = ((String) arg).split(":");
            String actual = content[0].concat(":").concat(content[1]);

            switch (actual) {
                case MapModel.UPDATE_MAP:
                    this.updateMap(((MapModel) o).getBmpFile());
                    break;
                case MapModel.UPDATE_ALL:
                    this.updateMap(((MapModel) o).getBmpFile());
                    this.updateContinent(((MapModel) o).getContinents());
                    this.updateCountries(((MapModel) o).getCountries());
                    break;
                case MapModel.UPDATE_CONTINENTS:
                    this.updateContinent(((MapModel) o).getContinents());
                    break;
                case MapModel.UPDATE_COUNTRIES:
                    this.updateCountries(((MapModel) o).getCountries());
                    break;
            }
        }
    }

    /**
     * Edit continent
     *
     * @param continent continent to edit
     */
    void editContinent(Continent continent) {
        this.textContinentName.setText(continent.getName());
        this.textContinentCv.setText(String.valueOf(continent.getControlValue()));
    }

    /**
     * Get the selected continent
     *
     * @return continent
     */
    String selectedContinent() {
        return (this.listContinents.getSelectedIndex() > -1)
                ? this.modelContinents.get(this.listContinents.getSelectedIndex())
                : null;
    }

    /**
     * Clear the continent selection from the view
     */
    void clearContinentSelection() {
        this.listContinents.setSelectedIndex(-1);
    }

    /**
     * Update the countries on the view
     *
     * @param countries list of countries
     */
    private void updateCountries(HashMap<String, Country> countries) {
        this.layeredPane.removeAll();
        this.layeredPane.add(this.imageLabel, 0, 10);
        for (Map.Entry<String, Country> countryEntry : countries.entrySet()) {
            Country country = countryEntry.getValue();
            JButton button = this.countries.getOrDefault(country.getName(), new JButton(country.getName()));
            int x = (int) country.getLatitude();
            int y = (int) country.getLongitude();
            int length = country.getName().length() * 8;
            button.setBounds(x - length, y - 6, length * 2 + 40, 20);
            button.setOpaque(false);
            button.setContentAreaFilled(false);
            button.setBorderPainted(false);
            button.removeActionListener(buttonCountryLs);
            button.setActionCommand(country.getName());

            for (ActionListener listener : button.getActionListeners()) {
                button.removeActionListener(listener);
            }
            button.addActionListener(buttonCountryLs);

            this.layeredPane.add(button, 0);
            this.countries.put(country.getName(), button);
        }
        this.panelMain.revalidate();
        this.panelMain.repaint();
    }

    /**
     * Update the continent on view
     *
     * @param continents list of continents
     */
    @SuppressWarnings("unchecked")
    private void updateContinent(HashMap<String, Continent> continents) {
        this.modelContinents.removeAllElements();

        for (Map.Entry<String, Continent> continentEntry : continents.entrySet()) {
            this.modelContinents.addElement(continentEntry.getKey());
        }

        this.listContinents.setModel(this.modelContinents);
    }

    /**
     * Updates the image file
     *
     * @param file image file
     */
    private void updateMap(File file) {
        try {
            BufferedImage image = ImageIO.read(file);

            this.layeredPane.setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));

            this.imageLabel = new JLabel(new ImageIcon(image));
            this.imageLabel.setBounds(0, 0, image.getWidth(), image.getHeight());

            this.layeredPane.add(this.imageLabel, 0, 10);
            this.labelMap.setText(file.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Makes the continent object
     *
     * @return continent
     */
    Continent constructContinentDetails() {
        String name = this.textContinentName.getText();
        int controlValue = Integer.parseInt(this.textContinentCv.getText());

        this.textContinentName.setText("");
        this.textContinentCv.setText("");

        return new Continent(name, controlValue);
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panelMain = new JPanel();
        panelMain.setLayout(new BorderLayout(0, 0));
        panelContent = new JPanel();
        panelContent.setLayout(new BorderLayout(0, 0));
        panelMain.add(panelContent, BorderLayout.WEST);
        panelContinent = new JPanel();
        panelContinent.setLayout(new BorderLayout(0, 0));
        panelContent.add(panelContinent, BorderLayout.NORTH);
        panelContinentName = new JPanel();
        panelContinentName.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panelContinent.add(panelContinentName, BorderLayout.NORTH);
        labelContinentName = new JLabel();
        labelContinentName.setText("Continent Name:");
        panelContinentName.add(labelContinentName);
        textContinentName = new JTextField();
        textContinentName.setPreferredSize(new Dimension(120, 27));
        panelContinentName.add(textContinentName);
        panelContinentCv = new JPanel();
        panelContinentCv.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panelContinent.add(panelContinentCv, BorderLayout.CENTER);
        labelContinentCv = new JLabel();
        labelContinentCv.setText("Control Value:");
        panelContinentCv.add(labelContinentCv);
        textContinentCv = new JTextField();
        textContinentCv.setPreferredSize(new Dimension(70, 27));
        panelContinentCv.add(textContinentCv);
        panelContinentActions = new JPanel();
        panelContinentActions.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panelContinent.add(panelContinentActions, BorderLayout.SOUTH);
        buttonAddContinent = new JButton();
        buttonAddContinent.setText("Add");
        panelContinentActions.add(buttonAddContinent);
        buttonUpdateContinent = new JButton();
        buttonUpdateContinent.setText("Update");
        panelContinentActions.add(buttonUpdateContinent);
        buttonDeleteContinent = new JButton();
        buttonDeleteContinent.setText("Delete");
        panelContinentActions.add(buttonDeleteContinent);
        scrollContinents = new JScrollPane();
        scrollContinents.setPreferredSize(new Dimension(300, 300));
        panelContent.add(scrollContinents, BorderLayout.CENTER);
        listContinents = new JList();
        scrollContinents.setViewportView(listContinents);
        panelMapLoader = new JPanel();
        panelMapLoader.setLayout(new BorderLayout(0, 0));
        panelContent.add(panelMapLoader, BorderLayout.SOUTH);
        panelMapName = new JPanel();
        panelMapName.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panelMapLoader.add(panelMapName, BorderLayout.NORTH);
        labelMap = new JLabel();
        labelMap.setText("Map:");
        panelMapName.add(labelMap);
        panelMapButton = new JPanel();
        panelMapButton.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panelMapLoader.add(panelMapButton, BorderLayout.CENTER);
        buttonChangeMap = new JButton();
        buttonChangeMap.setText("Change Image File");
        panelMapButton.add(buttonChangeMap);
        buttonSave = new JButton();
        buttonSave.setText("Save");
        panelMapButton.add(buttonSave);
        panelMap = new JPanel();
        panelMap.setLayout(new BorderLayout(0, 0));
        panelMain.add(panelMap, BorderLayout.CENTER);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panelMain;
    }
}
