/**
 * 
 */
package de.cm.repositoryanalyzer.gui.action;

import de.cm.repositoryanalyzer.dataImport.DataImport;
import de.cm.repositoryanalyzer.gui.ImportComponent;
import de.cm.repositoryanalyzer.gui.MainWindow;
import de.cm.repositoryanalyzer.gui.ProgressPanel;
import de.cm.repositoryanalyzer.util.DataStore;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import org.jvnet.flamingo.common.JCommandButton;

/**
 * @author Christian
 * 
 */
public class RunImportMouseListener implements MouseListener {

    private ImportComponent importComponent;

    /**
     * 
     * @param importComponent the importComponent
     */
    public RunImportMouseListener(final ImportComponent importComponent) {
        this.importComponent = importComponent;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseClicked(final MouseEvent e) {

        if (e.getSource() instanceof JCommandButton && ((JCommandButton) e.getSource()).isEnabled()) {
            MainWindow mainWindow = importComponent.getMainWindow();
            importComponent
                    .setInfoLabelText("<html><strong>Importiere...</strong><br>Bitte warten Sie bis der Import abgeschlossen ist.<br> Dieser Vorgang kann mehrere Stunden dauern.</html>");
            DataImport dataImport = new DataImport(DataStore.dataStore.getProject(importComponent.getImportProject()));
            ProgressPanel panel = new ProgressPanel(5);
            dataImport.addObserver(panel);
            dataImport.addObserver(mainWindow);
            mainWindow.setContentPane(panel.getPanel());

            // runs the import without blocking the Event Dispatch Thread
            Thread thread = new Thread(dataImport);
            thread.start();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseEntered(final MouseEvent e) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseExited(final MouseEvent e) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    @Override
    public void mousePressed(final MouseEvent e) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseReleased(final MouseEvent e) {

    }

}
