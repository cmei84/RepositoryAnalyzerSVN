/**
 * 
 */
package de.cm.repositoryanalyzer.dataImport.bugs;

import de.cm.repositoryanalyzer.dataImport.DataImport;
import de.cm.repositoryanalyzer.dataImport.Project;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

/**
 * @author Christian
 * 
 * 
 */
public class BugzillaHTTPImport implements IBugImport {

    private static String tmpFileName = "tmp.xml";

    private Project project;

    private DataImport dataImport;

    private IBugImport bugzillaXMLImport;

    private List<String[]> bugIds;
    private final int loadBugCount = 100;

    private int startId;
    private int endId;
    private int bugCount;
    private int bugFinishedCount;

    private HttpClient client;
    private GetMethod get;

    private File file;
    private BufferedWriter bufferedWriter;

    /**
     * 
     * @param project the project
     * @param dataImport the dataImport
     */
    public BugzillaHTTPImport(final Project project, final DataImport dataImport) {
        this.project = project;
        startId = project.getLastBugId();
        this.dataImport = dataImport;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.fhr.inf08.cm.bugs.IBugImport#runImport()
     */
    @Override
    public List<Ticket> runImport() {

        String bugURL = project.getBugUrl();

        if (!bugURL.endsWith("/")) {
            project.setBugUrl(bugURL + "/");
        }

        client = new HttpClient();

        endId = startId + loadBugCount;
        bugCount = 0;
        bugIds = new ArrayList<String[]>();
        boolean bugsFound = true;

        try {
            while (bugsFound) {

                bugsFound = false;

                get = new GetMethod(project.getBugUrl()
                        + "buglist.cgi?query_format=advanced&field0-0-0=bug_id&type0-0-0=greaterthan&value0-0-0=" + startId
                        + "&field1-0-0=bug_id&type1-0-0=lessthan&value1-0-0=" + endId);

                startId = endId;
                endId += loadBugCount;

                client.executeMethod(get);

                InputStreamReader isr = new InputStreamReader(get.getResponseBodyAsStream());
                BufferedReader br = new BufferedReader(isr);

                StringBuffer responseBuffer = new StringBuffer();

                for (String s; (s = br.readLine()) != null;) {
                    responseBuffer.append(s);
                }

                String tmp = responseBuffer.toString();

                if (tmp != null) {
                    if (tmp.contains("submit\" id=\"long_format")) {
                        tmp = tmp.substring(tmp.indexOf("submit\" id=\"long_format"));
                    }

                    String contains = "name=\"id\" value=\"";

                    while (tmp.contains(contains)) {
                        tmp = tmp.substring(tmp.indexOf(contains) + contains.length());
                        bugIds.add(new String[]{"id", tmp.substring(0, tmp.indexOf("\""))});
                        bugsFound = true;
                        tmp = tmp.substring(5);
                    }
                }
            }
        } catch (Exception e) {
            setError();
            e.printStackTrace();
        }

        bugCount = bugIds.size();

        bugFinishedCount = 0;

        if (bugCount > 0) {

            dataImport.notifyChange("Importiere " + bugCount + " Tickets");

            try {
                if (bugIds.size() > 0) {
                    file = new File(tmpFileName);

                    if (file.exists()) {
                        file.delete();
                    }

                    file.deleteOnExit();

                    bufferedWriter = new BufferedWriter(new FileWriter(file));

                    bufferedWriter.write("<bugzilla>");
                    bufferedWriter.newLine();
                    bufferedWriter.close();

                    InputStreamReader isr;
                    BufferedReader br;
                    String url;

                    final double percent = 100.0;

                    while ((url = getNextBugsUrl()) != null) {

                        get = new GetMethod(url);

                        try {
                            client.executeMethod(get);

                            isr = new InputStreamReader(get.getResponseBodyAsStream());
                            br = new BufferedReader(isr);

                            bufferedWriter = new BufferedWriter(new FileWriter(file, true));

                            boolean write = false;

                            for (String s; (s = br.readLine()) != null;) {

                                if (s.contains("<bug>")) {
                                    write = true;
                                }
                                if (s.contains("</bugzilla>")) {
                                    s = "";
                                }
                                // delete possible error source
                                if (s.contains("<thetext>")) {
                                    bufferedWriter.append(s.substring(0, s.indexOf("<thetext>")));
                                    bufferedWriter.newLine();
                                    write = false;
                                }
                                // reduce the size of the tmpFile by deleting the attachment data
                                if (s.contains("<data encoding=")) {
                                    bufferedWriter.append(s.substring(0, s.indexOf("<data encoding=")));
                                    bufferedWriter.newLine();
                                    write = false;
                                }
                                if (s.contains("</thetext>")) {
                                    s = s.substring(s.indexOf("</thetext>") + 10);
                                    write = true;
                                }
                                if (s.contains("</data>")) {
                                    s = s.substring(s.indexOf("</data>") + 7);
                                    write = true;
                                }
                                if (write) {
                                    if (s.length() > 0) {
                                        bufferedWriter.append(s);
                                        bufferedWriter.newLine();
                                    }
                                }
                            }

                            bufferedWriter.close();

                            bugFinishedCount += loadBugCount;
                            if (bugFinishedCount > bugCount) {
                                bugFinishedCount = bugCount;
                            }
                            dataImport.notifyChange((int) ((percent / bugCount) * bugFinishedCount));

                        } catch (Exception e) {
                            setError();
                            e.printStackTrace();
                        }
                    }

                    bufferedWriter = new BufferedWriter(new FileWriter(file, true));
                    bufferedWriter.append("</bugzilla>");
                    bufferedWriter.newLine();
                    bufferedWriter.close();
                }

            } catch (Exception e) {
                setError();
                e.printStackTrace();
            }

            bugzillaXMLImport = new BugzillaXMLImport(project, tmpFileName, dataImport);

            List<Ticket> tickets = new ArrayList<Ticket>();
            tickets = bugzillaXMLImport.runImport();

            return tickets;
        } else {
            dataImport.notifyChange("Keine Tickets zum Importieren gefunden");
            try {
                Thread.sleep(750);
            } catch (Exception e) {}
            return null;
        }
    }

    /**
     * 
     * @param url the url
     * @return the host
     */
    public String getHost(final String url) {
        String host = url;
        if (host.contains("http://")) {
            host = host.replace("http://", "");
        }
        if (host.contains("https://")) {
            host = host.replace("https://", "");
        }
        if (host.contains("/")) {
            host = host.substring(0, host.indexOf("/"));
        }
        return host;
    }

    /**
     * 
     * @return the next BugUrl to load in xml format
     */
    public synchronized String getNextBugsUrl() {
        if (bugIds.size() > 0) {
            StringBuffer result = new StringBuffer();
            int counter = 0;
            result.append(project.getBugUrl() + "show_bug.cgi?ctype=xml");
            while (bugIds.size() > 0) {
                result.append("&id=" + bugIds.remove(0)[1]);
                counter++;
                if (counter >= loadBugCount) {
                    break;
                }
            }
            return result.toString();
        } else {
            return null;
        }
    }

    /**
     * 
     * @param response the response
     * @return the response without <xml/> ..., <bugzilla>, </bugzilla>
     */
    public String getOnlyBugsFromXMLResponse(final String response) {

        StringBuffer result = new StringBuffer();

        if (response.contains("<bug>")) {
            if (response.contains("</bugzilla>")) {
                result.append(response.substring(response.indexOf("<bug>"), response.indexOf("</bugzilla>")));
            } else {
                result.append(response.substring(response.indexOf("<bug>")));
            }
        }

        // delete attachment data
        while (result.indexOf("<data encoding") != -1) {
            result.delete(result.indexOf("<data encoding"), result.indexOf("</data>") + 7);
        }

        // delete possible errer source
        while (result.indexOf("<thetext>") != -1) {
            result.delete(result.indexOf("<thetext>"), result.indexOf("</thetext>") + 10);
        }

        return result.toString();
    }
    
    public void setError(){
        dataImport.setImportStatus(DataImport.STATUSERROR);
    }

}