package com.binaryworkspace.rcp.wwj.resources;

/**
 * Provides a set of centralized layout offsets to promote a consist look and
 * feel across an application.
 * <p>
 * Margins are larger spacing values designed separate diverse sets of controls.
 * Pads are smaller spacing values designed to space but not separate similar
 * groupings of controls.
 * 
 * @author Chris Ludka
 * 
 */
public enum Margins {
	TOP_MARGIN(10), //
	BOTTOM_MARGIN(-10), //
	LEFT_MARGIN(10), //
	RIGHT_MARGIN(-10), //
	TOP_PAD(5), //
	BOTTOM_PAD(-5), //
	LEFT_PAD(5), //
	RIGHT_PAD(-5), //
	SPACER(2); //
	
	private int margin;

	private Margins(int margin) {
		this.margin = margin;
	}

	/**
	 * Returns the value of the margin or pad value.
	 * 
	 * @return margin
	 */
	public int margin() {
		return margin;
	}
}
