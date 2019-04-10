package risk.game.tournament.settings;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class TournamentSettingsView {
    private static final String[] COLORS = {"Red", "Purple", "Blue", "Teal", "Green", "Orange"};
    private static final String[] COLORS_HEX = {"#F44336", "#9C27B0", "#2196F3", "#009688", "#4CAF50", "#FF9800"};

    private JPanel panelMain;
    private JPanel panelNumbers;
    private JLabel labelNoMaps;
    private JComboBox comboNoMaps;
    private JLabel labelNoGames;
    private JComboBox comboNoGames;
    private JComboBox comboNoTurns;
    private JPanel panelPlayer;
    private JPanel panelNoPlayers;
    private JLabel labelNoPlayers;
    private JComboBox comboNoPlayers;
    private JPanel panelPlayers;
    private JLabel labelNoTurns;
    private JPanel panelActions;
    private JButton buttonStart;
    private JPanel panelStart;
    private JPanel panelTable;
    private JTable tableTournament;
    private JScrollPane scroll;
    private JPanel panelLogs;
    private JScrollPane scrollList;
    private JList listLogs;

    private DefaultComboBoxModel<Integer> modelNoPlayers = new DefaultComboBoxModel<>();
    private DefaultComboBoxModel<Integer> modelNoTurns = new DefaultComboBoxModel<>();
    private DefaultComboBoxModel<Integer> modelNoMaps = new DefaultComboBoxModel<>();
    private DefaultComboBoxModel<Integer> modelNoGames = new DefaultComboBoxModel<>();
    private DefaultTableModel modelTournament = new DefaultTableModel();
    private DefaultListModel<String> modelLogs = new DefaultListModel<>();

    private TournamentSettingsModel model = TournamentSettingsModel.getInstance();

    /**
     * Initializes the default values in UI.
     */
    @SuppressWarnings("unchecked")
    void initializeValues() {
        this.modelNoPlayers.removeAllElements();
        this.modelNoTurns.removeAllElements();

        for (int i = 2; i < 7; i++) {
            this.modelNoPlayers.addElement(i);
            this.modelNoTurns.addElement(i * 10);
            this.modelNoMaps.addElement(i - 1);
            this.modelNoGames.addElement(i - 1);
        }

        this.comboNoPlayers.setModel(this.modelNoPlayers);
        this.comboNoTurns.setModel(this.modelNoTurns);
        this.comboNoGames.setModel(this.modelNoGames);
        this.comboNoMaps.setModel(this.modelNoMaps);
        this.comboNoTurns.setSelectedIndex(0);
        this.createPlayerInfoPanels();

        this.tableTournament.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.tableTournament.setCellSelectionEnabled(true);
        this.tableTournament.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                modelLogs.removeAllElements();
                int column = tableTournament.getSelectedColumn() - 1;
                int row = tableTournament.getSelectedRow();
                if (column < 0) {
                    listLogs.setModel(modelLogs);
                    return;
                }

                TournamentSettingsModel model = TournamentSettingsModel.getInstance();
                for (String log : model.getLogs(String.valueOf(row).concat(String.valueOf(column)))) {
                    modelLogs.add(0, log);
                }
                modelLogs.add(0, "Map " + (row + 1) + " Game " + (column + 1));
                listLogs.setModel(modelLogs);
            }
        });
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

    void initTable() {
        int noOfMaps = this.modelNoMaps.getElementAt(this.comboNoMaps.getSelectedIndex());
        int noOfGames = this.modelNoGames.getElementAt(this.comboNoGames.getSelectedIndex());
        for (int i = 0; i < this.modelTournament.getRowCount(); i++) {
            this.modelTournament.removeRow(i);
        }
        this.modelTournament.addColumn("Maps");
        for (int i = 0; i < noOfGames; i++) {
            this.modelTournament.addColumn("Game " + (i + 1));
        }

        for (int i = 0; i < noOfMaps; i++) {
            String[] row = {"Map" + (i + 1)};
            this.modelTournament.addRow(row);
        }

        this.tableTournament.setModel(this.modelTournament);
    }

    void addGame(int map, int game, String winner) {
        if (this.modelTournament.getValueAt(map, game + 1) == null) {
            this.modelTournament.setValueAt(winner, map, game + 1);
            this.tableTournament.setModel(this.modelTournament);
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
        model.addElement("Random");
        model.addElement("Aggressive");
        model.addElement("Cheater");
        model.addElement("Benevolent");
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

    public void disableStartButton() {
        this.buttonStart.setEnabled(false);
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

    /**
     * It runs through all the UI components and collects the information
     */
    void collectData() {
        Component[] components = this.panelPlayers.getComponents();
        this.model.clearPlayers();
        int index = this.comboNoTurns.getSelectedIndex();
        int number = this.modelNoTurns.getElementAt(index);
        int noOfMaps = this.modelNoMaps.getElementAt(this.comboNoMaps.getSelectedIndex());
        int noOfGames = this.modelNoGames.getElementAt(this.comboNoGames.getSelectedIndex());

        this.model.setNoOfMapsAndGames(noOfGames, noOfMaps);

        for (Component component : components) {
            Component[] children = ((JPanel) component).getComponents();
            JTextField name = (JTextField) children[1];
            JComboBox type = (JComboBox) children[2];
            JComboBox color = (JComboBox) children[3];
            int selectedColor = color.getSelectedIndex();

            this.model.addPlayer(name.getText(), type.getSelectedIndex(), COLORS_HEX[selectedColor], number);
        }
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
        panelNumbers = new JPanel();
        panelNumbers.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panelMain.add(panelNumbers, BorderLayout.CENTER);
        labelNoMaps = new JLabel();
        labelNoMaps.setText("Number of maps:");
        panelNumbers.add(labelNoMaps);
        comboNoMaps = new JComboBox();
        panelNumbers.add(comboNoMaps);
        labelNoGames = new JLabel();
        labelNoGames.setText("Number of games:");
        panelNumbers.add(labelNoGames);
        comboNoGames = new JComboBox();
        panelNumbers.add(comboNoGames);
        panelPlayer = new JPanel();
        panelPlayer.setLayout(new BorderLayout(0, 0));
        panelMain.add(panelPlayer, BorderLayout.NORTH);
        panelNoPlayers = new JPanel();
        panelNoPlayers.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panelPlayer.add(panelNoPlayers, BorderLayout.NORTH);
        labelNoPlayers = new JLabel();
        labelNoPlayers.setText("Number of Players:");
        panelNoPlayers.add(labelNoPlayers);
        comboNoPlayers = new JComboBox();
        panelNoPlayers.add(comboNoPlayers);
        labelNoTurns = new JLabel();
        labelNoTurns.setText("Number of turns:");
        panelNoPlayers.add(labelNoTurns);
        comboNoTurns = new JComboBox();
        panelNoPlayers.add(comboNoTurns);
        panelPlayers = new JPanel();
        panelPlayers.setLayout(new GridBagLayout());
        panelPlayer.add(panelPlayers, BorderLayout.CENTER);
        panelActions = new JPanel();
        panelActions.setLayout(new BorderLayout(0, 0));
        panelMain.add(panelActions, BorderLayout.SOUTH);
        panelStart = new JPanel();
        panelStart.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panelActions.add(panelStart, BorderLayout.NORTH);
        buttonStart = new JButton();
        buttonStart.setText("Start Tournament");
        panelStart.add(buttonStart);
        panelTable = new JPanel();
        panelTable.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panelActions.add(panelTable, BorderLayout.CENTER);
        scroll = new JScrollPane();
        scroll.setPreferredSize(new Dimension(454, 100));
        panelTable.add(scroll);
        tableTournament = new JTable();
        scroll.setViewportView(tableTournament);
        panelLogs = new JPanel();
        panelLogs.setLayout(new BorderLayout(0, 0));
        panelActions.add(panelLogs, BorderLayout.SOUTH);
        scrollList = new JScrollPane();
        scrollList.setPreferredSize(new Dimension(300, 400));
        panelLogs.add(scrollList, BorderLayout.CENTER);
        listLogs = new JList();
        scrollList.setViewportView(listLogs);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panelMain;
    }
}
