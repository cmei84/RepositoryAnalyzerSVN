/**
 * 
 */
package de.cm.repositoryanalyzer.util;

import de.cm.repositoryanalyzer.analyze.DataAnalysis;
import de.cm.repositoryanalyzer.dataImport.Project;
import de.cm.repositoryanalyzer.db.SQLiteDB;

import java.util.List;

/**
 * @author Christian
 * 
 */
public class DataStore {

    public static DataStore dataStore;

    private List<Project> projects;
    private Project actualProject;

    /**
     * 
     */
    public DataStore() {
        projects = SQLiteDB.db.loadProjects();
    }

    /**
     * 
     * @return all projects
     */
    public List<Project> getProjects() {
        return projects;
    }

    /**
     * refreshes the list of Projects.
     */
    public void refreshProjects() {

        List<Project> projects = SQLiteDB.db.loadProjects();
        Project toDelete = null;
        boolean exists;

        for (Project project : this.projects) {
            exists = false;
            for (Project p : projects) {
                if (p.getName().equals(project.getName())) {
                    exists = true;
                    project.setLastRevision(p.getLastRevision());
                    project.setLastBugId(p.getLastBugId());
                    project.setProjectMin(p.getProjectMin());
                    project.setProjectMax(p.getProjectMax());
                }
            }
            if (!exists) {
                toDelete = project;
            }
        }
        if (toDelete != null) {
            this.projects.remove(toDelete);
        }
        for (Project p : projects) {
            addProject(p);
        }
    }

    /**
     * Adds a project if no project with this name already exists.
     * 
     * @param projectToAdd the project to add
     */
    public void addProject(final Project projectToAdd) {
        for (Project project : this.projects) {
            if (project.getName().equals(projectToAdd.getName())) {
                return;
            }
        }
        this.projects.add(projectToAdd);
    }

    /**
     * @return the actualProject
     */
    public Project getActualProject() {
        return actualProject;
    }

    /**
     * @param projectName the projectName to set
     */
    public void setActualProject(final String projectName) {
        for (Project project : projects) {
            if (project.getName().equals(projectName)) {
                actualProject = project;
                return;
            }
        }
        actualProject = null;
    }

    /**
     * 
     * @param dataAnalysis the {@link DataAnalysis} to add
     */
    public void addDataAnalysis(final DataAnalysis dataAnalysis) {
        actualProject.setDataAnalysis(dataAnalysis);
    }

    /**
     * 
     * @param projectName the projectName
     * @return the {@link DataAnalysis}
     */
    public DataAnalysis getDataAnalysis(final String projectName) {
        for (Project project : projects) {
            if (project.getName().equals(projectName)) {
                return project.getDataAnalysis();
            }
        }
        return null;
    }

    /**
     * 
     * @param importProject the importProject
     * @return the project
     */
    public Project getProject(final Project importProject) {
        for (Project p : projects) {
            if (p.getName().equals(importProject.getName())) {
                p.setBugUrl(importProject.getBugUrl());
                return p;
            }
        }
        return importProject;
    }

    /**
     * @return the dataAnalysis
     */
    public DataAnalysis getDataAnalysis() {
        return actualProject.getDataAnalysis();
    }
}
