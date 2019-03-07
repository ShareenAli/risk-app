package game.main.world;

import entity.Country;
import entity.Player;
import game.main.MainModel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * View that implements the World UI
 * @author shareenali
 * @version 0.1
 */
@SuppressWarnings("deprecation")
public class WorldView implements Observer {
    private JPanel panelMain;
    private JLayeredPane layeredPane;
    private HashMap<String, JButton> countries = new HashMap<>();
    private HashMap<String, JLabel> armies = new HashMap<>();

    /**
     * Updates the view the observable notifies
     * @param o the model
     * @param arg identification string
     */
    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof String) {
            String content[] = ((String) arg).split(":");
            String actual = content[0].concat(":").concat(content[1]);
            switch (actual) {
                case MainModel.CHANGE_ARMY:
                    this.distributeCountriesToPlayers(((MainModel) o).getPlayers());
                    break;
                case MainModel.UPDATE_PLAYER:
                    this.updateCountryOfPlayer(((MainModel) o).getPlayer(content[2]));
                    break;
            }
        }
    }

    /**
     * Prepares the world ui
     * @param bmpFile image of the map
     */
    void prepUi(File bmpFile) {
        try {
            BufferedImage image = ImageIO.read(bmpFile);

            this.layeredPane = new JLayeredPane();
            this.layeredPane.setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));

            JLabel imageLabel = new JLabel(new ImageIcon(image));
            imageLabel.setBounds(0, 0, image.getWidth(), image.getHeight());

            this.layeredPane.add(imageLabel, 0, 10);
            this.panelMain.add(this.layeredPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads countries into the UI
     * @param countries list of countries
     * @param buttonCountryLs button listeners when countries are clicked
     */
    void loadCountries(ArrayList<Country> countries, ActionListener buttonCountryLs) {
        for (Country country : countries) {
            JButton button = new JButton(country.getName());
            int x = (int) country.getLatitude();
            int y = (int) country.getLongitude();
            int length = country.getName().length() * 8;
            button.setBounds(x - length, y - 6, length * 2 + 40, 20);
            button.setOpaque(false);
            button.setContentAreaFilled(false);
            button.setBorderPainted(false);
            button.addActionListener(buttonCountryLs);
            JLabel label = new JLabel();
            label.setBounds(x, y + 6, 20, 20);

            this.layeredPane.add(button, 0);
            this.layeredPane.add(label, 0);
            this.countries.put(country.getName(), button);
            this.armies.put(country.getName(), label);
        }
    }

    /**
     * Selects a country
     * @param country name of the country
     */
    void selectCountry(String country) {
        JButton button = this.countries.get(country);
        this.layeredPane.remove(button);

        button.setForeground(Color.BLACK);
        this.layeredPane.add(button, 0);

        this.countries.put(country, button);
    }

    /**
     * Updates a countries of player
     * @param player player for which the country is to updated
     */
    private void updateCountryOfPlayer(Player player) {
        HashMap<String, Player> players = new HashMap<>();
        players.put(player.getName(), player);
        this.distributeCountriesToPlayers(players);
    }

    /**
     * Update countries of all the players
     * @param players list of players
     */
    private void distributeCountriesToPlayers(HashMap<String, Player> players) {
        for (Map.Entry<String, Player> entry : players.entrySet()) {
            String name = entry.getKey();
            Player player = entry.getValue();

            for (Map.Entry<String, Integer> countryEntry : player.getCountries().entrySet()) {
                String country = countryEntry.getKey();

                JButton button = this.countries.get(country);
                JLabel label = this.armies.get(country);
                this.layeredPane.remove(button);
                this.layeredPane.remove(label);

                button.setActionCommand(name + ":" + country);
                button.setForeground(player.getColor());
                label.setText(String.valueOf(countryEntry.getValue()));
                label.setForeground(player.getColor());
                this.layeredPane.add(button, 0);
                this.layeredPane.add(label, 0);

                this.countries.put(country, button);
                this.armies.put(country, label);
            }
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
        panelMain.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panelMain;
    }
}
