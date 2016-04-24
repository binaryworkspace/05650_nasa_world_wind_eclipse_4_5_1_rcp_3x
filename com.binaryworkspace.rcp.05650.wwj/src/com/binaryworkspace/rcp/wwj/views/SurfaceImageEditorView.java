package com.binaryworkspace.rcp.wwj.views;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import com.binaryworkspace.rcp.wwj.compositions.CameraPositionComposition;
import com.binaryworkspace.rcp.wwj.models.SineWaveSurfaceModel;
import com.binaryworkspace.rcp.wwj.models.SolidColorSurfaceModel;
import com.binaryworkspace.rcp.wwj.resources.AwtColorScheme;
import com.binaryworkspace.rcp.wwj.resources.ColorType;
import com.binaryworkspace.rcp.wwj.resources.SwtColors;
import com.binaryworkspace.rcp.wwj.util.SectorSurfaceImageEditor;
import com.binaryworkspace.rcp.wwj.util.WwjUtils;

import gov.nasa.worldwind.Model;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.SurfaceImage;

/**
 * NASA World Wind example that demonstrates how to overlay an edit the size and
 * shape of a surface image by using a corner drag-and-drop interface.
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
 * @see <a href="http://www.ibm.com/developerworks/library/j-wwj/"> http://www.
 *      ibm.com/developerworks/library/j-wwj/</a>
 * @see <a href=
 *      "https://github.com/nasa/World-Wind-Java/blob/master/WorldWind/src/gov/nasa/worldwindx/examples/SurfaceImages.java">
 *      https://github.com/nasa/World-Wind-Java/blob/master/WorldWind/src/gov/
 *      nasa/worldwindx/examples/SurfaceImages.java</a>
 */
public class SurfaceImageEditorView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = SurfaceImageEditorView.class.getName();

	private final WorldWindowGLCanvas wwjGLCanvas = new WorldWindowGLCanvas();

	private final Model wwjModel = (Model) WorldWind.createConfigurationComponent(AVKey.MODEL_CLASS_NAME);

	private final String SINE_SURFACE_IMAGE_LAYER_NAME = "SINE SURFACE IMAGE EDITOR";

	private final String SOLID_SURFACE_IMAGE_LAYER_NAME = "SOLID SURFACE IMAGE EDITOR";

	private SurfaceImage sineSurfaceImage = new SurfaceImage();

	private SurfaceImage solidSurfaceImage = new SurfaceImage();

	private SineWaveSurfaceModel sineWaveSurfaceModel = new SineWaveSurfaceModel();

	private SolidColorSurfaceModel solidColorSurfaceModel = new SolidColorSurfaceModel();

	private RenderableLayer activeSineSurfaceImageLayer = null;

	private RenderableLayer activeSolidSurfaceImageLayer = null;

	private CameraPositionComposition cameraPositionComposition;

	private final double CORNER_ALT = 10e3;

	private final double LAT_SINE_SURFACE_IMAGE = 38.0;

	private final double LON_SINE_SURFACE_IMAGE = -122.5;

	private final double LAT_SOLID_SURFACE_IMAGE = 38.0;

	private final double LON_SOLID_SURFACE_IMAGE = -100.0;

	private final double DELTA_LAT = 5.0;

	private final double DELTA_LON = 5.0;

	private final int HEIGHT_SINE_SURFACE_IMAGE = 100;

	private final int WIDTH_SINE_SURFACE_IMAGE = 100;

	private final int HEIGHT_SOLID_SURFACE_IMAGE = 100;

	private final int WIDTH_SOLID_SURFACE_IMAGE = 100;

	private final double GRID_SIZE = 1.0;

	private final double ALPHA_SINE_SURFACE_IMAGE = 0.5;

	private final double ALPHA_SOLID_SURFACE_IMAGE = 1.0;

	private final int MAX_FRAME_INDEX = 100;

	private final int FRAME_INDEX = 50;

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
		WwjUtils.initLayerTypes(wwjModel);
		wwjGLCanvas.setModel(wwjModel);

		// Init wwjModel
		WwjUtils.initLayerTypes(wwjModel);

		// Refresh
		refresh();
	}

	// Refresh the Surface Image
	private void refresh() {
		refreshSineSurfaceImage();
		refreshSolidSurfaceImage();
	}

	private void refreshSineSurfaceImage() {
		// Establish the grid corners
		double lowerLat = LAT_SINE_SURFACE_IMAGE - DELTA_LAT;
		double upperLat = LAT_SINE_SURFACE_IMAGE + DELTA_LAT;
		double lowerLon = LON_SINE_SURFACE_IMAGE - DELTA_LON;
		double upperLon = LON_SINE_SURFACE_IMAGE + DELTA_LON;
		List<Position> cornerPositionList = new ArrayList<Position>();
		cornerPositionList.add(Position.fromDegrees(lowerLat, lowerLon, CORNER_ALT));
		cornerPositionList.add(Position.fromDegrees(lowerLat, upperLon, CORNER_ALT));
		cornerPositionList.add(Position.fromDegrees(upperLat, upperLon, CORNER_ALT));
		cornerPositionList.add(Position.fromDegrees(upperLat, lowerLon, CORNER_ALT));

		// Image
		BufferedImage image = sineWaveSurfaceModel.getBufferedImage(WIDTH_SINE_SURFACE_IMAGE, HEIGHT_SINE_SURFACE_IMAGE, (float) GRID_SIZE, MAX_FRAME_INDEX, FRAME_INDEX,
				(float) ALPHA_SINE_SURFACE_IMAGE);
		sineSurfaceImage.setImageSource(image, cornerPositionList);

		// Renderable Layer
		RenderableLayer renderableLayer = new RenderableLayer();
		renderableLayer.setName(SINE_SURFACE_IMAGE_LAYER_NAME);
		renderableLayer.setPickEnabled(true);
		renderableLayer.addRenderable(sineSurfaceImage);

		// Update Active Layer
		if (activeSineSurfaceImageLayer == null) {
			WwjUtils.insertBeforeCompass(wwjModel, renderableLayer);
		} else {
			WwjUtils.replaceLayers(wwjModel, activeSineSurfaceImageLayer, renderableLayer);
		}
		activeSineSurfaceImageLayer = renderableLayer;

		// Editor
		SectorSurfaceImageEditor sectorSurfaceImageEditor = new SectorSurfaceImageEditor(wwjGLCanvas, sineSurfaceImage);
		sectorSurfaceImageEditor.setIsEditable(true);
	}

	private void refreshSolidSurfaceImage() {
		// Establish the grid corners
		double lowerLat = LAT_SOLID_SURFACE_IMAGE - DELTA_LAT;
		double upperLat = LAT_SOLID_SURFACE_IMAGE + DELTA_LAT;
		double lowerLon = LON_SOLID_SURFACE_IMAGE - DELTA_LON;
		double upperLon = LON_SOLID_SURFACE_IMAGE + DELTA_LON;
		List<Position> cornerPositionList = new ArrayList<Position>();
		cornerPositionList.add(Position.fromDegrees(lowerLat, lowerLon, CORNER_ALT));
		cornerPositionList.add(Position.fromDegrees(lowerLat, upperLon, CORNER_ALT));
		cornerPositionList.add(Position.fromDegrees(upperLat, upperLon, CORNER_ALT));
		cornerPositionList.add(Position.fromDegrees(upperLat, lowerLon, CORNER_ALT));

		// Image
		BufferedImage image = solidColorSurfaceModel.getBufferedImage(WIDTH_SOLID_SURFACE_IMAGE, HEIGHT_SOLID_SURFACE_IMAGE,
				AwtColorScheme.getVisibleSpectrum((float) FRAME_INDEX / (float) MAX_FRAME_INDEX));
		solidSurfaceImage.setImageSource(image, cornerPositionList);
		solidSurfaceImage.setOpacity(ALPHA_SOLID_SURFACE_IMAGE);

		// Renderable Layer
		RenderableLayer renderableLayer = new RenderableLayer();
		renderableLayer.setName(SOLID_SURFACE_IMAGE_LAYER_NAME);
		renderableLayer.setPickEnabled(true);
		renderableLayer.addRenderable(solidSurfaceImage);

		// Update Active Layer
		if (activeSolidSurfaceImageLayer == null) {
			WwjUtils.insertBeforeCompass(wwjModel, renderableLayer);
		} else {
			WwjUtils.replaceLayers(wwjModel, activeSolidSurfaceImageLayer, renderableLayer);
		}
		activeSolidSurfaceImageLayer = renderableLayer;

		// Editor
		SectorSurfaceImageEditor sectorSurfaceImageEditor = new SectorSurfaceImageEditor(wwjGLCanvas, solidSurfaceImage);
		sectorSurfaceImageEditor.setIsEditable(true);
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