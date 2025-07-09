/**
 * 
 */
package de.cm.repositoryanalyzer.dataImport.repository;

import java.util.List;

/**
 * @author Christian
 * 
 */
public interface IRepositoryImport {

    /**
     * runs the Repository-Import.
     * 
     * @return all Modification-Requests as ModificationRequest
     */
    public List<ModificationRequest> runImport();   
}

