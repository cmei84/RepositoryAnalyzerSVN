/**
 * 
 */
package de.cm.repositoryanalyzer.gui;

import de.cm.repositoryanalyzer.dataImport.Project;
import de.cm.repositoryanalyzer.gui.action.MaintenanceChooseProjectItemListener;
import de.cm.repositoryanalyzer.gui.action.RemoveProjectMouseListener;

import info.clearthought.layout.TableLayout;

import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.jvnet.flamingo.common.JCommandButton;
import org.jvnet.flamingo.common.RichTooltip;
import org.jvnet.flamingo.ribbon.JRibbonBand;
import org.jvnet.flamingo.ribbon.RibbonTask;

/**
 * @author Christian
 * 
 */
public class MaintenanceComponent {

    private Types types;
    private MainWindow mainWindow;

    private RibbonTask task;

    // chooseProject Band
    private JRibbonBand selectProjectBand;
    private JPanel selectProjectPanel;

    private JComboBox<String> selectProjectComboBox;

    // Maintenance Band
    private JRibbonBand commandButtonsBand;
    private JPanel commandButtonsPanel;

    private JCommandButton deleteProjectCommandButton;
    private RichTooltip deleteProjectCommandButtonToolTip;

    // info Band
    private JRibbonBand infoBand;
    private JPanel infoPanel;
    private JLabel infoLabel;
    private ImageIcon infoIcon;
    private JLabel infoIconLabel;

    // Strings
    private final String infoLabelText = "<html>Um ein Projekt zu löschen bitte das Projekt in der Liste Auswählen,<br>auf 'Löschen' klicken und Bestätigen.</html>";

    // Strings Tooltips
    private final String selectProjectBandToolTipText = "<html><strong>Projektauswahl:</strong><br>Hier bitte das Projekt auswählen,<br>das bearbeitet werden soll.</html>";
    private final String maintenanceBandToolTipText = "<html><strong>Wartung:</strong><br>Hier kann ein Projekt entfernt werden.</html>";

    // Icons
    private final String deleteProjectIcon = "icons/deleteProject.64.png";

    /**
     * @param mainWindow the mainWindow
     */
    public MaintenanceComponent(final MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        this.types = mainWindow.getTypes();

        // select Project Band
        selectProjectBand = new JRibbonBand("Projekt", types.getIcon(""));
        selectProjectPanel = new JPanel();
        selectProjectComboBox = new JComboBox<String>();
        selectProjectComboBox.addItemListener(new MaintenanceChooseProjectItemListener(this));
        selectProjectPanel.setLayout(new TableLayout(types.getSize()));
        selectProjectPanel.add(selectProjectComboBox, "1,1,3,1");
        selectProjectBand.addPanel(selectProjectPanel);
        selectProjectBand.setPreferredSize(types.getDimension());
        selectProjectBand.setToolTipText(selectProjectBandToolTipText);

        // runEvaluations Band
        commandButtonsBand = new JRibbonBand("Wartung", types.getIcon(""));
        commandButtonsPanel = new JPanel();
        deleteProjectCommandButton = new JCommandButton("", types.getIcon(deleteProjectIcon));
        deleteProjectCommandButton.setEnabled(false);
        deleteProjectCommandButton.addMouseListener(new RemoveProjectMouseListener(this));
        deleteProjectCommandButtonToolTip = new RichTooltip();
        deleteProjectCommandButtonToolTip.setTitle("Lï¿½schen");
        deleteProjectCommandButtonToolTip.addDescriptionSection(" ");
        deleteProjectCommandButton.setActionRichTooltip(deleteProjectCommandButtonToolTip);
        commandButtonsPanel.add(deleteProjectCommandButton);
        commandButtonsBand.addPanel(commandButtonsPanel);
        commandButtonsBand.setToolTipText(maintenanceBandToolTipText);

        // info Band
        infoBand = new JRibbonBand("Info", types.getIcon(""));
        infoPanel = new JPanel();
        infoPanel.setLayout(new TableLayout(types.getInfoSize()));
        infoLabel = new JLabel(infoLabelText);
        infoIcon = new ImageIcon(types.getImage(types.getInfoIconPath()));
        infoIconLabel = new JLabel(infoIcon);
        infoPanel.add(infoIconLabel, "1,1");
        infoPanel.add(infoLabel, "1,2,2,3");
        infoBand.addPanel(infoPanel);
        infoBand.setPreferredSize(types.getDimension());

        // create task and add to ribbon
        task = new RibbonTask("Wartung", selectProjectBand, commandButtonsBand, infoBand);
    }

    /**
     * Fill the analysisChooserEvaluationComboBox.
     * 
     * @param projects all projects
     */
    public void addProjects(final List<Project> projects) {

        int pos = 0;

        selectProjectComboBox.removeAllItems();
        selectProjectComboBox.insertItemAt(" ", pos++);

        for (Project project : projects) {
            selectProjectComboBox.insertItemAt(project.getName(), pos++);
        }

        if (selectProjectComboBox.getItemCount() > 0) {
            selectProjectComboBox.setSelectedIndex(0);
        }
    }

    /**
     * Checkt if the needed Input Data is given to perform an Import.
     * 
     */
    public void validateMaintenance() {
        if (selectProjectComboBox.getItemCount() > 0 && !selectProjectComboBox.getSelectedItem().equals("")) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    deleteProjectCommandButton.setEnabled(true);
                }
            });
        } else {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    deleteProjectCommandButton.setEnabled(false);
                }
            });
        }
    }

    /**
     * 
     * @return the importTask
     */
    public RibbonTask getTask() {
        return task;
    }

    /**
     * 
     * @return the mainWindow
     */
    public MainWindow getMainWindow() {
        return mainWindow;
    }

    /**
     * @return the selected project
     */
    public String getSelectedProject() {
        return (String) selectProjectComboBox.getSelectedItem();
    }
}
