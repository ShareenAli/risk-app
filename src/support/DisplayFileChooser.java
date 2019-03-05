package support;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

public class DisplayFileChooser {
    private JFrame parent;
    private String defaultFilePath;
    private JFileChooser chooser;

    public DisplayFileChooser(String defaultFilePath) {
        this.parent = new JFrame();
        this.defaultFilePath = defaultFilePath;

        this.initializeChooser();
    }

    private void initializeChooser() {
        this.chooser = new JFileChooser();
        this.chooser.setCurrentDirectory(new File(defaultFilePath));
    }

    public void updateExtension(String extension) {
        this.parent.setTitle("Choose " + extension + " file");

        FileNameExtensionFilter extensionFilter = new FileNameExtensionFilter(extension + " files", extension);
        this.chooser.setFileFilter(extensionFilter);
    }

    public File openFile() {
        int fileAction = this.chooser.showOpenDialog(this.parent);

        this.parent.dispose();

        return (fileAction == JFileChooser.APPROVE_OPTION) ? this.chooser.getSelectedFile() : null;
    }

    public String saveFile() {
        int fileAction = this.chooser.showSaveDialog(this.parent);
        this.parent.dispose();

        return (fileAction == JFileChooser.APPROVE_OPTION) ? this.chooser.getSelectedFile().getAbsolutePath() : null;
    }
}
