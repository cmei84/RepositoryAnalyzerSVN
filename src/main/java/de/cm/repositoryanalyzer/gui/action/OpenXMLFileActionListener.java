/**
 * 
 */
package de.cm.repositoryanalyzer.gui.action;

import de.cm.repositoryanalyzer.gui.ImportComponent;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;

/**
 * @author Christian
 * 
 */
public class OpenXMLFileActionListener implements ActionListener {

    private ImportComponent importComponent;

    /**
     * 
     * @param importComponent the import component
     */
    public OpenXMLFileActionListener(final ImportComponent importComponent) {
        this.importComponent = importComponent;
    }

    /**
     * @param e the Event
     */
    public void actionPerformed(final ActionEvent e) {
        JFileChooser chooser = new JFileChooser();

        int returnVal = chooser.showOpenDialog(null);

        String path;
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            path = chooser.getSelectedFile().getPath();
            if (path.toLowerCase().contains(".xml")) {
                importComponent.setBugUrlText(path);
            }
        }
    }
}
