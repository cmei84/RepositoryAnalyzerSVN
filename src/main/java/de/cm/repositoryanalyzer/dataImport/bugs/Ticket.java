/**
 * 
 */
package de.cm.repositoryanalyzer.dataImport.bugs;

/**
 * @author Christian
 * 
 */
public class Ticket {

    private int id;
    private long creation_ts;
    private long delta_ts;

    private String version;
    private String status;
    private String resolution;
    private String priority;
    private String severity;
    private String reporter;
    private String assigned_to;

    /**
     * 
     */
    public Ticket() {
    }

    /**
     * 
     * @param id the id
     */
    public Ticket(final int id) {
        this.id = id;
    }

    /**
     * 
     * @param id the id
     * @param creation_ts the creation_ts
     * @param delta_ts the delta_ts
     * @param version the version
     * @param status the status
     * @param resolution the resolution
     * @param priority the priority
     * @param severity the severity
     * @param reporter the reporter
     * @param assigned_to the assigned_to
     */
    public Ticket(final int id, final long creation_ts, final long delta_ts, final String version, final String status,
            final String resolution, final String priority, final String severity, final String reporter, final String assigned_to) {
        this.id = id;
        this.creation_ts = creation_ts;
        this.delta_ts = delta_ts;
        this.version = version;
        this.status = status;
        this.resolution = resolution;
        this.priority = priority;
        this.severity = severity;
        this.reporter = reporter;
        this.assigned_to = assigned_to;
    }

    /**
     * @return the id
     */
    public int getId() {
        return this.id;
    }

    /**
     * @param id the id to set
     */
    public void setId(final int id) {
        this.id = id;
    }

    /**
     * @return the creation_ts
     */
    public long getCreation_ts() {
        return this.creation_ts;
    }

    /**
     * @param creation_ts the creation_ts to set
     */
    public void setCreation_ts(final long creation_ts) {
        this.creation_ts = creation_ts;
    }

    /**
     * @return the delta_ts
     */
    public long getDelta_ts() {
        return this.delta_ts;
    }

    /**
     * @param delta_ts the delta_ts to set
     */
    public void setDelta_ts(final long delta_ts) {
        this.delta_ts = delta_ts;
    }

    /**
     * @return the version
     */
    public String getVersion() {
        return this.version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(final String version) {
        this.version = version;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return this.status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(final String status) {
        this.status = status;
    }

    /**
     * @return the resolution
     */
    public String getResolution() {
        return this.resolution;
    }

    /**
     * @param resolution the resolution to set
     */
    public void setResolution(final String resolution) {
        this.resolution = resolution;
    }

    /**
     * @return the priority
     */
    public String getPriority() {
        return this.priority;
    }

    /**
     * @param priority the priority to set
     */
    public void setPriority(final String priority) {
        this.priority = priority;
    }

    /**
     * @return the severity
     */
    public String getSeverity() {
        return this.severity;
    }

    /**
     * @param severity the severity to set
     */
    public void setSeverity(final String severity) {
        this.severity = severity;
    }

    /**
     * @return the reporter
     */
    public String getReporter() {
        return this.reporter;
    }

    /**
     * @param reporter the reporter to set
     */
    public void setReporter(final String reporter) {
        this.reporter = reporter;
    }

    /**
     * @return the assigned_to
     */
    public String getAssigned_to() {
        return this.assigned_to;
    }

    /**
     * @param assigned_to the assigned_to to set
     */
    public void setAssigned_to(final String assigned_to) {
        this.assigned_to = assigned_to;
    }
}
