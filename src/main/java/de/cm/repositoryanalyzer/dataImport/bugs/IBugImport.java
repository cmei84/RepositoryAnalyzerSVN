/**
 * 
 */
package de.cm.repositoryanalyzer.dataImport.bugs;

import java.util.List;

/**
 * @author Christian
 * 
 */
public interface IBugImport {

    /**
     * runs the Bug-Import.
     * 
     * @return all Bugs as Tickets
     */
    public List<Ticket> runImport();   
}
