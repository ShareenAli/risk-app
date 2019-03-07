package support;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

public class DisplayFileChooser {
    private JFrame parent;
    private String defaultFilePath;
    private JFileChooser chooser;

    /**
     * Initialize the chooser
     * @param defaultFilePath default directory
     */
    public DisplayFileChooser(String defaultFilePath) {
        this.parent = new JFrame();
        this.defaultFilePath = defaultFilePath;

        this.initializeChooser();
    }

    /**
     * Get the chooser ready
     */
    private void initializeChooser() {
        this.chooser = new JFileChooser();
        this.chooser.setCurrentDirectory(new File(defaultFilePath));
    }

    /**
     * Update the extension
     * @param extension new extension
     */
    public void updateExtension(String extension) {
        this.parent.setTitle("Choose " + extension + " file");

        FileNameExtensionFilter extensionFilter = new FileNameExtensionFilter(extension + " files", extension);
        this.chooser.setFileFilter(extensionFilter);
    }

    /**
     * Show open dialog
     * @return file that is opened
     */
    public File openFile() {
        int fileAction = this.chooser.showOpenDialog(this.parent);

        this.parent.dispose();

        return (fileAction == JFileChooser.APPROVE_OPTION) ? this.chooser.getSelectedFile() : null;
    }

    /**
     * Show save dialog
     * @return file that is saved
     */
    public String saveFile() {
        int fileAction = this.chooser.showSaveDialog(this.parent);
        this.parent.dispose();

        return (fileAction == JFileChooser.APPROVE_OPTION) ? this.chooser.getSelectedFile().getAbsolutePath() : null;
    }
}
