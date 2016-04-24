package com.binaryworkspace.rcp.wwj.structures;

import java.awt.Color;

import com.binaryworkspace.rcp.wwj.resources.AwtColorScheme;

import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.render.Path;

/**
 * Provides a color sampled from the visible spectrum based on a position's
 * altitude.
 * 
 * @author Chris Ludka
 * 
 */
public class AltitudePositionColors implements Path.PositionColors {

	private double maxAltitude = 1.0;

	/**
	 * Max altitude must be greater than zero. If a value of zero or less is
	 * provided the max altitude will be set to 1.0 to prevent division by zero
	 * errors.
	 * 
	 * @param maxAltitude
	 */
	public AltitudePositionColors(double maxAltitude) {
		// Check that maxAltitude is greater than zero
		if (maxAltitude > 0) {
			this.maxAltitude = maxAltitude;
		}
	}

	/**
	 * Provides a color along the visual spectrum for a given position's
	 * altitude.
	 * <p>
	 * The visual spectrum sampling is performed along the float interval of [0,
	 * 1]. The sample is determined by taking the altitude of the position
	 * divided by the max altitude.
	 * 
	 * @return
	 */
	@Override
	public Color getColor(Position position, int ordinal) {
		return AwtColorScheme.getVisibleSpectrum((float) position.getAltitude() / (float) maxAltitude);
	}
}