/**
 * 
 */
package de.cm.repositoryanalyzer.dataImport;

import de.cm.repositoryanalyzer.analyze.DataAnalysis;

/**
 * @author Christian
 * 
 */
public class Project {

    private int id;
    private String name;

    private String svnUrl;
    private String svnUser;
    private String svnPass;

    private String bugUrl;
    private String bugUser;
    private String bugPass;

    private long lastRevision;
    private int lastBugId;

    private long projectMin;
    private long projectMax;
    private long viewMin;
    private long viewMax;

    private DataAnalysis dataAnalysis;

    /**
     * 
     * @param id the id
     * @param name the name
     * @param svnUrl the svnUrl
     * @param svnUser the svnUser
     * @param svnPass the svnPass
     * @param bugUrl the bugUrl
     * @param bugUser the bugUser
     * @param bugPass the bugPass
     */
    public Project(final int id, final String name, final String svnUrl, final String svnUser, final String svnPass,
            final String bugUrl, final String bugUser, final String bugPass) {
        this.id = id;
        this.name = name;
        this.svnUrl = svnUrl;
        this.svnUser = svnUser;
        this.svnPass = svnPass;
        this.bugUrl = bugUrl;
        this.bugUser = bugUser;
        this.bugPass = bugPass;
        this.dataAnalysis = null;
    }

    /**
     * 
     */
    public Project() {
        this.dataAnalysis = null;
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
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * @param name the name to set
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @return the svnUrl
     */
    public String getSvnUrl() {
        return this.svnUrl;
    }

    /**
     * @param svnUrl the svnUrl to set
     */
    public void setSvnUrl(final String svnUrl) {
        this.svnUrl = svnUrl;
        this.name = svnUrl;
        // if (this.name.contains("//")) {
        // this.name = this.name.substring(this.name.indexOf("//") + 2);
        // }
        // if (this.name.contains("/")) {
        // this.name = this.name.substring(0, this.name.indexOf("/"));
        // }
    }

    /**
     * @return the svnUser
     */
    public String getSvnUser() {
        return this.svnUser;
    }

    /**
     * @param svnUser the svnUser to set
     */
    public void setSvnUser(final String svnUser) {
        this.svnUser = svnUser;
    }

    /**
     * @return the svnPass
     */
    public String getSvnPass() {
        return this.svnPass;
    }

    /**
     * @param svnPass the svnPass to set
     */
    public void setSvnPass(final String svnPass) {
        this.svnPass = svnPass;
    }

    /**
     * @return the bugUrl
     */
    public String getBugUrl() {
        return this.bugUrl;
    }

    /**
     * @param bugUrl the bugUrl to set
     */
    public void setBugUrl(final String bugUrl) {
        this.bugUrl = bugUrl;
    }

    /**
     * @return the bugUser
     */
    public String getBugUser() {
        return this.bugUser;
    }

    /**
     * @param bugUser the bugUser to set
     */
    public void setBugUser(final String bugUser) {
        this.bugUser = bugUser;
    }

    /**
     * @return the bugPass
     */
    public String getBugPass() {
        return this.bugPass;
    }

    /**
     * @param bugPass the bugPass to set
     */
    public void setBugPass(final String bugPass) {
        this.bugPass = bugPass;
    }

    /**
     * @return the dataAnalysis
     */
    public DataAnalysis getDataAnalysis() {
        return this.dataAnalysis;
    }

    /**
     * @param dataAnalysis the dataAnalysis to set
     */
    public void setDataAnalysis(final DataAnalysis dataAnalysis) {
        this.dataAnalysis = dataAnalysis;
    }

    /**
     * @return the min
     */
    public long getProjectMin() {
        return this.projectMin;
    }

    /**
     * @return the max
     */
    public long getProjectMax() {
        return this.projectMax;
    }

    /**
     * @param min the min to set
     */
    public void setProjectMin(final long min) {
        this.projectMin = min;
    }

    /**
     * @param max the max to set
     */
    public void setProjectMax(final long max) {
        this.projectMax = max;
    }

    /**
     * @return the viewMin
     */
    public long getViewMin() {
        return this.viewMin;
    }

    /**
     * @return the viewMax
     */
    public long getViewMax() {
        return this.viewMax;
    }

    /**
     * @param viewMin the viewMin to set
     */
    public void setViewMin(final long viewMin) {
        this.viewMin = viewMin;
    }

    /**
     * @param viewMax the viewMax to set
     */
    public void setViewMax(final long viewMax) {
        this.viewMax = viewMax;
    }

    /**
     * @return the lastRevision
     */
    public long getLastRevision() {
        if (lastRevision > 0) {
            return lastRevision + 1;
        }
        return this.lastRevision;
    }

    /**
     * @return the lastBugId
     */
    public int getLastBugId() {
        if (lastBugId > 0) {
            return lastBugId + 1;
        }
        return this.lastBugId;
    }

    /**
     * @param lastRevision the lastRevision to set
     */
    public void setLastRevision(final long lastRevision) {
        this.lastRevision = lastRevision;
    }

    /**
     * @param lastBugId the lastBugId to set
     */
    public void setLastBugId(final int lastBugId) {
        this.lastBugId = lastBugId;
    }
}
