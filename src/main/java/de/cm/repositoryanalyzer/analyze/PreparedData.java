/**
 * 
 */
package de.cm.repositoryanalyzer.analyze;

import de.cm.repositoryanalyzer.dataImport.Project;
import de.cm.repositoryanalyzer.dataImport.bugs.Ticket;
import de.cm.repositoryanalyzer.dataImport.repository.ChangedPath;
import de.cm.repositoryanalyzer.dataImport.repository.ModificationRequest;
import de.cm.repositoryanalyzer.db.SQLiteDB;
import de.cm.repositoryanalyzer.util.Config;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Christian
 * 
 */
public class PreparedData {

    private Project project;
    private DataAnalysis dataAnalysis;

    private List<ModificationRequest> modificationRequests;
    private List<ChangedPath> changedPaths;
    private List<Ticket> tickets;

    private List<ModificationRequest> allModificationRequests;

    /**
     * @param project the Project
     * @param dataAnalysis the dataAnalysis
     */
    public PreparedData(final Project project, final DataAnalysis dataAnalysis) {
        this.project = project;
        this.dataAnalysis = dataAnalysis;
    }

    /**
    * 
    */
    public void prepareData() {

        SQLiteDB.db.init(project);

        modificationRequests = new ArrayList<ModificationRequest>();
        changedPaths = new ArrayList<ChangedPath>();
        allModificationRequests = SQLiteDB.db.loadModificationRequests();

        tickets = new ArrayList<Ticket>();
        tickets = SQLiteDB.db.loadTickets();

        final int notifyEvery = 50;
        int notifyCounter = 0;
        int counter = 0;

        for (ModificationRequest modificationRequest : allModificationRequests) {
            for (ChangedPath c : modificationRequest.getChangedPaths()) {
                addPath(c);
            }
        }

        for (ModificationRequest modificationRequest : allModificationRequests) {
            if (modificationRequest.getDate() >= project.getViewMin() && modificationRequest.getDate() <= project.getViewMax()) {
                String comment = modificationRequest.getComment().toLowerCase();
                for (Ticket ticket : tickets) {

                    // check for bugkeywords
                    boolean bugkeywords = true;
                    for (String keyword : Config.config.getBugkeywords()) {
                        if (!comment.contains(keyword)) {
                            bugkeywords = false;
                        }
                    }
                    if (bugkeywords) {
                        // check for id format
                        boolean bugidformat = false;
                        for (String bugid : Config.config.getBugid()) {
                            if (bugid.contains("[id]")) {
                                bugid = bugid.replace("[id]", "" + ticket.getId());
                                bugid = bugid.replace("'", "");
                                if (comment.contains(bugid)) {
                                    bugidformat = true;
                                }
                            }
                        }
                        if (bugidformat) {
                            // check for right priority
                            boolean priority = false;
                            for (String part : Config.config.getPriorityValues()) {
                                if (ticket.getPriority().contains(part)) {
                                    priority = true;
                                }
                            }
                            if (bugkeywords && bugidformat && priority) {
                                modificationRequest.setTicket(ticket);
                            }
                        }
                    }

                }
                modificationRequests.add(modificationRequest);
                if (notifyCounter > (modificationRequests.size() / notifyEvery)) {
                    notifyCounter = 0;
                    dataAnalysis.notifyChange((int) ((100.0 / modificationRequests.size()) * counter * 1.0));
                }
                notifyCounter++;
                counter++;

                for (ChangedPath c : modificationRequest.getChangedPaths()) {
                    c.setValid(pathOK(c));
                }
            }
        }

        // complete
        dataAnalysis.notifyChange(100);

        // close the connection
        SQLiteDB.db.closeConnection();
    }

    /**
     * @param cp the ChangedPath
     * @return if valid
     */
    private boolean pathOK(final ChangedPath cp) {
        for (ChangedPath c : changedPaths) {
            if (c.getPath().equals(cp.getPath())) {
                if (c.getRevision() != -1 && c.getRevision() <= cp.getRevision()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @param cp the ChangedPath
     */
    private void addPath(final ChangedPath cp) {
        for (ChangedPath c : changedPaths) {
            if (c.getPath().equals(cp.getPath())) {
                if (cp.getType() == 'D') {
                    c.setRevision(-1);
                } else if (c.getRevision() == -1) {
                    c.setRevision(cp.getRevision());
                }
                return;
            }
        }

        changedPaths.add(cp);
    }

    /**
     * 
     * @return the preparedData
     */
    public List<ModificationRequest> getPreparedData() {
        return modificationRequests;
    }

    /**
     * Returns the complete list of Bugs (unprepared).
     * 
     * @return all tickets
     */
    public List<Ticket> getAllTickets() {
        return tickets;
    }

    /**
     * @return all Modinidation-Requests
     */
    public List<ModificationRequest> getAllModificationRequests() {
        return allModificationRequests;
    }

    /**
     * @return the project
     */
    public Project getProject() {
        return this.project;
    }

    /**
     * @param project the project to set
     */
    public void setProject(final Project project) {
        this.project = project;
    }
}
