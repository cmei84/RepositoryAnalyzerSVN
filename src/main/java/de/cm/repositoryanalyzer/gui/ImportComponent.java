/**
 * 
 */
package de.cm.repositoryanalyzer.gui;

import de.cm.repositoryanalyzer.dataImport.Project;
import de.cm.repositoryanalyzer.gui.action.EmptyTextfieldMouseListener;
import de.cm.repositoryanalyzer.gui.action.EnableJTextfieldActionListener;
import de.cm.repositoryanalyzer.gui.action.OpenXMLFileActionListener;
import de.cm.repositoryanalyzer.gui.action.RunImportMouseListener;
import de.cm.repositoryanalyzer.gui.action.ValidateImportActionListener;
import de.cm.repositoryanalyzer.gui.action.ValidateImportInputData;

import info.clearthought.layout.TableLayout;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jvnet.flamingo.common.JCommandButton;
import org.jvnet.flamingo.common.RichTooltip;
import org.jvnet.flamingo.ribbon.JRibbonBand;
import org.jvnet.flamingo.ribbon.RibbonApplicationMenuEntryPrimary;
import org.jvnet.flamingo.ribbon.RibbonTask;

/**
 * @author Christian
 * 
 */
public class ImportComponent {

    private Types types;
    private MainWindow mainWindow;

    private RibbonTask task;

    private JCommandButton openXMLFileCommandButton;
    private RichTooltip openXMLFileCommandButtonRichToolTip;

    // svn Band
    private JRibbonBand svnBand;
    private JPanel svnPanel;

    private JTextField svnUrlTextField;
    private JTextField svnUserTextField;
    private JTextField svnPassTextField;
    private JCheckBox svnUserPassNeededCheckBox;

    // bug Band
    private JRibbonBand bugBand;
    private JPanel bugPanel;

    private JTextField bugUrlTextField;
    private JTextField bugUserTextField;
    private JTextField bugPassTextField;
    private JCheckBox bugUserPassNeededCheckBox;

    // runImport Band
    private JRibbonBand runImportBand;
    private JPanel runImportPanel;

    private JCommandButton runImportCommandButton;
    private RichTooltip runImportRichTooltip;

    // info Band
    private JRibbonBand infoBand;
    private JPanel infoPanel;
    private JLabel infoIconLabel;
    private JLabel infoLabel;
    private ImageIcon infoIcon;

    // Field Strings
    private final String svnUrlEmptyText = "Hier SVN-Repository URL einfügen.";
    private final String bugUrlEmptyText = "Hier Bug-Tracker URL oder den Dateipfad einfügen.";
    private final String userEmptyText = "Benutzername";
    private final String passEmptyText = "Passwort";

    // Tooltip Strings
    private final String svnBandToolTipText = "<html><strong>SVN - Repository:</strong><br>Hier bitte alle Daten zum<br>SVN - Repository eingeben.</html>";
    private final String bugBandToolTipText = "<html><strong>Bug - Tracker:</strong><br>Hier bitte alle Daten zum<br>Bug - Tracker eingeben.</html>";
    private final String runImportBandToolTipText = "<html><strong>Import:</strong><br>Hier kann der Import gestartet werden.</html>";
    private final String infoBandToolTipText = "<html><strong>Info:</strong><br>Hier erhalten Sie alle nötigen Informationen,<br>um den Importvorgang auszuführen.</html>";

    // Icon
    private final String runImportIcon = "icons/runImport.64.png";
    private final String openXMLIcon = "icons/openXMLFile.png";

    /**
     * @param mainWindow the mainWindow
     */
    public ImportComponent(final MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        this.types = mainWindow.getTypes();

        openXMLFileCommandButton = new JCommandButton("XML-Datei öffnen", types.getIcon(openXMLIcon));
        openXMLFileCommandButtonRichToolTip = new RichTooltip();
        openXMLFileCommandButtonRichToolTip.setTitle("XML-Datei öffnen");
        openXMLFileCommandButtonRichToolTip.addDescriptionSection("Ã–ffnen einer XML-Datei mit Ticket-Informationen des Bug-Trackers");
        openXMLFileCommandButton.setActionRichTooltip(openXMLFileCommandButtonRichToolTip);

        openXMLFileCommandButton.addActionListener(new OpenXMLFileActionListener(this));

        mainWindow.addTaskBarButton(openXMLFileCommandButton);
        mainWindow.addApplicationMenuEntry(new RibbonApplicationMenuEntryPrimary(types.getIcon(openXMLIcon), "XML-Datei öffnen", new OpenXMLFileActionListener(this),
                openXMLFileCommandButton.getCommandButtonKind()));

        // SVN
        svnBand = new JRibbonBand("SVN - Repository", types.getIcon(""));
        svnPanel = new JPanel();

        svnUrlTextField = new JTextField(svnUrlEmptyText);
        svnUrlTextField.addMouseListener(new EmptyTextfieldMouseListener(svnUrlTextField));
        svnUrlTextField.addKeyListener(new ValidateImportInputData(this));

        svnUserTextField = new JTextField(userEmptyText);
        svnUserTextField.setEnabled(false);
        svnUserTextField.addMouseListener(new EmptyTextfieldMouseListener(svnUserTextField));
        svnUserTextField.addKeyListener(new ValidateImportInputData(this));
        svnPassTextField = new JTextField(passEmptyText);
        svnPassTextField.setEnabled(false);
        svnPassTextField.addMouseListener(new EmptyTextfieldMouseListener(svnPassTextField));
        svnPassTextField.addKeyListener(new ValidateImportInputData(this));
        svnUserPassNeededCheckBox = new JCheckBox("Benutzer und Passwort nicht nötig");
        svnUserPassNeededCheckBox.addActionListener(new EnableJTextfieldActionListener(svnUserTextField));
        svnUserPassNeededCheckBox.addActionListener(new EnableJTextfieldActionListener(svnPassTextField));
        svnUserPassNeededCheckBox.addActionListener(new ValidateImportActionListener(this));
        svnUserPassNeededCheckBox.setSelected(true);

        svnPanel.setLayout(new TableLayout(types.getSize()));
        svnPanel.add(svnUrlTextField, "1,1,4,1");
        svnPanel.add(svnUserTextField, "1,3");
        svnPanel.add(svnPassTextField, "3,3,4,3");
        svnPanel.add(svnUserPassNeededCheckBox, "1,5,3,5");
        svnBand.addPanel(svnPanel);
        svnBand.setPreferredSize(types.getDimension());
        svnBand.setToolTipText(svnBandToolTipText);

        // BUGS
        bugBand = new JRibbonBand("Bug - Tracker", types.getIcon(""));
        bugPanel = new JPanel();

        bugUrlTextField = new JTextField(bugUrlEmptyText);
        bugUrlTextField.addMouseListener(new EmptyTextfieldMouseListener(bugUrlTextField));
        bugUrlTextField.addKeyListener(new ValidateImportInputData(this));
        bugUserTextField = new JTextField(userEmptyText);
        bugUserTextField.setEnabled(false);
        bugUserTextField.addMouseListener(new EmptyTextfieldMouseListener(bugUserTextField));
        bugUserTextField.addKeyListener(new ValidateImportInputData(this));
        bugPassTextField = new JTextField(passEmptyText);
        bugPassTextField.setEnabled(false);
        bugPassTextField.addMouseListener(new EmptyTextfieldMouseListener(bugPassTextField));
        bugPassTextField.addKeyListener(new ValidateImportInputData(this));
        bugUserPassNeededCheckBox = new JCheckBox("Benutzer und Passwort nicht nötig");
        bugUserPassNeededCheckBox.addActionListener(new EnableJTextfieldActionListener(bugUserTextField));
        bugUserPassNeededCheckBox.addActionListener(new EnableJTextfieldActionListener(bugPassTextField));
        bugUserPassNeededCheckBox.addActionListener(new ValidateImportActionListener(this));
        bugUserPassNeededCheckBox.setSelected(true);

        bugPanel.setLayout(new TableLayout(types.getSize()));
        bugPanel.add(bugUrlTextField, "1,1,3,1");
        bugPanel.add(bugUserTextField, "1,3");
        bugPanel.add(bugPassTextField, "3,3");
        bugPanel.add(bugUserPassNeededCheckBox, "1,5,3,5");
        bugBand.addPanel(bugPanel);
        bugBand.setPreferredSize(types.getDimension());
        bugBand.setToolTipText(bugBandToolTipText);

        // run import
        runImportBand = new JRibbonBand("Import", types.getIcon(""));
        runImportPanel = new JPanel();

        runImportCommandButton = new JCommandButton("", types.getIcon(runImportIcon));
        runImportCommandButton.addMouseListener(new RunImportMouseListener(this));

        runImportRichTooltip = new RichTooltip();
        runImportRichTooltip.setTitle("Import starten");
        runImportRichTooltip.addDescriptionSection("Startet den Daten-Import.");
        runImportCommandButton.setActionRichTooltip(runImportRichTooltip);
        runImportCommandButton.setEnabled(false);

        runImportPanel.add(runImportCommandButton);
        runImportBand.addPanel(runImportPanel);
        runImportBand.setToolTipText(runImportBandToolTipText);

        // info band
        infoBand = new JRibbonBand("Info", types.getIcon(""));
        infoPanel = new JPanel();
        infoPanel.setLayout(new TableLayout(types.getInfoSize()));
        infoLabel = new JLabel("<html>Bitte füllen Sie alle nötigten Felder aus,<br>um den Import zu Starten.</html>");
        infoIcon = new ImageIcon(types.getImage(types.getInfoIconPath()));
        infoIconLabel = new JLabel(infoIcon);
        infoIconLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        infoPanel.add(infoLabel, "1,2,2,3");
        infoPanel.add(infoIconLabel, "1,1");

        infoBand.addPanel(infoPanel);
        infoBand.setToolTipText(infoBandToolTipText);

        task = new RibbonTask("Import", svnBand, bugBand, runImportBand, infoBand);
    }

    /**
     * Checkt if the needed Input Data is given to perform an Import.
     * 
     */
    public void validateImportInputData() {

        StringBuffer result = new StringBuffer();
        final int urlMinLength = 8;

        String svnUrl = this.svnUrlTextField.getText();
        String svnUser = this.svnUserTextField.getText();
        String svnPass = this.svnPassTextField.getText();

        String bugUrl = this.bugUrlTextField.getText();
        String bugUser = this.bugUserTextField.getText();
        String bugPass = this.bugPassTextField.getText();

        if (svnUrl.length() < urlMinLength || svnUrl.equals(this.svnUrlEmptyText)) {
            result.append("SVN-URL, ");
        }
        if ((svnUser.length() == 0 || svnUser.equals(this.userEmptyText)) && !svnUserPassNeededCheckBox.isSelected()) {
            result.append("SVN-Benutzername, ");
        }
        if ((svnPass.length() == 0 || svnPass.equals(this.passEmptyText)) && !svnUserPassNeededCheckBox.isSelected()) {
            result.append("SVN-Passwort, ");
        }

        if (bugUrl.length() < urlMinLength) {
            result.append("BUG-URL (Hier kann auch ein Dateipfad angegeben werden), ");
        }
        if ((bugUser.length() == 0 || bugUser.equals(this.userEmptyText)) && !bugUserPassNeededCheckBox.isSelected()) {
            result.append("BUG-Benutzername, ");
        }
        if ((bugPass.length() == 0 || bugPass.equals(this.passEmptyText)) && !bugUserPassNeededCheckBox.isSelected()) {
            result.append("BUG-Passwort, ");
        }

        if (result.toString().equals("")) {
            infoLabel.setText("Der Import kann nun gestartet werden.");
            this.runImportCommandButton.setEnabled(true);
        } else {
            infoLabel.setText("<html>Bitte füllen Sie noch folgende Felder aus:<br>" + result.substring(0, result.length() - 2) + "</html>");
            this.runImportCommandButton.setEnabled(false);
        }
    }

    /**
     * @return the Project to Import.
     */
    public Project getImportProject() {
        Project project = new Project();
        project.setSvnUrl(this.svnUrlTextField.getText());
        project.setSvnUser(this.getSvnUser());
        project.setSvnPass(this.getSvnPass());
        if (this.bugUrlTextField.getText().equals(bugUrlEmptyText)) {
            project.setBugUrl("");
        } else {
            project.setBugUrl(this.bugUrlTextField.getText());
        }
        project.setBugUser(this.getBugUser());
        project.setBugPass(this.getBugPass());
        return project;
    }

    /**
     * 
     * @return the svn Username
     */
    public String getSvnUser() {
        String svnUser = this.svnUserTextField.getText();
        if (svnUser.equals(userEmptyText)) {
            svnUser = "";
        }
        return svnUser;
    }

    /**
     * 
     * @return the svn Passwort
     */
    public String getSvnPass() {
        String svnPass = this.svnPassTextField.getText();
        if (svnPass.equals(passEmptyText)) {
            svnPass = "";
        }
        return svnPass;
    }

    /**
     * 
     * @return the bug Username
     */
    public String getBugUser() {
        String bugUser = this.bugUserTextField.getText();
        if (bugUser.equals(userEmptyText)) {
            bugUser = "";
        }
        return bugUser;
    }

    /**
     * 
     * @return the bug Passwort
     */
    public String getBugPass() {
        String bugPass = this.bugPassTextField.getText();
        if (bugPass.equals(passEmptyText)) {
            bugPass = "";
        }
        return bugPass;
    }

    /**
     * 
     * @param text the text
     */
    public void setInfoLabelText(final String text) {
        infoLabel.setText(text);
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
     * @param path the path
     */
    public void setBugUrlText(final String path) {
        bugUrlTextField.setText(path);
    }
}
