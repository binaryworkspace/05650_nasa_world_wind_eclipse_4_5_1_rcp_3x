package com.binaryworkspace.rcp.wwj.models;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * Provides an image of a solid color surface.
 * 
 * @author Chris Ludka
 * 
 */
public class SolidColorSurfaceModel {
	/**
	 * Provides an image of a solid color surface.
	 * 
	 * @param width
	 * @param height
	 * @param Color
	 * @return
	 */
	public BufferedImage getBufferedImage(int width, int height, Color color) {
		// Build image
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		for (int yIndex = 0; yIndex < height; yIndex++) {
			for (int xIndex = 0; xIndex < width; xIndex++) {
				image.setRGB(xIndex, yIndex, color.getRGB());
			}
		}
		return image;
	}
}
