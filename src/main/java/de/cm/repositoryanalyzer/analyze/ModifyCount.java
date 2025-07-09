/**
 * 
 */
package de.cm.repositoryanalyzer.analyze;

import de.cm.repositoryanalyzer.dataImport.repository.ChangedPath;
import de.cm.repositoryanalyzer.dataImport.repository.ModificationRequest;
import de.cm.repositoryanalyzer.gui.TreeMap.Node;

/**
 * @author Christian
 * 
 */
public class ModifyCount implements IAnalysis {

    private Node root;

    public ModifyCount() {
        super();
        this.root = new Node("root", 0);
    }

    public void analyze(final PreparedData preparedData) {

        for (ModificationRequest modificationRequest : preparedData.getPreparedData()) {
            for (ChangedPath changedPath : modificationRequest.getChangedPaths()) {
                if (changedPath.isValid()) {
                    root.add(changedPath.getPath(), changedPath.getSize());
                    root.incrementValue(changedPath.getPath());
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
