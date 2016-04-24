package com.binaryworkspace.rcp.wwj.structures;

import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.render.markers.BasicMarker;
import gov.nasa.worldwind.render.markers.MarkerAttributes;

/**
 * An indexed BasicMarker.
 * 
 * @author Chris Ludka
 * 
 */
public class IndexedMarker extends BasicMarker {
	
	private int index;
	
	/**
	 * A BasicMarker that contains an index.
	 * 
	 * @param position
	 * @param attrs
	 * @param index
	 */
	public IndexedMarker(Position position, MarkerAttributes attrs, int index) {
		super(position, attrs);
		this.index = index;
	}
	
	/**
	 * Provides the given index for this marker.
	 * 
	 * @return
	 */
	public int getIndex() {
		return index;
	}
}
