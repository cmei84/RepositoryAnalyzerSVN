/**
 * 
 */
package de.cm.repositoryanalyzer.dataImport.bugs;

import de.cm.repositoryanalyzer.dataImport.DataImport;
import de.cm.repositoryanalyzer.dataImport.Project;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Observable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Christian
 * 
 */
public class BugzillaXMLImport extends Observable implements IBugImport {

    private DataImport dataImport;
    private String filename;
    private Project project;

    DocumentBuilderFactory factory;

    List<Ticket> tickets;
    Ticket ticket;

    final String tmpfileName = "000.xml";

    /**
     * @param bugurl the path to the xml file
     */
    public BugzillaXMLImport(final Project project, final String filename, final DataImport dataImport) {
        this.project = project;
        this.filename = filename;
        this.dataImport = dataImport;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.fhr.inf08.cm.bugs.IBugImport#runImport()
     */
    @Override
    public List<Ticket> runImport() {

        tickets = new ArrayList<Ticket>();
        ticket = new Ticket();

        factory = DocumentBuilderFactory.newInstance();

        try {
            // format berichtigen

            File file = new File(filename);
            BufferedReader br = new BufferedReader(new FileReader(file));

            File tmpFile = new File(tmpfileName);

            if (tmpFile.exists()) {
                tmpFile.delete();
            }

            tmpFile.deleteOnExit();

            BufferedWriter bw = new BufferedWriter(new FileWriter(tmpFile));

            boolean b = true;

            int lineCount = 0;
            final int minLine = 100000;

            for (String s; (s = br.readLine()) != null;) {
                lineCount++;
                if (s.contains("UTF-8")) {
                    s = s.replace("UTF-8", "ISO-8859-1");
                }
                if (s.contains("<thetext>")) {
                    bw.write(s.substring(0, s.indexOf("<thetext>")));
                    bw.newLine();
                    b = false;
                }
                if (s.contains("</thetext>")) {
                    s = s.substring(s.indexOf("</thetext>") + 10);
                    b = true;
                }
                if (b) {
                    bw.write(s);
                    bw.newLine();
                }
                if (s.contains("</bug>") && lineCount > minLine) {

                    lineCount = 0;
                    bw.newLine();
                    bw.append("</bugzilla>");
                    bw.close();
                    try {
                        getTickets();
                    } catch (Exception e) {
                        dataImport.setImportStatus(DataImport.STATUSERROR);
                        e.printStackTrace();
                    }

                    bw = new BufferedWriter(new FileWriter(tmpFile));
                    bw.append("<bugzilla>");
                    bw.newLine();
                }
            }
            bw.close();
            br.close();

            getTickets();

            return tickets;
        } catch (Exception e) {
            dataImport.setImportStatus(DataImport.STATUSERROR);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 
     */
    public void getTickets() {

        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(tmpfileName);

            NodeList bugs = document.getElementsByTagName("bug");

            dataImport.notifyChange("ï¿½berprï¿½fe " + bugs.getLength() + " Tickets");

            int finishedCount = 0;

            for (int i = 0; i < bugs.getLength(); i++) {
                Node node = bugs.item(i);
                if (node instanceof Element) {
                    for (int j = 0; j < bugs.item(i).getChildNodes().getLength(); j++) {
                        Node child = bugs.item(i).getChildNodes().item(j);
                        if (child.getNodeName().equals("bug_id")) {
                            ticket = new Ticket(Integer.parseInt(child.getTextContent()));
                        }
                        if (child.getNodeName().equals("creation_ts")) {
                            ticket.setCreation_ts(convertDateStringtoLong(child.getTextContent()));
                            convertDateStringtoLong(child.getTextContent());
                        }
                        if (child.getNodeName().equals("delta_ts")) {
                            ticket.setDelta_ts(convertDateStringtoLong(child.getTextContent()));
                        }
                        if (child.getNodeName().equals("version")) {
                            ticket.setVersion(child.getTextContent());
                        }
                        if (child.getNodeName().equals("bug_status")) {
                            ticket.setStatus(child.getTextContent());
                        }
                        if (child.getNodeName().equals("resolution")) {
                            ticket.setResolution(child.getTextContent());
                        }
                        if (child.getNodeName().equals("priority")) {
                            ticket.setPriority(child.getTextContent());
                        }
                        if (child.getNodeName().equals("severity")) {
                            ticket.setSeverity(child.getTextContent());
                        }
                        if (child.getNodeName().equals("reporter")) {
                            ticket.setReporter(child.getTextContent());
                        }
                        if (child.getNodeName().equals("assigned_to")) {
                            ticket.setAssigned_to(child.getTextContent());
                        }
                    }
                    if (ticket.getId() > project.getLastBugId()) {
                        tickets.add(ticket);
                    }
                }
                dataImport.notifyChange((int) ((100.0 / bugs.getLength()) * finishedCount));
            }
        } catch (Exception e) {
            dataImport.setImportStatus(DataImport.STATUSERROR);
            e.printStackTrace();
        }
    }

    /**
     * 
     * @param date the date as String
     * @return long (Date)
     */
    public long convertDateStringtoLong(final String date) {

        String dateString = date;

        int year;
        int month;
        int day;
        int hour;
        int minute;
        int second;

        year = Integer.parseInt(dateString.substring(0, 4));
        dateString = dateString.substring(5);
        month = Integer.parseInt(dateString.substring(0, 2));
        dateString = dateString.substring(3);
        day = Integer.parseInt(dateString.substring(0, 2));
        dateString = dateString.substring(3);
        hour = Integer.parseInt(dateString.substring(0, 2));
        dateString = dateString.substring(3);
        minute = Integer.parseInt(dateString.substring(0, 2));

        if (dateString.length() > 3) {
            dateString = dateString.substring(3);
            try {
                second = Integer.parseInt(dateString.substring(0, 2));
            } catch (Exception e) {
                second = 0;
            }
        } else {
            second = 0;
        }

        return new GregorianCalendar(year, month, day, hour, minute, second).getTimeInMillis();
    }
}
