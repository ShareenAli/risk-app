package game.main.dialog;

import javax.swing.*;

public class NoOfArmiesDialog {
    private JPanel panel;
    private JComboBox comboNoOfArmies = new JComboBox();
    private DefaultComboBoxModel<Integer> modelNoOfArmies = new DefaultComboBoxModel<>();

    public NoOfArmiesDialog() {
        this.panel = new JPanel();
        this.panel.add(new JLabel("No of armies to assign: "));
        this.panel.add(this.comboNoOfArmies);
    }

    @SuppressWarnings("unchecked")
    public void setNoOfArmies(int noOfArmies) {
        this.modelNoOfArmies.removeAllElements();
        for (int i = 0; i < noOfArmies; i++) {
            this.modelNoOfArmies.addElement(i + 1);
        }
        this.comboNoOfArmies.setModel(this.modelNoOfArmies);
    }

    public int showUi(String title) {
        int result = JOptionPane.showOptionDialog(null, this.panel, title,
            JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);

        return (result != -1) ? this.comboNoOfArmies.getSelectedIndex() + 1 : 0;
    }
}
