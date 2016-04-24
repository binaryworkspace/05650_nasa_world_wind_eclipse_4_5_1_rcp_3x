package com.binaryworkspace.rcp.wwj.enums;

/**
 * An enumeration indicating surface editor model types.
 * <p>
 * This code is duplicated/modified from the World Wind example
 * SurfaceImageEditor. SurfaceImageEditor was refactored to support resizing and
 * moving surface images as well as surface sectors (a surface polygon
 * with four sides).
 * 
 * @author Chris Ludka
 * 
 * @see <a
 *      href="https://github.com/nasa/World-Wind-Java/blob/master/WorldWind/src/gov/nasa/worldwindx/examples/util/SurfaceImageEditor.java">
 *      https://github.com/nasa/World-Wind-Java/blob/master/WorldWind/src/gov/nasa/worldwindx/examples/util/SurfaceImageEditor.java</a>
 * 
 */
public enum SurfaceEditorModeType {
	NONE("None"), //
	MOVING_SURFACE("Moving the Surface"), //
	SIZING_SURFACE("Sizing the Surface"); //
	
	private String displayName;
	
	private SurfaceEditorModeType(String displayName) {
		this.displayName = displayName;
	}
	
	public String getDisplayName() {
		return displayName;
	}
}
