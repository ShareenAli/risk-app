package risk.game.settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * It initializes the view for Game Settings
 *
 * @author shareenali
 * @version 0.1
 */

public class SettingsView {
    private static final String[] COLORS = {"Red", "Purple", "Blue", "Teal", "Green", "Orange"};
    private static final String[] COLORS_HEX = {"#F44336", "#9C27B0", "#2196F3", "#009688", "#4CAF50", "#FF9800"};
    private JPanel panelSettings;
    private JPanel panelTitle;
    private JLabel labelTitle;
    private JPanel panelContent;
    private JPanel panelNoPlayers;
    private JLabel labelNoPlayers;
    private JComboBox comboNoPlayers;
    private JPanel panelPlayers;
    private JPanel panelActions;
    private JButton buttonStart;
    private JPanel panelFiles;
    private JPanel panelMapFile;
    private JPanel panelBmpFile;
    private JLabel labelMapFile;
    private JLabel labelMapFileName;
    private JButton buttonMap;
    private JLabel labelBmpFile;
    private JLabel labelBmpFileName;
    private JButton buttonBmp;

    private DefaultComboBoxModel<Integer> modelNoPlayers = new DefaultComboBoxModel<>();

    private SettingsModel model = SettingsModel.getInstance();

    /**
     * Initializes the default values in UI.
     */
    @SuppressWarnings("unchecked")
    void initializeValues() {
        this.modelNoPlayers.removeAllElements();

        for (int i = 2; i < 7; i++) {
            this.modelNoPlayers.addElement(i);
        }

        this.comboNoPlayers.setModel(this.modelNoPlayers);
        this.createPlayerInfoPanels();
    }

    /**
     * Modifies the file name label for bmp
     *
     * @param name name of the file
     */
    void updateBmpFileName(String name) {
        this.labelBmpFileName.setText(name);
    }

    /**
     * Modifies the file name label for map.
     *
     * @param name name of the file.
     */
    void updateMapFileName(String name) {
        this.labelMapFileName.setText(name);
    }

    /**
     * Binds the useful listeners to combo box of the number of players
     *
     * @param listener onItemSelected listener
     */
    void bindComboNoPlayersListeners(ActionListener listener) {
        this.comboNoPlayers.addActionListener(listener);
    }

    /**
     * Binds the useful listeners to start game button
     *
     * @param listener onClick listener
     */
    void bindButtonStartListeners(ActionListener listener) {
        this.buttonStart.addActionListener(listener);
    }

    /**
     * Binds useful listeners to map and bmp file buttons
     *
     * @param mapListener listener for map file
     * @param bmpListener listener for bmp file
     */
    void bindMapButtonsListeners(ActionListener mapListener, ActionListener bmpListener) {
        this.buttonMap.addActionListener(mapListener);
        this.buttonBmp.addActionListener(bmpListener);
    }

    /**
     * It runs through all the UI components and collects the information
     */
    void collectData() {
        Component[] components = this.panelPlayers.getComponents();
        this.model.clearPlayers();

        for (Component component : components) {
            Component[] children = ((JPanel) component).getComponents();
            JTextField name = (JTextField) children[1];
            JComboBox type = (JComboBox) children[2];
            JComboBox color = (JComboBox) children[3];
            int selectedColor = color.getSelectedIndex();

            this.model.addPlayer(name.getText(), type.getSelectedIndex(), COLORS_HEX[selectedColor]);
        }
    }

    /**
     * Creates a new panel of the player w.r.t number of players
     */
    @SuppressWarnings("unchecked")
    void createPlayerInfoPanels() {
        int noOfPlayers = this.modelNoPlayers.getElementAt(this.comboNoPlayers.getSelectedIndex());
        this.panelPlayers.removeAll();

        for (int i = 0; i < noOfPlayers; i++) {
            JPanel panelPlayer = getPlayerPanel(i);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.fill = GridBagConstraints.BOTH;

            panelPlayers.add(panelPlayer, gbc);
        }
    }

    /**
     * Construct and return the player panel for multiple players input.
     *
     * @param index index of the view to set to
     * @return panel of the player
     */
    @SuppressWarnings("unchecked")
    private JPanel getPlayerPanel(int index) {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        JLabel labelName = new JLabel();
        labelName.setText("Name:");
        panel.add(labelName);
        JTextField textName = new JTextField();
        textName.setText(this.getName(index));
        textName.setPreferredSize(new Dimension(164, 27));
        panel.add(textName);
        JComboBox comboType = new JComboBox();
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        model.addElement("Human");
        model.addElement("Computer");
        comboType.setModel(model);
        panel.add(comboType);
        JComboBox comboColor = new JComboBox();
        DefaultComboBoxModel<String> modelColors = new DefaultComboBoxModel<>();
        for (String color : COLORS) {
            modelColors.addElement(color);
        }
        comboColor.setModel(modelColors);
        comboColor.setSelectedIndex(index);
        panel.add(comboColor);

        return panel;
    }

    /**
     * Get the name for the default value in players' name input box
     *
     * @param i index of the box
     * @return name of the player
     */
    private String getName(int i) {
        switch (i) {
            case 0:
                return "shareen";
            case 1:
                return "dhaval";
            case 2:
                return "yashash";
            case 3:
                return "farhan";
            case 4:
                return "jasmeet";
        }

        return "player" + i;
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
        panelSettings = new JPanel();
        panelSettings.setLayout(new BorderLayout(0, 0));
        panelTitle = new JPanel();
        panelTitle.setLayout(new GridBagLayout());
        panelSettings.add(panelTitle, BorderLayout.NORTH);
        labelTitle = new JLabel();
        Font labelTitleFont = this.$$$getFont$$$(null, -1, 16, labelTitle.getFont());
        if (labelTitleFont != null) labelTitle.setFont(labelTitleFont);
        labelTitle.setText("Game Settings");
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.ipadx = 16;
        gbc.ipady = 16;
        panelTitle.add(labelTitle, gbc);
        panelContent = new JPanel();
        panelContent.setLayout(new BorderLayout(0, 0));
        panelSettings.add(panelContent, BorderLayout.CENTER);
        panelNoPlayers = new JPanel();
        panelNoPlayers.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panelContent.add(panelNoPlayers, BorderLayout.NORTH);
        labelNoPlayers = new JLabel();
        labelNoPlayers.setText("Number of Players:");
        panelNoPlayers.add(labelNoPlayers);
        comboNoPlayers = new JComboBox();
        panelNoPlayers.add(comboNoPlayers);
        panelPlayers = new JPanel();
        panelPlayers.setLayout(new GridBagLayout());
        panelContent.add(panelPlayers, BorderLayout.CENTER);
        panelFiles = new JPanel();
        panelFiles.setLayout(new BorderLayout(0, 0));
        panelContent.add(panelFiles, BorderLayout.SOUTH);
        panelMapFile = new JPanel();
        panelMapFile.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panelFiles.add(panelMapFile, BorderLayout.NORTH);
        labelMapFile = new JLabel();
        labelMapFile.setText("Map File:");
        panelMapFile.add(labelMapFile);
        labelMapFileName = new JLabel();
        labelMapFileName.setText("<no file>");
        panelMapFile.add(labelMapFileName);
        buttonMap = new JButton();
        buttonMap.setText("Change Map");
        panelMapFile.add(buttonMap);
        panelBmpFile = new JPanel();
        panelBmpFile.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panelFiles.add(panelBmpFile, BorderLayout.CENTER);
        labelBmpFile = new JLabel();
        labelBmpFile.setText("Bmp File:");
        panelBmpFile.add(labelBmpFile);
        labelBmpFileName = new JLabel();
        labelBmpFileName.setText("<no file>");
        panelBmpFile.add(labelBmpFileName);
        buttonBmp = new JButton();
        buttonBmp.setText("Change Bmp");
        panelBmpFile.add(buttonBmp);
        panelActions = new JPanel();
        panelActions.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panelSettings.add(panelActions, BorderLayout.SOUTH);
        buttonStart = new JButton();
        buttonStart.setText("Start");
        panelActions.add(buttonStart);
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        return new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panelSettings;
    }
}
