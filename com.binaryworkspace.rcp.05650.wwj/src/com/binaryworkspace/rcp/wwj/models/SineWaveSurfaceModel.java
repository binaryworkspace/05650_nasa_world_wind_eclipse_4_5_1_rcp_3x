package com.binaryworkspace.rcp.wwj.models;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.binaryworkspace.rcp.wwj.resources.AwtColorScheme;

/**
 * Provides an example model of a Sine Wave Surface that can be animated by
 * iterating through a series of given frame steps.
 * 
 * @author Chris Ludka
 * 
 * 
 * @see <a href=
 *      "https://reference.wolfram.com/language/ref/ContourShading.html"> https:
 *      //reference.wolfram.com/language/ref/ContourShading.html</a>
 */
public class SineWaveSurfaceModel {

	private boolean isCacheOn = false;

	private Map<Integer, List<float[]>> magnitudeCacheMap = new LinkedHashMap<Integer, List<float[]>>();

	private Map<Integer, List<float[]>> colorCacheMap = new LinkedHashMap<Integer, List<float[]>>();

	private Map<Integer, BufferedImage> imageCacheMap = new LinkedHashMap<Integer, BufferedImage>();

	/**
	 * For a given frame, provides a List of float[] containing the magnitudes
	 * of the Sine Wave Surface.
	 * <p>
	 * The magnitude values are a normalized value between [0,1].
	 * <p>
	 * Each float[] represents the magnitude each cell value in the row where
	 * each row is added from the bottom-to-top with left-to-right cell
	 * addition.
	 * <p>
	 * Example row: [m1,m2,...]
	 * 
	 * @param width
	 * @param height
	 * @param gridIncrement
	 * @param frames
	 * @param frame
	 * @return
	 */
	public List<float[]> getMagnitudeFloatArrayList(int width, int height, float gridIncrement, int frames, int frame) {
		// Check cache
		if (isCacheOn) {
			if (magnitudeCacheMap.containsKey(frame)) {
				return magnitudeCacheMap.get(frame);
			}
		}

		// Init variables
		float y = -0.5f * height;
		double xScalar = width / (2.0 * Math.PI);
		double yScalar = height / (2.0 * Math.PI);
		List<float[]> magnitudeFloatArrayList = new ArrayList<float[]>();

		// Shift (or animate) based on the frame
		float shift = frame / (float) (frames - 1);

		// Add rows from the bottom, going from left-to-right for cell addition
		for (int yIndex = 0; yIndex < height; yIndex++) {
			// y-value
			double dy = Math.sin(y / (yScalar));

			// Start at the first cell in the row
			float x = -0.5f * width;

			// Add cells to the row going from left-to-right
			float[] rowCellArray = new float[width];
			for (int xIndex = 0; xIndex < width; xIndex++) {
				// x-value
				double dx = Math.sin(x / (xScalar));

				// Magnitude
				float rawValue = (float) (0.5 * dx * dy + 0.5);
				float shiftedMagnitude = rawValue + shift;
				float magnitude = shiftedMagnitude % 1;
				rowCellArray[xIndex] = magnitude;

				// Determine start position of the next cell
				x = x + gridIncrement;
			}

			// Add row
			magnitudeFloatArrayList.add(rowCellArray);
			y = y + gridIncrement;
		}

		// Add to cache
		if (isCacheOn) {
			magnitudeCacheMap.put(frame, magnitudeFloatArrayList);
		}

		return magnitudeFloatArrayList;
	}

	/**
	 * For a given frame, produces a List of float[] containing the colors of
	 * the Sine Wave Surface based on the magnitudes of
	 * getMagnitudeFloatArrayList(). Each float[] represents the RGB sequence
	 * for each cell (pixel) in the row where each row is added from the
	 * bottom-to-top with left-to-right cell (pixel) addition (drawing).
	 * <p>
	 * Example row: [{r1,g1,b1},{r2,g2,b3},...]
	 * 
	 * @param width
	 * @param height
	 * @param gridIncrement
	 * @param frames
	 * @param frame
	 * @return
	 */
	public List<float[]> getColorFloatArrayList(int width, int height, float gridIncrement, int frames, int frame) {
		// Check cache
		if (isCacheOn) {
			if (colorCacheMap.containsKey(frame)) {
				return colorCacheMap.get(frame);
			}
		}

		// Init variables
		List<float[]> magnitudeFloatArrayList = getMagnitudeFloatArrayList(width, height, gridIncrement, frames, frame);
		List<float[]> colorFloatArrayList = new ArrayList<float[]>();

		// Add rows from the bottom, going from left-to-right in the pixel draw
		for (int yIndex = 0; yIndex < height; yIndex++) {
			// Magnitude Float Array
			float[] magnitudeFloatArray = magnitudeFloatArrayList.get(yIndex);

			// Start at the first pixel in the row
			int pixelIndex = 0;

			// Add pixels to the row going from left-to-right
			float[] rowColorArray = new float[3 * width];
			for (int xIndex = 0; xIndex < width; xIndex++) {
				// Magnitude
				float magnitude = magnitudeFloatArray[xIndex];

				// Determine and the color
				Color awtColor = AwtColorScheme.getVisibleSpectrum(magnitude);
				rowColorArray[pixelIndex] = (float) awtColor.getRed() / 255.0f;
				rowColorArray[pixelIndex + 1] = (float) awtColor.getGreen() / 255.0f;
				rowColorArray[pixelIndex + 2] = (float) awtColor.getBlue() / 255.0f;
				pixelIndex = pixelIndex + 3;
			}
			// Complete and add the row
			colorFloatArrayList.add(rowColorArray);
		}

		// Add to cache
		if (isCacheOn) {
			colorCacheMap.put(frame, colorFloatArrayList);
		}

		return colorFloatArrayList;
	}

	/**
	 * Produces an image of a given frame based on the getColorFloatArrayList()
	 * model.
	 * 
	 * @param width
	 * @param height
	 * @param gridIncrement
	 * @param frames
	 * @param frame
	 * @return
	 */
	public BufferedImage getBufferedImage(int width, int height, float gridIncrement, int frames, int frame, float alphaWeight) {
		// Check cache
		if (isCacheOn) {
			if (imageCacheMap.containsKey(frame)) {
				return imageCacheMap.get(frame);
			}
		}

		// Model data
		float alpha = (alphaWeight * 255.0f) / 255.0f;
		List<float[]> floatArrayColorList = getColorFloatArrayList(width, height, gridIncrement, frames, frame);

		// Build image
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		for (int yIndex = 0; yIndex < height; yIndex++) {
			// Row data
			float[] rowColorArray = floatArrayColorList.get(yIndex);

			/*
			 * Add pixels in the row. Be sure to use 'width' and not
			 * rowColorArray.length() because the rowColorArray is x3 longer
			 * than the 'width' to account for the rgb pixel data (e.g.
			 * [{r1,g1,b1},{r2,g2,b3},...]).
			 */
			int pixelIndex = 0;
			for (int xIndex = 0; xIndex < width; xIndex++) {
				Color color = new Color(rowColorArray[pixelIndex], rowColorArray[pixelIndex + 1], rowColorArray[pixelIndex + 2], alpha);
				image.setRGB(xIndex, yIndex, color.getRGB());
				pixelIndex = pixelIndex + 3;
			}
		}

		// Add to cache
		if (isCacheOn) {
			imageCacheMap.put(frame, image);
		}

		return image;
	}

	/**
	 * Turns the cache on. Note this can consume significant amounts of memory.
	 * 
	 * @param isCacheOn
	 */
	public void setIsCacheOn(boolean isCacheOn) {
		this.isCacheOn = isCacheOn;
		if (!isCacheOn) {
			// Dump the maps since the cache has been turned off
			magnitudeCacheMap = new LinkedHashMap<Integer, List<float[]>>();
			colorCacheMap = new LinkedHashMap<Integer, List<float[]>>();
		}
	}
}
