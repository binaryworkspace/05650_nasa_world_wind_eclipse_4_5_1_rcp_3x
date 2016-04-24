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
import com.binaryworkspace.rcp.wwj.resources.AwtColorScheme;
import com.binaryworkspace.rcp.wwj.resources.ColorType;
import com.binaryworkspace.rcp.wwj.resources.Margins;
import com.binaryworkspace.rcp.wwj.resources.SwtColors;
import com.binaryworkspace.rcp.wwj.util.WwjUtils;

import gov.nasa.worldwind.Model;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.BasicShapeAttributes;
import gov.nasa.worldwind.render.ExtrudedPolygon;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.ShapeAttributes;
import gov.nasa.worldwind.render.SurfacePolygon;

/**
 * NASA World Wind example that demonstrates how to draw extruded and surface
 * polygons.
 * <p>
 * Draws a ringed color flower according to a specified Latitude and Longitude
 * origin, where:
 * <ul>
 * <li>Flowers with specified altitudes greater than 0.0 m are drawn using
 * ExtrudedPolygon.
 * <li>Flowers with specified altitude of 0.0 m are drawn using SurfacePolygon.
 * </ul>
 * <p>
 * ExtrudedPolygon flowers will draw the petals and ringCount as a spiral
 * staircase with the outer ring beginning the extrusions at 0.0 m and the most
 * inner ring completing the extrusions at the provided altitude.
 * 
 * <p>
 * <b>Notes:</b>
 * <ul>
 * <li>This is a modified code example that was originally provided in the World
 * Wind SDK xwordwind package.
 * <li>The code that embeds the NASA World Wind Java SDK in an SWT Composite is
 * a modified code example originally provided by IBM Developerworks.
 * <li>The algorithm for generating the color flower is a modified code example
 * from Mathematica: Polygon -> Neat Examples -> Digital Petals.
 * </ul>
 * 
 * @author Chris Ludka
 *         <p>
 * @see <a href=
 *      "https://github.com/nasa/World-Wind-Java/blob/master/WorldWind/src/gov/nasa/worldwindx/examples/ExtrudedPolygons.java">
 *      https://github.com/nasa/World-Wind-Java/blob/master/WorldWind/src/gov/
 *      nasa/worldwindx/examples/ExtrudedPolygons.java</a>
 * @see <a href=
 *      "https://github.com/nasa/World-Wind-Java/blob/master/WorldWind/src/gov/nasa/worldwindx/examples/shapebuilder/ExtrudedPolygonBuilder.java">
 *      https://github.com/nasa/World-Wind-Java/blob/master/WorldWind/src/gov/
 *      nasa/worldwindx/examples/shapebuilder/ExtrudedPolygonBuilder.java</a>
 * @see <a href=
 *      "https://github.com/nasa/World-Wind-Java/blob/master/WorldWind/src/gov/nasa/worldwindx/examples/DraggingShapes.java">
 *      https://github.com/nasa/World-Wind-Java/blob/master/WorldWind/src/gov/
 *      nasa/worldwindx/examples/DraggingShapes.java</a>
 * @see <a href="http://www.ibm.com/developerworks/library/j-wwj/"> http://www.
 *      ibm.com/developerworks/library/j-wwj/</a>
 * @see <a href="http://reference.wolfram.com/mathematica/ref/Polygon.html">
 *      http://reference.wolfram.com/mathematica/ref/Polygon.html</a>
 */
public class SurfacePolygonsView extends ViewPart implements IScaleDoubleControlComposition, IScaleIntControlComposition {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = SurfacePolygonsView.class.getName();

	private final WorldWindowGLCanvas wwjGLCanvas = new WorldWindowGLCanvas();

	private final Model wwjModel = (Model) WorldWind.createConfigurationComponent(AVKey.MODEL_CLASS_NAME);

	private final String LAYER_NAME = "SURFACE POLYGONS";

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
	 * Petal Controls
	 */
	private final int MIN_PETAL_COUNT = 0;

	private final int MAX_PETAL_COUNT = 100;

	private final int INCREMENT_PETAL_COUNT = 1;

	private int petalCount = 10;

	/**
	 * Ring Controls
	 */
	private final int MIN_RING_COUNT = 0;

	private final int MAX_RING_COUNT = 100;

	private final int INCREMENT_RING_COUNT = 1;

	private int ringCount = 7;

	/**
	 * Scaling Factor Control
	 * <p>
	 * All altitude specifications are in meters. A default minimum altitude of
	 * zero is specified.
	 */
	private final double MIN_SCALING_FACTOR = 0.0;

	private final double MAX_SCALING_FACTOR = 5.0;

	private final double INCREMENT_SCALING_FACTOR = 0.1;

	private double scalingFactor = 0.3;

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

		// LHS: Petal Count Composite
		Composite petalCountComposite = new Composite(lhs, SWT.NONE);
		petalCountComposite.setBackground(SwtColors.color(ColorType.BACKGROUND_DARK));
		petalCountComposite.setLayout(new FormLayout());
		formData = new FormData();
		formData.top = new FormAttachment(altControlComposite, Margins.TOP_MARGIN.margin());
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100, 0);
		petalCountComposite.setLayoutData(formData);

		// LHS: Petal Count Scale
		ScaleIntControlComposition petalCountScale = new ScaleIntControlComposition( //
				petalCountComposite, // Parent
				this, // Ref
				MIN_PETAL_COUNT, // Min Value
				MAX_PETAL_COUNT, // Max Value
				INCREMENT_PETAL_COUNT, // Increment
				petalCount, // Initial Value
				ScaleIntType.PETAL_COUNT // Scale Int Type
		); //
		petalCountScale.init();

		// LHS: Ring Count Composite
		Composite ringCountComposite = new Composite(lhs, SWT.NONE);
		ringCountComposite.setBackground(SwtColors.color(ColorType.BACKGROUND_DARK));
		ringCountComposite.setLayout(new FormLayout());
		formData = new FormData();
		formData.top = new FormAttachment(petalCountComposite, Margins.TOP_MARGIN.margin());
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100, 0);
		ringCountComposite.setLayoutData(formData);

		// LHS: Petal Count Scale
		ScaleIntControlComposition ringCountScale = new ScaleIntControlComposition( //
				ringCountComposite, // Parent
				this, // Ref
				MIN_RING_COUNT, // Min Value
				MAX_RING_COUNT, // Max Value
				INCREMENT_RING_COUNT, // Increment
				ringCount, // Initial Value
				ScaleIntType.RING_COUNT // Scale Int Type
		); //
		ringCountScale.init();

		// LHS: Scaling Factor Composite
		Composite scalingFactorComposite = new Composite(lhs, SWT.NONE);
		scalingFactorComposite.setBackground(SwtColors.color(ColorType.BACKGROUND_DARK));
		scalingFactorComposite.setLayout(new FormLayout());
		formData = new FormData();
		formData.top = new FormAttachment(ringCountComposite, Margins.TOP_MARGIN.margin());
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

		// Refresh
		refresh();
	}

	@Override
	public void refreshScaleIntValue(ScaleIntType type, int value) {
		switch (type) {
		case HEIGHT:
			throw new RuntimeException("Unspported Type: " + type);
		case PETAL_COUNT:
			petalCount = value;
			break;
		case POINT_COUNT:
			throw new RuntimeException("Unspported Type: " + type);
		case RING_COUNT:
			ringCount = value;
			break;
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
			throw new RuntimeException("Unspported Type: " + type);
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

	/**
	 * Build a RenderableLayer that contains a list of renderable polygons that
	 * from a ringed color flower.
	 * <p>
	 * The flower's circular center is defined by the specified LAT-LON origin.
	 * Flowers with altitudes greater than 0.0 m are drawn using
	 * ExtrudedPolygon. Flowers with altitude of 0.0 m are drawn using
	 * SurfacePolygon.
	 * <p>
	 * ExtrudedPolygon flowers will draw the petals and ringCount as a spiral
	 * staircase with the outer ring beginning the extrusions at 0.0 m and the
	 * most inner ring completing the extrusions at the provided altitude.
	 */
	private void refresh() {
		// Enable shape dragging
		// wwjGLCanvas.addSelectListener(new BasicDragger(wwjGLCanvas));

		// Create layer and compute flower polygon count
		RenderableLayer renderableLayer = new RenderableLayer();
		int totalPolygonCount = petalCount * ringCount;
		int polygonIndex = 0;
		double circularSegment = 2.0 * Math.PI / (double) petalCount;

		// Add petals to each of the ringCount
		double latRef = lat;
		double lonRef = lon;
		for (int r = 0; r < ringCount; r++) {
			for (int p = 0; p < petalCount; p++) {
				// Add a petal
				polygonIndex++;
				List<LatLon> positionList = new ArrayList<LatLon>();

				// Vertex 1
				double lat = latRef + scalingFactor * (ringCount - r) * Math.cos(circularSegment * (p - 1));
				double lon = lonRef + scalingFactor * (ringCount - r) * Math.sin(circularSegment * (p - 1));
				positionList.add(LatLon.fromDegrees(lat, lon));

				// Vertex 2
				lat = latRef + scalingFactor * (ringCount - r) * Math.cos(circularSegment * (p + 1));
				lon = lonRef + scalingFactor * (ringCount - r) * Math.sin(circularSegment * (p + 1));
				positionList.add(LatLon.fromDegrees(lat, lon));

				// Vertex 3
				lat = latRef + scalingFactor * ((ringCount + 2) - r) * Math.cos(circularSegment * p);
				lon = lonRef + scalingFactor * ((ringCount + 2) - r) * Math.sin(circularSegment * p);
				positionList.add(LatLon.fromDegrees(lat, lon));

				// Vertex 4 = Vertex 1 (closes the polygon)
				lat = latRef + scalingFactor * (ringCount - r) * Math.cos(circularSegment * (p - 1));
				lon = lonRef + scalingFactor * (ringCount - r) * Math.sin(circularSegment * (p - 1));
				positionList.add(LatLon.fromDegrees(lat, lon));

				// Get attributes
				double percentage = (double) polygonIndex / (double) (totalPolygonCount);
				ShapeAttributes sideAttributes = getSideAttributes(percentage);
				ShapeAttributes capAttributes = getCapAttributes(sideAttributes, percentage);
				ShapeAttributes sideHighlightAttributes = getSideHighlightAttributes(sideAttributes);

				/**
				 * Get ExtrudedPolygon (altitude > 0.0) or SurfacePolygon
				 * (altitude = 0.0) color flower
				 * <p>
				 * Flowers with altitudes greater than 0.0 m are drawn using
				 * ExtrudedPolygon. Flowers with altitude of 0.0 m are drawn
				 * using SurfacePolygon.
				 */
				if (alt > 0.0) {
					/*
					 * Extrude the polygons such that the flower forms a spiral
					 * staircase with the outer ring beginning the extrusions at
					 * 0.0 m and the most inner ring completing the extrusions
					 * at the provided altitude.
					 */
					ExtrudedPolygon poly = new ExtrudedPolygon(positionList, percentage * alt + 1);
					poly.setSideAttributes(sideAttributes);
					poly.setSideHighlightAttributes(sideHighlightAttributes);
					poly.setCapAttributes(capAttributes);
					renderableLayer.addRenderable(poly);
				} else {
					SurfacePolygon poly = new SurfacePolygon(positionList);
					poly.setAttributes(sideAttributes);
					poly.setHighlightAttributes(sideHighlightAttributes);
					renderableLayer.addRenderable(poly);
				}
			}
		}
		renderableLayer.setName(LAYER_NAME);

		// Update Active Layer
		if (activeLayer == null) {
			WwjUtils.insertBeforeCompass(wwjModel, renderableLayer);
		} else {
			WwjUtils.replaceLayers(wwjModel, activeLayer, renderableLayer);
		}
		activeLayer = renderableLayer;
	}

	// Helper function for Side Attributes
	private ShapeAttributes getSideAttributes(double colorPercentage) {
		ShapeAttributes sideAttributes = new BasicShapeAttributes();
		sideAttributes.setInteriorMaterial(new Material(AwtColorScheme.getVisibleSpectrum((float) colorPercentage)));
		sideAttributes.setOutlineOpacity(0.5);
		sideAttributes.setInteriorOpacity(0.5);
		sideAttributes.setOutlineMaterial(Material.BLACK);
		sideAttributes.setOutlineWidth(1);
		sideAttributes.setDrawOutline(true);
		sideAttributes.setDrawInterior(true);
		sideAttributes.setEnableLighting(true);
		return sideAttributes;
	}

	// Helper function for Cap Attributes
	private ShapeAttributes getCapAttributes(ShapeAttributes sideAttributes, double colorPercentage) {
		ShapeAttributes capAttributes = new BasicShapeAttributes(sideAttributes);
		capAttributes.setInteriorMaterial(new Material(AwtColorScheme.getVisibleSpectrum((float) colorPercentage)));
		capAttributes.setInteriorOpacity(0.8);
		capAttributes.setDrawInterior(true);
		capAttributes.setEnableLighting(true);
		return capAttributes;
	}

	// Helper function for Side Highlight Attributes
	private ShapeAttributes getSideHighlightAttributes(ShapeAttributes sideAttributes) {
		ShapeAttributes sideHighlightAttributes = new BasicShapeAttributes(sideAttributes);
		sideHighlightAttributes.setOutlineMaterial(Material.WHITE);
		sideHighlightAttributes.setOutlineOpacity(1);
		return sideHighlightAttributes;
	}

	@Override
	public void setFocus() {
		// Do Nothing
	}

	@Override
	public void dispose() {
		super.dispose();
	}
}