/**
 * 
 */
package de.cm.repositoryanalyzer.dataImport.repository;

/**
 * @author Christian
 * 
 */
public class ChangedPath {

    private long revision;
    private char type;
    private String path;
    private long size;
    
    private boolean valid;

    /**
     * 
     */
    public ChangedPath() {
    }

    /**
     * 
     * @param revision the revisions number
     * @param type the type (c)reated, (m)odified, (d)eleted, (r)eplaced
     * @param path the path
     * @param size the size
     */
    public ChangedPath(final long revision, final char type, final String path, final long size) {
        super();
        this.revision = revision;
        this.type = type;
        this.path = path;
        this.size = size;
    }

    /**
     * 
     * @return the revisions number
     */
    public long getRevision() {
        return this.revision;
    }

    /**
     * 
     * @param revision the revisions number
     */
    public void setRevision(final long revision) {
        this.revision = revision;
    }

    /**
     * 
     * @return the type (c)reated, (m)odified, (d)eleted, (r)eplaced
     */
    public char getType() {
        return this.type;
    }

    /**
     * 
     * @param type the type (c)reated, (m)odified, (d)eleted, (r)eplaced
     */
    public void setType(final char type) {
        this.type = type;
    }

    /**
     * 
     * @return the path
     */
    public String getPath() {
        return this.path;
    }

    /**
     * 
     * @param path the path
     */
    public void setPath(final String path) {
        this.path = path;
    }

    /**
     * @return the size
     */
    public long getSize() {
        return this.size;
    }

    /**
     * @param size the size to set
     */
    public void setSize(long size) {
        this.size = size;
    }

    /**
     * @return the valid
     */
    public boolean isValid() {
        return this.valid;
    }

    /**
     * @param valid the valid to set
     */
    public void setValid(boolean valid) {
        this.valid = valid;
    }
}
