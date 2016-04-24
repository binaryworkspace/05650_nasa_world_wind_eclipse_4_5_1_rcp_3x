package com.binaryworkspace.rcp.wwj.resources;

import java.awt.Color;

/**
 * Provides an AWT Color for a given ColorType.
 * <p>
 * The AWT Colors can be obtained either as a solid AWT Color when no alpha
 * value is provided, or as a transparent AWT Color when an alpha value is
 * provided.
 * 
 * @author Chris Ludka
 * 
 */
public final class AwtColors {

	private AwtColors() {
		// Hidden Constructor
	}

	// Helper conversion method
	private static Color getAwtColorFromArray(int[] array) {
		return new Color(array[0], array[1], array[2], array[3]);
	}

	/**
	 * Returns a solid color AWT color. Each color channel will be bounded to
	 * the octect interval of [0, 255].
	 * 
	 * @return
	 */
	public static Color color(ColorType colorType) {
		return getAwtColorFromArray(colorType.getOctectColor());
	}

	/**
	 * Returns an AWT Color with a custom value alpha channel where 0 is
	 * transparent and 255 is solid. Each color channel will be bounded to the
	 * octect interval of [0, 255].
	 * 
	 * @return
	 */
	public static Color color(ColorType colorType, int a) {
		return getAwtColorFromArray(colorType.getOctectColorWithAlpha(a));
	}

}