package com.binaryworkspace.rcp.wwj.enums;

/**
 * An enumeration for scale int types.
 * 
 * @author Chris Ludka
 * 
 */
public enum ScaleIntType {
	HEIGHT("Height:", ""), //
	PETAL_COUNT("Petal Count:", ""), //
	POINT_COUNT("Point Count:", ""), //
	RING_COUNT("Ring Count:", ""), //
	WIDTH("Width:", ""); //

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

	private ScaleIntType(String prefixTitle, String postfixTitle) {
		this.prefixTitle = prefixTitle;
		this.postfixTitle = postfixTitle;
	}
}
