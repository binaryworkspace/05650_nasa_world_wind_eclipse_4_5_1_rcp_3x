package com.binaryworkspace.rcp.wwj.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.image.BufferedImage;
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
import com.binaryworkspace.rcp.wwj.util.WwjUtils;

import gov.nasa.worldwind.Model;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.Polyline;
import gov.nasa.worldwind.render.SurfaceImage;

/**
 * NASA World Wind example that demonstrates how to overlay a surface image.
 * Animation of the image is an option.
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
 *      href="https://github.com/nasa/World-Wind-Java/blob/master/WorldWind/src/gov/nasa/worldwindx/examples/SurfaceImages.java">
 *      https://github.com/nasa/World-Wind-Java/blob/master/WorldWind/src/gov/nasa/worldwindx/examples/SurfaceImages.java</a>
 */
public class SurfaceImageView extends ViewPart implements IScaleDoubleControlComposition, IScaleIntControlComposition, IAnimationControlComposition {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = SurfaceImageView.class.getName();

	private final WorldWindowGLCanvas wwjGLCanvas = new WorldWindowGLCanvas();

	private final Model wwjModel = (Model) WorldWind.createConfigurationComponent(AVKey.MODEL_CLASS_NAME);

	private final String LAYER_NAME = "SURFACE IMAGE";

	private SurfaceImage surfaceImage = new SurfaceImage();

	private SineWaveSurfaceModel model = new SineWaveSurfaceModel();

	private Sash sash;

	private RenderableLayer activeLayer = null;

	private CameraPositionComposition cameraPositionComposition;

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
	 * Image Height Control
	 * <p>
	 * Image height specifications are in pixels.
	 */
	private final int MIN_IMAGE_HEIGHT = 100;

	private final int MAX_IMAGE_HEIGHT = 5000;

	private final int INCREMENT_IMAGE_HEIGHT = 100;

	private int imageHeight = 500;

	/**
	 * Image Width Control
	 * <p>
	 * Image width specifications are in pixels.
	 */
	private final int MIN_IMAGE_WIDTH = 100;

	private final int MAX_IMAGE_WIDTH = 5000;

	private final int INCREMENT_IMAGE_WIDTH = 100;

	private int imageWidth = 500;

	/**
	 * Grid Size Control
	 */
	private final double MIN_GRID_SIZE = 0.0;

	private final double MAX_GRID_SIZE = 10.0;

	private final double INCREMENT_GRID_SIZE = 0.5;

	private double gridSize = 1.0;

	/**
	 * Image Alpha channel (Color Transparency) Control
	 */
	private final double MIN_ALPHA = 0.0;

	private final double MAX_ALPHA = 1.0;

	private final double INCREMENT_ALPHA = 0.01;

	private double alpha = 1.0;

	/**
	 * Animation Controls
	 */
	private final int MAX_FRAMES_PER_SECOND = 30;

	private final int FRAMES_PER_SECOND_INTERVAL = 1;

	private final int FRAMES_PER_SECOND = 30;

	private final int MAX_FRAME_INDEX = 100;

	private int frameIndex = 0;

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

		// LHS: Height Composite
		Composite heightControlComposite = new Composite(lhs, SWT.NONE);
		heightControlComposite.setBackground(SwtColors.color(ColorType.BACKGROUND_DARK));
		heightControlComposite.setLayout(new FormLayout());
		formData = new FormData();
		formData.top = new FormAttachment(deltaLonComposite, Margins.TOP_MARGIN.margin());
		formData.left = new FormAttachment(0, Margins.LEFT_MARGIN.margin());
		formData.right = new FormAttachment(100, Margins.RIGHT_MARGIN.margin());
		heightControlComposite.setLayoutData(formData);

		// LHS: Height Scale
		ScaleIntControlComposition heightScale = new ScaleIntControlComposition( //
				heightControlComposite, // Parent
				this, // Ref
				MIN_IMAGE_HEIGHT, // Min Value
				MAX_IMAGE_HEIGHT, // Max Value
				INCREMENT_IMAGE_HEIGHT, // Increment
				imageHeight, // Initial Value
				ScaleIntType.HEIGHT // Scale Int Type
		); //
		heightScale.init();

		// LHS: Width Composite
		Composite widthControlComposite = new Composite(lhs, SWT.NONE);
		widthControlComposite.setBackground(SwtColors.color(ColorType.BACKGROUND_DARK));
		widthControlComposite.setLayout(new FormLayout());
		formData = new FormData();
		formData.top = new FormAttachment(heightControlComposite, Margins.TOP_MARGIN.margin());
		formData.left = new FormAttachment(0, Margins.LEFT_MARGIN.margin());
		formData.right = new FormAttachment(100, Margins.RIGHT_MARGIN.margin());
		widthControlComposite.setLayoutData(formData);

		// LHS: Width Scale
		ScaleIntControlComposition widthScale = new ScaleIntControlComposition( //
				widthControlComposite, // Parent
				this, // Ref
				MIN_IMAGE_WIDTH, // Min Value
				MAX_IMAGE_WIDTH, // Max Value
				INCREMENT_IMAGE_WIDTH, // Increment
				imageWidth, // Initial Value
				ScaleIntType.WIDTH // Scale Int Type
		); //
		widthScale.init();

		// LHS: Grid Size Composite
		Composite gridSizeControlComposite = new Composite(lhs, SWT.NONE);
		gridSizeControlComposite.setBackground(SwtColors.color(ColorType.BACKGROUND_DARK));
		gridSizeControlComposite.setLayout(new FormLayout());
		formData = new FormData();
		formData.top = new FormAttachment(widthControlComposite, Margins.TOP_MARGIN.margin());
		formData.left = new FormAttachment(0, Margins.LEFT_MARGIN.margin());
		formData.right = new FormAttachment(100, Margins.RIGHT_MARGIN.margin());
		gridSizeControlComposite.setLayoutData(formData);

		// LHS: Alpha Scale
		ScaleDoubleControlComposition gridSizeScale = new ScaleDoubleControlComposition( //
				gridSizeControlComposite, // Parent
				this, // Ref
				MIN_GRID_SIZE, // Min Value
				MAX_GRID_SIZE, // Max Value
				INCREMENT_GRID_SIZE, // Increment
				gridSize, // Initial Value
				ScaleDoubleType.GRID_SIZE // Scale Double Type
		); //
		gridSizeScale.init();

		// LHS: Alpha Composite
		Composite alphaControlComposite = new Composite(lhs, SWT.NONE);
		alphaControlComposite.setBackground(SwtColors.color(ColorType.BACKGROUND_DARK));
		alphaControlComposite.setLayout(new FormLayout());
		formData = new FormData();
		formData.top = new FormAttachment(gridSizeControlComposite, Margins.TOP_MARGIN.margin());
		formData.left = new FormAttachment(0, Margins.LEFT_MARGIN.margin());
		formData.right = new FormAttachment(100, Margins.RIGHT_MARGIN.margin());
		alphaControlComposite.setLayoutData(formData);

		// LHS: Alpha Scale
		ScaleDoubleControlComposition alphaScale = new ScaleDoubleControlComposition( //
				alphaControlComposite, // Parent
				this, // Ref
				MIN_ALPHA, // Min Value
				MAX_ALPHA, // Max Value
				INCREMENT_ALPHA, // Increment
				alpha, // Initial Value
				ScaleDoubleType.ALPHA // Scale Double Type
		); //
		alphaScale.init();

		// LHS: Animation Control Composite
		Composite animationControlComposite = new Composite(lhs, SWT.NONE);
		animationControlComposite.setBackground(SwtColors.color(ColorType.BACKGROUND_DARK));
		animationControlComposite.setLayout(new FormLayout());
		formData = new FormData();
		formData.top = new FormAttachment(alphaControlComposite, Margins.TOP_MARGIN.margin());
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
			imageHeight = value;
			break;
		case PETAL_COUNT:
			throw new RuntimeException("Unspported Type: " + type);
		case POINT_COUNT:
			throw new RuntimeException("Unspported Type: " + type);
		case RING_COUNT:
			throw new RuntimeException("Unspported Type: " + type);
		case WIDTH:
			imageWidth = value;
			break;
		default:
			throw new RuntimeException("Unspported Type: " + type);
		}
		refresh();
	}

	@Override
	public void refreshScaleDoubleValue(ScaleDoubleType type, double value) {
		switch (type) {
		case ALPHA:
			alpha = value;
			break;
		case ALT:
			throw new RuntimeException("Unspported Type: " + type);
		case DETLA_LAT:
			deltaLat = value;
			break;
		case DETLA_LON:
			deltaLon = value;
			break;
		case GRID_SIZE:
			gridSize = value;
			break;
		case LAT:
			lat = value;
			break;
		case LON:
			this.lon = value;
			break;
		case POINT_SIZE:
			throw new RuntimeException("Unspported Type: " + type);
		case SCALING_FACTOR:
			throw new RuntimeException("Unspported Type: " + type);
		case TERRAIN_CONFORMANCE:
			throw new RuntimeException("Unspported Type: " + type);
		default:
			throw new RuntimeException("Unspported Type: " + type);
		}
		refresh();
	}

	// Refreshes the Tessellation Surface
	private void refresh() {
		// Establish the grid corners
		double lowerLat = lat - deltaLat;
		double upperLat = lat + deltaLat;
		double lowerLon = lon - deltaLon;
		double upperLon = lon + deltaLon;
		List<Position> cornerPositions = new ArrayList<Position>();
		cornerPositions.add(Position.fromDegrees(lowerLat, lowerLon, CORNER_ALT));
		cornerPositions.add(Position.fromDegrees(lowerLat, upperLon, CORNER_ALT));
		cornerPositions.add(Position.fromDegrees(upperLat, upperLon, CORNER_ALT));
		cornerPositions.add(Position.fromDegrees(upperLat, lowerLon, CORNER_ALT));

		// Image
		BufferedImage image = model.getBufferedImage(imageWidth, imageHeight, (float) gridSize, MAX_FRAME_INDEX, frameIndex, (float) alpha);
		surfaceImage.setImageSource(image, cornerPositions);

		// Polyline
		Polyline polyline = new Polyline(surfaceImage.getCorners(), 0);
		polyline.setFollowTerrain(true);
		polyline.setClosed(true);
		polyline.setPathType(Polyline.RHUMB_LINE);
		polyline.setColor(new Color(255, 255, 255));

		// Renderable Layer
		RenderableLayer renderableLayer = new RenderableLayer();
		renderableLayer.setName(LAYER_NAME);
		renderableLayer.setPickEnabled(false);
		renderableLayer.addRenderable(surfaceImage);
		renderableLayer.addRenderable(polyline);

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