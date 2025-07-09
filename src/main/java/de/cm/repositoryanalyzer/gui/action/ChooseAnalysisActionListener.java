/**
 * 
 */
package de.cm.repositoryanalyzer.gui.action;

import de.cm.repositoryanalyzer.gui.EvaluationComponent;
import de.cm.repositoryanalyzer.util.DataStore;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

/**
 * @author Christian
 * 
 */
public class ChooseAnalysisActionListener implements ActionListener {

    private EvaluationComponent evaluationComponent;

    /**
     * 
     * @param evaluationComponent the evaluationComponent
     */
    public ChooseAnalysisActionListener(final EvaluationComponent evaluationComponent) {
        this.evaluationComponent = evaluationComponent;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(final ActionEvent e) {
        if (e.getSource() instanceof JComboBox) {
            @SuppressWarnings("unchecked")
            JComboBox<String> comboBox = (JComboBox<String>) e.getSource();
            if (comboBox.getSelectedItem() instanceof String) {
                String projectName = (String) comboBox.getSelectedItem();
                if (projectName.length() > 4) {
                    DataStore.dataStore.setActualProject(projectName);
                }
                evaluationComponent.validateAnalysis();
            }
        }
    }
}
