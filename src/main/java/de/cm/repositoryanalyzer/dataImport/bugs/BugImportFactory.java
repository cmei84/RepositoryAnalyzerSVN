/**
 * 
 */
package de.cm.repositoryanalyzer.dataImport.bugs;

import de.cm.repositoryanalyzer.dataImport.DataImport;
import de.cm.repositoryanalyzer.dataImport.Project;

/**
 * @author Christian
 * 
 */
public class BugImportFactory {

    /**
     * 
     */
    public BugImportFactory() {

    }

    /**
     * Finds and returns the matching BUG-Importer by cecking the bugUrl of the project.
     * 
     * @param project the project to import bugs
     * @param dataImport the dataImport
     * @return IBugImport implementetion
     */
    public IBugImport getBugImport(final Project project, final DataImport dataImport) {

        final int minBugUrlLength = 4;

        String bugUrl = project.getBugUrl();
        if (bugUrl.length() > minBugUrlLength) {
            if (bugUrl.toLowerCase().contains(".xml")) {
                return new BugzillaXMLImport(project, bugUrl, dataImport);
            } else {
                return new BugzillaHTTPImport(project, dataImport);
            }
        } else {
            return null;
        }
    }
}
