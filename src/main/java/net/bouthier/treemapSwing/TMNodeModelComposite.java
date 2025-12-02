/*
 * TMNodeModelComposite.java
 * www.bouthier.net
 *
 * The MIT License :
 * -----------------
 * Copyright (c) 2001 Christophe Bouthier
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

package net.bouthier.treemapSwing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The TMNodeModelComposite implements the Composite design pattern for TMNodeModel. It represent a
 * TMNodeModel which is not a leaf.
 * 
 * @author Christophe Bouthier [bouthier@loria.fr]
 * @version 2.5
 */
class TMNodeModelComposite extends TMNodeModel {

    private final List<TMNodeModel> children; // children of this node
    private boolean dirtyBufC = true; // the buffered children is dirty
    private List<TMNodeModel> bufChild = Collections.emptyList(); // children buffer

    /* --- Constructor --- */

    /**
     * Constructor.
     * 
     * @param root the root of the TMNode tree
     * @param modelRoot the root of the model
     */
    TMNodeModelComposite(TMNode root, TMNodeModelRoot modelRoot) {
        this(root, null, modelRoot);
    }

    /**
     * Constructor.
     * 
     * @param node the TMNode encapsulated
     * @param parent the parent of this node
     * @param modelRoot the root of the model
     */
    TMNodeModelComposite(TMNode node, TMNodeModelComposite parent, TMNodeModelRoot modelRoot) {
        super(node, parent, modelRoot);
        this.children = new ArrayList<>();

        for (TMNode childNode : node.children()) {
            TMNodeModel child;
            if (childNode.isLeaf()) {
                child = new TMNodeModel(childNode, this, modelRoot);
            } else {
                child = new TMNodeModelComposite(childNode, this, modelRoot);
            }
            addChild(child);
        }
    }

    /* --- Tree management --- */

    /**
     * Returns the children of this node.
     * 
     * @return the children of this node
     */
    List<TMNodeModel> children() {
        return Collections.unmodifiableList(bufChild);
    }

    /**
     * Returns the non-buffered children of this node. Could only be called in a
     * TMThreadQueue thread.
     * 
     * @return the non-buffered children of this node
     */
    private List<TMNodeModel> trueChildren() {
        return Collections.unmodifiableList(children);
    }

    /**
     * Adds the TMNodeModel as a children.
     * 
     * @param child the child
     */
    private void addChild(TMNodeModel child) {
        children.add(child);
        dirtyBufC = true;
    }

    /**
     * Removes the TMNodeModel as a children.
     * 
     * @param child the child
     */
    private void removeChild(TMNodeModel child) {
        children.remove(child);
        dirtyBufC = true;
    }

    /**
     * Returns <CODE>false</CODE> as this node is an instance of TMNodeModelComposite.
     * 
     * @return <CODE>false</CODE>
     */
    boolean isLeaf() {
        return false;
    }

    /* --- Finding node --- */

    /**
     * Returns the most inner TMNodeModel which contains in its drawing area the given coordonates.
     * 
     * @param x the X coordonate
     * @param y the Y coordonate
     * @return the TMNodeModel containing thoses coordonates; <CODE>null</CODE> if there is no such
     *         TMNodeModel
     */
    TMNodeModel nodeContaining(int x, int y) {
        if (area.contains(x, y)) {
            for (TMNodeModel child : children()) {
                TMNodeModel neo = child.nodeContaining(x, y);
                if (neo != null) {
                    return neo;
                }
            }
            return this;
        } else {
            return null;
        }
    }

    /**
     * Returns the most inner TMNodeModel which contains the given TMNode. As this method works on
     * non-buffered children, it should be called only within a TMThreadQueue thread.
     * 
     * @param node the TMNode
     * @return the TMNodeModel containing this TMNode; <CODE>null</CODE> if there is no such
     *         TMNodeModel
     */
    TMNodeModel nodeContaining(TMNode node) {
        if (this.node == node) {
            return this;
        } else {
            for (TMNodeModel child : trueChildren()) {
                TMNodeModel neo = child.nodeContaining(node);
                if (neo != null) {
                    return neo;
                }
            }
            return null;
        }
    }

    /* --- Computing --- */

    /**
     * Compute the size of the node.
     * 
     * @return the size of the node
     */
    float computeSize() {
        if (dirtyS) {
            size = 0.0f;
            TMNodeModel child = null;
            for (TMNodeModel childNode : trueChildren()) {
                child = childNode;
                size += child.computeSize();
            }
            dirtyBufS = true;
            modelRoot.decrementNumberOfDirtySNodes();
            dirtyS = false;
        }
        return size;
    }

    /**
     * Compute the filling and the tooltip of the node.
     */
    void computeDrawing() {
        super.computeDrawing();
        TMNodeModel child = null;
        for (TMNodeModel childNode : trueChildren()) {
            child = childNode;
            child.computeDrawing();
        }
    }

    /**
     * Clear dirty buffers.
     */
    void clearBuffers() {
        if (dirtyBufC) {
            bufChild = new ArrayList<>(children);
            dirtyBufC = false;
        }
        super.clearBuffers();
        TMNodeModel child = null;
        for (TMNodeModel childNode : trueChildren()) {
            child = childNode;
            child.clearBuffers();
        }
    }

    /* --- Updates --- */

    /**
     * Adds the given TMNodeModel as a child of this node and updates the size and the drawing of
     * the parents.
     * 
     * @param child the new child
     */
    void newChild(TMNodeModel child) {
        addChild(child);
        setMeAndMyParentsAsDirty();
    }

    /**
     * Removes a child of this node and updates the size and the drawing of the parents.
     * 
     * @param child the lost child
     */
    void lostChild(TMNodeModel child) {
        removeChild(child);
        setMeAndMyParentsAsDirty();
    }

    /**
     * Flush the dirtyD flag for this node and its children.
     */
    void flushDraw() {
        super.flushDraw();
        TMNodeModel child = null;
        for (TMNodeModel childNode : trueChildren()) {
            child = childNode;
            child.flushDraw();
        }
    }

    /**
     * Flush the dirtyS and dirtyD flags for this node and its children.
     */
    void flushAll() {
        super.flushAll();
        TMNodeModel child = null;
        for (TMNodeModel childNode : trueChildren()) {
            child = childNode;
            child.flushAll();
        }
    }

}
