/**
 * 
 */
package de.cm.repositoryanalyzer.gui.action;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JTextField;

/**
 * @author Christian
 * 
 */
public class EmptyTextfieldMouseListener implements MouseListener {

    private JTextField textField;
    private String defaultText;

    public EmptyTextfieldMouseListener(JTextField textField) {
        this.textField = textField;
        this.defaultText = textField.getText();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseClicked(MouseEvent arg0) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseEntered(MouseEvent arg0) {
        if (textField.isEnabled() && textField.getText().equals(defaultText)) {
            textField.setText("");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseExited(MouseEvent arg0) {
        if (textField.getText().equals("")) {
            textField.setText(defaultText);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    @Override
    public void mousePressed(MouseEvent arg0) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseReleased(MouseEvent arg0) {
    }

}
