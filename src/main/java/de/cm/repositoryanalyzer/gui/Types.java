/**
 * 
 */
package de.cm.repositoryanalyzer.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.jvnet.flamingo.common.icon.ResizableIcon;

/**
 * @author Christian
 * 
 */
public class Types {

    private final int border = 2;
    private final int freeSpace = 5;
    private final int textfieldHeight = 23;
    private final int textfieldWidth = 145;
    private final int infoIconWidth = 20;
    private final int ribbonTaskHeight = 112; // Ribbon Task heigth in px

    private final Dimension dimension = new Dimension(0, ribbonTaskHeight);

    private final double size[][] = {{border, textfieldWidth, freeSpace, textfieldWidth, border},
            {border, textfieldHeight, freeSpace, textfieldHeight, freeSpace, textfieldHeight, border}};

    private final double infoSize[][] = {{border, infoIconWidth, (textfieldWidth * 3) - (textfieldWidth / 2), border},
            {border, textfieldHeight, freeSpace, textfieldHeight * 2 + freeSpace + freeSpace, border}};

    // Icons
    private final String infoIconPath = "icons/info.20.png";

    /**
     * 
     */
    public Types() {
    }

    /**
     * 
     * @param pathToIcon the path to the Icon to paint
     * @return the ResizableIcon
     */
    public ResizableIcon getIcon(final String pathToIcon) {
        return new ResizableIcon() {

            final int defaultWidth = 64;
            final int defaultheight = 64;

            int width = defaultWidth;
            int height = defaultheight;

            @Override
            public void setDimension(final Dimension newDimension) {
                setWidth(newDimension.width);
                setHeight(newDimension.height);
            }

            public void setHeight(final int height) {
                this.height = height;
            }

            public void setWidth(final int width) {
                this.width = width;
            }

            @Override
            public int getIconHeight() {
                return height;
            }

            @Override
            public int getIconWidth() {
                return width;
            }

            @Override
            public void paintIcon(final Component c, final Graphics g, final int x, final int y) {
                try {
                    BufferedImage img;
                    img = ImageIO.read(getClass().getResourceAsStream("/" + pathToIcon));
                    g.drawImage(img, x, y, getIconHeight(), getIconWidth(), null);
                } catch (Exception e) {
                    // nothing to paint
                }
            }
        };
    }

    /**
     * 
     * @param pathToIcon the path to the Icon to paint
     * @return the Image
     */
    public Image getImage(final String pathToIcon) {
        try {
            BufferedImage image;
			image = ImageIO.read(getClass().getResourceAsStream("/" + pathToIcon));
            return image;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @return the border
     */
    public int getBorder() {
        return this.border;
    }

    /**
     * @return the freeSpace
     */
    public int getFreeSpace() {
        return this.freeSpace;
    }

    /**
     * @return the textfieldHeight
     */
    public int getTextfieldHeight() {
        return this.textfieldHeight;
    }

    /**
     * @return the textfieldWidth
     */
    public int getTextfieldWidth() {
        return this.textfieldWidth;
    }

    /**
     * @return the infoIconWidth
     */
    public int getInfoIconWidth() {
        return this.infoIconWidth;
    }

    /**
     * @return the ribbonTaskHeight
     */
    public int getRibbonTaskHeight() {
        return this.ribbonTaskHeight;
    }

    /**
     * @return the dimension
     */
    public Dimension getDimension() {
        return this.dimension;
    }

    /**
     * @return the size
     */
    public double[][] getSize() {
        return this.size;
    }

    /**
     * @return the infoSize
     */
    public double[][] getInfoSize() {
        return this.infoSize;
    }

    /**
     * @return the infoIconPath
     */
    public String getInfoIconPath() {
        return this.infoIconPath;
    }
}
