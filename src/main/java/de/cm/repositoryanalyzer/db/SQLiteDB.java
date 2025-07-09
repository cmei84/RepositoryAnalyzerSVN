/**
 * 
 */
package de.cm.repositoryanalyzer.db;

import de.cm.repositoryanalyzer.dataImport.Project;
import de.cm.repositoryanalyzer.dataImport.bugs.Ticket;
import de.cm.repositoryanalyzer.dataImport.repository.ChangedPath;
import de.cm.repositoryanalyzer.dataImport.repository.ModificationRequest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Christian
 * 
 */
public class SQLiteDB {

    public static SQLiteDB db = new SQLiteDB();

    private final String projectTable = "projects";
    private final String bugTable = "tickets";
    private final String modificationRequestTable = "modificationRequests";
    private final String pathTable = "paths";

    private Connection conn;
    private Statement statement;

    private Project project;

    /**
     * 
     */
    public SQLiteDB() {

    }

    /**
     * 
     * @param project the project
     */
    public void init(final Project project) {
        try {
            Class.forName("org.sqlite.JDBC");
            openConnection();
            if (project != null) {
                this.project = project;
                checkAndCreateTables();
                findOrInsertProject(project.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens a Connection and creates the statement.
     */
    public void openConnection() {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:data/db.db");
            statement = conn.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes the statement and the connection.
     */
    public void closeConnection() {
        try {
            statement.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     * @param sql the sql string
     * @return statement.executeQuery(sql);
     * @throws SQLException if error
     */
    public ResultSet executeQuery(final String sql) throws SQLException {
        ResultSet rs;
        rs = statement.executeQuery(sql);
        return rs;
    }

    /**
     * 
     * @param sql the sql string
     */
    public void execute(final String sql) {
        try {
            init(null);
            statement.execute(sql);
            closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param project the project
     */
    public void insertProject(final Project project) {

        try {
            StringBuffer query = new StringBuffer("INSERT INTO " + projectTable + " VALUES (");
            query.append("'" + project.getName() + "', ");
            query.append("'" + project.getSvnUrl() + "', ");
            query.append("'" + project.getSvnUser() + "', ");
            query.append("'" + project.getSvnPass() + "', ");
            query.append("'" + project.getBugUrl() + "', ");
            query.append("'" + project.getBugUser() + "', ");
            query.append("'" + project.getBugPass() + "')");
            statement.execute(query.toString());
            findOrInsertProject(project.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param projectName the projectName
     */
    public void findOrInsertProject(final String projectName) {
        StringBuffer query = new StringBuffer("SELECT rowid, name, svnurl, svnuser, svnpass, bugurl, buguser, bugpass FROM "
                + projectTable);
        ResultSet rs;
        try {
            List<Project> projects = new ArrayList<Project>();
            statement.execute(query.toString());
            rs = statement.getResultSet();

            if (rs != null) {
                while (rs.next()) {
                    int col = 1;
                    Project project = new Project();
                    project.setId(rs.getInt(col++));
                    project.setName(rs.getString(col++));
                    project.setSvnUrl(rs.getString(col++));
                    project.setSvnUser(rs.getString(col++));
                    project.setSvnPass(rs.getString(col++));
                    project.setBugUrl(rs.getString(col++));
                    project.setBugUser(rs.getString(col++));
                    project.setBugPass(rs.getString(col++));

                    projects.add(project);
                }
                rs.close();
            }
            for (Project project : projects) {
                if (project.getName().equals(projectName)) {
                    if (this.project.getViewMin() > 0 && this.project.getViewMax() > 0) {
                        project.setViewMin(this.project.getViewMin());
                        project.setViewMax(this.project.getViewMax());
                    }
                    this.project = project;
                    return;
                }
            }
            insertProject(this.project);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param ticket the ticket to insert
     */
    private void insertTicket(final Ticket ticket) {

        StringBuffer query = new StringBuffer("INSERT INTO " + bugTable + " VALUES (");

        query.append(ticket.getId() + ", ");
        query.append("'" + ticket.getCreation_ts() + "', ");
        query.append("'" + ticket.getDelta_ts() + "', ");
        query.append("'" + ticket.getVersion() + "', ");
        query.append("'" + ticket.getStatus() + "', ");
        query.append("'" + ticket.getResolution() + "', ");
        query.append("'" + ticket.getPriority() + "', ");
        query.append("'" + ticket.getSeverity() + "', ");
        query.append("'" + ticket.getReporter() + "', ");
        query.append("'" + ticket.getAssigned_to() + "', ");
        query.append(project.getId());
        query.append(")");

        try {
            statement.execute(query.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param tickets the List with tickets to insert
     * @throws SQLException if error
     */
    public void insertTickets(final List<Ticket> tickets) throws SQLException {

        if (tickets != null && tickets.size() > 1) {
            conn.setAutoCommit(false);
            for (Ticket ticket : tickets) {
                insertTicket(ticket);
            }
            conn.commit();
        }
    }

    /**
     * @param modificationRequest the modificationRequest to insert
     */
    private void insertModificationRequest(final ModificationRequest modificationRequest) {

        StringBuffer query = new StringBuffer("INSERT INTO " + modificationRequestTable + " VALUES (");

        query.append(modificationRequest.getRevision() + ", ");
        query.append(modificationRequest.getDate() + ", ");
        query.append("'" + modificationRequest.getAutor() + "', ");
        query.append("'" + modificationRequest.getComment() + "', ");
        query.append(project.getId());
        query.append(")");

        try {
            statement.execute(query.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param modificationRequests the List of modificationRequests to insert
     * @throws SQLException if error
     */
    public void insertModificationRequests(final List<ModificationRequest> modificationRequests) throws SQLException {

        if (modificationRequests != null && modificationRequests.size() > 0) {

            conn.setAutoCommit(false);

            for (ModificationRequest modificationRequest : modificationRequests) {
                insertModificationRequest(modificationRequest);
                for (ChangedPath changedPath : modificationRequest.getChangedPaths()) {
                    insertChangedPath(changedPath);
                }
            }

            conn.commit();
        }
    }

    /**
     * 
     * @param changedPath the changedPath to insert
     * @return statement.execute();
     */
    public boolean insertChangedPath(final ChangedPath changedPath) {

        StringBuffer query = new StringBuffer("INSERT INTO " + pathTable + " VALUES (");

        query.append(changedPath.getRevision() + ", ");
        query.append("'" + changedPath.getType() + "', ");
        query.append("'" + changedPath.getPath() + "', ");
        query.append(changedPath.getSize() + ", ");
        query.append(project.getId());
        query.append(")");

        try {
            return statement.execute(query.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @return all Projects
     */
    public List<Project> loadProjects() {
        init(null);
        List<Project> projects = new ArrayList<Project>();
        Project project;
        boolean tableFound = false;
        ResultSet rs;

        try {
            rs = executeQuery("SELECT tbl_name FROM sqlite_master");
            while (rs.next()) {
                if (rs.getString(1).equals(projectTable)) {
                    tableFound = true;
                    break;
                }
            }
            if (tableFound) {

                rs = executeQuery("SELECT rowid, name, svnurl, svnuser, svnpass, bugurl, buguser, bugpass  FROM " + projectTable);

                while (rs.next()) {
                    int col = 1;
                    project = new Project();
                    project.setId(rs.getInt(col++));
                    project.setName(rs.getString(col++));
                    project.setSvnUrl(rs.getString(col++));
                    project.setSvnUser(rs.getString(col++));
                    project.setSvnPass(rs.getString(col++));
                    project.setBugUrl(rs.getString(col++));
                    project.setBugUser(rs.getString(col++));
                    project.setBugPass(rs.getString(col++));
                    projects.add(project);
                }

                long revision = 0;
                long maxRevision = 0;

                long dateValue = 0;
                long minDate = 0;
                long maxDate = 0;

                for (Project p : projects) {
                    rs = executeQuery("select * from " + modificationRequestTable + " where projectId=" + p.getId());
                    while (rs.next()) {
                        // find max revision
                        revision = rs.getLong(1);
                        if (revision > maxRevision) {
                            maxRevision = revision;
                        }
                        // find min, max date
                        dateValue = rs.getLong(2);
                        if (maxDate == 0 || maxDate < dateValue) {
                            maxDate = dateValue;
                        }
                        if (minDate == 0 || minDate > dateValue) {
                            minDate = dateValue;
                        }
                    }
                    p.setLastRevision(maxRevision);
                    p.setProjectMin(minDate);
                    p.setProjectMax(maxDate);

                    int maxTicketId = 0;
                    int ticketId = 0;

                    rs = executeQuery("select * from " + bugTable + " where projectId=" + p.getId());
                    while (rs.next()) {
                        // find max ticketId
                        ticketId = rs.getInt(1);
                        if (ticketId > maxTicketId) {
                            maxTicketId = ticketId;
                        }
                    }
                    p.setLastBugId(maxTicketId);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeConnection();
        return projects;
    }

    /**
     * 
     * @return all tickets wich matches the project
     */
    public List<Ticket> loadTickets() {
        List<Ticket> tickets = new ArrayList<Ticket>();
        Ticket ticket;
        ResultSet rs;
        try {
            rs = executeQuery("SELECT * FROM " + bugTable + " WHERE projectId=" + project.getId());

            while (rs.next()) {
                int col = 1;
                ticket = new Ticket();
                ticket.setId(rs.getInt(col++));
                ticket.setCreation_ts(rs.getLong(col++));
                ticket.setDelta_ts(rs.getLong(col++));
                ticket.setVersion(rs.getString(col++));
                ticket.setStatus(rs.getString(col++));
                ticket.setResolution(rs.getString(col++));
                ticket.setPriority(rs.getString(col++));
                ticket.setSeverity(rs.getString(col++));
                ticket.setReporter(rs.getString(col++));
                ticket.setAssigned_to(rs.getString(col++));
                tickets.add(ticket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tickets;
    }

    /**
     * 
     * @return all modificationRequests wich matches the project
     */
    public List<ModificationRequest> loadModificationRequests() {
        List<ModificationRequest> modificationRequests = new ArrayList<ModificationRequest>();
        ModificationRequest modificationRequest;
        ResultSet rs;

        try {
            rs = executeQuery("SELECT * FROM " + modificationRequestTable + " WHERE projectId=" + project.getId());

            while (rs.next()) {
                int col = 1;
                modificationRequest = new ModificationRequest();
                modificationRequest.setRevision(rs.getLong(col++));
                modificationRequest.setDate(rs.getLong(col++));
                modificationRequest.setAutor(rs.getString(col++));
                modificationRequest.setComment(rs.getString(col++));

                modificationRequests.add(modificationRequest);
            }

            for (ModificationRequest m : modificationRequests) {
                m.setChangedPaths(loadChangedPaths(m.getRevision()));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return modificationRequests;
    }

    /**
     * @param revision the revision
     * @return all changedPaths wich matches the project
     */
    public List<ChangedPath> loadChangedPaths(final long revision) {
        List<ChangedPath> changedPaths = new ArrayList<ChangedPath>();
        ChangedPath path;
        ResultSet rs;

        try {
            rs = executeQuery("SELECT * FROM " + pathTable + " WHERE projectId=" + project.getId() + " AND revision=" + revision);

            while (rs.next()) {
                int col = 1;
                path = new ChangedPath();
                path.setRevision(rs.getLong(col++));
                path.setType(rs.getString(col++).charAt(0));
                path.setPath(rs.getString(col++));
                path.setSize(rs.getLong(col++));

                changedPaths.add(path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return changedPaths;
    }

    /**
     * 
     * @return true is everything is ok false if error
     */
    private boolean checkAndCreateTables() {
        boolean ok = true;

        try {
            StringBuffer query = new StringBuffer("CREATE TABLE IF NOT EXISTS " + projectTable + " (");

            query.append("name VARCHAR, ");
            query.append("svnurl VARCHAR, ");
            query.append("svnuser VARCHAR, ");
            query.append("svnpass VARCHAR, ");
            query.append("bugurl VARCHAR, ");
            query.append("buguser VARCHAR, ");
            query.append("bugpass VARCHAR");
            query.append(")");

            statement.execute(query.toString());
        } catch (Exception e) {
            e.printStackTrace();
            ok = false;
        }

        try {
            StringBuffer query = new StringBuffer("CREATE TABLE IF NOT EXISTS " + bugTable + " (");

            query.append("id INTEGER, ");
            query.append("creation_ts LONG, ");
            query.append("deltats LONG, ");
            query.append("version VARCHAR, ");
            query.append("status VARCHAR, ");
            query.append("resolution VARCHAR, ");
            query.append("priority VARCHAR, ");
            query.append("severity VARCHAR, ");
            query.append("reporter VARCHAR, ");
            query.append("assigned_to VARCHAR, ");
            query.append("projectId INTEGER NOT NULL CONSTRAINT projectId REFERENCES " + projectTable
                    + "(rowid) ON DELETE CASCADE");
            query.append(")");

            statement.execute(query.toString());
        } catch (Exception e) {
            e.printStackTrace();
            ok = false;
        }

        try {
            StringBuffer query = new StringBuffer("CREATE TABLE IF NOT EXISTS " + modificationRequestTable + " (");

            query.append("revision LONG, ");
            query.append("date LONG, ");
            query.append("autor VARCHAR, ");
            query.append("comment VARCHAR, ");
            query.append("projectId INTEGER NOT NULL CONSTRAINT projectId REFERENCES " + projectTable
                    + "(rowid) ON DELETE CASCADE");
            query.append(")");

            statement.execute(query.toString());
        } catch (Exception e) {
            e.printStackTrace();
            ok = false;
        }

        try {
            StringBuffer query = new StringBuffer("CREATE TABLE IF NOT EXISTS " + pathTable + " (");

            query.append("revision LONG, ");
            query.append("type CHAR, ");
            query.append("path VARCHAR, ");
            query.append("size LONG, ");
            query.append("projectId INTEGER NOT NULL CONSTRAINT projectId REFERENCES " + projectTable
                    + "(rowid) ON DELETE CASCADE");
            query.append(")");

            statement.execute(query.toString());
        } catch (Exception e) {
            e.printStackTrace();
            ok = false;
        }
        return ok;
    }

    /**
     * Lï¿½scht alle zum angegebenen Projekt gehï¿½renden Eintrï¿½ge in der Datenbank.
     * 
     * @param projectName the name
     */
    public void deleteProject(final String projectName) {
        try {
            init(null);
            this.project = null;
            findOrInsertProject(projectName);
            if (this.project != null) {
                statement.execute("DELETE FROM " + projectTable + " WHERE name='" + projectName + "'");
                statement.execute("DELETE FROM " + modificationRequestTable + " WHERE projectId=" + this.project.getId());
                statement.execute("DELETE FROM " + pathTable + " WHERE projectId=" + this.project.getId());
                statement.execute("DELETE FROM " + bugTable + " WHERE projectId=" + this.project.getId());
            }
            closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
            closeConnection();
        }
    }

    /**
     * @param projectId the projectId
     */
    public void printProjectData(final int projectId) {
        init(null);
        ResultSet rs;
        try {
            rs = executeQuery("SELECT name, svnurl, svnuser, svnpass, bugurl, buguser, bugpass  FROM " + projectTable
                    + " where rowid=" + projectId);

            while (rs.next()) {
                int col = 1;
                System.out.print("Nr: " + projectId);
                System.out.print(" name: " + rs.getString(col++));
                System.out.print(" svn: " + rs.getString(col++));
                System.out.print(" svnuser: " + rs.getString(col++));
                System.out.print(" svnpass: " + rs.getString(col++));
                System.out.print(" bugurl: " + rs.getString(col++));
                System.out.print(" buguser: " + rs.getString(col++));
                System.out.println(" bugpass: " + rs.getString(col++));
            }

            List<ModificationRequest> modificationRequests = new ArrayList<ModificationRequest>();
            modificationRequests = loadModificationRequests();

            for (ModificationRequest m : modificationRequests) {
                System.out.print("Rev: " + m.getRevision());
                System.out.print(" date: " + m.getDate());
                System.out.print(" autor: " + m.getAutor());
                System.out.println();
            }

            rs = executeQuery("SELECT * FROM " + bugTable + " WHERE projectId=" + projectId);

            while (rs.next()) {
                int col = 1;
                System.out.print(rs.getInt(col++));
                System.out.print(rs.getLong(col++));
                System.out.print(rs.getLong(col++));
                System.out.print(rs.getString(col++));
                System.out.print(rs.getString(col++));
                System.out.print(rs.getString(col++));
                System.out.print(rs.getString(col++));
                System.out.print(rs.getString(col++));
                System.out.print(rs.getString(col++));
                System.out.println(rs.getString(col++));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        closeConnection();
    }

    /**
     * 
     */
    public void printCompleteDB() {
        List<Project> projects = new ArrayList<Project>();
        try {
            projects = loadProjects();
            for (Project p : projects) {
                project = p;
                printProjectData(p.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @return the Project
     */
    public Project getProject() {
        return project;
    }
}
