package risk.map.country;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Set;

public class CountryDialog {
    private JPanel panelMain;
    private JPanel panelName;
    private JLabel labelName;
    private JTextField textName;
    private JPanel panelContent;
    private JLabel labelContinent;
    private JComboBox comboContinent;
    private JPanel panelNeighbours;
    private JScrollPane scrollNeighbours;
    private JList listNeighbours;
    private JLabel labelNeighbours;
    private JPanel panelContinent;
    private JPanel panelCoord;
    private JLabel labelCoordinates;
    private JTextField textX;
    private JTextField textY;
    private JPanel panelDelete;
    private JCheckBox checkDelete;

    private DefaultComboBoxModel<String> modelContinent = new DefaultComboBoxModel<>();
    private DefaultListModel<String> modelNeighbours = new DefaultListModel<>();

    public void setupData(String name, double latitude, double longitude) {
        this.textName.setText(name);
        this.textX.setText(String.valueOf(latitude));
        this.textY.setText(String.valueOf(longitude));
    }

    @SuppressWarnings("unchecked")
    public void allocateContinents(Set<String> continents, String incomingContinent) {
        this.modelContinent.removeAllElements();
        int index = 0, i = 0;
        for (String continent : continents) {
            if (continent.equalsIgnoreCase(incomingContinent))
                index = i;
            this.modelContinent.addElement(continent);
            i++;
        }
        this.comboContinent.setModel(this.modelContinent);
        this.comboContinent.setSelectedIndex(index);
    }

    @SuppressWarnings("unchecked")
    public void allocateCountries(Set<String> countries, ArrayList<String> neighbours) {
        ArrayList<Integer> indexes = new ArrayList<>();
        this.modelNeighbours.removeAllElements();
        int i = 0;
        for (String country : countries) {
            if (neighbours.indexOf(country) != -1)
                indexes.add(i);
            if (this.textName.getText().equalsIgnoreCase(country))
                continue;
            this.modelNeighbours.addElement(country);
            i++;
        }
        this.listNeighbours.setModel(this.modelNeighbours);
        int indices[] = new int[indexes.size()];
        for (int j = 0; j < indexes.size(); j++) {
            indices[j] = indexes.get(j);
        }
        this.listNeighbours.setSelectedIndices(indices);
    }

    public String getCountryName() {
        return this.textName.getText();
    }

    public String getContinentName() {
        return this.modelContinent.getElementAt(this.comboContinent.getSelectedIndex());
    }

    public double getCountryLatitude() {
        return Double.parseDouble(this.textX.getText());
    }

    public double getCountryLongitude() {
        return Double.parseDouble(this.textY.getText());
    }

    public boolean isToDelete() {
        return this.checkDelete.isSelected();
    }

    public ArrayList<String> getNeighbours() {
        ArrayList<String> result = new ArrayList<>();
        int index[] = this.listNeighbours.getSelectedIndices();
        for (int i : index) {
            result.add(this.modelNeighbours.get(i));
        }

        return result;
    }

    public int showUi() {
        return JOptionPane.showOptionDialog(null, this.$$$getRootComponent$$$(), "Add a country",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
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
        panelName = new JPanel();
        panelName.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panelMain.add(panelName, BorderLayout.NORTH);
        labelName = new JLabel();
        labelName.setText("Name:");
        panelName.add(labelName);
        textName = new JTextField();
        textName.setPreferredSize(new Dimension(120, 27));
        panelName.add(textName);
        panelContent = new JPanel();
        panelContent.setLayout(new BorderLayout(0, 0));
        panelMain.add(panelContent, BorderLayout.CENTER);
        panelContinent = new JPanel();
        panelContinent.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panelContent.add(panelContinent, BorderLayout.NORTH);
        labelContinent = new JLabel();
        labelContinent.setText("Continent:");
        panelContinent.add(labelContinent);
        comboContinent = new JComboBox();
        panelContinent.add(comboContinent);
        panelCoord = new JPanel();
        panelCoord.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panelContent.add(panelCoord, BorderLayout.CENTER);
        labelCoordinates = new JLabel();
        labelCoordinates.setText("(x, y):");
        panelCoord.add(labelCoordinates);
        textX = new JTextField();
        textX.setPreferredSize(new Dimension(50, 27));
        panelCoord.add(textX);
        textY = new JTextField();
        textY.setPreferredSize(new Dimension(50, 27));
        panelCoord.add(textY);
        panelDelete = new JPanel();
        panelDelete.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panelContent.add(panelDelete, BorderLayout.SOUTH);
        checkDelete = new JCheckBox();
        checkDelete.setText("Delete this country?");
        panelDelete.add(checkDelete);
        panelNeighbours = new JPanel();
        panelNeighbours.setLayout(new BorderLayout(0, 0));
        panelMain.add(panelNeighbours, BorderLayout.SOUTH);
        scrollNeighbours = new JScrollPane();
        scrollNeighbours.setPreferredSize(new Dimension(256, 180));
        panelNeighbours.add(scrollNeighbours, BorderLayout.SOUTH);
        listNeighbours = new JList();
        scrollNeighbours.setViewportView(listNeighbours);
        labelNeighbours = new JLabel();
        labelNeighbours.setText("Choose neighbours:");
        panelNeighbours.add(labelNeighbours, BorderLayout.CENTER);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panelMain;
    }
}
