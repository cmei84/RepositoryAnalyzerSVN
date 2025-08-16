/**
 * 
 */
package de.cm.repositoryanalyzer.gui;

import de.cm.repositoryanalyzer.dataImport.Project;
import de.cm.repositoryanalyzer.util.DataStore;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.SwingUtilities;

import org.jvnet.flamingo.common.JCommandButton;
import org.jvnet.flamingo.ribbon.JRibbon;
import org.jvnet.flamingo.ribbon.JRibbonFrame;
import org.jvnet.flamingo.ribbon.RibbonApplicationMenu;
import org.jvnet.flamingo.ribbon.RibbonApplicationMenuEntryFooter;
import org.jvnet.flamingo.ribbon.RibbonApplicationMenuEntryPrimary;

/**
 * @author Christian
 * 
 */
public class MainWindow extends JRibbonFrame implements Observer {

    /**
     * 
     */
    private static final long serialVersionUID = 7817800385540069135L;

    /**
     * Global needed variables.
     * 
     */
    private Types types;

    private JRibbon ribbon;
    private RibbonApplicationMenu applicationMenu;

    private ImportComponent importComponent;
    private AnalysisComponent analysisComponent;
    private EvaluationComponent evaluationComponent;
    private MaintenanceComponent maintenanceComponent;

    /**
     * Constuctor.
     * 
     * @param title the title of the mainWindow
     */
    public MainWindow(final String title) {
        super(title);

        ribbon = this.getRibbon();
        applicationMenu = new RibbonApplicationMenu();

        types = new Types();

        importComponent = new ImportComponent(this);
        ribbon.addTask(importComponent.getTask());

        analysisComponent = new AnalysisComponent(this);
        ribbon.addTask(analysisComponent.getTask());

        evaluationComponent = new EvaluationComponent(this);
        ribbon.addTask(evaluationComponent.getTask());

        maintenanceComponent = new MaintenanceComponent(this);
        ribbon.addTask(maintenanceComponent.getTask());

        try {
            new HideContextualTaskGroup(this, ribbon).start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        /**
         * Application Menu
         */

        applicationMenu.addFooterEntry(new RibbonApplicationMenuEntryFooter(types.getIcon("icons/close.32.png"), "Beenden", new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                System.exit(0);
            }
        }));

        ribbon.setApplicationMenu(applicationMenu);

        loadProjects();
    }

    /**
     * Loads all the projects from the DataStore.
     */
    public void loadProjects() {
        if (DataStore.dataStore != null) {
            List<Project> projects = DataStore.dataStore.getProjects();
            if (projects != null && projects.size() > 0) {
                analysisComponent.addProjects(DataStore.dataStore.getProjects());
                maintenanceComponent.addProjects(DataStore.dataStore.getProjects());
            }
        }
        loadAnalysis();
        // evaluationComponent.validateAnalysis();
    }

    /**
     * 
     */
    public void refreshProjects() {
        DataStore.dataStore.refreshProjects();
        loadProjects();
    }

    /**
     * Loads the DataAnalysis from the actual project.
     */
    public void loadAnalysis() {
        evaluationComponent.addProjects(DataStore.dataStore.getProjects());
    }

    /**
     * 
     * @param contentPane the contentPane to add
     */
    public void setContentPane(final Container contentPane) {

        for (int i = 0; i < this.getContentPane().getComponentCount(); i++) {
            if (!(this.getContentPane().getComponent(i) instanceof JRibbon)) {
                this.getContentPane().remove(i);
                i--;
            }
        }
        if (contentPane != null) {
            this.getContentPane().add(contentPane);
        }

        this.repaint();
    }

    /**
     */
    public void hideAllContextualTasks() {
        for (int i = 0; i < ribbon.getContextualTaskGroupCount(); i++) {
            ribbon.setVisible(ribbon.getContextualTaskGroup(i), false);
        }
    }

    /**
     * 
     * @return the types
     */
    public Types getTypes() {
        return types;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    public void update(final Observable o, final Object message) {
        if (message instanceof String) {
            String messageString = (String) message;
            if (messageString.equals("import finished")) {
                DataStore.dataStore.refreshProjects();
                loadProjects();
                this.setContentPane(null);
                this.importComponent.setInfoLabelText("<html>Import erfolgreich.<br>Nun können Sie die importierten Daten analysieren.</html>");
            }
            if (messageString.equals("analysis finished")) {
                this.loadAnalysis();
                this.setContentPane(null);
            }
        }
    }

    /**
     * 
     * @author Christian
     * 
     */
    public class HideContextualTaskGroup extends Thread {

        private MainWindow mainWindow;
        private JRibbon ribbon;
        private String lastSelected;

        /**
         * 
         * @param mainWindow the mainWindow
         * @param ribbon the ribbon
         */
        public HideContextualTaskGroup(final MainWindow mainWindow, final JRibbon ribbon) {
            this.mainWindow = mainWindow;
            this.ribbon = ribbon;
        }

        /**
         * 
         */
        public void run() {
            lastSelected = ribbon.getSelectedTask().getTitle();
            while (true) {
                if ((lastSelected.equals("Auswertung") && !ribbon.getSelectedTask().getTitle().equals("Auswertung") && !ribbon.getSelectedTask().getTitle()
                        .equals("Visualisierung anpassen"))
                        || (lastSelected.equals("Visualisierung anpassen") && !ribbon.getSelectedTask().getTitle().equals("Auswertung") && !ribbon.getSelectedTask().getTitle()
                                .equals("Visualisierung anpassen"))) {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            mainWindow.setContentPane(null);
                            mainWindow.hideAllContextualTasks();
                            mainWindow.setVisible(true);
                        }
                    });
                }
                if (!lastSelected.equals(ribbon.getSelectedTask().getTitle())) {
                    lastSelected = ribbon.getSelectedTask().getTitle();
                }
                try {
                    sleep(150);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 
     * @param cmdButton the JCommandButton to add
     */
    public void addTaskBarButton(final JCommandButton cmdButton) {
        ribbon.addTaskbarComponent(cmdButton);
    }

    /**
     * 
     * @param entry the RibbonApplicationMenuEntryPrimary to add
     */
    public void addApplicationMenuEntry(final RibbonApplicationMenuEntryPrimary entry) {
        applicationMenu.addMenuEntry(entry);
    }
}
