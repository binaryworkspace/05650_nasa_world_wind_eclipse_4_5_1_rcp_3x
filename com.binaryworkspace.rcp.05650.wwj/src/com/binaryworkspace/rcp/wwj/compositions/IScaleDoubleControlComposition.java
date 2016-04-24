package com.binaryworkspace.rcp.wwj.compositions;

import com.binaryworkspace.rcp.wwj.enums.ScaleDoubleType;

/**
 * Interface indicating that scale double value has changed requiring a refresh.
 * 
 * @author Chris Ludka
 *
 */
public interface IScaleDoubleControlComposition {

	/**
	 * Refresh as the result of a change in the scale double value.
	 * 
	 * @param value
	 */
	public void refreshScaleDoubleValue(ScaleDoubleType type, double value);

}
