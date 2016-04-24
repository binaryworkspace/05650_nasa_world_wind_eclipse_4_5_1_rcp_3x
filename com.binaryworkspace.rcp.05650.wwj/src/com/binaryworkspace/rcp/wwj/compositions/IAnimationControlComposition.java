package com.binaryworkspace.rcp.wwj.compositions;

/**
 * Interface indicating that animations are occurring that requiring a refresh
 * of the World Wind Model.
 * 
 * @author Chris Ludka
 *
 */
public interface IAnimationControlComposition {
	
	/**
	 * Refresh World Wind Model as the result of a change in the animation frame
	 * index.
	 * 
	 * @param frameIndex
	 */
	public void refreshFrameIndex(int frameIndex);

}
