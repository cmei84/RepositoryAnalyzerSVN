/**
 * 
 */
package de.cm.repositoryanalyzer.dataImport;

import de.cm.repositoryanalyzer.dataImport.bugs.BugImportFactory;
import de.cm.repositoryanalyzer.dataImport.bugs.IBugImport;
import de.cm.repositoryanalyzer.dataImport.repository.IRepositoryImport;
import de.cm.repositoryanalyzer.dataImport.repository.RepImportFactory;
import de.cm.repositoryanalyzer.db.SQLiteDB;

import java.util.Observable;

/**
 * @author Christian
 * 
 */
public class DataImport extends Observable implements Runnable {

    public final static int STATUSOK = 0;
    public final static int STATUSERROR = 1;

    private Project project;

    private int importStatus;
    private int importStatusMessage;

    /**
     * 
     * @param project the project
     */
    public DataImport(final Project project) {
        this.project = project;
    }

    /**
     * Runs the import.
     * 
     */
    public void startImport() {
        try {

            // initialize the db
            SQLiteDB.db.init(project);

            // import SVN Commits
            setChanged();
            notifyObservers("Importiere Modifikation-Requests");

            RepImportFactory repFactory = new RepImportFactory();
            IRepositoryImport repositoryImport = repFactory.getBugImport(project, this);
            SQLiteDB.db.insertModificationRequests(repositoryImport.runImport());

            // import Bugs
            setChanged();
            notifyObservers("Importiere Tickets");

            BugImportFactory factory = new BugImportFactory();
            IBugImport bugImport = factory.getBugImport(project, this);
            if (bugImport != null) {
                SQLiteDB.db.insertTickets(bugImport.runImport());
            }

            // close Connecton
            SQLiteDB.db.closeConnection();

            // refresh the projects in the project
            setChanged();
            notifyObservers("import finished");

        } catch (Exception e) {
            setImportStatus(STATUSERROR);
            e.printStackTrace();
        }
        // complete
        return;
    }

    /**
     * 
     * @param progress the progress of the work unit
     */
    public void notifyChange(final int progress) {
        setChanged();
        notifyObservers(new Integer(progress));
    }

    /**
     * 
     * @param message the message to display
     */
    public void notifyChange(final String message) {
        setChanged();
        notifyObservers(message);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        startImport();
    }

    /**
     * @return the importStatus
     */
    public int getImportStatus() {
        return this.importStatus;
    }

    /**
     * @return the importStatusMessage
     */
    public int getImportStatusMessage() {
        return this.importStatusMessage;
    }

    /**
     * @param importStatus the importStatus to set
     */
    public void setImportStatus(final int importStatus) {
        this.importStatus = importStatus;
    }

    /**
     * @param importStatusMessage the importStatusMessage to set
     */
    public void setImportStatusMessage(final int importStatusMessage) {
        this.importStatusMessage = importStatusMessage;
    }
}
