/**
 * 
 */
package de.cm.repositoryanalyzer.gui.TreeMap;

import javax.swing.JPanel;


import net.bouthier.treemapSwing.TMView;
import net.bouthier.treemapSwing.TreeMap;

/**
 * @author Christian
 * 
 */
public class TreeMapVisualizer {

    private final int borderSize = 11;
    private final int factor = 1;
    private final int intensity = 125;
    private final double height = 0.4;

    private JPanel view;
    private JPanel configView;

    /**
     * 
     * @param rootNode the root node
     */
    public TreeMapVisualizer(final Node rootNode) {
        generateView(rootNode);
    }

    /**
     * 
     * @param rootNode the root node
     */
    public void generateView(final Node rootNode) {

        TreeMap treeMap = new TreeMap(rootNode);

        /* get the views */
        Size aSize = new Size();
        Draw aDraw = new Draw();
        TMView view = treeMap.getView(aSize, aDraw);

        /* configure the views */
        view.setAlgorithm(TMView.SQUARIFIED);
        view.DrawTitles(true);
        view.getAlgorithm().setCushion(false);
        view.getAlgorithm().setBorderOnCushion(false);
        view.getAlgorithm().setH(height);
        view.getAlgorithm().setF(factor);
        view.getAlgorithm().setIS(intensity);
        view.getAlgorithm().setBorderSize(borderSize);

        Action action = new Action(view);
        view.addMouseListener(action);

        configView = view.getAlgorithm().getConfiguringView();
        // config window
        // javax.swing.JFrame f = new javax.swing.JFrame("conf");
        // f.setContentPane(view.getAlgorithm().getConfiguringView());
        // f.pack();
        // f.setVisible(true);

        this.view = view;
    }

    /**
     * 
     * @return the view
     */
    public JPanel getView() {
        return this.view;
    }

    /**
     * 
     * @return the configView
     */
    public JPanel getConfigView() {
        return this.configView;
    }
}
