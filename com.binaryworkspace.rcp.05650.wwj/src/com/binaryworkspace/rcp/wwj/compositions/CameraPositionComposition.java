package com.binaryworkspace.rcp.wwj.compositions;

import java.text.DecimalFormat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.PlatformUI;

import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwind.event.PositionEvent;
import gov.nasa.worldwind.event.PositionListener;
import com.binaryworkspace.rcp.wwj.resources.ColorType;
import com.binaryworkspace.rcp.wwj.resources.Margins;
import com.binaryworkspace.rcp.wwj.resources.SwtColors;
import com.binaryworkspace.rcp.wwj.resources.SwtFonts;

/**
 * Provides a camera position dashboard using labels based on the NASA World
 * Wind model.
 * 
 * @author Chris Ludka
 */
public class CameraPositionComposition implements IComposition {

	private Label latHeaderLabel;
	
	private Label latLabel;
	
	private Label lonHeaderLabel;
	
	private Label lonLabel;
	
	private Label elevHeaderLabel;
	
	private Label elevLabel;
	
	private Label headingHeaderLabel;
	
	private Label headingLabel;
	
	private Label pitchHeaderLabel;
	
	private Label pitchLabel;
	
	private WorldWindowGLCanvas wwjGLCanvas;
	
	private Composite parent;
	
	private final int DECIMAL_PRECISION = 4;
	
	private final DecimalFormat DECIMAL_FORMATTER = new DecimalFormat("#,###");
		
	public CameraPositionComposition(Composite parent, WorldWindowGLCanvas wwjGLCanvas){
		this.parent = parent;
		this.wwjGLCanvas = wwjGLCanvas;
	}
	
	@Override
	public void init() {		
		// LAT-header
		latHeaderLabel = new Label(parent, SWT.NONE);
		latHeaderLabel.setBackground(SwtColors.color(ColorType.BLACK));
		latHeaderLabel.setForeground(SwtColors.color(ColorType.WHITE));
		latHeaderLabel.setFont(SwtFonts.TEXT_BOLD.font());
		latHeaderLabel.setText("Latitude: ");
		FormData formData = new FormData();
		formData.top = new FormAttachment(0, 0);
		formData.left = new FormAttachment(0, Margins.LEFT_MARGIN.margin());
		latHeaderLabel.setLayoutData(formData);
		
		// LAT
		latLabel = new Label(parent, SWT.NONE);
		latLabel.setBackground(SwtColors.color(ColorType.BLACK));
		latLabel.setForeground(SwtColors.color(ColorType.WHITE));
		latLabel.setText("");
		formData = new FormData();
		formData.top = new FormAttachment(0, 0);
		formData.left = new FormAttachment(latHeaderLabel, Margins.LEFT_PAD.margin());
		latLabel.setLayoutData(formData);
		
		// LON-header
		lonHeaderLabel = new Label(parent, SWT.NONE);
		lonHeaderLabel.setBackground(SwtColors.color(ColorType.BLACK));
		lonHeaderLabel.setForeground(SwtColors.color(ColorType.WHITE));
		lonHeaderLabel.setFont(SwtFonts.TEXT_BOLD.font());
		lonHeaderLabel.setText("Longitude: ");
		formData = new FormData();
		formData.top = new FormAttachment(0, 0);
		formData.left = new FormAttachment(latLabel, Margins.LEFT_MARGIN.margin());
		lonHeaderLabel.setLayoutData(formData);
		
		// LON
		lonLabel = new Label(parent, SWT.NONE);
		lonLabel.setBackground(SwtColors.color(ColorType.BLACK));
		lonLabel.setForeground(SwtColors.color(ColorType.WHITE));
		lonLabel.setText("");
		formData = new FormData();
		formData.top = new FormAttachment(0, 0);
		formData.left = new FormAttachment(lonHeaderLabel, Margins.LEFT_PAD.margin());
		lonLabel.setLayoutData(formData);
		
		// ELEV-header
		elevHeaderLabel = new Label(parent, SWT.NONE);
		elevHeaderLabel.setBackground(SwtColors.color(ColorType.BLACK));
		elevHeaderLabel.setForeground(SwtColors.color(ColorType.WHITE));
		elevHeaderLabel.setFont(SwtFonts.TEXT_BOLD.font());
		elevHeaderLabel.setText("Elevation: ");
		formData = new FormData();
		formData.top = new FormAttachment(0, 0);
		formData.left = new FormAttachment(lonLabel, Margins.LEFT_MARGIN.margin());
		elevHeaderLabel.setLayoutData(formData);
		
		// ELEV
		elevLabel = new Label(parent, SWT.NONE);
		elevLabel.setBackground(SwtColors.color(ColorType.BLACK));
		elevLabel.setForeground(SwtColors.color(ColorType.WHITE));
		elevLabel.setText("");
		formData = new FormData();
		formData.top = new FormAttachment(0, 0);
		formData.left = new FormAttachment(elevHeaderLabel, Margins.LEFT_PAD.margin());
		elevLabel.setLayoutData(formData);
		
		// Heading-header
		headingHeaderLabel = new Label(parent, SWT.NONE);
		headingHeaderLabel.setBackground(SwtColors.color(ColorType.BLACK));
		headingHeaderLabel.setForeground(SwtColors.color(ColorType.WHITE));
		headingHeaderLabel.setFont(SwtFonts.TEXT_BOLD.font());
		headingHeaderLabel.setText("Heading: ");
		formData = new FormData();
		formData.top = new FormAttachment(0, 0);
		formData.left = new FormAttachment(elevLabel, Margins.LEFT_MARGIN.margin());
		headingHeaderLabel.setLayoutData(formData);
		
		// Heading
		headingLabel = new Label(parent, SWT.NONE);
		headingLabel.setBackground(SwtColors.color(ColorType.BLACK));
		headingLabel.setForeground(SwtColors.color(ColorType.WHITE));
		headingLabel.setText("");
		formData = new FormData();
		formData.top = new FormAttachment(0, 0);
		formData.left = new FormAttachment(headingHeaderLabel, Margins.LEFT_PAD.margin());
		headingLabel.setLayoutData(formData);
		
		// Pitch-header
		pitchHeaderLabel = new Label(parent, SWT.NONE);
		pitchHeaderLabel.setBackground(SwtColors.color(ColorType.BLACK));
		pitchHeaderLabel.setForeground(SwtColors.color(ColorType.WHITE));
		pitchHeaderLabel.setFont(SwtFonts.TEXT_BOLD.font());
		pitchHeaderLabel.setText("Pitch: ");
		formData = new FormData();
		formData.top = new FormAttachment(0, 0);
		formData.left = new FormAttachment(headingLabel, Margins.LEFT_MARGIN.margin());
		pitchHeaderLabel.setLayoutData(formData);
		
		// Pitch
		pitchLabel = new Label(parent, SWT.NONE);
		pitchLabel.setBackground(SwtColors.color(ColorType.BLACK));
		pitchLabel.setForeground(SwtColors.color(ColorType.WHITE));
		pitchLabel.setText("");
		formData = new FormData();
		formData.top = new FormAttachment(0, 0);
		formData.left = new FormAttachment(pitchHeaderLabel, Margins.LEFT_PAD.margin());
		pitchLabel.setLayoutData(formData);
		
		// Listen for camera moves to update LAT-LON-ELEV controls
		this.wwjGLCanvas.addPositionListener(new PositionListener() {
			
			@Override
			public void moved(PositionEvent event) {
				PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {
					
					@Override
					public void run() {
						refresh();
					}
				});
			}
		});
		
		// Refresh
		refresh();
	}

	/**
	 * Refreshes the labels that show the camera position.
	 */
	public void refresh() {
		// Validate the application is still running
		if(parent.isDisposed()){
			return;
		}
		
		/**
		 * Refresh the labels by first getting the camera positions.
		 * <p>
		 * Note that elevation unit of measure is meters.
		 * 
		 * <pre>
		 * @see http://forum.worldwindcentral.com/showthread.php?18662-Elevation-Unit-of-Measure\
		 * </pre>
		 */
		String lat = wwjGLCanvas.getView().getCurrentEyePosition().getLatitude().toDecimalDegreesString(DECIMAL_PRECISION);
		String lon = wwjGLCanvas.getView().getCurrentEyePosition().getLongitude().toDecimalDegreesString(DECIMAL_PRECISION);
		double elevValue = wwjGLCanvas.getView().getCurrentEyePosition().getElevation();
		String elev = DECIMAL_FORMATTER.format(elevValue);
		String heading = wwjGLCanvas.getView().getHeading().toDecimalDegreesString(DECIMAL_PRECISION);
		String pitch = wwjGLCanvas.getView().getPitch().toDecimalDegreesString(DECIMAL_PRECISION);
		
		// Update UI controls
		latLabel.setText(lat);
		lonLabel.setText(lon);
		elevLabel.setText(elev + " m");
		headingLabel.setText(heading);
		pitchLabel.setText(pitch);
		parent.layout(true, true);
	}
	
	@Override
	public void dispose() {
		// Do Nothing.
	}
}
