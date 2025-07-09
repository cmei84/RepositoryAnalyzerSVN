/*
 * TMXMLNode.java
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

import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import net.bouthier.treemapSwing.TMNode;
import net.bouthier.treemapSwing.TMUpdater;

/**
 * TMXMLNode implements an TMNode read from an XML file.
 */
public class Node implements TMNode {

    private static boolean showAll;
    private static int factor = 5;
    private static int analysis;
    private static int minValue;

    private String name; // name of the node
    private int value; // the value
    private int count; // needed for color
    private int bugID;
    private Vector<Node> children; // children of the node
    private Node parent;

    /* --- Constructor --- */

    /**
     * Constructor, taking the name and the value.
     * 
     * @param name name
     * @param value value
     */
    public Node(final String name, final int value) {
        children = new Vector<Node>();
        this.name = name;
        this.value = value;
        this.parent = null;
    }

    /* --- Accessors --- */

    /**
     * Returns the name of this node.
     * 
     * @return the name of this node
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the type of this node.
     * 
     * @return the type of this node
     */
    public int getValue() {
        if (value == 0) {
            return 1;
        } else {
            return value;
        }
    }

    /* --- Tree management --- */

    /**
     * Add child to the node.
     * 
     * @param child the TMXMLNode to add as a child
     */
    public void addChild(final Node child) {
        children.add(child);
    }

    /* --- TMNode --- */

    /**
     * Returns the children of this node in an Enumeration.
     * 
     * @return an Enumeration containing childs of this node
     */
    public Enumeration<Node> children() {
        return children.elements();
    }

    /**
     * Checks if this node is a leaf or not. Returns always <CODE>false</CODE> here.
     * 
     * @return <CODE>true</CODE> if this node is a leaf; <CODE>false</CODE> otherwise
     */
    public boolean isLeaf() {
        if (children == null || children.isEmpty() || children.size() < 1) {
            return true;
        }
        return false;
    }

    /**
     * Called by the TMUpdater constructor. Gives to this node a reference to a TMUpdater object. Do nothing here.
     * 
     * @param updater the TMUpdater to be called when something has changed
     */
    public void setUpdater(final TMUpdater updater) {
    }

    /**
     * 
     * @param s string
     * @param size the size
     * @return the Node
     */
    public Node addChild(final String s, final long size) {
        if (!isLeaf()) {
            for (Node node : children) {
                if (node.getName().equals(s)) {
                    node.setValue((int) size);
                    return node;
                }
            }
        }
        Node node = new Node(s, (int) size);
        node.setParent(this);
        addChild(node);
        return node;
    }

    /**
     * 
     * @param path the path
     * @param size the size
     */
    public void add(final String path, final long size) {
        String[] parts = path.split("/");
        Node node = this;
        for (String part : parts) {
            if (part != null && part.length() > 0) {
                node = node.addChild(part, size);
            }
        }
    }

    /**
     * 
     * @param path the path
     */
    public void incrementValue(final String path) {
        String[] teile = path.split("/");
        Node node = this;
        String teil;
        for (int i = 0; i < teile.length; i++) {
            teil = teile[i];
            if (teil != null && teil.length() > 0) {
                node = node.getChild(teil);
                if (node.isLeaf()) {
                    // increment Value
                    node.incrementValue();
                }
            }
        }
    }

    /**
     * 
     */
    private void incrementValue() {
        count = count + factor;
    }

    /**
     * 
     * @param path the path
     * @param count the count
     */
    public void setMaxValue(final String path, final int count, final int id) {
        String[] teile = path.split("/");
        Node node = this;
        String teil;
        for (int i = 0; i < teile.length; i++) {
            teil = teile[i];
            if (teil != null && teil.length() > 0) {
                node = node.getChild(teil);
            }
        }

        // set max value
        if (node.isLeaf()) {
            node.setCount(Math.max(node.getCount(), count));
            node.setBugID(id);
        }
    }

    /**
     * 
     * @param name the name
     * @return the child or null if not found
     */
    public Node getChild(final String name) {
        for (Node child : children) {
            if (child.getName().equals(name)) {
                return child;
            }
        }
        return null;
    }

    /**
     * 
     * @return all children as sublist
     */
    public List<Node> getChildren() {
        return children.subList(0, children.size());
    }

    /**
     * 
     * @param value the value
     */
    public void setValue(final int value) {
        this.value = value;
    }

    /**
     * 
     * @param node the root node for max value search
     * @return max value of the tree starting by given node
     */
    public int getMaxCount(final Node node) {
        int result = 0;
        if (node.isLeaf()) {
            return node.getCount();
        } else {
            for (Node child : node.children) {
                result = Math.max(result, getMaxCount(child));
            }
        }
        return result;
    }

    /**
     * Fills the Values of the nonLeaf Nodes.
     * 
     * @param node the node
     * @return avg value from childs
     */
    public int fillDirectoryValues(final Node node) {
        if (node.isLeaf()) {
            return node.getCount();
        } else {
            int avg = 0;
            for (Node child : node.getChildren()) {
                avg += fillDirectoryValues(child);
            }
            avg = avg / node.getChildren().size();
            node.setCount(avg);
            return avg;
        }
    }

    /**
     * Fills the Values of the nonLeaf Nodes.
     * 
     * @param node the node
     * @return avg value from childs
     */
    public int fillDirectorySize(final Node node) {
        if (node.isLeaf()) {
            return node.getValue();
        } else {
            int size = 0;
            for (Node child : node.getChildren()) {
                size += fillDirectorySize(child);
            }
            node.setValue(size);
            return size;
        }
    }

    /**
     * 
     * @return full name
     */
    public String getFullName() {
        Node node = this;
        String fullName = this.name;
        while (!node.name.equals("root")) {
            node = node.getParent();
            if (!node.getName().equals("root")) {
                fullName = node.getName() + "\\" + fullName;
            }
        }

        return fullName;
    }

    /**
     * 
     * @return parent
     */
    public Node getParent() {
        return parent;
    }

    /**
     * 
     * @param parent parent node
     */
    public void setParent(final Node parent) {
        this.parent = parent;
    }

    /**
     * @param val the val to set
     */
    public static void setShowAll(final boolean val) {
        showAll = val;
    }

    /**
     * 
     * @return showAll
     */
    public static boolean isShowALL() {
        return showAll;
    }

    public static void setMinValue(final int value) {
        minValue = value;
    }

    public static int getMinValue() {
        return minValue;
    }

    /**
     * 
     * @return factor
     */
    public static int getFactor() {
        return factor;
    }

    /**
     * @return the analysis
     */
    public static int getAnalysis() {
        return analysis;
    }

    /**
     * @param analysis the analysis to set
     */
    public static void setAnalysis(final int analysis) {
        Node.analysis = analysis;
    }

    /**
     * @return the count
     */
    public int getCount() {
        return this.count;
    }

    /**
     * @param count the count to set
     */
    public void setCount(final int count) {
        this.count = count;
    }

    /**
     * @return the bugID
     */
    public int getBugID() {
        return this.bugID;
    }

    /**
     * @param bugID the bugID to set
     */
    public void setBugID(int bugID) {
        this.bugID = bugID;
    }
}
