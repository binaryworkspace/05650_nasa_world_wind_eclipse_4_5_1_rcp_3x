package com.binaryworkspace.rcp.wwj.compositions;

import com.binaryworkspace.rcp.wwj.enums.ScaleIntType;

/**
 * Interface indicating that scale int value has changed requiring a refresh.
 * 
 * @author Chris Ludka
 *
 */
public interface IScaleIntControlComposition {

	/**
	 * Refresh as the result of a change in the scale int value.
	 * 
	 * @param value
	 */
	public void refreshScaleIntValue(ScaleIntType type, int value);

}
