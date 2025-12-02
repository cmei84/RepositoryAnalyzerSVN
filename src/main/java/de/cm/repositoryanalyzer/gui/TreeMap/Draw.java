/*
 * TMAppletDraw.java
 * www.bouthier.net
 *
 * The MIT License :
 * -----------------
 * Copyright (c) 2005 Christophe Bouthier
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package de.cm.repositoryanalyzer.gui.TreeMap;

import java.awt.Color;
import java.awt.Paint;

import javax.swing.UIManager;

import de.cm.repositoryanalyzer.analyze.DataAnalysis;

import net.bouthier.treemapSwing.TMComputeDraw;
import net.bouthier.treemapSwing.TMExceptionBadTMNodeKind;
import net.bouthier.treemapSwing.TMNode;
import net.bouthier.treemapSwing.TMNodeAdapter;

public class Draw implements TMComputeDraw {

    private int max;
    private Color color;

    /**
     * Test if this TMComputeDraw could be used with the kind of TMNode passed in parameter.
     * 
     * @param node the TMNode to test the compatibility with
     * @return <CODE>true</CODE> if this kind of node is compatible; <CODE>false</CODE> otherwise
     */
    public boolean isCompatibleWith(TMNode node) {
        if (node instanceof Node) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns the filling of the node.
     * 
     * @param nodeAdapter the node which we will draw
     * @return the filling of the node
     * @throws TMExceptionBadTMNodeKind If the kind of TMNode returned is incompatible with this TMComputeDraw.
     */
    public Paint getFilling(final TMNodeAdapter nodeAdapter) throws TMExceptionBadTMNodeKind {

        TMNode node = nodeAdapter.getNode();
        if (node instanceof Node) {
            Node fNode = (Node) node;
            int count = fNode.getCount();

            if (fNode.getName().equals("root")) {
                max = fNode.getMaxCount(fNode);
                color = UIManager.getColor("Label.background");
                try {
                    Thread.sleep(20);
                } catch (Exception e) {
                }
            }

            if (count >= Node.getFactor()) {
                int green = (int)(color.getGreen() - (color.getGreen() * 1.0 / max) * count);
                int blue = (int)(color.getBlue() - (color.getBlue() * 1.0 / max) * count);
                if (green < 0) {
                    green = 0;
                }
                if (blue < 0) {
                    blue = 0;
                }
                return new Color(color.getRed(), green, blue);
            } else {
                // return new Color(238, 243, 230);
                return color;
            }

        } else {
            throw new TMExceptionBadTMNodeKind(this, node);
        }
    }

    /**
     * Returns the tooltip of the node.
     * 
     * @param nodeAdapter the node for which we want the tooltip
     * @return the tooltip of the node
     * @throws TMExceptionBadTMNodeKind If the kind of TMNode returned is incompatible with this TMComputeDraw.
     */
    public String getTooltip(final TMNodeAdapter nodeAdapter) throws TMExceptionBadTMNodeKind {

        TMNode node = nodeAdapter.getNode();
        if (node instanceof Node) {
            Node fNode = (Node) node;

            String name = fNode.getFullName();
            int count = fNode.getCount();
            int size = fNode.getValue();

            count = count / Node.getFactor();

            String tooltip;
            tooltip = "<html>" + "Pfad: " + name + "<br>" + "Größe: " + getSizeString(size) + "<br>";
            if (Node.getAnalysis() == DataAnalysis.BUGRELATEDCOUNT) {
                if (fNode.isLeaf()) {
                    tooltip = tooltip + "Anzahl Änderungen: " + count;
                } else {
                    tooltip = tooltip + "Durchschnittliche Anzahl Änderungen: " + count;
                }
            } else if (Node.getAnalysis() == DataAnalysis.BUGRELATEDDURATION) {
                if (fNode.isLeaf()) {
                    tooltip = tooltip + "Dauer: " + timeSecToString(count) + " (Bug: " + fNode.getBugID() + ")";
                } else {
                    tooltip = tooltip + "Dauer (Durchschnitt): " + timeSecToString(count);
                }
            }
            tooltip = tooltip + "</html>";
            return tooltip;
        } else {
            throw new TMExceptionBadTMNodeKind(this, node);
        }
    }

    /**
     * 
     * @param timeSec the timeSec
     * @return
     */
    public String timeSecToString(long timeSec) {

        StringBuffer ausgabe = new StringBuffer();
        long d, h, m, s;// ms;

        if (timeSec < 0)
            timeSec = -timeSec; // Vorzeichenproblemen vorbeugen

        // ms = timeMillies % 1000; // Millisekunden der Zeit timeMillies herausfiltern
        // timeMillies /= 1000;
        s = timeSec % 60; // Sekunden der Zeit timeSec herausfiltern
        timeSec /= 60;
        m = timeSec % 60; // Minuten der Zeit timeSec herausfiltern
        timeSec /= 60;
        h = timeSec % 24; // Stunden der Zeit timeSec
        timeSec /= 24;
        d = timeSec; // Tage der Zeit timeSec

        // Tage ausgeben
        if (d > 0) {
            ausgabe.append(Long.toString(d) + " Tage ");
        }

        // Stunden ausgeben
        if (h > 0) {
            ausgabe.append(Long.toString(h) + " Stunden ");
        }

        // Minuten ausgeben
        if (m > 0) {
            if (h > 0 && m < 10)
                ausgabe.append("0");
            ausgabe.append(Long.toString(m) + " Minuten ");
        } else if (h > 0) {
            ausgabe.append("00 Minuten");
        }

        // Sekunden ausgeben
        if (ausgabe.length() > 0 && s < 10) {
            ausgabe.append("0");
        }
        ausgabe.append(Long.toString(s));// + ":";
        ausgabe.append(" Sekunden");
        // Millisekunden ausgeben
        // if (ms < 10)
        // ausgabe += "0";
        // if (ms < 100)
        // ausgabe += "0";
        // ausgabe += Long.toString(ms); // nicht benötigt

        return ausgabe.toString();
    }

    public String getSizeString(int size) {

        String result = "";
        int count = 0;

        while (size > 1024) {
            size = size / 1024;
            count++;
        }

        if (count == 0) {
            result += size + " B";
        } else if (count == 1) {
            result += size + " KB";
        } else if (count == 2) {
            result += size + " MB";
        } else if (count == 3) {
            result += size + " GB";
        } else if (count == 4) {
            result += size + " TB";
        }

        return result;
    }

    /**
     * Returns the title of the node.
     * 
     * @param nodeAdapter the node for which we want the title
     * @return the title of the node
     * @throws TMExceptionBadTMNodeKind if the kind of TMNode returned is incompatible with this TMComputeDraw.
     */
    public String getTitle(TMNodeAdapter nodeAdapter) throws TMExceptionBadTMNodeKind {

        TMNode node = nodeAdapter.getNode();
        if (node instanceof Node) {
            Node fNode = (Node) node;
            return fNode.getName();
        } else {
            throw new TMExceptionBadTMNodeKind(this, node);
        }
    }

    /**
     * Returns the color of the title of the node.
     * 
     * @param nodeAdapter the node for which we want the title
     * @return the title of the node
     * @throws TMExceptionBadTMNodeKind if the kind of TMNode returned is incompatible with this TMComputeDraw.
     */
    public Paint getTitleColor(TMNodeAdapter nodeAdapter) throws TMExceptionBadTMNodeKind {

        return Color.black;
    }

}
