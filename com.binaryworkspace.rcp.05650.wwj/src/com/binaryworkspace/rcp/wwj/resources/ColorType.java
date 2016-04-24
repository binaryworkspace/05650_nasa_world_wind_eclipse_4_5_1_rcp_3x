package com.binaryworkspace.rcp.wwj.resources;

import com.binaryworkspace.rcp.wwj.util.ColorUtils;

/**
 * Provides centralized access to a Enumeration of predefined colors.
 * <p>
 * Color values are accessible as Arrays that can be freely mutated by the
 * consuming caller (e.g. The results are defensive copies). The Array will have
 * a size count of 4, where each index is assigned as:
 * 
 * <pre>
 * [0] = Red
 * [1] = Green
 * [2] = Blue
 * [3] = Alpha
 * </pre>
 * <p>
 * Color values are accessible in octect [0, 255] and Decimal [0, 1] formats in
 * order to support a variety of client technologies.
 * <p>
 * Colors can be obtained either as a solid color when no alpha value is
 * provided, or as transparent when an alpha value is provided.
 * 
 * @author Chris Ludka
 * 
 */
public enum ColorType {
	BACKGROUND_DARK(51, 51, 51), //
	BLACK(0, 0, 0), //
	BLUE_LIGHT(103, 187, 255), //
	BLUE(0, 129, 230), //
	BLUE_DARK(0, 90, 192), //
	GREEN_LIGHT(139, 245, 92), //
	GREEN(43, 183, 30), //
	GREEN_DARK(0, 139, 0), //
	GREY_LIGHT(249, 249, 249), //
	GREY(140, 140, 140), //
	GREY_DARK(85, 85, 85), //
	ORANGE_LIGHT(255, 198, 97), //
	ORANGE(251, 153, 2), //
	ORANGE_DARK(228, 135, 1), //
	RED_LIGHT(255, 162, 103), //
	RED(255, 61, 9), //
	RED_DARK(180, 14, 0), //
	WHITE(255, 255, 255), //
	YELLOW_LIGHT(254, 238, 35), //
	YELLOW(200, 169, 0), //
	YELLOW_DARK(172, 121, 0); //

	// Octect color array
	private int[] octectColor = new int[4];

	// Decimal color array
	private float[] decimalColor = new float[4];

	// Internal decimal color array
	private ColorType(int r, int g, int b) {
		this(r, g, b, 255);
	}

	/*
	 * Helper function to create the color array.
	 * 
	 * This helper function also guarantees that the octect values that are
	 * provided are bounded on the interval of [0, 1].
	 */
	private ColorType(int r, int g, int b, int a) {
		// Octect Array
		octectColor = new int[4];
		octectColor[0] = ColorUtils.getBoundedOctect(r);
		octectColor[1] = ColorUtils.getBoundedOctect(g);
		octectColor[2] = ColorUtils.getBoundedOctect(b);
		octectColor[3] = ColorUtils.getBoundedOctect(a);

		// Decimal Array
		for (int i = 0; i < octectColor.length; i++) {
			decimalColor[i] = ColorUtils.getOctectToDecimal(octectColor[i]);
		}
	}

	/**
	 * Returns a solid color. Each color channel will be bounded to the octect
	 * interval of [0, 255].
	 * 
	 * @return
	 */
	public int[] getOctectColor() {
		int[] result = new int[4];
		result[0] = octectColor[0];
		result[1] = octectColor[1];
		result[2] = octectColor[2];
		result[3] = octectColor[3];
		return result;
	}

	/**
	 * Returns a color with a custom value alpha channel where 0 is transparent
	 * and 255 is solid. Each color channel will be bounded to the octect
	 * interval of [0, 255].
	 * 
	 * @return
	 */
	public int[] getOctectColorWithAlpha(int a) {
		int[] result = new int[4];
		result[0] = octectColor[0];
		result[1] = octectColor[1];
		result[2] = octectColor[2];
		result[3] = ColorUtils.getBoundedOctect(a);
		return result;
	}

	/**
	 * Returns a solid color. Each color channel will be bounded to the decimal
	 * interval of [0.0f, 1.0f].
	 * 
	 * @return
	 */
	public float[] getDecimalColor() {
		float[] result = new float[4];
		result[0] = decimalColor[0];
		result[1] = decimalColor[1];
		result[2] = decimalColor[2];
		result[3] = decimalColor[3];
		return result;
	}

	/**
	 * Returns a color with a custom value alpha channel where 0.0f is
	 * transparent and 1.0f is solid. Each color channel will be bounded to the
	 * decimal interval of [0.0f, 1.0f].
	 * 
	 * @return
	 */
	public float[] getDecimalColorWithAlpha(float a) {
		float[] result = new float[4];
		result[0] = decimalColor[0];
		result[1] = decimalColor[1];
		result[2] = decimalColor[2];
		result[3] = ColorUtils.getBoundedDecimal(a);
		return result;
	}
}