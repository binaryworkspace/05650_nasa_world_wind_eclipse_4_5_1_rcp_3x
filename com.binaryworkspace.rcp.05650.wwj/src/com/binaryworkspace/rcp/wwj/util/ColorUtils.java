package com.binaryworkspace.rcp.wwj.util;

/**
 * Utility class for working with Colors.
 * 
 * @author Chris Ludka
 * 
 */
public class ColorUtils {

	/**
	 * Bounds a given float value to the decimal interval of [0.0f, 1.0f].
	 * <p>
	 * For instance, if a value of -1.5f is provided, 0.0f is returned. If a
	 * value of 95.7f is provided, 1.0f is returned. If a value of 0.579f is
	 * provided, 0.579f is returned.
	 * 
	 * @param value
	 * @return
	 */
	public static float getBoundedDecimal(float value) {
		if (value < 0.0f) {
			return 0.0f;
		} else if (value > 1.0f) {
			return 1.0f;
		} else {
			return value;
		}
	}

	/**
	 * Bounds a given integer value to the octect interval of [0, 255].
	 * <p>
	 * For instance, if a value of -25 is provided, 0 is returned. If a value of
	 * 458 is provided, 255 is returned. If a value of 128 is provided, 128 is
	 * returned.
	 * 
	 * @param value
	 * @return
	 */
	public static int getBoundedOctect(int value) {
		if (value < 0) {
			return 0;
		} else if (value > 255) {
			return 255;
		} else {
			return value;
		}
	}

	/**
	 * Converts a given integer value on the octect interval of [0, 255] to a
	 * bounded float value to the decimal interval of [0.0f, 1.0f].
	 * <p>
	 * For instance, if a value of -25 is provided, 0.0f is returned. If a value
	 * of 458 is provided, 1.0f is returned. If a value of 128 is provided,
	 * 0.490196078f is returned.
	 * 
	 * @param value
	 * @return
	 */
	public static float getOctectToDecimal(int value) {
		return getBoundedDecimal((float) value / 255.0f);
	}

	/**
	 * Converts a given decimal value on the interval of [0.0f, 1.0f] to a
	 * bounded octect value on interval of [0, 255].
	 * <p>
	 * For instance, if a value of -1.5f is provided, 0 is returned. If a value
	 * of 95.7f is provided it will be interpreted as 1.0f which will be
	 * returned as 255 on the octect interval. If a value of 0.579f is provided,
	 * 0.579f*255.0 = 147.645, which will be rounded and returned as 148 on the
	 * octect interval.
	 * 
	 * @param value
	 * @return
	 */
	public static int getDecimalToOctect(float value) {
		int roundedOctect = Math.round(value * 255.0f);
		return getBoundedOctect(roundedOctect);
	}
}