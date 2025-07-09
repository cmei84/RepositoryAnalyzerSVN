/**
 * 
 */
package de.cm.repositoryanalyzer;

import de.cm.repositoryanalyzer.gui.MainWindow;
import de.cm.repositoryanalyzer.util.DataStore;

import java.awt.Frame;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * @author Christian
 * 
 */
public class RepositoryAnalyzer {

    /**
     * 
     */
    public RepositoryAnalyzer() {

    }

    /**
     * @param args the commandline args
     */
    public static void main(final String[] args) {

        // set the Look&Feel
        try {
            // win system ui
            // UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            UIManager.setLookAndFeel("org.jvnet.substance.skin.SubstanceCremeLookAndFeel");
            // UIManager.setLookAndFeel("org.jvnet.substance.skin.SubstanceMistSilverLookAndFeel");
            // UIManager.setLookAndFeel("org.jvnet.substance.skin.SubstanceRavenGraphiteGlassLookAndFeel");
            // UIManager.setLookAndFeel("org.jvnet.substance.skin.SubstanceBusinessBlackSteelLookAndFeel");
            // UIManager.setLookAndFeel("org.jvnet.substance.skin.SubstanceBusinessBlueSteelLookAndFeel");
            // UIManager.setLookAndFeel("org.jvnet.substance.skin.SubstanceModerateLookAndFeel");
            // UIManager.setLookAndFeel("org.jvnet.substance.skin.SubstanceSaharaLookAndFeel");

        } catch (Exception e) {
            e.printStackTrace();
        }

        DataStore.dataStore = new DataStore();

        // open the window correctly
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                final int width = 1000;
                final int height = 562;
                MainWindow window = new MainWindow("Project-Analyzer");
                window.setSize(width, height);
                window.setExtendedState(Frame.MAXIMIZED_BOTH);
                window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                window.setVisible(true);
            }
        });
    }
}
