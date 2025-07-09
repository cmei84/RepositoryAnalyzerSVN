/**
 * 
 */
package de.cm.repositoryanalyzer.gui.action;

import de.cm.repositoryanalyzer.gui.AnalysisComponent;
import de.cm.repositoryanalyzer.util.DataStore;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

/**
 * @author Christian
 * 
 */
public class ChooseProjectActionListener implements ActionListener {

    private AnalysisComponent analysisComponent;

    /**
     * @param analysisComponent the analysisComponent
     * 
     */
    public ChooseProjectActionListener(final AnalysisComponent analysisComponent) {
        this.analysisComponent = analysisComponent;
    }

    @SuppressWarnings("unchecked")
    public void actionPerformed(final ActionEvent e) {
        JComboBox<String> comboBox = (JComboBox<String>) e.getSource();
        if (comboBox.getItemCount() > 0) {
            String projectName = comboBox.getSelectedItem().toString();
            DataStore.dataStore.setActualProject(projectName);
            analysisComponent.setActualProject(projectName);
        }
    }
}
