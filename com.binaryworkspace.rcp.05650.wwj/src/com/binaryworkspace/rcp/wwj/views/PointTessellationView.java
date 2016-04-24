package com.binaryworkspace.rcp.wwj.views;

import java.awt.BorderLayout;
import java.awt.Color;
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

import com.binaryworkspace.rcp.wwj.compositions.AnimationControlComposition;
import com.binaryworkspace.rcp.wwj.compositions.CameraPositionComposition;
import com.binaryworkspace.rcp.wwj.compositions.IAnimationControlComposition;
import com.binaryworkspace.rcp.wwj.compositions.IScaleDoubleControlComposition;
import com.binaryworkspace.rcp.wwj.compositions.IScaleIntControlComposition;
import com.binaryworkspace.rcp.wwj.compositions.ScaleDoubleControlComposition;
import com.binaryworkspace.rcp.wwj.compositions.ScaleIntControlComposition;
import com.binaryworkspace.rcp.wwj.enums.ScaleDoubleType;
import com.binaryworkspace.rcp.wwj.enums.ScaleIntType;
import com.binaryworkspace.rcp.wwj.models.SineWaveSurfaceModel;
import com.binaryworkspace.rcp.wwj.resources.ColorType;
import com.binaryworkspace.rcp.wwj.resources.Margins;
import com.binaryworkspace.rcp.wwj.resources.SwtColors;
import com.binaryworkspace.rcp.wwj.structures.ColorizedPointGrid;
import com.binaryworkspace.rcp.wwj.util.WwjUtils;

import gov.nasa.worldwind.Model;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.RenderableLayer;

/**
 * NASA World Wind example that demonstrates how to generate 10,000's to
 * 100,000's of points in varying colors. These points are in a
 * grid/tessellation at an elevation relative to the ground. Animation of the
 * grid/tessellation is an option.
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
 * @see <a href="http://www.ibm.com/developerworks/library/j-wwj/">
 *      http://www.ibm.com/developerworks/library/j-wwj/</a>
 * @see <a
 *      href="https://github.com/nasa/World-Wind-Java/blob/master/WorldWind/src/gov/nasa/worldwindx/examples/lineofsight/GridOfPoints.java">
 *      https://github.com/nasa/World-Wind-Java/blob/master/WorldWind/src/gov/nasa/worldwindx/examples/lineofsight/GridOfPoints.java</a>
 * @see <a
 *      href="https://github.com/nasa/World-Wind-Java/blob/master/WorldWind/src/gov/nasa/worldwindx/examples/lineofsight/PointGrid.java">
 *      https://github.com/nasa/World-Wind-Java/blob/master/WorldWind/src/gov/nasa/worldwindx/examples/lineofsight/PointGrid.java</a>
 */
public class PointTessellationView extends ViewPart implements IScaleDoubleControlComposition, IScaleIntControlComposition, IAnimationControlComposition {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = PointTessellationView.class.getName();

	private final WorldWindowGLCanvas wwjGLCanvas = new WorldWindowGLCanvas();

	private final Model wwjModel = (Model) WorldWind.createConfigurationComponent(AVKey.MODEL_CLASS_NAME);
	
	private final String LAYER_NAME = "POINT TESSELLATION";

	private Sash sash;

	private RenderableLayer activeLayer = null;

	private CameraPositionComposition cameraPositionComposition;
	
	private List<Position> cornerPositionList;
	
	private double lowerLat;
	
	private double upperLat;
	
	private double lowerLon;
	
	private double upperLon;
	
	private final double CORNER_ALT = 10e3;
	
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
	 * Delta Latitude is the plus and minus in degrees a shape can be changed.
	 */
	private final double MIN_DELTA_LAT = 0.0;

	private final double MAX_DELTA_LAT = 90.0;

	private final double INCREMENT_DELTA_LAT = 0.5;

	private double deltaLat = 5.0;
	
	/**
	 * Delta Longitude is the plus and minus in degrees a shape can be changed.
	 */
	private final double MIN_DELTA_LON = 0.0;

	private final double MAX_DELTA_LON = 180.0;

	private final double INCREMENT_DELTA_LON = 0.5;

	private double deltaLon = 5.0;

	/**
	 * Altitude Control
	 * <p>
	 * All altitude specifications are in meters. A default minimum altitude of
	 * zero is specified.
	 */
	private final double MIN_ALT = 0.0;

	private final double MAX_ALT = 500000.0;

	private final double INCREMENT_ALT = 10000.0;

	private double alt = 0.0;
	
	/**
	 * Point Count
	 */
	private final int MIN_POINT_COUNT = 0;

	private final int MAX_POINT_COUNT = 250000;

	private final int INCREMENT_POINT_COUNT = 10000;

	private int pointCount = 100000;

	/**
	 * Point Size
	 */
	private final double MIN_POINT_SIZE = 0.0;

	private final double MAX_POINT_SIZE = 15.0;

	private final double INCREMENT_POINT_SIZE = 0.5;

	private double pointSize = 15.0;
	
	/**
	 * Animation Controls
	 */
	private final int MAX_FRAMES_PER_SECOND = 30;
	
	private final int FRAMES_PER_SECOND_INTERVAL = 1;
	
	private final int FRAMES_PER_SECOND = 30;
	
	private final int MAX_FRAME_INDEX = 100;
	
	private int frameIndex = 0;
	
	private SineWaveSurfaceModel model = new SineWaveSurfaceModel();

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

		// Delta Lat Composite
		Composite deltaLatComposite = new Composite(lhs, SWT.NONE);
		deltaLatComposite.setBackground(SwtColors.color(ColorType.BACKGROUND_DARK));
		deltaLatComposite.setLayout(new FormLayout());
		formData = new FormData();
		formData.top = new FormAttachment(lonComposite, Margins.TOP_MARGIN.margin());
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100, 0);
		deltaLatComposite.setLayoutData(formData);

		// Delta Lat Scale
		ScaleDoubleControlComposition deltaLatScale = new ScaleDoubleControlComposition( //
				deltaLatComposite, // Parent
				this, // Ref
				MIN_DELTA_LAT, // Min Value
				MAX_DELTA_LAT, // Max Value
				INCREMENT_DELTA_LAT, // Increment
				deltaLat, // Initial Value
				ScaleDoubleType.DETLA_LAT // Scale Double Type
		); //
		deltaLatScale.init();

		// Delta Lon Composite
		Composite deltaLonComposite = new Composite(lhs, SWT.NONE);
		deltaLonComposite.setBackground(SwtColors.color(ColorType.BACKGROUND_DARK));
		deltaLonComposite.setLayout(new FormLayout());
		formData = new FormData();
		formData.top = new FormAttachment(deltaLatComposite, Margins.TOP_MARGIN.margin());
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100, 0);
		deltaLonComposite.setLayoutData(formData);

		// Delta Lon Scale
		ScaleDoubleControlComposition deltaLonScale = new ScaleDoubleControlComposition( //
				deltaLonComposite, // Parent
				this, // Ref
				MIN_DELTA_LON, // Min Value
				MAX_DELTA_LON, // Max Value
				INCREMENT_DELTA_LON, // Increment
				deltaLon, // Initial Value
				ScaleDoubleType.DETLA_LON // Scale Double Type
		); //
		deltaLonScale.init();
		
		// LHS: Alt Composite
		Composite altControlComposite = new Composite(lhs, SWT.NONE);
		altControlComposite.setBackground(SwtColors.color(ColorType.BACKGROUND_DARK));
		altControlComposite.setLayout(new FormLayout());
		formData = new FormData();
		formData.top = new FormAttachment(deltaLonComposite, Margins.TOP_MARGIN.margin());
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

		// LHS: Animation Control Composite
		Composite animationControlComposite = new Composite(lhs, SWT.NONE);
		animationControlComposite.setBackground(SwtColors.color(ColorType.BACKGROUND_DARK));
		animationControlComposite.setLayout(new FormLayout());
		formData = new FormData();
		formData.top = new FormAttachment(pointSizeComposite, Margins.TOP_MARGIN.margin());
		formData.left = new FormAttachment(0, Margins.LEFT_MARGIN.margin());
		formData.right = new FormAttachment(100, Margins.RIGHT_MARGIN.margin());
		animationControlComposite.setLayoutData(formData);

		// LHS: Animation Control Composition
		AnimationControlComposition animationControlComposition = new AnimationControlComposition( //
				animationControlComposite, // Parent
				this, // View
				MAX_FRAMES_PER_SECOND, // Maximum Frames Per Second
				FRAMES_PER_SECOND_INTERVAL, // Frames Per Second Interval
				FRAMES_PER_SECOND, // Initial Frames Per Second
				MAX_FRAME_INDEX, // Number of Frames
				false // Initial Animation State
		); //
		animationControlComposition.init();
		
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
		
		// Refresh
		model.setIsCacheOn(false);
		refresh();
	}

	@Override
	public void refreshFrameIndex(int frameIndex) {
		this.frameIndex = frameIndex;
		refresh();
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
			deltaLat = value;
			refreshCornerPositions();
			break;
		case DETLA_LON:
			deltaLon = value;
			refreshCornerPositions();
			break;
		case GRID_SIZE:
			throw new RuntimeException("Unspported Type: " + type);
		case LAT:
			lat = value;
			refreshCornerPositions();
			break;
		case LON:
			this.lon = value;
			refreshCornerPositions();
			break;
		case POINT_SIZE:
			pointSize = value;
			break;
		case SCALING_FACTOR:
			throw new RuntimeException("Unspported Type: " + type);
		case TERRAIN_CONFORMANCE:
			throw new RuntimeException("Unspported Type: " + type);
		default:
			throw new RuntimeException("Unspported Type: " + type);
		}
		refresh();
	}
	
	// Refresh Corner Positions
	private void refreshCornerPositions(){
		// Establish the grid corners
		lowerLat = lat - deltaLat;
		upperLat = lat + deltaLat;
		lowerLon = lon - deltaLon;
		upperLon = lon + deltaLon;
		
		// Build Corner Position List
		cornerPositionList = new ArrayList<Position>();
		cornerPositionList.add(Position.fromDegrees(lowerLat, lowerLon, CORNER_ALT));
		cornerPositionList.add(Position.fromDegrees(lowerLat, upperLon, CORNER_ALT));
		cornerPositionList.add(Position.fromDegrees(upperLat, upperLon, CORNER_ALT));
		cornerPositionList.add(Position.fromDegrees(upperLat, lowerLon, CORNER_ALT));		
	}
	
	// Refreshes the Tessellation Surface
	private void refresh() {
		// Init Surface variables
		double surfaceLat = 0.0;
		double surfaceLon = 0.0;
		double surfaceElev = 0.0;
		int width = (int) Math.sqrt(pointCount);
		int height = (int) Math.sqrt(pointCount);
		float gridIncrement = 1.0f;
		
		// Model data
		List<float[]> magnitudeFloatArrayList = model.getMagnitudeFloatArrayList(width, height, gridIncrement, MAX_FRAME_INDEX, frameIndex);
		List<float[]> colorFloatArrayList = model.getColorFloatArrayList(width, height, gridIncrement, MAX_FRAME_INDEX, frameIndex);
		
		// Point positions
		List<Position> meshPositions = new ArrayList<Position>(width * height);
		List<Color> meshColors = new ArrayList<Color>(width * height);
		for (int yIndex = 0; yIndex < height; yIndex++) {
			// Get lists for this 'row'
			float[] magnitudeFloatArray = magnitudeFloatArrayList.get(yIndex);
			float[] colorFloatArray = colorFloatArrayList.get(yIndex);
			
			// Compute the lat for this 'row'
			surfaceLat = lat + (double) yIndex * (deltaLat / (double) height);
			
			// Populate the 'row'
			for (int xIndex = 0; xIndex < width; xIndex++) {
				// Add position
				surfaceLon = lon + (double) xIndex * (deltaLon / (double) width);
				float magnitude = magnitudeFloatArray[xIndex];
				surfaceElev = CORNER_ALT + alt * magnitude;
				meshPositions.add(Position.fromDegrees(surfaceLat, surfaceLon, surfaceElev));
				
				// Add color
				int colorIndex = 3 * xIndex;
				float red = colorFloatArray[colorIndex];
				float green = colorFloatArray[colorIndex + 1];
				float blue = colorFloatArray[colorIndex + 2];
				meshColors.add(new Color(red, green, blue));
			}
		}
		
		// Create the PointGrid shape
		ColorizedPointGrid grid = new ColorizedPointGrid(cornerPositionList, meshPositions, meshColors, meshPositions.size());
		ColorizedPointGrid.Attributes attrs = new ColorizedPointGrid.Attributes();
		attrs.setPointSize(pointSize);
		grid.setAttributes(attrs);
		grid.setAltitudeMode(WorldWind.RELATIVE_TO_GROUND);

		// Renderable Layer
		RenderableLayer renderableLayer = new RenderableLayer();
		renderableLayer.setName(LAYER_NAME);
		renderableLayer.setPickEnabled(false);
		renderableLayer.addRenderable(grid);

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