package com.binaryworkspace.rcp.wwj.views;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Sash;
import org.eclipse.ui.part.ViewPart;

import com.binaryworkspace.rcp.wwj.compositions.CameraPositionComposition;
import com.binaryworkspace.rcp.wwj.compositions.IScaleDoubleControlComposition;
import com.binaryworkspace.rcp.wwj.compositions.IScaleIntControlComposition;
import com.binaryworkspace.rcp.wwj.compositions.ScaleDoubleControlComposition;
import com.binaryworkspace.rcp.wwj.compositions.ScaleIntControlComposition;
import com.binaryworkspace.rcp.wwj.enums.ScaleDoubleType;
import com.binaryworkspace.rcp.wwj.enums.ScaleIntType;
import com.binaryworkspace.rcp.wwj.resources.ColorType;
import com.binaryworkspace.rcp.wwj.resources.Margins;
import com.binaryworkspace.rcp.wwj.resources.SwtColors;
import com.binaryworkspace.rcp.wwj.structures.AltitudePositionColors;
import com.binaryworkspace.rcp.wwj.util.WwjUtils;

import gov.nasa.worldwind.Model;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.BasicShapeAttributes;
import gov.nasa.worldwind.render.Path;
import gov.nasa.worldwind.render.ShapeAttributes;

/**
 * A NASA World Wind sample view that demonstrates how to draw elevated paths.
 * <p>
 * <b>Notes:</b>
 * <ul>
 * <li>This is a modified code example that was originally provided in the World
 * Wind SDK xwordwind package.
 * <li>The code that embeds the NASA World Wind Java SDK in an SWT Composite is
 * a modified code example originally provided by IBM Developerworks.
 * <li>The algorithm for generating the parametric 3D spiral line plot is a
 * modified code example from Mathematica: ParametricPlot3D -> Basic Examples ->
 * Plot a parametric space curve.
 * </ul>
 * 
 * @author Chris Ludka
 *         <p>
 * @see <a href=
 *      "https://github.com/nasa/World-Wind-Java/blob/master/WorldWind/src/gov/nasa/worldwindx/examples/Paths.java">
 *      https://github.com/nasa/World-Wind-Java/blob/master/WorldWind/src/gov/
 *      nasa/worldwindx/examples/Paths.java</a>
 * @see <a href=
 *      "http://worldwind31.arc.nasa.gov/svn/tags/1.4.0/WorldWind/src/gov/nasa/worldwindx/examples/ParallelPaths.java">
 *      http://worldwind31.arc.nasa.gov/svn/tags/1.4.0/WorldWind/src/gov/nasa/
 *      worldwindx/examples/ParallelPaths.java</a>
 * @see <a href="http://www.ibm.com/developerworks/library/j-wwj/"> http://www.
 *      ibm.com/developerworks/library/j-wwj/</a>
 * @see <a href=
 *      "http://reference.wolfram.com/mathematica/ref/ParametricPlot3D.html">
 *      http://reference.wolfram.com/mathematica/ref/ParametricPlot3D.html</a>
 */
public class ElevatedPathView extends ViewPart implements IScaleDoubleControlComposition, IScaleIntControlComposition {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = ElevatedPathView.class.getName();

	private final WorldWindowGLCanvas wwjGLCanvas = new WorldWindowGLCanvas();

	private final Model wwjModel = (Model) WorldWind.createConfigurationComponent(AVKey.MODEL_CLASS_NAME);

	private final String LAYER_NAME = "ELEVATED PATH";

	private Sash sash;

	private RenderableLayer activeLayer = null;

	private CameraPositionComposition cameraPositionComposition;

	/**
	 * Latitude Control
	 * 
	 * Latitude spans -90 degrees to +90 degrees. The scale control does not
	 * allow negative values, so the scale index will range from 0 to 180.
	 * 
	 * <pre>
	 * 0 Index -> -90 degrees latitude
	 * 90 Index -> 0 degrees latitude
	 * 180 Index -> +90 degrees latitude
	 * </pre>
	 */
	private final double MIN_LAT = -90.0;

	private final double MAX_LAT = 90.0;

	private final double INCREMENT_LAT = 0.5;

	private double lat = 38.0;

	/**
	 * Longitude Control
	 * 
	 * Longitude spans -180 degrees to +180 degrees. The scale control does not
	 * allow negative values, so the scale index will range from 0 to 360.
	 * 
	 * <pre>
	 * 0 Index -> -180 degrees longitude
	 * 180 Index -> 0 degrees longitude
	 * 360 Index -> +180 degrees longitude
	 * </pre>
	 */
	private final double MIN_LON = -180.0;

	private final double MAX_LON = 180.0;

	private final double INCREMENT_LON = 0.5;

	private double lon = -122.5;

	/**
	 * Altitude Control
	 * <p>
	 * All altitude specifications are in meters. A default minimum altitude of
	 * zero is specified.
	 */
	private final double MIN_ALT = 0.0;

	private final double MAX_ALT = 500000.0;

	private final double INCREMENT_ALT = 10000.0;

	private double alt = 250000.0;

	/**
	 * Point Count
	 */
	private final int MIN_POINT_COUNT = 0;

	private final int MAX_POINT_COUNT = 10000;

	private final int INCREMENT_POINT_COUNT = 10;

	private int pointCount = 500;

	/**
	 * Point Size
	 */
	private final double MIN_POINT_SIZE = 0.0;

	private final double MAX_POINT_SIZE = 15.0;

	private final double INCREMENT_POINT_SIZE = 0.5;

	private double pointSize = 2.0;

	/**
	 * Scaling Factor Control
	 * <p>
	 * All altitude specifications are in meters. A default minimum altitude of
	 * zero is specified.
	 */
	private final double MIN_SCALING_FACTOR = 0.0;

	private final double MAX_SCALING_FACTOR = 10.0;

	private final double INCREMENT_SCALING_FACTOR = 0.5;

	private double scalingFactor = 1.0;

	private Listener sashListener = new Listener() {

		@Override
		public void handleEvent(Event event) {
			// Allow the sash to be movable
			int limit = 100;
			Rectangle sashRect = sash.getBounds();
			Rectangle shellRect = sash.getParent().getClientArea();
			FormData formData = (FormData) sash.getLayoutData();

			int right = shellRect.width - sashRect.width - limit;
			event.x = Math.max(Math.min(event.x, right), limit);
			if (event.x != sashRect.x) {
				// Reattach to the left edge of the event
				formData.left = new FormAttachment(0, event.x);

				// Sash will not be redrawn until the parent performs a layout.
				sash.getParent().layout();
			}
		}
	};

	@Override
	public void createPartControl(Composite parent) {
		// WWJ
		wwjGLCanvas.setModel(wwjModel);

		// Init wwjModel
		WwjUtils.initLayerTypes(wwjModel);

		// Parent
		parent.setBackground(SwtColors.color(ColorType.BACKGROUND_DARK));
		parent.setLayout(new FormLayout());
		FormData formData = new FormData();
		formData.top = new FormAttachment(0, 0);
		formData.bottom = new FormAttachment(100, 0);
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100, 0);
		parent.setLayoutData(formData);

		// Sash
		sash = new Sash(parent, SWT.VERTICAL);
		sash.setBackground(SwtColors.color(ColorType.BLUE_LIGHT));
		sash.addListener(SWT.Selection, sashListener);
		formData = new FormData();
		formData.top = new FormAttachment(0, 0);
		formData.bottom = new FormAttachment(100, 0);
		formData.left = new FormAttachment(25, 0);
		sash.setLayoutData(formData);

		// LHS Composite
		Composite lhs = new Composite(parent, SWT.NONE);
		lhs.setBackground(SwtColors.color(ColorType.BACKGROUND_DARK));
		lhs.setLayout(new FormLayout());
		formData = new FormData();
		formData.top = new FormAttachment(0, Margins.TOP_MARGIN.margin());
		formData.bottom = new FormAttachment(100, Margins.BOTTOM_MARGIN.margin());
		formData.left = new FormAttachment(0, Margins.LEFT_MARGIN.margin());
		formData.right = new FormAttachment(sash, Margins.RIGHT_MARGIN.margin());
		lhs.setLayoutData(formData);

		// Lat Composite
		Composite latComposite = new Composite(lhs, SWT.NONE);
		latComposite.setBackground(SwtColors.color(ColorType.BACKGROUND_DARK));
		latComposite.setLayout(new FormLayout());
		formData = new FormData();
		formData.top = new FormAttachment(0, 0);
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100, 0);
		latComposite.setLayoutData(formData);

		// Lat Scale
		ScaleDoubleControlComposition latScale = new ScaleDoubleControlComposition( //
				latComposite, // Parent
				this, // Ref
				MIN_LAT, // Min Value
				MAX_LAT, // Max Value
				INCREMENT_LAT, // Increment
				lat, // Initial Value
				ScaleDoubleType.LAT // Scale Double Type
		); //
		latScale.init();

		// Lon Composite
		Composite lonComposite = new Composite(lhs, SWT.NONE);
		lonComposite.setBackground(SwtColors.color(ColorType.BACKGROUND_DARK));
		lonComposite.setLayout(new FormLayout());
		formData = new FormData();
		formData.top = new FormAttachment(latComposite, Margins.TOP_MARGIN.margin());
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100, 0);
		lonComposite.setLayoutData(formData);

		// Lon Scale
		ScaleDoubleControlComposition lonScale = new ScaleDoubleControlComposition( //
				lonComposite, // Parent
				this, // Ref
				MIN_LON, // Min Value
				MAX_LON, // Max Value
				INCREMENT_LON, // Increment
				lon, // Initial Value
				ScaleDoubleType.LON // Scale Double Type
		); //
		lonScale.init();

		// LHS: Alt Composite
		Composite altControlComposite = new Composite(lhs, SWT.NONE);
		altControlComposite.setBackground(SwtColors.color(ColorType.BACKGROUND_DARK));
		altControlComposite.setLayout(new FormLayout());
		formData = new FormData();
		formData.top = new FormAttachment(lonComposite, Margins.TOP_MARGIN.margin());
		formData.left = new FormAttachment(0, Margins.LEFT_MARGIN.margin());
		formData.right = new FormAttachment(100, Margins.RIGHT_MARGIN.margin());
		altControlComposite.setLayoutData(formData);

		// LHS: Alt Scale
		ScaleDoubleControlComposition altScale = new ScaleDoubleControlComposition( //
				altControlComposite, // Parent
				this, // Ref
				MIN_ALT, // Min Value
				MAX_ALT, // Max Value
				INCREMENT_ALT, // Increment
				alt, // Initial Value
				ScaleDoubleType.ALT // Scale Double Type
		); //
		altScale.init();

		// LHS: Point Count Composite
		Composite pointCountComposite = new Composite(lhs, SWT.NONE);
		pointCountComposite.setBackground(SwtColors.color(ColorType.BACKGROUND_DARK));
		pointCountComposite.setLayout(new FormLayout());
		formData = new FormData();
		formData.top = new FormAttachment(altControlComposite, Margins.TOP_MARGIN.margin());
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100, 0);
		pointCountComposite.setLayoutData(formData);

		// LHS: Point Count Scale
		ScaleIntControlComposition pointCountScale = new ScaleIntControlComposition( //
				pointCountComposite, // Parent
				this, // Ref
				MIN_POINT_COUNT, // Min Value
				MAX_POINT_COUNT, // Max Value
				INCREMENT_POINT_COUNT, // Increment
				pointCount, // Initial Value
				ScaleIntType.POINT_COUNT // Scale Double Type
		); //
		pointCountScale.init();

		// LHS: Point Size Composite
		Composite pointSizeComposite = new Composite(lhs, SWT.NONE);
		pointSizeComposite.setBackground(SwtColors.color(ColorType.BACKGROUND_DARK));
		pointSizeComposite.setLayout(new FormLayout());
		formData = new FormData();
		formData.top = new FormAttachment(pointCountComposite, Margins.TOP_MARGIN.margin());
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100, 0);
		pointSizeComposite.setLayoutData(formData);

		// LHS: Point Size Scale
		ScaleDoubleControlComposition pointSizeScale = new ScaleDoubleControlComposition( //
				pointSizeComposite, // Parent
				this, // Ref
				MIN_POINT_SIZE, // Min Value
				MAX_POINT_SIZE, // Max Value
				INCREMENT_POINT_SIZE, // Increment
				pointSize, // Initial Value
				ScaleDoubleType.POINT_SIZE // Scale Double Type
		); //
		pointSizeScale.init();

		// LHS: Scaling Factor Composite
		Composite scalingFactorComposite = new Composite(lhs, SWT.NONE);
		scalingFactorComposite.setBackground(SwtColors.color(ColorType.BACKGROUND_DARK));
		scalingFactorComposite.setLayout(new FormLayout());
		formData = new FormData();
		formData.top = new FormAttachment(pointSizeComposite, Margins.TOP_MARGIN.margin());
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100, 0);
		scalingFactorComposite.setLayoutData(formData);

		// LHS: Scaling Factor Scale
		ScaleDoubleControlComposition scalingFactorScale = new ScaleDoubleControlComposition( //
				scalingFactorComposite, // Parent
				this, // Ref
				MIN_SCALING_FACTOR, // Min Value
				MAX_SCALING_FACTOR, // Max Value
				INCREMENT_SCALING_FACTOR, // Increment
				scalingFactor, // Initial Value
				ScaleDoubleType.SCALING_FACTOR // Scale Double Type
		); //
		scalingFactorScale.init();

		// RHS Composite
		Composite rhs = new Composite(parent, SWT.NONE);
		rhs.setBackground(SwtColors.color(ColorType.BLACK));
		rhs.setLayout(new FormLayout());
		formData = new FormData();
		formData.top = new FormAttachment(0, 0);
		formData.bottom = new FormAttachment(100, 0);
		formData.left = new FormAttachment(sash, 0);
		formData.right = new FormAttachment(100, 0);
		rhs.setLayoutData(formData);

		// RHS: Camera Position Composite
		Composite cameraPositionComposite = new Composite(rhs, SWT.NONE);
		cameraPositionComposite.setBackground(SwtColors.color(ColorType.BLACK));
		cameraPositionComposite.setLayout(new FormLayout());
		formData = new FormData();
		formData.top = new FormAttachment(0, 0);
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100, 0);
		cameraPositionComposite.setLayoutData(formData);

		// RHS: Camera Position Composition
		cameraPositionComposition = new CameraPositionComposition(cameraPositionComposite, wwjGLCanvas);
		cameraPositionComposition.init();

		/**
		 * RHS: WWJ Composite
		 * 
		 * Embedding the NASA World Wind Java SDK in an SWT Composite. Code
		 * modified from:
		 * 
		 * <pre>
		 * &#64;see http://www.ibm.com/developerworks/library/j-wwj/
		 * </pre>
		 */
		final Composite wwjComposite = new Composite(rhs, SWT.EMBEDDED);
		wwjComposite.setLayout(new FormLayout());
		formData = new FormData();
		formData.top = new FormAttachment(cameraPositionComposite, 0);
		formData.bottom = new FormAttachment(100, 0);
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100, 0);
		wwjComposite.setLayoutData(formData);

		// Swing Frame and Panel
		java.awt.Frame awtFrame = SWT_AWT.new_Frame(wwjComposite);
		java.awt.Panel awtPanel = new java.awt.Panel(new java.awt.BorderLayout());
		awtFrame.add(awtPanel);

		// Add the WWJ GLCanvas to the AWT Panel
		awtPanel.add(wwjGLCanvas, BorderLayout.CENTER);
		parent.setLayoutData(new GridData(GridData.FILL_BOTH));
		parent.layout();
	}

	@Override
	public void refreshScaleIntValue(ScaleIntType type, int value) {
		switch (type) {
		case HEIGHT:
			throw new RuntimeException("Unspported Type: " + type);
		case PETAL_COUNT:
			throw new RuntimeException("Unspported Type: " + type);
		case POINT_COUNT:
			pointCount = value;
			break;
		case RING_COUNT:
			throw new RuntimeException("Unspported Type: " + type);
		case WIDTH:
			throw new RuntimeException("Unspported Type: " + type);
		default:
			throw new RuntimeException("Unspported Type: " + type);
		}
		refresh();
	}

	@Override
	public void refreshScaleDoubleValue(ScaleDoubleType type, double value) {
		switch (type) {
		case ALPHA:
			throw new RuntimeException("Unspported Type: " + type);
		case ALT:
			alt = value;
			break;
		case DETLA_LAT:
			throw new RuntimeException("Unspported Type: " + type);
		case DETLA_LON:
			throw new RuntimeException("Unspported Type: " + type);
		case GRID_SIZE:
			throw new RuntimeException("Unspported Type: " + type);
		case LAT:
			lat = value;
			break;
		case LON:
			lon = value;
			break;
		case POINT_SIZE:
			pointSize = value;
			break;
		case SCALING_FACTOR:
			scalingFactor = value;
			break;
		case TERRAIN_CONFORMANCE:
			throw new RuntimeException("Unspported Type: " + type);
		default:
			throw new RuntimeException("Unspported Type: " + type);
		}
		refresh();
	}

	// Refreshes the Surface Path
	private void refresh() {
		// Build Positions
		List<Position> positionList = new ArrayList<Position>(pointCount);
		for (int i = 0; i < pointCount; i++) {
			double projectedAlt = ((double) i / pointCount) * alt;
			double latitude = Math.sin(i) / scalingFactor + lat;
			double longitude = Math.cos(i) / scalingFactor + lon;
			LatLon latLon = LatLon.fromDegrees(latitude, longitude);
			positionList.add(new Position(latLon, projectedAlt));
		}

		// Build Path
		Path path = new Path(positionList);
		path.setValue(AVKey.DISPLAY_NAME, "ELEVATED PATH");
		path.setPathType(AVKey.LINEAR);
		path.setAltitudeMode(WorldWind.RELATIVE_TO_GROUND);
		path.setPositionColors(new AltitudePositionColors(alt));
		path.setShowPositions(true);

		// Path Attributes
		ShapeAttributes attrs = new BasicShapeAttributes();
		attrs.setOutlineWidth(pointSize);
		path.setAttributes(attrs);

		// Renderable Layer
		RenderableLayer renderableLayer = new RenderableLayer();
		renderableLayer.setName(LAYER_NAME);
		renderableLayer.addRenderable(path);

		// Update Active Layer
		if (activeLayer == null) {
			WwjUtils.insertBeforeCompass(wwjModel, renderableLayer);
		} else {
			WwjUtils.replaceLayers(wwjModel, activeLayer, renderableLayer);
		}
		activeLayer = renderableLayer;
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