package com.binaryworkspace.rcp.wwj.compositions;

import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwind.layers.LatLonGraticuleLayer;
import gov.nasa.worldwind.layers.Layer;

import com.binaryworkspace.rcp.wwj.enums.OverlayLayerType;
import com.binaryworkspace.rcp.wwj.resources.ColorType;
import com.binaryworkspace.rcp.wwj.resources.Margins;
import com.binaryworkspace.rcp.wwj.resources.SwtColors;
import com.binaryworkspace.rcp.wwj.resources.SwtFonts;
import com.binaryworkspace.rcp.wwj.util.WwjUtils;

/**
 * This composition provides a set of checkbox buttons that allows zero to many
 * overlay layers to be selected at a time.
 * 
 * @author Chris Ludka
 * 
 */
public class OverlayLayerSelectionComposition implements IComposition {

	private Composite parent;

	// Graticule Layer is not predefined and must be dynamically added
	private Layer latLonGraticuleLayer = null;

	private Map<OverlayLayerType, Button> overlayLayerTypeToButtonMap = new LinkedHashMap<OverlayLayerType, Button>();

	/**
	 * This layer name to layer map is used for fast lookup of a layer. This is
	 * an informal implementation that relies on the fact that the display name
	 * of OverlayLayerType will be the same as the named layer in the wwj model.
	 */
	private Map<OverlayLayerType, Layer> overlayLayerTypeToLayerMap = new LinkedHashMap<OverlayLayerType, Layer>();

	public OverlayLayerSelectionComposition(Composite parent, WorldWindowGLCanvas wwjGLCanvas) {
		this.parent = parent;

		// Set up layer map to enable fast lookup
		gov.nasa.worldwind.Model wwjModel = wwjGLCanvas.getModel();
		for (Layer layer : wwjModel.getLayers()) {
			for (OverlayLayerType overlayLayerType : OverlayLayerType.values()) {
				if (layer.getName().equals(overlayLayerType.getDisplayName())) {
					overlayLayerTypeToLayerMap.put(overlayLayerType, layer);
					break;
				}
			}
		}

		// Dynamically add LatLonGraticuleLayer
		try {
			latLonGraticuleLayer = (Layer) LatLonGraticuleLayer.class.newInstance();
			WwjUtils.insertBeforeCompass(wwjModel, latLonGraticuleLayer);
			latLonGraticuleLayer.setEnabled(false);
			overlayLayerTypeToLayerMap.put(OverlayLayerType.LAT_LON_GRATICULE, latLonGraticuleLayer);
		} catch (Exception e) {
			System.out.println("Can't get a graticule layer " + e);
		}
	}

	@Override
	public void init() {
		// Header Label
		CLabel header = new CLabel(parent, SWT.NONE);
		header.setText("Overlay Layers");
		header.setBackground(SwtColors.color(ColorType.BACKGROUND_DARK));
		header.setForeground(SwtColors.color(ColorType.WHITE));
		header.setFont(SwtFonts.TEXT_BOLD.font());
		header.setLayout(new FormLayout());
		FormData formData = new FormData();
		formData.top = new FormAttachment(0, 0);
		formData.left = new FormAttachment(0, 0);
		header.setLayoutData(formData);

		/**
		 * The SWT Button control does not allow the text (foreground) color to
		 * be changed. As a workaround the button text is left empty and a
		 * CLabel is added to display text. Handlers therefore must be added to
		 * both the Button and CLabel controls.
		 * 
		 * <pre>
		 * http://stackoverflow.com/questions/4747562/how-to-set-swt-button-foreground-color
		 * </pre>
		 */
		Control prevControl = header;
		for (OverlayLayerType overlayLayerType : OverlayLayerType.values()) {
			// Check Button
			Button checkButton = new Button(parent, SWT.CHECK);
			checkButton.setForeground(SwtColors.color(ColorType.WHITE));
			checkButton.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent event) {
					refresh();
				};
			});
			formData = new FormData();
			formData.top = new FormAttachment(prevControl, Margins.TOP_PAD.margin());
			formData.left = new FormAttachment(0, 0);
			checkButton.setLayoutData(formData);

			// Set button default state
			checkButton.setSelection(overlayLayerType.isOnLoad());

			// CLabel
			CLabel cLabel = new CLabel(parent, SWT.NONE);
			cLabel.setText(overlayLayerType.getDisplayName());
			cLabel.setBackground(SwtColors.color(ColorType.BACKGROUND_DARK));
			cLabel.setForeground(SwtColors.color(ColorType.WHITE));
			cLabel.addListener(SWT.MouseUp, new Listener() {

				@Override
				public void handleEvent(Event event) {
					// Get CLabel
					CLabel cLabel = (CLabel) event.widget;

					// Update button selection state
					OverlayLayerType overlayLayerType = OverlayLayerType.fromDisplayName(cLabel.getText());
					Button button = overlayLayerTypeToButtonMap.get(overlayLayerType);
					button.setSelection(!button.getSelection());

					// Refresh
					refresh();
				}
			});
			cLabel.setLayout(new FormLayout());
			formData = new FormData();
			formData.top = new FormAttachment(checkButton, 0, SWT.CENTER);
			formData.left = new FormAttachment(checkButton, Margins.LEFT_PAD.margin());
			cLabel.setLayoutData(formData);

			// Previous Control Reference for layout
			prevControl = checkButton;

			// Map the name to the button for reference on click
			overlayLayerTypeToButtonMap.put(overlayLayerType, checkButton);
		}
		parent.layout();

		// Refresh
		refresh();
	}

	private void refresh() {
		// Update layer state
		for (OverlayLayerType overlayLayerType : OverlayLayerType.values()) {
			// Get layer and button
			Layer layer = overlayLayerTypeToLayerMap.get(overlayLayerType);
			Button button = overlayLayerTypeToButtonMap.get(overlayLayerType);

			// Validation Check
			if (button == null) {
				System.out.println("Button for " + overlayLayerType.getDisplayName() + " == null");
				continue;
			}

			// Validation Check
			if (layer == null) {
				System.out.println("Layer for " + overlayLayerType.getDisplayName() + " == null");
				continue;
			}

			// Set layer state based on button selection state
			layer.setEnabled(button.getSelection());
		}
	}

	@Override
	public void dispose() {
		// Do Nothing.
	}
}
