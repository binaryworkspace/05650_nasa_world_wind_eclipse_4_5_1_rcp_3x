package com.binaryworkspace.rcp.wwj.structures;

import java.awt.Color;

import com.binaryworkspace.rcp.wwj.resources.AwtColorScheme;

import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.render.Path;

/**
 * Provides a color sampled from the visible spectrum based on a position's
 * index.
 * 
 * @author Chris Ludka
 * 
 */
public class IndexedPositionColors implements Path.PositionColors {

	private int pathPointCount = 1;

	/**
	 * Path point count must be greater than zero. If a value of zero or less is
	 * provided the path point count will be set to 1 to prevent division by
	 * zero errors.
	 * 
	 * @param pathPointCount
	 */
	public IndexedPositionColors(int pathPointCount) {
		// Check that path point count is greater than zero
		if (pathPointCount > 0) {
			this.pathPointCount = pathPointCount;
		}
	}

	/**
	 * Provides a color along the visual spectrum for a given position.
	 * <p>
	 * The visual spectrum sampling is performed along the float interval of [0,
	 * 1]. The sample is determined by taking the index of the position along
	 * the path divided by the path point count, assuming an IndexPosition is
	 * provided. If an IndexPosition is not provided then index is assumed to be
	 * zero and therefore the color will return at the zero sample position from
	 * [0, 1].
	 * 
	 * @return
	 */
	@Override
	public Color getColor(Position position, int ordinal) {
		// Default index value
		int index = 0;

		/*
		 * Check for IndexedPostion and if found, use the index provided on the
		 * position.
		 */
		if (position instanceof IndexedPosition) {
			index = ((IndexedPosition) position).getIndex();
		}
		return AwtColorScheme.getVisibleSpectrum((float) index / (float) pathPointCount);
	}
}