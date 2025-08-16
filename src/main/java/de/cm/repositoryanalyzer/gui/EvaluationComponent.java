/**
 * 
 */
package de.cm.repositoryanalyzer.gui;

import de.cm.repositoryanalyzer.analyze.DataAnalysis;
import de.cm.repositoryanalyzer.dataImport.Project;
import de.cm.repositoryanalyzer.gui.action.ChooseAnalysisActionListener;
import de.cm.repositoryanalyzer.gui.action.RunEvaluationMouseListener;
import de.cm.repositoryanalyzer.util.DataStore;

import info.clearthought.layout.TableLayout;

import java.awt.Color;
import java.awt.Dimension;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;

import org.jvnet.flamingo.common.JCommandButton;
import org.jvnet.flamingo.common.RichTooltip;
import org.jvnet.flamingo.ribbon.JRibbon;
import org.jvnet.flamingo.ribbon.JRibbonBand;
import org.jvnet.flamingo.ribbon.RibbonContextualTaskGroup;
import org.jvnet.flamingo.ribbon.RibbonTask;

/**
 * @author Christian
 * 
 */
public class EvaluationComponent {

    private Types types;
    private MainWindow mainWindow;

    private RibbonTask task;

    // selectAnalysis Band
    private JRibbonBand selectAnalysisBand;
    private JPanel selectAnalysisPanel;

    private JComboBox<String> selectAnalysisComboBox;

    // selectOptions Band
    private JRibbonBand selectOptionsBand;
    private JPanel selectOptionsPanel;
    private JCheckBox showAllCheckBox;
    private JSlider minValue;

    // runEvaluation Band
    private JRibbonBand runEvaluationBand;
    private JPanel runEvaluationPanel;

    private JCommandButton showBugRelatedCoundCommandButton;
    private JCommandButton showBugRelatedDurationCommandButton;
    private JCommandButton showStatisticsCommandButton;

    // info Band
    private JRibbonBand infoBand;
    private JPanel infoPanel;
    private JLabel infoLabel;
    private ImageIcon infoIcon;
    private JLabel infoIconLabel;

    // Field Strings
    private final String infoLabelTextTreeMap = "<html>Einzoomen können Sie, indem Sie auf einen beliebigen Knoten klicken.<br>Ein Klick mit der rechten Maustaste zoomt auf den Vaterknoten aus.<br>Etwas anpassen können Sie die TreeMap unter 'Visualisierung anpassen'.</html>";
    private final String infoLabelTextPanel = "<html>Hier sehen Sie einige Zahlen,<br> welche während der Analyse des Projektes erhoben wurden.</html>";
    private final String manipulateTreeMapInfoLabelText = "<html>Hier können Sie die angezeigte TreeMap etwas anpassen.<br>Beachten Sie bitte, das wenn Sie Cushion einschalten<br>die Antwortzeit dieser Anwendung deutlich nachlassen kann.</html>";
    private final String infoLabelTextCount = "<html><strong>Änderngshäufigkeit:</strong><br>Zeigt die Änderungshäufigkeit von Dateien an,<br>welche durch Bugfixes aufgetreten sind.</html>";
    private final String infoLabelTextDuration = "<html><strong>Dauer von Bugfixes:</strong><br>Zeigt die Dauer von Bugfixes an.<br>Es werden nur Bugs der Priorität P1 und P2 berücksichtigt.</html>";
    private final String infoLabelTextStats = "<html><strong>Statistiken:</strong><br>Zeigt umfangreiche Informationen bezüglich des analysierten Projekts an.</html>";

    // Tooltip Strings
    private final String selectAnalysisBandToolTipText = "<html><strong>Analyse:</strong><br>Hier bitte die Analyse auswählen,<br>welche ausgewertet werden soll.</html>";
    private final String runEvaluationBandToolTipText = "<html><strong>Auswertung:</strong><br>Hier können die einzelnen Auswertungen angezeigt werden.</html>";
    private final String selectOptionsBandToolTipText = "<html><strong>Optionen:</strong><br>Hier bitte auswählen,<br>ob alle Daten visualisiert werden sollen.</html>";

    // Icons
    private final String runBugRelatedCountIcon = "icons/runBugRelatedCount.64.png";
    private final String runBugRelatedDurationIcon = "icons/runBugRelatedDuration.64.png";
    private final String runStatisticsIcon = "icons/runStatistics.64.png";

    /**
     * 
     * @param mainWindow the mainWindow
     */
    public EvaluationComponent(final MainWindow mainWindow) {

        this.mainWindow = mainWindow;
        this.types = mainWindow.getTypes();

        // select Analysis Band
        selectAnalysisBand = new JRibbonBand("Analyse", types.getIcon(""));
        selectAnalysisPanel = new JPanel();
        selectAnalysisComboBox = new JComboBox<String>();
        selectAnalysisComboBox.addActionListener(new ChooseAnalysisActionListener(this));
        selectAnalysisPanel.setLayout(new TableLayout(types.getSize()));
        selectAnalysisPanel.add(selectAnalysisComboBox, "1,1,3,1");
        selectAnalysisBand.addPanel(selectAnalysisPanel);
        selectAnalysisBand.setPreferredSize(types.getDimension());
        selectAnalysisBand.setToolTipText(selectAnalysisBandToolTipText);

        // selectOptions Band
        selectOptionsBand = new JRibbonBand("Optionen", types.getIcon(""));
        selectOptionsPanel = new JPanel();
        showAllCheckBox = new JCheckBox("auch unbetroffene Dateien zeigen");
        showAllCheckBox.setSelected(false);

        minValue = new JSlider();
        minValue.setMinimum(0);
        minValue.setMaximum(50);
        minValue.setValue(5);

        selectOptionsPanel.setLayout(new TableLayout(types.getSize()));
        selectOptionsPanel.add(showAllCheckBox, "1,1,3,1");
        selectOptionsPanel.add(minValue, "1,2,3,3");
        selectOptionsBand.addPanel(selectOptionsPanel);
        selectOptionsBand.setPreferredSize(types.getDimension());
        selectOptionsBand.setToolTipText(selectOptionsBandToolTipText);

        // runEvaluation Band
        runEvaluationBand = new JRibbonBand("Auswertung", types.getIcon(""));
        runEvaluationPanel = new JPanel();
        showBugRelatedCoundCommandButton = new JCommandButton("", types.getIcon(runBugRelatedCountIcon));
        showBugRelatedCoundCommandButton.addMouseListener(new RunEvaluationMouseListener(this, DataAnalysis.BUGRELATEDCOUNT));
        showBugRelatedCoundCommandButton.setEnabled(false);

        RichTooltip showBugRelatedCoundCommandButtonRichToolTip = new RichTooltip();
        showBugRelatedCoundCommandButtonRichToolTip.setTitle("Änderungshäufigkeit");
        showBugRelatedCoundCommandButtonRichToolTip.addDescriptionSection(" ");
        showBugRelatedCoundCommandButton.setActionRichTooltip(showBugRelatedCoundCommandButtonRichToolTip);

        showBugRelatedDurationCommandButton = new JCommandButton("", types.getIcon(runBugRelatedDurationIcon));
        showBugRelatedDurationCommandButton.addMouseListener(new RunEvaluationMouseListener(this, DataAnalysis.BUGRELATEDDURATION));
        showBugRelatedDurationCommandButton.setEnabled(false);

        RichTooltip showBugRelatedDurationCommandButtonRichToolTip = new RichTooltip();
        showBugRelatedDurationCommandButtonRichToolTip.setTitle("Bugfix dauer");
        showBugRelatedDurationCommandButtonRichToolTip.addDescriptionSection(" ");
        showBugRelatedDurationCommandButton.setActionRichTooltip(showBugRelatedDurationCommandButtonRichToolTip);

        showStatisticsCommandButton = new JCommandButton("", types.getIcon(runStatisticsIcon));
        showStatisticsCommandButton.addMouseListener(new RunEvaluationMouseListener(this, DataAnalysis.STATISTICS));
        showStatisticsCommandButton.setEnabled(false);

        RichTooltip showStatisticsCommandButtonRichToolTip = new RichTooltip();
        showStatisticsCommandButtonRichToolTip.setTitle("Statistiken");
        showStatisticsCommandButtonRichToolTip.addDescriptionSection(" ");
        showStatisticsCommandButton.setActionRichTooltip(showStatisticsCommandButtonRichToolTip);

        runEvaluationPanel.add(showBugRelatedCoundCommandButton);
        runEvaluationPanel.add(showBugRelatedDurationCommandButton);
        runEvaluationPanel.add(showStatisticsCommandButton);
        runEvaluationBand.addPanel(runEvaluationPanel);
        runEvaluationBand.setToolTipText(runEvaluationBandToolTipText);

        // info Band
        infoBand = new JRibbonBand("Info", types.getIcon(""));
        infoPanel = new JPanel();
        infoPanel.setLayout(new TableLayout(types.getInfoSize()));
        infoLabel = new JLabel(
                "<html>Bitte wählen Sie eine Analyse aus.<br>Für weitere Informationen über die Auswertungsmöglichkeiten<br>gehen Sie bitte mit der Maus über den entsprechenden Button.</html>");
        infoIcon = new ImageIcon(types.getImage(types.getInfoIconPath()));
        infoIconLabel = new JLabel(infoIcon);

        infoPanel.add(infoIconLabel, "1,1");
        infoPanel.add(infoLabel, "1,2,2,3");
        infoBand.addPanel(infoPanel);
        infoBand.setPreferredSize(types.getDimension());

        // create task and add to ribbon
        task = new RibbonTask("Auswertung", selectAnalysisBand, selectOptionsBand, runEvaluationBand, infoBand);
    }

    /**
     * loads the project Analysis.
     * 
     * @param projectName the projectName
     */
    public void loadAnalysis(final String projectName) {

        DataAnalysis dataAnalysis = DataStore.dataStore.getDataAnalysis(projectName);

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                showBugRelatedCoundCommandButton.setEnabled(false);
                showBugRelatedDurationCommandButton.setEnabled(false);
                showStatisticsCommandButton.setEnabled(false);
            }
        });

        // enable buttons
        if (dataAnalysis != null) {
            if (dataAnalysis.isDoBugRelatedCount()) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        showBugRelatedCoundCommandButton.setEnabled(true);
                    }
                });
            }
            if (dataAnalysis.isDoBugRelatedDuration()) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        showBugRelatedDurationCommandButton.setEnabled(true);
                    }
                });
            }
            if (dataAnalysis.isDoStatistics()) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        showStatisticsCommandButton.setEnabled(true);
                    }
                });
            }
        }
    }

    /**
     * Fill the analysisChooserEvaluationComboBox.
     * 
     * @param projects all projects
     */
    public void addProjects(final List<Project> projects) {

        int pos = 0;

        mainWindow.setContentPane(null);

        selectAnalysisComboBox.removeAllItems();
        selectAnalysisComboBox.insertItemAt(" ", pos++);

        for (Project project : projects) {
            DataAnalysis dataAnalysis = project.getDataAnalysis();
            if (dataAnalysis != null) {
                selectAnalysisComboBox.insertItemAt(project.getName(), pos++);
            }
        }

        if (selectAnalysisComboBox.getItemCount() > 0) {
            selectAnalysisComboBox.setSelectedIndex(0);
        }
    }

    /**
     * @param message the new message
     */
    public void setEvaluationInfoLabelText(final String message) {
        infoLabel.setText(message);
    }

    /**
     * @return the current text of the infoLabel
     */
    public String getEvaluationInfoLabelText() {
        return infoLabel.getText();
    }

    /**
     * @param configView the view
     */
    public void setEvaluationConfigView(final JPanel configView) {
        RibbonTask manipulateTreeMapTask;

        JRibbonBand manipulateTreeMapBand = new JRibbonBand("Visualisierung anpassen", types.getIcon(""));
        JPanel manipulateTreeMapPanel = configView;
        manipulateTreeMapBand.addPanel(manipulateTreeMapPanel);

        JRibbonBand manipulateTreeMapInfoBand = new JRibbonBand("Informationen", types.getIcon(""));
        JPanel manipulateTreeMapInfoPanel = new JPanel();
        manipulateTreeMapInfoPanel.setLayout(new TableLayout(types.getInfoSize()));
        JLabel manipulateTreeMapInfoLabel = new JLabel(manipulateTreeMapInfoLabelText);
        ImageIcon manipulateTreeMapInfoIcon = new ImageIcon(types.getImage(types.getInfoIconPath()));
        JLabel manipulateTreeMapInfoIconLabel = new JLabel(manipulateTreeMapInfoIcon);
        manipulateTreeMapInfoIconLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        manipulateTreeMapInfoPanel.add(manipulateTreeMapInfoIconLabel, "1,1");
        manipulateTreeMapInfoPanel.add(manipulateTreeMapInfoLabel, "1,2,2,3");

        manipulateTreeMapInfoBand.addPanel(manipulateTreeMapInfoPanel);

        manipulateTreeMapTask = new RibbonTask("Visualisierung anpassen", manipulateTreeMapBand, manipulateTreeMapInfoBand);

        manipulateTreeMapBand.setPreferredSize(new Dimension(0, types.getRibbonTaskHeight()));

        RibbonContextualTaskGroup group = new RibbonContextualTaskGroup("Visualisierung anpassen", Color.blue, manipulateTreeMapTask);

        mainWindow.hideAllContextualTasks();

        JRibbon ribbon = mainWindow.getRibbon();

        mainWindow.getRibbon().addContextualTaskGroup(group);

        if (ribbon.getContextualTaskGroupCount() > 0) {
            ribbon.setVisible(ribbon.getContextualTaskGroup(ribbon.getContextualTaskGroupCount() - 1), true);
        }
    }

    /**
     * @param result the JPanel
     */
    public void setContentPane(final JPanel result) {
        mainWindow.setContentPane(result);
    }

    /**
     * @return the infoLabelTextTreeMap
     */
    public String getInfoLabelTextTreeMap() {
        return this.infoLabelTextTreeMap;
    }

    /**
     * @return the infoLabelTextStats
     */
    public String getInfoLabelTextPanel() {
        return this.infoLabelTextPanel;
    }

    /**
     * @return the infoLabelTextCount
     */
    public String getInfoLabelTextCount() {
        return this.infoLabelTextCount;
    }

    /**
     * @return the infoLabelTextDuration
     */
    public String getInfoLabelTextDuration() {
        return this.infoLabelTextDuration;
    }

    /**
     * @return the infoLabelTextStats
     */
    public String getInfoLabelTextStats() {
        return this.infoLabelTextStats;
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
     * 
     */
    public void validateAnalysis() {
        loadAnalysis((String) selectAnalysisComboBox.getSelectedItem());
    }

    /**
     * 
     * @return if the option showAll is selected
     */
    public boolean isShowAllSelected() {
        return showAllCheckBox.isSelected();
    }

    public int getMinValue() {
        return minValue.getValue();
    }
}
