package com.binaryworkspace.rcp.wwj.views;

import java.awt.BorderLayout;

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
import com.binaryworkspace.rcp.wwj.compositions.GlobeLayerSelectionComposition;
import com.binaryworkspace.rcp.wwj.compositions.MapLayerSelectionComposition;
import com.binaryworkspace.rcp.wwj.compositions.OverlayLayerSelectionComposition;
import com.binaryworkspace.rcp.wwj.resources.ColorType;
import com.binaryworkspace.rcp.wwj.resources.Margins;
import com.binaryworkspace.rcp.wwj.resources.SwtColors;

import gov.nasa.worldwind.Model;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.awt.WorldWindowGLCanvas;

/**
 * A NASA World Wind sample view that allows for the selection/deselection of
 * layers.
 * <p>
 * <b>Notes:</b>
 * <ul>
 * <li>The code that embeds the NASA World Wind Java SDK in an SWT Composite is
 * a modified code example originally provided by IBM Developerworks.
 * </ul>
 * 
 * @author Chris Ludka
 *         <p>
 * @see <a href="http://www.ibm.com/developerworks/library/j-wwj/">
 *      http://www.ibm.com/developerworks/library/j-wwj/</a>
 */
public class LayerSelectionView extends ViewPart {
	
	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = LayerSelectionView.class.getName();
	
	private final WorldWindowGLCanvas wwjGLCanvas = new WorldWindowGLCanvas();
	
	private Sash sash;
	
	private CameraPositionComposition cameraPositionComposition;
	
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
		Model wwjModel = (Model) WorldWind.createConfigurationComponent(AVKey.MODEL_CLASS_NAME);
		wwjGLCanvas.setModel(wwjModel);
		
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
		
		// LHS: Globe Layer Selection Composite
		Composite globeLayerSelectionComposite = new Composite(lhs, SWT.NONE);
		globeLayerSelectionComposite.setBackground(SwtColors.color(ColorType.BACKGROUND_DARK));
		globeLayerSelectionComposite.setLayout(new FormLayout());
		formData = new FormData();
		formData.top = new FormAttachment(0, Margins.TOP_MARGIN.margin());
		formData.left = new FormAttachment(0, Margins.LEFT_MARGIN.margin());
		formData.right = new FormAttachment(100, 0);
		globeLayerSelectionComposite.setLayoutData(formData);
		
		// LHS: Globe Layer Selection Composition
		GlobeLayerSelectionComposition globeLayerSelectionComposition = new GlobeLayerSelectionComposition(globeLayerSelectionComposite, wwjGLCanvas);
		globeLayerSelectionComposition.init();
		
		// LHS: Map Layer Selection Composite
		Composite mapLayerSelectionComposite = new Composite(lhs, SWT.NONE);
		mapLayerSelectionComposite.setBackground(SwtColors.color(ColorType.BACKGROUND_DARK));
		mapLayerSelectionComposite.setLayout(new FormLayout());
		formData = new FormData();
		formData.top = new FormAttachment(globeLayerSelectionComposite, Margins.TOP_MARGIN.margin());
		formData.left = new FormAttachment(0, Margins.LEFT_MARGIN.margin());
		formData.right = new FormAttachment(100, 0);
		mapLayerSelectionComposite.setLayoutData(formData);
		
		// LHS: Map Layer Selection Composition
		MapLayerSelectionComposition mapLayerSelectionComposition = new MapLayerSelectionComposition(mapLayerSelectionComposite, wwjGLCanvas);
		mapLayerSelectionComposition.init();
		
		// LHS: Overlay Layer Selection Composite
		Composite overlayLayerSelectionComposite = new Composite(lhs, SWT.NONE);
		overlayLayerSelectionComposite.setBackground(SwtColors.color(ColorType.BACKGROUND_DARK));
		overlayLayerSelectionComposite.setLayout(new FormLayout());
		formData = new FormData();
		formData.top = new FormAttachment(mapLayerSelectionComposite, Margins.TOP_MARGIN.margin());
		formData.left = new FormAttachment(0, Margins.LEFT_MARGIN.margin());
		formData.right = new FormAttachment(100, 0);
		overlayLayerSelectionComposite.setLayoutData(formData);
		
		// LHS: Overlay Layer Selection Composition
		OverlayLayerSelectionComposition overlayLayerSelectionComposition = new OverlayLayerSelectionComposition(overlayLayerSelectionComposite, wwjGLCanvas);
		overlayLayerSelectionComposition.init();
		
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
		 * @see http://www.ibm.com/developerworks/library/j-wwj/
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
	public void setFocus() {
		// Do Nothing
	}
	
	@Override
	public void dispose() {
		cameraPositionComposition.dispose();
		super.dispose();
	}
}