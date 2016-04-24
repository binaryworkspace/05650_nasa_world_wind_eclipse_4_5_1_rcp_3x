package com.binaryworkspace.rcp.wwj.views;

import gov.nasa.worldwind.Model;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwind.event.PositionEvent;
import gov.nasa.worldwind.event.PositionListener;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.ContourLine;
import gov.nasa.worldwind.render.ContourLinePolygon;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.binaryworkspace.rcp.wwj.compositions.CameraPositionComposition;
import com.binaryworkspace.rcp.wwj.resources.AwtColorScheme;
import com.binaryworkspace.rcp.wwj.resources.ColorType;
import com.binaryworkspace.rcp.wwj.resources.SwtColors;
import com.binaryworkspace.rcp.wwj.util.WwjUtils;

/**
 * This NASA World Wind example demonstrates how to draw contour lines at
 * specific elevations with colors that vary by elevation.
 * <p>
 * The contour line colors will vary according to elevation.
 * <ul>
 * <li>To scale the color to an elevation it is necessary to bound the color
 * spectrum to a lower and upper bound elevation level.
 * <li>Lower bound elevation: Sea Level: 0 ft, 0 m
 * <li>Upper bound elevation: Highest mountain in the 'Lower 48' Contiguous
 * United States: Mount Whitney, Sierra Nevada, California: 14,505 ft, 4421 m
 * <li>Recall in NASA World Wind, all distances (including elevation which is
 * measured as the perpendicular distance from the Earth ellipsoid) and are
 * measured in meters. See reference below.
 * </ul>
 * <p>
 * <b>Notes:</b>
 * <ul>
 * <li>This is a modified code example that was originally provided in the World
 * Wind SDK xwordwind package.
 * <li>The code that embeds the NASA World Wind Java SDK in an SWT Composite is
 * a modified code example originally provided by IBM Developerworks.
 * </ul>
 * 
 * @author Chris Ludka
 *         <p>
 * @see <a href=
 *      "https://github.com/nasa/World-Wind-Java/blob/master/WorldWind/src/gov/nasa/worldwindx/examples/ContourLines.java">
 *      https://github.com/nasa/World-Wind-Java/blob/master/WorldWind/src/gov/
 *      nasa/worldwindx/examples/ContourLines.java</a>
 * @see <a href="http://www.ibm.com/developerworks/library/j-wwj/"> http://www.
 *      ibm.com/developerworks/library/j-wwj/</a>
 * @see <a href=
 *      "http://forum.worldwindcentral.com/showthread.php?18662-Elevation-Unit-of-Measure">
 *      http://forum.worldwindcentral.com/showthread.php?18662-Elevation-Unit-of
 *      -Measure</a>
 * @see <a href=
 *      "http://en.wikipedia.org/wiki/Mountain_peaks_of_the_United_States"> http
 *      ://en.wikipedia.org/wiki/Mountain_peaks_of_the_United_States</a>
 */
public class ElevationContoursView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = ElevationContoursView.class.getName();

	private CameraPositionComposition cameraPositionComposition;

	private final WorldWindowGLCanvas wwjGLCanvas = new WorldWindowGLCanvas();

	private Model wwjModel;
	
	private final String LAYER_NAME = "ELEVATION CONTOURS";

	private Layer activeRenderableLayer;

	private int activeDeltaElevevationForContourlines = 100;

	@Override
	public void createPartControl(Composite parent) {
		// Parent
		parent.setBackground(SwtColors.color(ColorType.BLACK));
		parent.setLayout(new FormLayout());
		FormData formData = new FormData();
		formData.top = new FormAttachment(0, 0);
		formData.bottom = new FormAttachment(100, 0);
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100, 0);
		parent.setLayoutData(formData);

		// Camera Position Composite
		Composite cameraPositionComposite = new Composite(parent, SWT.NONE);
		cameraPositionComposite.setBackground(SwtColors.color(ColorType.BLACK));
		cameraPositionComposite.setLayout(new FormLayout());
		formData = new FormData();
		formData.top = new FormAttachment(0, 0);
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100, 0);
		cameraPositionComposite.setLayoutData(formData);

		// Camera Position Composition
		cameraPositionComposition = new CameraPositionComposition(cameraPositionComposite, wwjGLCanvas);
		cameraPositionComposition.init();

		// Swing to SWT bridge
		final Composite swtEmbeddedComposite = new Composite(parent, SWT.EMBEDDED);
		swtEmbeddedComposite.setLayout(new FormLayout());
		formData = new FormData();
		formData.top = new FormAttachment(cameraPositionComposite, 0);
		formData.bottom = new FormAttachment(100, 0);
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100, 0);
		swtEmbeddedComposite.setLayoutData(formData);

		// Swing Frame and Panel
		java.awt.Frame worldFrame = SWT_AWT.new_Frame(swtEmbeddedComposite);
		java.awt.Panel panel = new java.awt.Panel(new java.awt.BorderLayout());
		worldFrame.add(panel);

		// Add the WWJ 3D OpenGL Canvas to the Swing Panel
		panel.add(wwjGLCanvas, BorderLayout.CENTER);
		parent.setLayoutData(new GridData(GridData.FILL_BOTH));
		wwjModel = (Model) WorldWind.createConfigurationComponent(AVKey.MODEL_CLASS_NAME);
		WwjUtils.initLayerTypes(wwjModel);
		wwjGLCanvas.setModel(wwjModel);
		
		// Init wwjModel
		WwjUtils.initLayerTypes(wwjModel);

		// Refresh
		refresh();
		
		// Listen for camera moves to update LAT-LON-ELEV controls
		this.wwjGLCanvas.addPositionListener(new PositionListener() {

			@Override
			public void moved(PositionEvent event) {
				/*
				 * Update the separation distance of contour lines based on the
				 * camera elevation such that less are contour lines are drawn
				 * at high camera elevations and more are drawn at low camera
				 * elevations.
				 */
				int deltaElevevationForContourlines = (int) (50.0 * Math.pow(wwjGLCanvas.getView().getCurrentEyePosition().getElevation(), 0.0925));
				if (deltaElevevationForContourlines == activeDeltaElevevationForContourlines) {
					return;
				}
				activeDeltaElevevationForContourlines = deltaElevevationForContourlines;

				// Refresh
				PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {

					@Override
					public void run() {
						refresh();
					}
				});
			}
		});
	}

	// Update contour render layer
	private void refresh() {
		// Create a singular layer to display all elevation contour lines
		RenderableLayer renderableLayer = new RenderableLayer();
		renderableLayer.setName(LAYER_NAME);
		renderableLayer.setPickEnabled(false);

		/*
		 * Create a contour line to mark Sea Level Elevations across the entire
		 * globe (not bounded to the 'Lower 48' Contiguous United States as the
		 * rest of the elevation lines will be).
		 */
		ContourLine contourLine = new ContourLine();
		contourLine.setElevation(0);
		contourLine.setColor(AwtColorScheme.getVisibleSpectrum(0.0f));
		contourLine.setLineWidth(1);
		renderableLayer.addRenderable(contourLine);

		/*
		 * Establish a boundary area (or Sector) for custom contour elevation
		 * lines: Approximately the 'Lower 48' Contiguous United States.
		 */
		ArrayList<LatLon> positionList = new ArrayList<LatLon>();
		positionList.add(LatLon.fromDegrees(20.00, -130.00));
		positionList.add(LatLon.fromDegrees(20.00, -70.00));
		positionList.add(LatLon.fromDegrees(50.00, -70.00));
		positionList.add(LatLon.fromDegrees(50.00, -130.00));

		/*
		 * Vary the contour line colors according to elevation where the lower
		 * bound elevation is Sea Level (0 m) and the upper bound elevation
		 * approximately to Highest mountain in the 'Lower 48' Contiguous United
		 * States: Mount Whitney, Sierra Nevada, California (4421 m).
		 */
		int lowerBoundElevevation = 0;
		int upperBoundElevation = 4421;

		// Add contour lines where color varies by elevation
		for (int elevation = lowerBoundElevevation; elevation <= upperBoundElevation; elevation += activeDeltaElevevationForContourlines) {
			ContourLinePolygon contourLinePolygon = new ContourLinePolygon(elevation, positionList);
			Color color = AwtColorScheme.getVisibleSpectrum(elevation / (float) upperBoundElevation);
			contourLinePolygon.setColor(color);
			contourLinePolygon.setLineWidth(1);
			renderableLayer.addRenderable(contourLinePolygon);
		}
		
		// Replace current active layer
		WwjUtils.replaceLayers(wwjModel, activeRenderableLayer, renderableLayer);
		activeRenderableLayer = renderableLayer;
	}
	
	@Override
	public void setFocus() {
		// Do Nothing
	}

	@Override
	public void dispose() {
		cameraPositionComposition.dispose();
		super.dispose();
	}
}