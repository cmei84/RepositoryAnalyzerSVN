
package de.cm.repositoryanalyzer.gui.TreeMap;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import net.bouthier.treemapSwing.TMView;

/**
 * 
 * @author Christian
 *
 */
public class Action extends MouseAdapter {
    private TMView theView = null;

    public Action(TMView view) {
        this.theView = view;
    }

    /**
     */
    public void mouseClicked(final MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            theView.zoom(theView, e);
        }

        if (e.getButton() == MouseEvent.BUTTON3) {
            theView.unzoom();
        }
    }
}
