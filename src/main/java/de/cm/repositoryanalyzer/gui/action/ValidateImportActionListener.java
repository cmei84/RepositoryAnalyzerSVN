/**
 * 
 */
package de.cm.repositoryanalyzer.gui.action;

import de.cm.repositoryanalyzer.gui.ImportComponent;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Christian
 * 
 */
public class ValidateImportActionListener implements ActionListener {

    private ImportComponent importComponents;

    /**
     * @param mainWindow the mainWindow
     */
    public ValidateImportActionListener(final ImportComponent importComponents) {
        this.importComponents = importComponents;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(final ActionEvent e) {
        importComponents.validateImportInputData();
    }
}
