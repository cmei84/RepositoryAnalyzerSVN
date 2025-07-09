/**
 * 
 */
package de.cm.repositoryanalyzer.gui.action;

import de.cm.repositoryanalyzer.gui.ImportComponent;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * @author Christian
 * 
 */
public class ValidateImportInputData implements KeyListener {

    private ImportComponent importComponent;

    /**
     * 
     * @param mainWindow the mainWindow
     */
    public ValidateImportInputData(final ImportComponent importComponent) {
        this.importComponent = importComponent;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
     */
    @Override
    public void keyPressed(KeyEvent e) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
     */
    @Override
    public void keyReleased(KeyEvent e) {
        importComponent.validateImportInputData();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
     */
    @Override
    public void keyTyped(KeyEvent e) {

    }
}
