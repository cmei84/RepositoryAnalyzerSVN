/*
 * TMAppletSize.java
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

import net.bouthier.treemapSwing.TMComputeSize;
import net.bouthier.treemapSwing.TMExceptionBadTMNodeKind;
import net.bouthier.treemapSwing.TMNode;

public class Size implements TMComputeSize {

    /**
     * Test if this TMComputeSize could be used with the kind of TMNode passed in parameter.
     * 
     * @param node the TMNode to test the compatibility with
     * @return <CODE>true</CODE> if this kind of node is compatible; <CODE>false</CODE> otherwise
     */
    public boolean isCompatibleWith(final TMNode node) {
        if (node instanceof Node) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns the size of the TMNode.
     * 
     * @param node we will compute the size of this TMNode
     * @return the computed size of the TMNode
     */
    public float getSize(final TMNode node) {

        float size = 0;

        if (node instanceof Node) {
            Node svnNode = (Node) node;

            if (Node.isShowALL()) {
                size = svnNode.getValue();
            } else {
                if (svnNode.getCount() > Node.getMinValue()) {
                    size = svnNode.getValue();
                } else {
                    size = 0;
                }
            }

        } else {
            throw new TMExceptionBadTMNodeKind(this, node);
        }

        return size;
    }

}
