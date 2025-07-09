/**
 * 
 */
package de.cm.repositoryanalyzer.gui.action;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;

import de.cm.repositoryanalyzer.gui.MaintenanceComponent;

/**
 * @author Christian
 * 
 */
public class MaintenanceChooseProjectItemListener implements ItemListener {

    private MaintenanceComponent maintenanceComponent;

    /**
     * @param maintenanceComponent the maintenanceComponent
     */
    public MaintenanceChooseProjectItemListener(final MaintenanceComponent maintenanceComponent) {
        this.maintenanceComponent = maintenanceComponent;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
     */
    @SuppressWarnings("unchecked")
    @Override
    public void itemStateChanged(final ItemEvent e) {
        JComboBox<String> comboBox = (JComboBox<String>) e.getSource();
        if (comboBox.getItemCount() > 0) {
            maintenanceComponent.validateMaintenance();
        }
    }
}
