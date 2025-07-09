/**
 * 
 */
package de.cm.repositoryanalyzer.dataImport.repository;

import java.util.ArrayList;
import java.util.List;

import de.cm.repositoryanalyzer.dataImport.bugs.Ticket;

/**
 * @author Christian
 * 
 */
public class ModificationRequest {

    private long revision;
    private String autor;
    private long date;
    private String comment;
    private List<ChangedPath> changedPaths;
    private Ticket ticket;

    /**
     * default constructor.
     */
    public ModificationRequest() {
        changedPaths = new ArrayList<ChangedPath>();
        ticket = null;
    }

    /**
     * @param revision the revision
     * @param autor the autor
     * @param date the date
     * @param comment the comment
     */
    public ModificationRequest(final long revision, final String autor, final long date, final String comment) {
        super();
        this.revision = revision;
        this.autor = autor;
        this.date = date;
        this.comment = comment;
        changedPaths = new ArrayList<ChangedPath>();
        ticket = null;
    }

    /**
     * 
     * @param changedPath the changedPath to add
     */
    public void addChangedPath(final ChangedPath changedPath) {
        changedPaths.add(changedPath);
    }

    /**
     * @return the revision
     */
    public long getRevision() {
        return this.revision;
    }

    /**
     * @param revision the revision to set
     */
    public void setRevision(final long revision) {
        this.revision = revision;
    }

    /**
     * @return the autor
     */
    public String getAutor() {
        return this.autor;
    }

    /**
     * @param autor the autor to set
     */
    public void setAutor(final String autor) {
        this.autor = autor;
    }

    /**
     * @return the date
     */
    public long getDate() {
        return this.date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(final long date) {
        this.date = date;
    }

    /**
     * @return the comment
     */
    public String getComment() {
        return this.comment;
    }

    /**
     * @param comment the comment to set
     */
    public void setComment(final String comment) {
        this.comment = comment;
    }

    /**
     * @return the changedPaths
     */
    public List<ChangedPath> getChangedPaths() {
        return this.changedPaths;
    }

    /**
     * @param changedPaths the changedPaths to set
     */
    public void setChangedPaths(final List<ChangedPath> changedPaths) {
        this.changedPaths = changedPaths;
    }

    /**
     * @return the ticket
     */
    public Ticket getTicket() {
        return this.ticket;
    }

    /**
     * @param ticket the ticket to set
     */
    public void setTicket(final Ticket ticket) {
        this.ticket = ticket;
    }
}
