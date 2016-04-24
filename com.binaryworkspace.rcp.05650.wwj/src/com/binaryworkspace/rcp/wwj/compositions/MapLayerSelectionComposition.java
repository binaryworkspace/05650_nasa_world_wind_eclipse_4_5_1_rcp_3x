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
import gov.nasa.worldwind.layers.Layer;

import com.binaryworkspace.rcp.wwj.enums.MapLayerType;
import com.binaryworkspace.rcp.wwj.resources.ColorType;
import com.binaryworkspace.rcp.wwj.resources.Margins;
import com.binaryworkspace.rcp.wwj.resources.SwtColors;
import com.binaryworkspace.rcp.wwj.resources.SwtFonts;

/**
 * This composition provides a set of radio buttons that allows exactly one map
 * layer to be selected at a time.
 * 
 * @author Chris Ludka
 * 
 */
public class MapLayerSelectionComposition implements IComposition {

	private Composite parent;

	private Map<MapLayerType, Button> mapLayerTypeToButtonMap = new LinkedHashMap<MapLayerType, Button>();

	/**
	 * This layer name to layer map is used for fast lookup of a layer. This is
	 * an informal implementation that relies on the fact that the display name
	 * of MapLayerType will be the same as the named layer in the wwj model.
	 */
	private Map<MapLayerType, Layer> mapLayerTypeToLayerMap = new LinkedHashMap<MapLayerType, Layer>();

	public MapLayerSelectionComposition(Composite parent, WorldWindowGLCanvas wwjGLCanvas) {
		this.parent = parent;

		// Set up layer map to enable fast lookup
		gov.nasa.worldwind.Model wwjModel = wwjGLCanvas.getModel();
		for (Layer layer : wwjModel.getLayers()) {
			for (MapLayerType mapLayerType : MapLayerType.values()) {
				if (layer.getName().equals(mapLayerType.getDisplayName())) {
					mapLayerTypeToLayerMap.put(mapLayerType, layer);
					break;
				}
			}
		}
	}

	@Override
	public void init() {
		// Header Label
		CLabel header = new CLabel(parent, SWT.NONE);
		header.setText("Map Layer");
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
		for (MapLayerType mapLayerType : MapLayerType.values()) {
			// Radio Button
			Button radioButton = new Button(parent, SWT.RADIO);
			radioButton.setForeground(SwtColors.color(ColorType.WHITE));
			radioButton.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent event) {
					refresh();
				};
			});
			formData = new FormData();
			formData.top = new FormAttachment(prevControl, Margins.TOP_PAD.margin());
			formData.left = new FormAttachment(0, 0);
			radioButton.setLayoutData(formData);

			// Set button default state
			radioButton.setSelection(mapLayerType.isOnLoad());

			// CLabel
			CLabel cLabel = new CLabel(parent, SWT.NONE);
			cLabel.setText(mapLayerType.getDisplayName());
			cLabel.setBackground(SwtColors.color(ColorType.BACKGROUND_DARK));
			cLabel.setForeground(SwtColors.color(ColorType.WHITE));
			cLabel.addListener(SWT.MouseUp, new Listener() {

				@Override
				public void handleEvent(Event event) {
					// Get CLabel
					CLabel cLabel = (CLabel) event.widget;

					// Update button selection state
					for (MapLayerType mapLayerType : MapLayerType.values()) {
						Button button = mapLayerTypeToButtonMap.get(mapLayerType);
						button.setSelection(mapLayerType.getDisplayName().equals(cLabel.getText()));
					}

					// Refresh
					refresh();
				}
			});
			cLabel.setLayout(new FormLayout());
			formData = new FormData();
			formData.top = new FormAttachment(radioButton, 0, SWT.CENTER);
			formData.left = new FormAttachment(radioButton, Margins.LEFT_PAD.margin());
			cLabel.setLayoutData(formData);

			// Previous Control Reference for layout
			prevControl = radioButton;

			// Map the name to the button for reference on click
			mapLayerTypeToButtonMap.put(mapLayerType, radioButton);
		}
		parent.layout();

		// Refresh
		refresh();
	}

	private void refresh() {
		// Update layer state
		for (MapLayerType mapLayerType : MapLayerType.values()) {
			// Get layer and button
			Layer layer = mapLayerTypeToLayerMap.get(mapLayerType);
			Button button = mapLayerTypeToButtonMap.get(mapLayerType);

			// Set layer state based on button selection state
			layer.setEnabled(button.getSelection());
		}
	}

	@Override
	public void dispose() {
		// Do Nothing.
	}
}
