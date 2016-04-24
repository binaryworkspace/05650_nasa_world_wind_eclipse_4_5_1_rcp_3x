package com.binaryworkspace.rcp.wwj.resources;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;

/**
 * Enum for SWT Fonts.
 * 
 * @author Chris Ludka
 */
public enum SwtFonts {
	DEFAULT, //
	SUBTITLE("Arial", 18, SWT.NONE), //
	SECTION("Arial", 12, SWT.BOLD), //
	TEXT("Arial", 10, SWT.NONE), //
	TEXT_BOLD("Arial", 10, SWT.BOLD), //
	TITLE("Arial", 24, SWT.BOLD) //
	; // end

	// Default Font
	private SwtFonts() {
		this(JFaceResources.getDefaultFont().getFontData()[0]);
	}

	/*
	 * Creates and returns a custom FontData with the given name, height and
	 * style. Note that the named Font must exist on the Client UI's OS.
	 * Otherwise, the default Font will be returned modified with the given
	 * height and style.
	 */
	private SwtFonts(FontData fontData) {
		FontData[] fontDataArray = new FontData[] { fontData };
		JFaceResources.getFontRegistry().put(name(), fontDataArray);
	}

	/*
	 * Creates and returns a custom FontData with the given name, height and
	 * style. Note that the named Font must exist on the Client UI's OS.
	 * Otherwise, the default Font will be returned modified with the given
	 * height and style.
	 */
	private SwtFonts(String name, int height, int style) {
		this(new FontData(name, height, style));
	}

	/**
	 * Returns the Font defined for this Enum.
	 * 
	 * @return font
	 */
	public Font font() {
		return JFaceResources.getFontRegistry().get(name());
	}
}