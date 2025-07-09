/**
 * 
 */
package de.cm.repositoryanalyzer.gui.action;

import de.cm.repositoryanalyzer.analyze.DataAnalysis;
import de.cm.repositoryanalyzer.gui.AnalysisComponent;
import de.cm.repositoryanalyzer.gui.ProgressPanel;
import de.cm.repositoryanalyzer.util.DataStore;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import org.jvnet.flamingo.common.JCommandButton;

/**
 * @author Christian
 * 
 */
public class RunAnalysisMouseListener implements MouseListener {

    private AnalysisComponent analysisComponent;

    /**
     * 
     * @param analysisComponent the analysisComponent
     */
    public RunAnalysisMouseListener(final AnalysisComponent analysisComponent) {
        this.analysisComponent = analysisComponent;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseClicked(final MouseEvent e) {

        if (e.getSource() instanceof JCommandButton && ((JCommandButton) e.getSource()).isEnabled()
                && analysisComponent.isValidDate()) {

            analysisComponent.setProjectDates();
            DataAnalysis dataAnalysis = new DataAnalysis(analysisComponent);
            DataStore.dataStore.addDataAnalysis(dataAnalysis);

            ProgressPanel panel = new ProgressPanel(4);
            dataAnalysis.addObserver(panel);
            dataAnalysis.addObserver(analysisComponent.getMainWindow());
            analysisComponent.getMainWindow().setContentPane(panel.getPanel());

            // runs the analysis without blocking the Event Dispatch Thread
            Thread thread = new Thread(dataAnalysis);
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
