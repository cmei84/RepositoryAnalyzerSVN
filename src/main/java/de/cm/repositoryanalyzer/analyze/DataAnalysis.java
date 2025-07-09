/**
 * 
 */
package de.cm.repositoryanalyzer.analyze;

import de.cm.repositoryanalyzer.gui.AnalysisComponent;
import de.cm.repositoryanalyzer.gui.TreeMap.Node;
import de.cm.repositoryanalyzer.util.DataStore;

import java.util.Observable;

/**
 * @author Christian
 * 
 */
public class DataAnalysis extends Observable implements Runnable {

    public static final int BUGRELATEDCOUNT = 0;
    public static final int BUGRELATEDDURATION = 1;
    public static final int STATISTICS = 2;

    private PreparedData prepareData;

    private IAnalysis analyzeBugRelatedCount;
    private IAnalysis analyzeBugRelatedDuration;
    private IAnalysis analyzeStatistics;

    private boolean doBugRelatedCount;
    private boolean doBugRelatedDuration;
    private boolean doStatistics;

    /**
     * @param analysisComponent the analysisComponent
     */
    public DataAnalysis(final AnalysisComponent analysisComponent) {
        String analysisTypes = analysisComponent.getAnalysisTypes();

        doBugRelatedCount = false;
        doBugRelatedDuration = false;
        doStatistics = false;

        if (analysisTypes != null && analysisTypes.length() > 0) {
            if (analysisTypes.charAt(BUGRELATEDCOUNT) == '1') {
                doBugRelatedCount = true;
            }
            if (analysisTypes.charAt(BUGRELATEDDURATION) == '1') {
                doBugRelatedDuration = true;
            }
            if (analysisTypes.charAt(STATISTICS) == '1') {
                doStatistics = true;
            }
        }
    }

    /**
     * 
     */
    public void run() {
        startAnalysis();
    }

    /**
     * Runs the import.
     * 
     */
    public void startAnalysis() {
        try {
            setChanged();
            notifyObservers("Daten fÃ¼r die Analyse vorbereiten");

            prepareData = new PreparedData(DataStore.dataStore.getActualProject(), this);
            prepareData.prepareData();

            if (doBugRelatedCount) {
                setChanged();
                notifyObservers("Analysiere die durch Bugs verursachten Ã„nderungen");
                // Thread.sleep(500);

                // analyzeBugRelatedCount = new BugRelatedCount();
                analyzeBugRelatedCount = new ModifyCount();
                analyzeBugRelatedCount.analyze(prepareData);
            }

            if (doBugRelatedDuration) {
                setChanged();
                notifyObservers("Analysiere die Dauer der kritischen Bugs");
                // Thread.sleep(500);

                analyzeBugRelatedDuration = new BugRelatedDuration();
                analyzeBugRelatedDuration.analyze(prepareData);
            }

            if (doStatistics) {
                setChanged();
                notifyObservers("Erstelle Statistiken");
                // Thread.sleep(500);

                analyzeStatistics = new Statistics();
                analyzeStatistics.analyze(prepareData);

            }

            setChanged();
            notifyObservers("analysis finished");
            // Thread.sleep(400);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     * @param progress the progress of the work unit
     */
    public void notifyChange(final int progress) {
        setChanged();
        notifyObservers(new Integer(progress));
    }

    /**
     * @param analysis the analysis
     * @return the bugRelatedCount result
     */
    public Object getResult(final int analysis) {
        Object result = null;
        if (analysis == BUGRELATEDCOUNT) {
            result = analyzeBugRelatedCount.getResults();
        }
        if (analysis == BUGRELATEDDURATION) {
            result = analyzeBugRelatedDuration.getResults();
        }
        if (analysis == STATISTICS) {
            result = analyzeStatistics.getResults();
        }
        Node.setAnalysis(analysis);
        return result;
    }

    /**
     * @return the doBugRelatedCount
     */
    public boolean isDoBugRelatedCount() {
        return this.doBugRelatedCount;
    }

    /**
     * @param doBugRelatedCount the doBugRelatedCount to set
     */
    public void setDoBugRelatedCount(final boolean doBugRelatedCount) {
        this.doBugRelatedCount = doBugRelatedCount;
    }

    /**
     * @return the doBugRelatedDuration
     */
    public boolean isDoBugRelatedDuration() {
        return this.doBugRelatedDuration;
    }

    /**
     * @param doBugRelatedDuration the doBugRelatedDuration to set
     */
    public void setDoBugRelatedDuration(final boolean doBugRelatedDuration) {
        this.doBugRelatedDuration = doBugRelatedDuration;
    }

    /**
     * @return the doStatistics
     */
    public boolean isDoStatistics() {
        return this.doStatistics;
    }

    /**
     * @param doStatistics the doStatistics to set
     */
    public void setDoStatistics(final boolean doStatistics) {
        this.doStatistics = doStatistics;
    }
}
