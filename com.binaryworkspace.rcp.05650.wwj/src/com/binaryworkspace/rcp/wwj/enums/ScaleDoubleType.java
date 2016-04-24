package com.binaryworkspace.rcp.wwj.enums;

/**
 * An enumeration for scale double types.
 * 
 * @author Chris Ludka
 * 
 */
public enum ScaleDoubleType {
	ALPHA("Alpha Channel:", ""), //
	ALT("Altitude:", "(m)"), //
	DETLA_LAT("Delta Latitude:", ""), //
	DETLA_LON("Delta Longitude:", ""), //
	GRID_SIZE("Grid Size:", ""), //
	LAT("Latitude:", ""), //
	LON("Longitude:", ""), //
	POINT_SIZE("Point Size:", ""), //
	SCALING_FACTOR("Scaling Factor:", ""), //
	TERRAIN_CONFORMANCE("Terrain Conformance:", "(m)"); //

	private String prefixTitle;

	private String postfixTitle;

	/**
	 * Provides the prefix title.
	 * 
	 * @return
	 */
	public String getPrefixTitle() {
		return prefixTitle;
	}

	/**
	 * Provides the postfix title.
	 * 
	 * @return
	 */
	public String getPostfixTitle() {
		return postfixTitle;
	}

	private ScaleDoubleType(String prefixTitle, String postfixTitle) {
		this.prefixTitle = prefixTitle;
		this.postfixTitle = postfixTitle;
	}
}
