package com.binaryworkspace.rcp.wwj.structures;

import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.geom.Position;

/**
 * A Position extension that supports being indexed.
 * <p>
 * The index value of the position can be only set during construction and must
 * be zero or greater. If the index value is not specified during construction
 * or is specified as less than zero, then a default value of zero will be used.
 * 
 * @author Chris Ludka
 * 
 */
public class IndexedPosition extends Position {

	private int index = 0;

	public IndexedPosition(Angle latitude, Angle longitude, double elevation) {
		super(latitude, longitude, elevation);
	}

	public IndexedPosition(Angle latitude, Angle longitude, double elevation, int index) {
		super(latitude, longitude, elevation);
		// Check that index is zero or greater
		if (index >= 0) {
			this.index = index;
		}
	}

	public IndexedPosition(LatLon latLon, double elevation) {
		super(latLon, elevation);
	}

	public IndexedPosition(LatLon latLon, double elevation, int index) {
		super(latLon, elevation);
		// Check that index is zero or greater
		if (index >= 0) {
			this.index = index;
		}
	}

	/**
	 * Provides the indexed position value.
	 * 
	 * @return
	 */
	public int getIndex() {
		return index;
	}
}
