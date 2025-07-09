/**
 * 
 */
package de.cm.repositoryanalyzer.dataImport.repository;

import de.cm.repositoryanalyzer.dataImport.DataImport;
import de.cm.repositoryanalyzer.dataImport.Project;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNLogEntryPath;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

/**
 * @author Christian
 * 
 */
public class SVNImport implements IRepositoryImport {

    private Project project;
    private DataImport dataImport;

    private long latestRevision;
    private long startRevision;
    private long endRevision;

    private SVNRepository repository;

    private final int importAtOnce = 1;
    private long toImportCount;
    private long finishedCount;

    private List<ModificationRequest> modificationRequests;

    /**
     * 
     * @param project the project
     * @param dataImport the dataImport
     */
    public SVNImport(final Project project, final DataImport dataImport) {
        this.project = project;
        this.dataImport = dataImport;
    }

    /**
     * @throws SVNException if error
     * 
     */
    public void init() throws SVNException {
        // http
        DAVRepositoryFactory.setup();

        // svn
        SVNRepositoryFactoryImpl.setup();

        // file
        FSRepositoryFactory.setup();

        SVNURL svnUrl = SVNURL.parseURIEncoded(project.getSvnUrl());
        repository = SVNRepositoryFactory.create(svnUrl);
        ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(project.getSvnUser(), project.getSvnPass());
        repository.setAuthenticationManager(authManager);

        latestRevision = repository.getLatestRevision();
        startRevision = project.getLastRevision();
        finishedCount = 0;
    }

    /**
     * Imports all log entries.
     * 
     * @return all modificationRequests
     */
    @SuppressWarnings("unchecked")
    public List<ModificationRequest> runImport() {
        try {
            init();
            if (startRevision < latestRevision) {

                modificationRequests = new ArrayList<ModificationRequest>();

                endRevision = -1;

                Collection<SVNLogEntry> logEntries = null;

                try {
                    logEntries = repository.log(new String[]{""}, null, startRevision, endRevision, true, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                ModificationRequest modificationRequest;

                if (logEntries != null) {
                    toImportCount = logEntries.size();

                    dataImport.notifyChange(toImportCount + " Modifikation-Requests werden importiert");

                    try {
                        for (SVNLogEntry logEntry : logEntries) {
                            modificationRequest = new ModificationRequest();
                            modificationRequest.setRevision(logEntry.getRevision());
                            modificationRequest.setAutor(logEntry.getAuthor());
                            modificationRequest.setDate(logEntry.getDate().getTime());

                            String message = logEntry.getMessage();
                            if (message != null && message.contains("'")) {
                                message = message.replace("'", "");
                            }
                            modificationRequest.setComment(message);

                            Set<String> changedPathsSet = logEntry.getChangedPaths().keySet();

                            long size = 0;

                            for (String s : changedPathsSet) {
                                SVNLogEntryPath entryPath = (SVNLogEntryPath) logEntry.getChangedPaths().get(s);

                                SVNDirEntry de = repository.info(entryPath.getPath(), modificationRequest.getRevision());

                                if (de != null) {
                                    size = de.getSize();
                                } else {
                                    size = 0;
                                }
                                modificationRequest.addChangedPath(new ChangedPath(modificationRequest.getRevision(), entryPath.getType(), entryPath.getPath(), size));
                            }

                            modificationRequests.add(modificationRequest);
                            finishedCount++;
                            dataImport.notifyChange((int) ((100.0 / toImportCount) * finishedCount));

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                dataImport.notifyChange("Keine Modifikation-Requests gefunden");
            }
            try {
                Thread.sleep(750);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return modificationRequests;
        } catch (Exception e) {
            dataImport.setImportStatus(DataImport.STATUSERROR);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 
     * @return the next start Revision to import
     */
    public synchronized long getNextStartRevision() {
        long result;
        result = startRevision;
        startRevision += importAtOnce;
        if (startRevision - importAtOnce > latestRevision) {
            result = -1;
        }
        return result;
    }

    /**
     * 
     * @param modificationRequests the modificationRequests from the treads to add
     */
    public synchronized void addModificationRequests(final List<ModificationRequest> modificationRequests) {
        this.modificationRequests.addAll(modificationRequests);
        finishedCount += importAtOnce;
        if (finishedCount > latestRevision) {
            finishedCount = latestRevision;
        }
        dataImport.notifyChange((int) ((100.0 / latestRevision) * finishedCount * 1.0));
    }

    /**
     * 
     * @return the latest rev of the repository
     * @throws SVNException if error
     */
    public long getLatestRevision() throws SVNException {
        return latestRevision;
    }

    /**
     * @return the startRevision
     */
    public long getStartRevision() {
        return this.startRevision;
    }

    /**
     * @param startRevision the startRevision to set
     */
    public void setStartRevision(final long startRevision) {
        this.startRevision = startRevision;
    }

    /**
     * @return the endRevision
     */
    public long getEndRevision() {
        return this.endRevision;
    }

    /**
     * @param endRevision the endRevision to set
     */
    public void setEndRevision(final long endRevision) {
        this.endRevision = endRevision;
    }

    /**
     * @return the repository
     */
    public SVNRepository getRepository() {
        return this.repository;
    }

    /**
     * @return the number of modificationRequests to import at once
     */
    public long getImportAtOnce() {
        return importAtOnce;
    }
}