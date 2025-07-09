/**
 * 
 */
package de.cm.repositoryanalyzer.gui;

import info.clearthought.layout.TableLayout;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

/**
 * @author Christian
 * 
 */
public class ProgressPanel implements Observer {

    private JPanel panel;

    private int operationCount;
    private int currentOperation;
    private JLabel headline;
    private JLabel currentProgressMessage;
    private JLabel currentProgress;
    private JProgressBar currentProgressBar;
    private JLabel overallProgressMessage;
    private JLabel overallProgress;
    private JProgressBar overallProgressBar;
    private JLabel remainingTimeLabel;

    private long startTime;
    private long remainingTime;

    private final int minProgress = 0;
    private final int maxProgress = 100;

    /**
     * @param operationCount the number of Operations in the process
     */
    public ProgressPanel(final int operationCount) {
        this.operationCount = operationCount;

        final int lineHeight = 20;
        final int lineWidth = 500;
        final int SpaceBetweenLines = 5;
        final double currentProgressFactor = 0.1;

        double size[][] = {
                {TableLayout.FILL, lineWidth, (int) (lineWidth * currentProgressFactor), TableLayout.FILL},
                {TableLayout.FILL, lineHeight, SpaceBetweenLines, lineHeight, SpaceBetweenLines, lineHeight, SpaceBetweenLines, lineHeight, SpaceBetweenLines, lineHeight,
                        lineHeight, SpaceBetweenLines, lineHeight, TableLayout.FILL, TableLayout.FILL}};

        this.panel = new JPanel();
        this.panel.setLayout(new TableLayout(size));

        headline = new JLabel("Aktuelle Aufgabe:");
        currentProgressMessage = new JLabel();
        currentProgress = new JLabel("0 %");
        currentProgress.setHorizontalAlignment(JLabel.RIGHT);
        currentProgressBar = new JProgressBar(minProgress, maxProgress);
        overallProgressMessage = new JLabel("Gesamtfortschritt:");
        overallProgress = new JLabel("0 %");
        overallProgress.setHorizontalAlignment(JLabel.RIGHT);
        overallProgressBar = new JProgressBar(minProgress, maxProgress);
        remainingTimeLabel = new JLabel("Verbleibend: ");

        this.panel.add(headline, "1,1");
        this.panel.add(currentProgressMessage, "1,3");
        this.panel.add(currentProgress, "2,3");
        this.panel.add(currentProgressBar, "1,5,2,5");
        this.panel.add(remainingTimeLabel, "1,7,2,7");
        this.panel.add(overallProgressMessage, "1,10");
        this.panel.add(overallProgress, "2,10");
        this.panel.add(overallProgressBar, "1,12,2,12");

        startTime = System.currentTimeMillis();
        new TimeReduceThread(1000, this).start();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    @Override
    public void update(final Observable o, final Object message) {
        if (message instanceof String && !((String) message).contains("finished")) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    currentProgressMessage.setText((String) message + "...");
                    currentOperation++;
                    int overall = (maxProgress / operationCount) * currentOperation;
                    startTime = System.currentTimeMillis();
                    overallProgress.setText(overall + " %");
                    overallProgressBar.setValue(overall);
                    currentProgressBar.setValue(0);
                    currentProgress.setText("0 %");
                }
            });
        } else if (message instanceof Integer) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    int oneOperation = maxProgress / operationCount;
                    int current = (Integer) message;
                    int overall = oneOperation * currentOperation + (int) (oneOperation * 1.0 * (current / 100.0));
                    currentProgress.setText(current + " %");
                    currentProgressBar.setValue(current);
                    overallProgress.setText(overall + " %");
                    overallProgressBar.setValue(overall);
                    getTimeString(current);
                }
            });
        }
        this.panel.repaint();
    }

    /**
     * 
     * @return the JPanel
     */
    public JPanel getPanel() {
        return panel;
    }

    /**
     * 
     * @param current current progress
     * @return remaining time
     */
    public void getTimeString(final int current) {
        long time = System.currentTimeMillis() - startTime;
        if (current == 0) {
            time = (long) ((100 - current) * (time));
        } else {
            time = (long) ((100 - current) * (time / current));
        }
        setRemainingTime(time);
    }

    public String timeMilliesToString(long timeMillies) {

        StringBuffer ausgabe = new StringBuffer();
        long h, m, s;// ms;

        if (timeMillies < 0)
            timeMillies = -timeMillies; // Vorzeichenproblemen vorbeugen

        // ms = timeMillies % 1000; // Millisekunden der Zeit timeMillies herausfiltern
        timeMillies /= 1000;
        s = timeMillies % 60; // Sekunden der Zeit timeMillies herausfiltern
        timeMillies /= 60;
        m = timeMillies % 60; // Minuten der Zeit timeMillies herausfiltern
        timeMillies /= 60;
        h = timeMillies; // Stunden der Zeit timeMillies

        // Stunden ausgeben
        if (h > 0)
            ausgabe.append(Long.toString(h) + " Stunden ");

        // Minuten ausgeben
        if (m > 0) {
            ausgabe.append(Long.toString(m) + " Minuten ");
        } else if (h > 0)
            ausgabe.append("0 Minuten ");

        // Sekunden ausgeben
        ausgabe.append(Long.toString(s) + " Sekunden");// + ":";

        // Millisekunden ausgeben
        // if (ms < 10)
        // ausgabe += "0";
        // if (ms < 100)
        // ausgabe += "0";
        // ausgabe += Long.toString(ms); // nicht benï¿½tigt

        return ausgabe.toString();
    }

    /**
     * @return the remainingTime
     */
    public long getRemainingTime() {
        return this.remainingTime;
    }

    /**
     * @param remainingTime the remainingTime to set
     */
    public synchronized void setRemainingTime(final long remainingTime) {
        if (remainingTime > 0) {
            this.remainingTime = remainingTime;
            remainingTimeLabel.setText("Verbleibend: " + timeMilliesToString(remainingTime));
        }
    }
}

/**
 * 
 * @author Christian
 * 
 */
class TimeReduceThread extends Thread {

    private int intervall;
    private ProgressPanel panel;

    /**
     * 
     * @param intervall the intervall
     * @param panel the Panel
     */
    public TimeReduceThread(final int intervall, final ProgressPanel panel) {
        this.intervall = intervall;
        this.panel = panel;
    }

    /**
     * 
     */
    public void run() {

        while (true) {

            try {
                sleep(intervall);
            } catch (Exception e) {
                if (e instanceof InterruptedException) {
                    return;
                }
            }
            panel.setRemainingTime(panel.getRemainingTime() - intervall);
        }
    }
}
