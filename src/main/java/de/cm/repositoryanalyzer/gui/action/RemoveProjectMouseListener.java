/**
 * 
 */
package de.cm.repositoryanalyzer.gui.action;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JOptionPane;

import org.jvnet.flamingo.common.JCommandButton;

import de.cm.repositoryanalyzer.db.SQLiteDB;
import de.cm.repositoryanalyzer.gui.MaintenanceComponent;

/**
 * @author Christian
 * 
 */
public class RemoveProjectMouseListener implements MouseListener {

    private MaintenanceComponent maintenanceComponent;

    /**
     * @param maintenanceComponent the maintenanceComponent
     * 
     */
    public RemoveProjectMouseListener(final MaintenanceComponent maintenanceComponent) {
        this.maintenanceComponent = maintenanceComponent;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseClicked(final MouseEvent e) {

        String project = maintenanceComponent.getSelectedProject();

        if (e.getSource() instanceof JCommandButton && ((JCommandButton) e.getSource()).isEnabled()) {
            int result = JOptionPane.showConfirmDialog(null, "<html>Wollen Sie wirklich das Projekt '" + project
                    + "' lï¿½schen?<br>Es gibt keine weitere Warnung und dieser Vorgang kann nicht Rï¿½ckgï¿½ngig gemacht werden.</html>", "Projekt lï¿½schen", JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            switch (result) {
                case 0 : {
                    // Yes
                    SQLiteDB.db.deleteProject(project);
                    maintenanceComponent.getMainWindow().refreshProjects();
                    break;
                }
                case 1 : {
                    // No
                    break;
                }
                case -1 : {
                    // close window (X)
                    break;
                }
                default :
                    break;
            }
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
