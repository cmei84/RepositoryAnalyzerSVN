/**
 * 
 */
package de.cm.repositoryanalyzer.gui;

import com.toedter.calendar.JDateChooser;

import de.cm.repositoryanalyzer.dataImport.Project;
import de.cm.repositoryanalyzer.gui.action.ChooseProjectActionListener;
import de.cm.repositoryanalyzer.gui.action.RunAnalysisMouseListener;
import de.cm.repositoryanalyzer.util.DataStore;

import info.clearthought.layout.TableLayout;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
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
public class AnalysisComponent {

    private Types types;
    private MainWindow mainWindow;

    private RibbonTask task;

    // selectProject Band
    private JRibbonBand selectProjectBand;
    private JPanel selectProjectPanel;

    private JComboBox<String> selectProjectComboBox;
    private JDateChooser selectProjectStartDate;
    private JDateChooser selectProjectEndDate;

    // selectOptions Band
    private JRibbonBand selectOptionsBand;
    private JPanel selectOptionsPanel;
    private JCheckBox bugRelatedCountCheckBox;
    private JCheckBox bugRelatedDurationCheckBox;
    private JCheckBox statisticsCheckBox;

    // runAnaysis Band
    private JRibbonBand runAnalysisBand;
    private JPanel runAnalysisPanel;

    private JCommandButton runAnalysisCommandButton;
    private RichTooltip runAnalysisCommandButtonTooltip;

    // info Band
    private JRibbonBand infoBand;
    private JPanel infoPanel;
    private JLabel infoIconLabel;
    private JLabel infoLabel;
    private ImageIcon infoIcon;

    // Strings
    private final String selectProjectBandToolTipText = "<html><strong>Projektauswahl:</strong><br>Hier bitte das Projekt auswÃ¤hlen,<br>dass analysiert werden soll.</html>";
    private final String selectOptionsBandToolTipText = "<html><strong>Optionen:</strong><br>Hier bitte auswÃ¤hlen,<br>was alles analysiert werden soll.</html>";
    private final String runAnalysisBandToolTipText = "<html><strong>Import:</strong><br>Hier kann die Analyse gestartet werden.</html>";
    private final String infoBandToolTipText = "<html><strong>Info:</strong><br>Hier erhalten Sie alle nÃ¶tigen Informationen,<br>" + "um den Analysevorgang auszufÃ¼hren.</html>";
    private final String infoLabelMsg = "<html>Bitte wÃ¤hlen Sie ein Projekt aus der Liste, den Datumsbereich<br>und klicken Sie auf den Button 'Analyse Starten'.<br>"
            + "Tastatureingaben in den Datumsfeldern bitte mit Enter anschlieÃŸen.<br>" + "Sie haben auch die MÃ¶glichkeit einzelne Analyseaufgaben abzuwÃ¤hlen.</html>";

    // Icon
    private final String runAnalysisIcon = "icons/runAnalysis.64.png";

    /**
     * 
     * @param mainWindow the mainWindow
     */
    public AnalysisComponent(final MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        this.types = mainWindow.getTypes();

        // Project
        selectProjectBand = new JRibbonBand("Projekt", types.getIcon(""));
        selectProjectPanel = new JPanel();
        selectProjectComboBox = new JComboBox<String>();
        selectProjectComboBox.addActionListener(new ChooseProjectActionListener(this));
        selectProjectStartDate = new JDateChooser();
        selectProjectStartDate.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(final PropertyChangeEvent evt) {
                validateAnalysis();
            }
        });

        selectProjectEndDate = new JDateChooser();
        selectProjectEndDate.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(final PropertyChangeEvent evt) {
                validateAnalysis();
            }
        });

        selectProjectPanel.setLayout(new TableLayout(types.getSize()));
        selectProjectPanel.add(selectProjectComboBox, "1,1,3,1");
        selectProjectPanel.add(selectProjectStartDate, "1,3");
        selectProjectPanel.add(selectProjectEndDate, "3,3");
        selectProjectBand.addPanel(selectProjectPanel);
        selectProjectBand.setPreferredSize(types.getDimension());
        selectProjectBand.setToolTipText(selectProjectBandToolTipText);

        // select analysis options
        selectOptionsBand = new JRibbonBand("AuswertungsmÃ¶glichkeiten", types.getIcon(""));
        selectOptionsPanel = new JPanel();
        bugRelatedCountCheckBox = new JCheckBox("Ã„nderungshÃ¤ufigkeit");
        bugRelatedCountCheckBox.setSelected(true);
        bugRelatedDurationCheckBox = new JCheckBox("Fehlerbehebungsdauer");
        bugRelatedDurationCheckBox.setSelected(true);
        statisticsCheckBox = new JCheckBox("Statistiken");
        statisticsCheckBox.setSelected(true);

        selectOptionsPanel.setLayout(new TableLayout(types.getSize()));
        selectOptionsPanel.add(bugRelatedCountCheckBox, "1,1,3,1");
        selectOptionsPanel.add(bugRelatedDurationCheckBox, "1,3,3,3");
        selectOptionsPanel.add(statisticsCheckBox, "1,5,3,5");
        selectOptionsBand.addPanel(selectOptionsPanel);
        selectOptionsBand.setPreferredSize(types.getDimension());
        selectOptionsBand.setToolTipText(selectOptionsBandToolTipText);

        // run analyses
        runAnalysisBand = new JRibbonBand("Analyse", types.getIcon(""));
        runAnalysisPanel = new JPanel();
        runAnalysisCommandButton = new JCommandButton("", types.getIcon(runAnalysisIcon));
        runAnalysisCommandButton.addMouseListener(new RunAnalysisMouseListener(this));
        runAnalysisCommandButtonTooltip = new RichTooltip();
        runAnalysisCommandButtonTooltip.setTitle("Analyse starten");
        runAnalysisCommandButtonTooltip.addDescriptionSection("Startet die Analyse des gewÃ¤hlten Projekts.");
        runAnalysisCommandButton.setActionRichTooltip(runAnalysisCommandButtonTooltip);
        runAnalysisCommandButton.setEnabled(false);
        runAnalysisPanel.add(runAnalysisCommandButton);
        runAnalysisBand.addPanel(runAnalysisPanel);
        runAnalysisBand.setToolTipText(runAnalysisBandToolTipText);

        // info band
        infoBand = new JRibbonBand("Info", types.getIcon(""));
        infoPanel = new JPanel();
        infoPanel.setLayout(new TableLayout(types.getInfoSize()));
        infoLabel = new JLabel(infoLabelMsg);
        infoIcon = new ImageIcon(types.getImage(types.getInfoIconPath()));
        infoIconLabel = new JLabel(infoIcon);
        infoIconLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        infoPanel.add(infoLabel, "1,2,2,3");
        infoPanel.add(infoIconLabel, "1,1");

        infoBand.addPanel(infoPanel);
        infoBand.setToolTipText(infoBandToolTipText);

        // create task and add to ribbon
        task = new RibbonTask("Analyse", selectProjectBand, selectOptionsBand, runAnalysisBand, infoBand);

    }

    /**
     * Checks if the needed Input Data is given to perform an Import.
     * 
     */
    public void validateAnalysis() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                String projectName = (String) selectProjectComboBox.getSelectedItem();
                if (projectName != null && projectName.length() > 4 && isValidDate()) {
                    runAnalysisCommandButton.setEnabled(true);
                } else {
                    runAnalysisCommandButton.setEnabled(false);
                }
            }
        });
    }

    /**
     * 
     * @return is the Date is valid
     */
    public boolean isValidDate() {
        boolean validDate = true;

        Date min = selectProjectStartDate.getMinSelectableDate();
        Date max = selectProjectStartDate.getMaxSelectableDate();
        Date start = selectProjectStartDate.getDate();
        Date end = selectProjectEndDate.getDate();

        if (start == null || end == null) {
            validDate = false;
        } else {
            if (start.getTime() < min.getTime() || start.getTime() > max.getTime()) {
                validDate = false;
            }
            if (end.getTime() < min.getTime() || end.getTime() > max.getTime()) {
                validDate = false;
            }
            if (start.getTime() > end.getTime()) {
                validDate = false;
            }
        }
        return validDate;
    }

    /**
     * @return the selected analysis types
     */
    public String getAnalysisTypes() {
        String result = "";
        if (bugRelatedCountCheckBox.isSelected()) {
            result += "1";
        } else {
            result += "0";
        }
        if (bugRelatedDurationCheckBox.isSelected()) {
            result += "1";
        } else {
            result += "0";
        }
        if (statisticsCheckBox.isSelected()) {
            result += "1";
        } else {
            result += "0";
        }
        return result;
    }

    /**
     * 
     * @param projectName the projectName
     */
    public void setActualProject(final String projectName) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (projectName.length() < 4) {
                    runAnalysisCommandButton.setEnabled(false);
                    selectProjectStartDate.setDate(null);
                    selectProjectEndDate.setDate(null);
                } else {
                    runAnalysisCommandButton.setEnabled(true);
                    selectProjectStartDate.setDate(GregorianCalendar.getInstance().getTime());
                    Project project = DataStore.dataStore.getActualProject();

                    long min = project.getProjectMin();
                    long max = project.getProjectMax();

                    Calendar cal = GregorianCalendar.getInstance();
                    cal.setTimeInMillis(min);
                    cal.set(Calendar.HOUR, 0);
                    cal.set(Calendar.MINUTE, 0);
                    cal.set(Calendar.SECOND, 0);
                    cal.set(Calendar.MILLISECOND, 0);
                    min = cal.getTimeInMillis();

                    cal.setTimeInMillis(max);
                    cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + 1);
                    max = cal.getTimeInMillis();

                    selectProjectStartDate.setDate(new Date(min));
                    selectProjectStartDate.setMinSelectableDate(new Date(min));
                    selectProjectStartDate.setMaxSelectableDate(new Date(max));

                    selectProjectEndDate.setDate(new Date(max));
                    selectProjectEndDate.setMinSelectableDate(new Date(min));
                    selectProjectEndDate.setMaxSelectableDate(new Date(max));
                }
            }
        });
    }

    /**
     * @param projects inserts all projects in the analysisSelectProjectComboBox
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

        validateAnalysis();
    }

    /**
     * 
     * @return the analysisTask
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
     * 
     */
    public void setProjectDates() {
        DataStore.dataStore.getActualProject().setViewMin(selectProjectStartDate.getDate().getTime());
        DataStore.dataStore.getActualProject().setViewMax(selectProjectEndDate.getDate().getTime());
    }
}
