/**
 * 
 */
package de.cm.repositoryanalyzer.analyze;

import de.cm.repositoryanalyzer.dataImport.bugs.Ticket;
import de.cm.repositoryanalyzer.dataImport.repository.ChangedPath;
import de.cm.repositoryanalyzer.dataImport.repository.ModificationRequest;
import de.cm.repositoryanalyzer.gui.TreeMap.Node;

/**
 * @author Christian
 * 
 */
public class BugRelatedDuration implements IAnalysis {

    private Node root;

    public BugRelatedDuration() {
        super();
        this.root = new Node("root", 0);
    }

    public void analyze(final PreparedData preparedData) {

        for (ModificationRequest modificationRequest : preparedData.getPreparedData()) {

            Ticket ticket = modificationRequest.getTicket();

            for (ChangedPath changedPath : modificationRequest.getChangedPaths()) {
                if (changedPath.isValid()) {
                    root.add(changedPath.getPath(), changedPath.getSize());
                    if (ticket != null) {
                        root.setMaxValue(changedPath.getPath(), (int) ((ticket.getDelta_ts() - ticket.getCreation_ts()) / 1000), ticket.getId());
                    }
                }
            }

        }

        root.fillDirectoryValues(root);
        root.fillDirectorySize(root);
    }

    public Object getResults() {
        return root;
    }
}
