/**
 * 
 */
package de.cm.repositoryanalyzer.gui.action;

import de.cm.repositoryanalyzer.analyze.DataAnalysis;
import de.cm.repositoryanalyzer.gui.EvaluationComponent;
import de.cm.repositoryanalyzer.gui.TreeMap.Node;
import de.cm.repositoryanalyzer.gui.TreeMap.TreeMapVisualizer;
import de.cm.repositoryanalyzer.util.DataStore;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jvnet.flamingo.common.JCommandButton;

/**
 * @author Christian
 * 
 */
public class RunEvaluationMouseListener implements MouseListener {

    private EvaluationComponent evaluationComponent;
    private int analysis;

    private String oriText;

    /**
     * 
     * @param evaluationComponent the evaluationComponent
     * @param analysis the analysis
     */
    public RunEvaluationMouseListener(final EvaluationComponent evaluationComponent, final int analysis) {
        this.evaluationComponent = evaluationComponent;
        this.analysis = analysis;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseClicked(final MouseEvent e) {

        if (e.getSource() instanceof JCommandButton && ((JCommandButton) e.getSource()).isEnabled()) {

            DataAnalysis dataAnalysis = DataStore.dataStore.getDataAnalysis();

            if (dataAnalysis != null) {
                Object result = dataAnalysis.getResult(analysis);
                if (result instanceof Node) {
                    Node root = (Node) result;
                    if (evaluationComponent.isShowAllSelected()) {
                        Node.setShowAll(true);
                    } else {
                        Node.setShowAll(false);
                    }
                    Node.setMinValue(evaluationComponent.getMinValue());
                    TreeMapVisualizer treeMapVisualizer = new TreeMapVisualizer(root);
                    evaluationComponent.setContentPane(treeMapVisualizer.getView());
                    evaluationComponent.setEvaluationInfoLabelText(evaluationComponent.getInfoLabelTextTreeMap());
                    evaluationComponent.setEvaluationConfigView(treeMapVisualizer.getConfigView());
                } else if (result instanceof String) {
                    String strResult = (String) result;
                    JLabel label = new JLabel(strResult);
                    JPanel panel = new JPanel();
                    panel.add(label);
                    evaluationComponent.setContentPane(panel);
                    evaluationComponent.setEvaluationInfoLabelText(evaluationComponent.getInfoLabelTextPanel());
                    evaluationComponent.getMainWindow().hideAllContextualTasks();
                }
                evaluationComponent.getMainWindow().setVisible(true);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseEntered(final MouseEvent arg0) {
        oriText = evaluationComponent.getEvaluationInfoLabelText();
        if (analysis == DataAnalysis.BUGRELATEDCOUNT) {
            evaluationComponent.setEvaluationInfoLabelText(evaluationComponent.getInfoLabelTextCount());
        }
        if (analysis == DataAnalysis.BUGRELATEDDURATION) {
            evaluationComponent.setEvaluationInfoLabelText(evaluationComponent.getInfoLabelTextDuration());
        }
        if (analysis == DataAnalysis.STATISTICS) {
            evaluationComponent.setEvaluationInfoLabelText(evaluationComponent.getInfoLabelTextStats());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseExited(final MouseEvent arg0) {
        if (evaluationComponent.getEvaluationInfoLabelText().equals(evaluationComponent.getInfoLabelTextCount())
                || evaluationComponent.getEvaluationInfoLabelText().equals(evaluationComponent.getInfoLabelTextDuration())
                || evaluationComponent.getEvaluationInfoLabelText().equals(evaluationComponent.getInfoLabelTextStats())) {
            evaluationComponent.setEvaluationInfoLabelText(oriText);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    @Override
    public void mousePressed(final MouseEvent arg0) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseReleased(final MouseEvent arg0) {
    }

}
