/**
 * 
 */
package test;

import java.util.Collection;
import java.util.Iterator;

import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

/**
 * @author Christian
 * 
 */
public class GetSVNData {

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {

        String url = "https://svn.spicebird.org/repos/collab/";
        String name = "";
        String password = "";
        long startRevision = 0;
        long endRevision = -1;
        int counter = 0;
        long lastRevision = 0;

        // http
        DAVRepositoryFactory.setup();

        // svn
        SVNRepositoryFactoryImpl.setup();

        // file
        // FSRepositoryFactory.setup();

        SVNRepository repository = null;

        try {
            SVNURL svnurl = SVNURL.parseURIEncoded(url);
            repository = SVNRepositoryFactory.create(svnurl);
            ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(name, password);
            repository.setAuthenticationManager(authManager);
            lastRevision = repository.getLatestRevision();
            System.out.println("Revisionen Gesamt: " + lastRevision);
            Collection<SVNLogEntry> logEntries = null;
                logEntries = repository.log(new String[] { "" }, null, startRevision, endRevision, true, true);
                System.out.println(logEntries.size());
                for (Iterator<SVNLogEntry> entries = logEntries.iterator(); entries.hasNext();) {
                    SVNLogEntry logEntry = entries.next();
                    if (logEntry != null
                            && logEntry.getMessage() != null
                            && (logEntry.getMessage().contains("Bug") || logEntry.getMessage().contains("bug")
                                    || logEntry.getMessage().contains("fix") || logEntry.getMessage().contains("#"))) {
                        counter++;
                        // System.out.println("---------------------------------------------");
                        // System.out.println("revision: " + logEntry.getRevision());
                        // System.out.println("author: " + logEntry.getAuthor());
                        // System.out.println("date: " + logEntry.getDate());
                         System.out.print("log message: " + logEntry.getMessage());
                        //
                        // if (logEntry.getChangedPaths().size() > 0) {
                        // System.out.println();
                        // System.out.println("changed paths:");
                        // Set changedPathsSet = logEntry.getChangedPaths().keySet();
                        //
                        // for (Iterator changedPaths = changedPathsSet.iterator(); changedPaths.hasNext();) {
                        // SVNLogEntryPath entryPath = (SVNLogEntryPath) logEntry.getChangedPaths().get(changedPaths.next());
                        // System.out.println(" "
                        // + entryPath.getType()
                        // + " "
                        // + entryPath.getPath()
                        // + ((entryPath.getCopyPath() != null) ? " (from " + entryPath.getCopyPath() + " revision "
                        // + entryPath.getCopyRevision() + ")" : ""));
                        // }
                        // }
                    }
                }          
            System.out.println("commits mit buginfo : " + counter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
