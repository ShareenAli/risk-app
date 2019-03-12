package risk.game.main.dialog;

import javax.swing.*;

public class NoOfArmiesDialog {
    private JPanel panel;
    private JComboBox comboNoOfArmies = new JComboBox();
    private DefaultComboBoxModel<Integer> modelNoOfArmies = new DefaultComboBoxModel<>();

    /**
     * It initializes the dialog box values
     */
    public NoOfArmiesDialog() {
        this.panel = new JPanel();
        this.panel.add(new JLabel("No of armies to assign: "));
        this.panel.add(this.comboNoOfArmies);
    }

    /**
     * Sets number of armies for the drop down
     * @param noOfArmies number of armies
     */
    @SuppressWarnings("unchecked")
    public void setNoOfArmies(int noOfArmies) {
        this.modelNoOfArmies.removeAllElements();
        for (int i = 0; i < noOfArmies; i++) {
            this.modelNoOfArmies.addElement(i + 1);
        }
        this.comboNoOfArmies.setModel(this.modelNoOfArmies);
    }

    /**
     * Displays the dialog box
     * @param title title of the dialog box
     * @return result
     */
    public int showUi(String title) {
        int result = JOptionPane.showOptionDialog(null, this.panel, title,
            JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);

        return (result != -1) ? this.comboNoOfArmies.getSelectedIndex() + 1 : 0;
    }
}
