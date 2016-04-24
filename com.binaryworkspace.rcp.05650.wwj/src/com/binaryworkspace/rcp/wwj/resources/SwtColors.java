package com.binaryworkspace.rcp.wwj.resources;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

/**
 * Provides an SWT Color for a given ColorType.
 * <p>
 * The SWT Colors can only be obtained as a solid SWT Color. SWT Color does not
 * provide direct support for alpha values.
 * 
 * @author Chris Ludka
 * 
 */
public final class SwtColors {

	private SwtColors() {
		// Hidden Constructor
	}

	// Helper conversion method
	private static RGB getRGBFromArray(int[] array) {
		return new RGB(array[0], array[1], array[2]);
	}

	/**
	 * Returns a solid color SWT color. Each color channel will be bounded to
	 * the octect interval of [0, 255].
	 * 
	 * @return
	 */
	public static Color color(ColorType colorType) {
		// Check to see if color is already registered with JFaceResources
		if (!JFaceResources.getColorRegistry().hasValueFor(colorType.name())) {
			JFaceResources.getColorRegistry().put(colorType.name(), getRGBFromArray(colorType.getOctectColor()));
		}

		// Return the color
		return JFaceResources.getColorRegistry().get(colorType.name());
	}

}
