/**
 * 
 */
package de.cm.repositoryanalyzer.gui.action;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;

/**
 * @author Christian
 * 
 */
public class EnableJTextfieldActionListener implements ActionListener {

    private Component component;

    /**
     * 
     * @param comp component to enable
     */
    public EnableJTextfieldActionListener(Component component) {
        this.component = component;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JCheckBox) {
            JCheckBox source = (JCheckBox) e.getSource();
            if (source.isSelected()) {
                component.setEnabled(false);
            } else {
                component.setEnabled(true);
            }
        }
    }
}
