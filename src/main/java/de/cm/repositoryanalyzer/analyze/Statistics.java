/**
 * 
 */
package de.cm.repositoryanalyzer.analyze;

import de.cm.repositoryanalyzer.dataImport.Project;
import de.cm.repositoryanalyzer.dataImport.bugs.Ticket;
import de.cm.repositoryanalyzer.dataImport.repository.ModificationRequest;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @author Christian
 * 
 */
public class Statistics implements IAnalysis {

    private StringBuffer report;

    List<Counttype> timeWindowTicketFixAutors;

    List<Counttype> overallTicketFixAutors;

    /**
     * 
     */
    public Statistics() {
    }

    public void analyze(final PreparedData preparedData) {

        Project project = preparedData.getProject();

        /**
         * TimeWindow
         */
        ModificationRequest timeWindowModificationRequestMostPathsChanged = new ModificationRequest();
        int timeWindowModificationRequestCount = 0;
        int timeWindowAvgPathsPerModificationRequest = 0;
        int timeWindowTicketRelatedModificationRequests = 0;
        List<String> timeWindowModificationRequestAutors = new ArrayList<String>();
        int timeWindowTicketAddedCount = 0;
        timeWindowTicketFixAutors = new ArrayList<Counttype>();
        int timeWindowTicketResolvedCount = 0;

        /**
         * overall
         */
        ModificationRequest overallModificationRequestMostPathsChanged = new ModificationRequest();
        int overallModificationRequestPathsChangedAvg = 0;
        int overallModificationRequestCount = 0;
        int overallAvgPathsPerModificationRequest = 0;
        List<String> overallModificationRequestAutors = new ArrayList<String>();
        int overallTicketCount = 0;
        long overallTicketMinDuration = System.currentTimeMillis();
        overallTicketFixAutors = new ArrayList<Counttype>();
        int overallTicketResolvedCount = 0;
        int overallUnassignedTicketCount = 0;

        List<ModificationRequest> modificationRequests = preparedData.getAllModificationRequests();

        overallModificationRequestCount = modificationRequests.size();

        for (ModificationRequest m : modificationRequests) {

            if (m.getDate() >= project.getViewMin() && m.getDate() <= project.getViewMax()) {
                timeWindowModificationRequestCount++;
                if (m.getChangedPaths().size() > timeWindowModificationRequestMostPathsChanged.getChangedPaths().size()) {
                    timeWindowModificationRequestMostPathsChanged = m;
                }
                if (m.getTicket() != null) {
                    timeWindowTicketRelatedModificationRequests++;
                }

                timeWindowAvgPathsPerModificationRequest += m.getChangedPaths().size();
                timeWindowModificationRequestAutors.add(m.getAutor());
            }

            overallModificationRequestPathsChangedAvg += m.getChangedPaths().size();

            if (m.getChangedPaths().size() > overallModificationRequestMostPathsChanged.getChangedPaths().size()) {
                overallModificationRequestMostPathsChanged = m;
            }
            overallAvgPathsPerModificationRequest += m.getChangedPaths().size();
            overallModificationRequestAutors.add(m.getAutor());
        }

        if (overallModificationRequestCount > 0) {
            overallModificationRequestPathsChangedAvg /= overallModificationRequestCount;
        }

        List<Ticket> tickets = preparedData.getAllTickets();
        overallTicketCount = tickets.size();

        for (Ticket ticket : tickets) {
            long duration = ticket.getDelta_ts() - ticket.getCreation_ts();

            if (ticket.getCreation_ts() >= project.getViewMin() && ticket.getCreation_ts() <= project.getViewMax()) {
                timeWindowTicketAddedCount++;
            }

            if ((ticket.getDelta_ts() >= project.getViewMin() && ticket.getDelta_ts() <= project.getViewMax())) {

                if (ticket.getResolution().equals("FIXED")) {
                    incrementTimeWindowFixAutor(ticket.getAssigned_to());
                }
                if (ticket.getStatus().equals("RESOLVED")) {
                    timeWindowTicketResolvedCount++;
                }
            }

            if (duration < overallTicketMinDuration && duration > 0) {
                overallTicketMinDuration = duration;
            }
            if (ticket.getResolution().equals("FIXED")) {
                incrementOverallFixAutor(ticket.getAssigned_to());
            }
            if (ticket.getStatus().equals("RESOLVED")) {
                overallTicketResolvedCount++;
            }
            if (ticket.getStatus().equals("NEW")) {
                overallUnassignedTicketCount++;
            }
        }

        Counttype maxTimeWindowTicketsFixed = new Counttype("");
        for (Counttype s : timeWindowTicketFixAutors) {
            if (s.getValue() > maxTimeWindowTicketsFixed.getValue()) {
                maxTimeWindowTicketsFixed = s;
            }
        }

        Counttype maxOverallTicketsFixed = new Counttype("");
        for (Counttype s : overallTicketFixAutors) {
            if (s.getValue() > maxOverallTicketsFixed.getValue()) {
                maxOverallTicketsFixed = s;
            }
        }

        report = new StringBuffer();
        report.append("<html><center><table><tr><td>");
        report.append("<h2>Projekt:</h2></td><td><h2><i>" + project.getName() + "</i></h2></td></tr>");

        report.append("<tr><td><h3>Gewählter Zeitraum</h3></td><td></td></tr>");
        report.append("<tr><td>Modifikation-Requests:</td><td>" + timeWindowModificationRequestCount + "</td></tr>");
        report.append("<tr><td>Modifikation-Requests mit Bug-Info:</td><td>" + timeWindowTicketRelatedModificationRequests + "</td></tr>");
        report.append("<tr><td>Größte Änderung</td><td>Modifikation-Request der Revision " + timeWindowModificationRequestMostPathsChanged.getRevision() + " änderte "
                + timeWindowModificationRequestMostPathsChanged.getChangedPaths().size() + " Dateien</td></tr>");
        report.append("<tr><td>Tickets hinzugefügt:</td><td>" + timeWindowTicketAddedCount + "</td></tr>");
        report.append("<tr><td>Tickets geschlossen:</td><td>" + timeWindowTicketResolvedCount + "</td></tr>");
        report.append("<tr><td>Ticket Tendenz:</td><td>");
        int tendenz = timeWindowTicketAddedCount - timeWindowTicketResolvedCount;
        if (tendenz > 0) {
            report.append("steigend (" + tendenz + " Bugs mehr hinzugefügt als behoben)");
        } else if (tendenz == 0) {
            report.append("haltend");
        } else {
            report.append("fallend (" + tendenz + " mehr Bugs behoben als hinzugefügt)");
        }
        report.append("</td></tr>");
        report.append("<tr><td>Am meisten Tickets gefixt:</td><td>" + maxTimeWindowTicketsFixed.getName() + " (" + maxTimeWindowTicketsFixed.getValue() + ")</td></tr>");

        /**
         * overall
         */
        report.append("<tr><td><h3>Gesamtes Projekt</h3></td><td></td></tr>");
        report.append("<tr><td>Modifikation-Requests:</td><td>" + overallModificationRequestCount + "</td></tr>");
        report.append("<tr><td>Größte Änderung</td><td>Modifikation-Request der Revision " + overallModificationRequestMostPathsChanged.getRevision() + " änderte "
                + overallModificationRequestMostPathsChanged.getChangedPaths().size() + " Dateien</td></tr>");
        report.append("<tr><td>Ein Modifikation-Request änderte Durchschnittlich:</td><td>"

        + overallModificationRequestPathsChangedAvg + " Dateien</td></tr>");
        report.append("<tr><td>Tickets:</td><td>" + overallTicketCount + "</td></tr>");
        report.append("<tr><td>Tickets geschlossen:</td><td>" + overallTicketResolvedCount + "</td></tr>");
        report.append("<tr><td>Tickets offen (verbleibend):</td><td>" + (overallTicketCount - overallTicketResolvedCount) + "</td></tr>");
        report.append("<tr><td>Tickets unzugeordnet:</td><td>" + overallUnassignedTicketCount + "</td></tr>");
        report.append("<tr><td>Ticket fix Dauer Min:</td><td>" + timeToString(overallTicketMinDuration) + "</td></tr>");
        report.append("<tr><td>Am meisten Tickets gefixt:</td><td>" + maxOverallTicketsFixed.getName() + " (" + maxOverallTicketsFixed.getValue() + ")</td></tr>");

        report.append("<br>");

        report.append("</td></tr></table></center></html>");

        /**
         * text version als beispiel
         */

        // report = new StringBuffer();
        // report.append("<html><center><h2>Statistiken</h2></center><br><center><table><tr><td><left>");
        // report.append("Es wurde das Projekt " + project.getName() + "analsert.<br><br>");
        // report.append("<h4>Gewï¿½hlter Zeitraum</h4>");
        // report.append("Als Zeitraum wurde vom " + project.getViewMax() + " bis zum " + project.getViewMin() + " gewï¿½hlt.<br>");
        // report.append("In diesen Zeitraum fallen " + timeWindowModificationRequestCount + "Modifikation-Requests.");
        // report.append("Von diesen konnten " + timeWindowTicketRelatedModificationRequests
        // + " Buginformationen zugeordnet werden.<br>");
        // report.append("<Der Modifikation-Request mit der RevisionsNr.: "
        // + timeWindowModificationRequestMostPathsChanged.getRevision() + " ï¿½nderte mit "
        // + timeWindowModificationRequestMostPathsChanged.getChangedPaths().size()
        // + " Dateien die meisten im Zeitraum.<br>");
        // report.append("Die Anzahl der Tickets welche in diesem Zeitraum geschlossen wurden " + timeWindowTicketCount
        // + ".<br><br>");
        //
        // /**
        // * overall
        // */
        // report.append("<h4>Gesamtes Projekt</h4>");
        // report.append("Bei betrchtung des gesamten Projekts ergeben sich folgende Aussagen.<br>");
        // report.append("Gesamt gibt es " + overallModificationRequestCount + "Modifikation-Requests.");
        // report.append("Von diesen ï¿½nderte Modifikation-Request der Revision "
        // + overallModificationRequestMostPathsChanged.getRevision() + " mit "
        // + overallModificationRequestMostPathsChanged.getChangedPaths().size() + " Dateien die meisten.<br>");
        // report.append("Dies geschah am: " + overallModificationRequestMostPathsChanged.getDate() + ".");
        // report.append("Ein Modifikation-Request ï¿½nderte Durchschnittlich " + overallModificationRequestPathsChangedAvg
        // + " Dateien.<br>");
        // report.append("Die gesamte Anzahl der eingetragenen Fehler belï¿½uft sich auf " + overallTicketCount + ".");
        // report.append("Von diesen wurden " + overallTicketCount + " bereits geschlossen.<br>");
        // report.append("Somit verbleiben noch " + (overallTicketCount - overallTicketResolvedCount) + ".<br>");
        // report.append("Dabei dauerte die Bearbeitung eines Tickets durchschnittlich " + timeToString(overallTicketAvgDuration)
        // + ".<br>");
        // report.append("Der lï¿½mgste Fehler dauerte " + timeToString(overallTicketMaxDuration) + ".<br>");
        // report.append("Es sind noch " + overallUnassignedTicketCount
        // + " Unzugeordnete (Neue) Fehler im Bug-Tracker eingetragen.<br>");
        // report.append("");
        //
        // report.append("</left></td></tr></table></center></html>");
    }

    /**
     * @param assigned_to
     */
    private void incrementTimeWindowFixAutor(String assigned_to) {
        for (Counttype c : timeWindowTicketFixAutors) {
            if (c.getName().equals(assigned_to)) {
                c.setValue(c.getValue() + 1);
                return;
            }
        }
        timeWindowTicketFixAutors.add(new Counttype(assigned_to));
    }

    /**
     * @param assigned_to
     */
    private void incrementOverallFixAutor(String assigned_to) {
        for (Counttype c : overallTicketFixAutors) {
            if (c.getName().equals(assigned_to)) {
                c.setValue(c.getValue() + 1);
                return;
            }
        }

        overallTicketFixAutors.add(new Counttype(assigned_to));
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.fhr.inf.meister.analyse.IAnalysis#getResults()
     */
    @Override
    public Object getResults() {
        return report.toString();
    }

    /**
     * 
     * @param time
     * @return
     */
    public String timeToString(final long time) {

        String result = "";
        long timeVal = time;
        int day = 0;
        int hour = 0;
        int min = 0;
        int sek = 0;

        timeVal /= 1000;

        sek = (int) (timeVal % 60);

        timeVal /= 60;

        min = (int) (timeVal % 60);

        timeVal /= 60;

        hour = (int) (timeVal % 24);

        timeVal /= 24;

        day = (int) timeVal;

        hour++;

        if (day > 0) {
            result += day + " Tage ";
        }

        // if (hour < 10) {
        // result += "0";
        // }
        result += hour + " Stunden ";
        // if (min < 10) {
        // result += "0";
        // }
        result += min + " Minuten ";
        // if (sek < 10) {
        // result += "0";
        // }
        result += sek + " Sekunden ";

        return result;

    }

    public String timeString(final long time) {

        String result = "";

        Calendar cal = GregorianCalendar.getInstance();
        cal.setTimeInMillis(time);

        result += cal.get(Calendar.DAY_OF_MONTH) + ".";
        result += cal.get(Calendar.MONTH) + ".";
        result += cal.get(Calendar.YEAR) + " - ";
        result += cal.get(Calendar.HOUR) + ":";
        result += cal.get(Calendar.MINUTE) + ":";
        result += cal.get(Calendar.SECOND);

        return result;

    }
}

class Counttype {

    private String name;
    private int value;

    public Counttype(String name) {
        this.name = name;
        value = 1;
    }

    /**
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return the value
     */
    public int getValue() {
        return this.value;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param value the value to set
     */
    public void setValue(int value) {
        this.value = value;
    }

}
