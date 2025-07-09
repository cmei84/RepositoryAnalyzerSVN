/**
 * 
 */
package de.cm.repositoryanalyzer.analyze;

/**
 * @author Christian
 * 
 */
public interface IAnalysis {

    /**
     * starts the analysis.
     * 
     * @param preparedData the preparedData for the analysis
     */
    public void analyze(PreparedData preparedData);

    /**
     * 
     * @return the results for further usage
     */
    public Object getResults();
}
