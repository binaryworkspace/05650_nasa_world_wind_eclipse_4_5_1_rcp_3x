package com.binaryworkspace.rcp.wwj.compositions;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Scale;

import com.binaryworkspace.rcp.wwj.enums.ScaleIntType;
import com.binaryworkspace.rcp.wwj.resources.ColorType;
import com.binaryworkspace.rcp.wwj.resources.Margins;
import com.binaryworkspace.rcp.wwj.resources.SwtColors;
import com.binaryworkspace.rcp.wwj.resources.SwtFonts;

/**
 * This composition provides controls for a scale that has double values and
 * increments.
 * 
 * @author Chris Ludka
 * 
 */
public class ScaleIntControlComposition implements IComposition {

	private Composite parent;

	private IScaleIntControlComposition ref;

	private CLabel headerLabel;

	private Scale scale;

	private final int MIN_INDEX = 0;

	private final int MAX_INDEX;

	private final int INCREMENT;

	private final int MIN_VALUE;

	private int value;

	private final ScaleIntType TYPE;

	public ScaleIntControlComposition(Composite parent, IScaleIntControlComposition ref, int minValue, int maxValue, int increment, int initialValue, ScaleIntType scaleIntType) {
		this.parent = parent;
		this.ref = ref;
		this.MAX_INDEX = (int) (maxValue / increment);
		this.INCREMENT = increment;
		this.MIN_VALUE = minValue;
		this.value = Math.max(minValue, initialValue);
		this.TYPE = scaleIntType;
	}

	@Override
	public void init() {
		// Header Label
		headerLabel = new CLabel(parent, SWT.NONE);
		headerLabel.setText(TYPE.getPrefixTitle() + " " + String.format("%,d", value) + " " + TYPE.getPostfixTitle());
		headerLabel.setBackground(SwtColors.color(ColorType.BACKGROUND_DARK));
		headerLabel.setForeground(SwtColors.color(ColorType.WHITE));
		headerLabel.setFont(SwtFonts.TEXT_BOLD.font());
		headerLabel.setLayout(new FormLayout());
		FormData formData = new FormData();
		formData.top = new FormAttachment(0, 0);
		formData.left = new FormAttachment(0, 0);
		headerLabel.setLayoutData(formData);

		// Scale
		scale = new Scale(parent, SWT.NONE);
		scale.setBackground(SwtColors.color(ColorType.BACKGROUND_DARK));
		scale.setForeground(SwtColors.color(ColorType.BLACK));
		scale.setMinimum(MIN_INDEX);
		scale.setMaximum(MAX_INDEX - (int) (MIN_VALUE / INCREMENT));
		scale.setIncrement(1);
		scale.setPageIncrement(1);
		scale.setSelection((int) ((value - MIN_VALUE) / INCREMENT));
		scale.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {
				refresh();
			}
		});
		formData = new FormData();
		formData.top = new FormAttachment(headerLabel, Margins.TOP_PAD.margin());
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100, 0);
		scale.setLayoutData(formData);

		// Min Altitude Label
		CLabel minAltitudeLabel = new CLabel(parent, SWT.NONE);
		minAltitudeLabel.setText(String.format("%,d", MIN_VALUE));
		minAltitudeLabel.setBackground(SwtColors.color(ColorType.BACKGROUND_DARK));
		minAltitudeLabel.setForeground(SwtColors.color(ColorType.WHITE));
		minAltitudeLabel.setFont(SwtFonts.TEXT.font());
		minAltitudeLabel.setLayout(new FormLayout());
		formData = new FormData();
		formData.top = new FormAttachment(scale, Margins.SPACER.margin());
		formData.left = new FormAttachment(0, 0);
		minAltitudeLabel.setLayoutData(formData);

		// Max Altitude Label
		CLabel maxAltitudeLabel = new CLabel(parent, SWT.NONE);
		maxAltitudeLabel.setText(String.format("%,d", MAX_INDEX * INCREMENT));
		maxAltitudeLabel.setBackground(SwtColors.color(ColorType.BACKGROUND_DARK));
		maxAltitudeLabel.setForeground(SwtColors.color(ColorType.WHITE));
		maxAltitudeLabel.setFont(SwtFonts.TEXT.font());
		maxAltitudeLabel.setLayout(new FormLayout());
		formData = new FormData();
		formData.top = new FormAttachment(scale, Margins.SPACER.margin());
		formData.right = new FormAttachment(100, 0);
		maxAltitudeLabel.setLayoutData(formData);

		// Refresh
		refresh();
	}

	// Refresh
	private void refresh() {
		value = (scale.getSelection() * INCREMENT + MIN_VALUE);
		headerLabel.setText(TYPE.getPrefixTitle() + " " + String.format("%,d", value) + " " + TYPE.getPostfixTitle());
		ref.refreshScaleIntValue(TYPE, value);
		parent.layout();
	}

	@Override
	public void dispose() {
		// Do Nothing.
	}
}
