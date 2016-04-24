package com.binaryworkspace.rcp.wwj.resources;

import java.awt.Color;

import com.binaryworkspace.rcp.wwj.util.ColorUtils;

/**
 * Provides an AWT Color sampled from a predefined gradient color schemes. A
 * color sample based on a Polynomial Least Squares Regression Fit from a
 * predefined gradient will be return when a percentage value on the Decimal
 * interval [0, 1] is provided.
 * <p>
 * The AWT Colors can be obtained either as a solid AWT Color when no alpha
 * value is provided, or as a transparent AWT Color when an alpha value is
 * provided.
 * 
 * @author Chris Ludka
 * 
 */
public class AwtColorScheme {

	private AwtColorScheme() {
		// Hidden Constructor
	}
	
	/**
	 * Returns a solid color. Each color channel will be bounded to the octect
	 * interval of [0, 255].
	 * 
	 * @return
	 */
	public static Color getVisibleSpectrum(float percentage) {
		// Ensure percentage is bounded to the interval [0.0, 1.0]
		double x = ColorUtils.getBoundedDecimal(percentage);

		// Get Math.pow for x
		double x2 = Math.pow(x, 2.0);
		double x3 = Math.pow(x, 3.0);
		double x4 = Math.pow(x, 4.0);

		// Obtain color channels
		int[] result = new int[4];

		/*
		 * Red Octect Channel: 1.85813 + 1459.45 x - 9472.85 x^2 + 18540.8 x^3 -
		 * 10572.3 x^4
		 * 
		 * Value will be returned as an Octect that must be then bounded on the
		 * interval [0, 255].
		 */
		double value = 1.85813 + 1459.45 * x - 9472.85 * x2 + 18540.8 * x3 - 10572.3 * x4;
		result[0] = ColorUtils.getBoundedOctect(Math.round((float) value));

		/*
		 * Green Channel: 18.673 - 911.549 x + 7267.87 x^2 - 12198.1 x^3 +
		 * 5826.59 x^4, bounded on the interval [0, 1].
		 */
		value = 18.673 - 911.549 * x + 7267.87 * x2 - 12198.1 * x3 + 5826.59 * x4;
		result[1] = ColorUtils.getBoundedOctect(Math.round((float) value));

		/*
		 * Blue Channel: -43.5676 + 3460.92 x - 13727.5 x^2 + 18104.7 x^3 -
		 * 7826.49 x^4, bounded on the interval [0, 1].
		 */
		value = -43.5676 + 3460.92 * x - 13727.5 * x2 + 18104.7 * x3 - 7826.49 * x4;
		result[2] = ColorUtils.getBoundedOctect(Math.round((float) value));

		// Alpha channel
		result[3] = 255;

		// Return result
		return new Color(result[0], result[1], result[2], result[3]);
	}

	/**
	 * Returns a color with a custom value alpha channel where 0 is transparent
	 * and 255 is solid. Each color channel will be bounded to the octect
	 * interval of [0, 255].
	 * 
	 * @return
	 */
	public static Color getVisibleSpectrumWithAlpha(float percentage, int a) {
		// Get non-alpha valued result
		Color result = getVisibleSpectrum(percentage);

		// Validate that the provided alpha value is a bounded octect
		int aBounded = ColorUtils.getBoundedOctect(a);

		// Return result
		return new Color(result.getRed(), result.getGreen(), result.getBlue(), aBounded);
	}
}
