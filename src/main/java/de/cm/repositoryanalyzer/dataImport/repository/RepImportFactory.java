/**
 * 
 */
package de.cm.repositoryanalyzer.dataImport.repository;

import de.cm.repositoryanalyzer.dataImport.DataImport;
import de.cm.repositoryanalyzer.dataImport.Project;

/**
 * @author Christian
 * 
 */
public class RepImportFactory {

    /**
     * 
     */
    public RepImportFactory() {

    }

    /**
     * Finds and returns the matching BUG-Importer by cecking the bugUrl of the project.
     * 
     * @param project the project to import bugs
     * @param dataImport the dataImport
     * @return IBugImport implementetion
     */
    public IRepositoryImport getBugImport(final Project project, final DataImport dataImport) {

        return new SVNImport(project, dataImport);
    }
}
