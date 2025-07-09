/**
 * 
 */
package de.cm.repositoryanalyzer.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Christian
 * 
 */
public class Config {

    public static Config config = new Config();

    private final String configFileName = "./data/config.txt";
    private final String seperator = ";";

    private List<String> bugid = new ArrayList<String>();
    private List<String> bugkeywords = new ArrayList<String>();
    private List<String> priorityValues = new ArrayList<String>();

    /**
     * Constructor.
     */
    public Config() {
        readConfigFile();
    }

    /**
     * reads the config file ans stores the values.
     */
    public void readConfigFile() {

        File file = new File(configFileName);

        if (file.exists()) {
            try {
                FileReader fr = new FileReader(file, StandardCharsets.UTF_8);
                BufferedReader br = new BufferedReader(fr);
                String line;
                while ((line = br.readLine()) != null) {

                    if (line.contains("priority")) {
                        line = line.substring(line.indexOf("=") + 1);
                        String[] parts = line.split(seperator);
                        for (String part : parts) {
                            if (part.length() > 1) {
                                priorityValues.add(part);
                            }
                        }
                    }

                    if (line.contains("bugid")) {
                        line = line.substring(line.indexOf("=") + 1);
                        String[] parts = line.split(seperator);
                        for (String part : parts) {
                            if (part.length() > 1) {
                                bugid.add(part);
                            }
                        }
                    }

                    if (line.contains("bugkeywords")) {
                        line = line.substring(line.indexOf("=") + 1);
                        String[] parts = line.split(seperator);
                        for (String part : parts) {
                            if (part.length() > 1) {
                                bugkeywords.add(part);
                            }
                        }
                    }

                }
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            // nothing found
        }
    }

    /**
     * @return the priorityValues
     */
    public List<String> getPriorityValues() {
        return this.priorityValues;
    }

    /**
     * @param priorityValues the priorityValues to set
     */
    public void setPriorityValues(List<String> priorityValues) {
        this.priorityValues = priorityValues;
    }

    /**
     * @return the bugid
     */
    public List<String> getBugid() {
        return this.bugid;
    }

    /**
     * @return the bugkeywords
     */
    public List<String> getBugkeywords() {
        return this.bugkeywords;
    }

    /**
     * @param bugid the bugid to set
     */
    public void setBugid(List<String> bugid) {
        this.bugid = bugid;
    }

    /**
     * @param bugkeywords the bugkeywords to set
     */
    public void setBugkeywords(List<String> bugkeywords) {
        this.bugkeywords = bugkeywords;
    }
}
