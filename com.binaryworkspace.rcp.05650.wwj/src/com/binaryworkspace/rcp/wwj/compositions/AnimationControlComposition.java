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
import org.eclipse.ui.PlatformUI;

import com.binaryworkspace.rcp.wwj.resources.ColorType;
import com.binaryworkspace.rcp.wwj.resources.Margins;
import com.binaryworkspace.rcp.wwj.resources.SwtColors;
import com.binaryworkspace.rcp.wwj.resources.SwtFonts;

/**
 * This composition provides controls for animation properties.
 * 
 * @author Chris Ludka
 * 
 */
public class AnimationControlComposition implements IComposition {

	private Composite parent;

	private IAnimationControlComposition ref;

	/**
	 * Frames Per Second
	 */
	private CLabel framesPerSecondTitle;

	private final int MIN_FRAMES_PER_SECOND = 0;

	private final int MAX_FRAMES_PER_SECOND;

	private final int FRAMES_PER_SECOND_INTERVAL;

	private int framesPerSecond;

	/**
	 * Frame Index
	 */
	private CLabel frameIndexTitle;

	private Scale frameIndexScale;

	private final int MIN_FRAME_INDEX = 0;

	private final int MAX_FRAME_INDEX;

	private final int FRAME_INDEX_INTERVAL = 1;

	private int frameIndex = 0;

	private boolean hasRunnableScheduled = false;

	public AnimationControlComposition(Composite parent, IAnimationControlComposition ref, int maxFramesPerSecond, int framesPerSecondIncrement, int framesPerSecond, int maxFrameIndex,
			boolean isAnimationEnabled) {
		this.parent = parent;
		this.ref = ref;
		this.MAX_FRAMES_PER_SECOND = maxFramesPerSecond;
		this.FRAMES_PER_SECOND_INTERVAL = framesPerSecondIncrement;
		this.framesPerSecond = framesPerSecond;
		this.MAX_FRAME_INDEX = maxFrameIndex;
	}

	@Override
	public void init() {
		// Frames Per Second Scale Title Label
		framesPerSecondTitle = new CLabel(parent, SWT.NONE);
		if (framesPerSecond > 0) {
			framesPerSecondTitle.setForeground(SwtColors.color(ColorType.GREEN));
			framesPerSecondTitle.setText("Animation ON: " + String.format("%,d", framesPerSecond) + " (Updates Per Sec)");
		} else {
			framesPerSecondTitle.setForeground(SwtColors.color(ColorType.RED));
			framesPerSecondTitle.setText("Animation OFF");
		}
		framesPerSecondTitle.setBackground(SwtColors.color(ColorType.BACKGROUND_DARK));
		framesPerSecondTitle.setFont(SwtFonts.TEXT_BOLD.font());
		framesPerSecondTitle.setLayout(new FormLayout());
		FormData formData = new FormData();
		formData.top = new FormAttachment(0, 0);
		formData.left = new FormAttachment(0, 0);
		framesPerSecondTitle.setLayoutData(formData);

		// Frames Per Second Scale
		Scale framesPerSecondScale = new Scale(parent, SWT.NONE);
		framesPerSecondScale.setBackground(SwtColors.color(ColorType.BACKGROUND_DARK));
		framesPerSecondScale.setForeground(SwtColors.color(ColorType.BLACK));
		framesPerSecondScale.setMinimum(MIN_FRAMES_PER_SECOND / FRAMES_PER_SECOND_INTERVAL);
		framesPerSecondScale.setMaximum(MAX_FRAMES_PER_SECOND / FRAMES_PER_SECOND_INTERVAL);
		framesPerSecondScale.setIncrement(1);
		framesPerSecondScale.setPageIncrement(1);
		framesPerSecondScale.setSelection(framesPerSecond / FRAMES_PER_SECOND_INTERVAL);
		framesPerSecondScale.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {
				framesPerSecond = framesPerSecondScale.getSelection() * FRAMES_PER_SECOND_INTERVAL;
				if (framesPerSecond > 0) {
					framesPerSecondTitle.setForeground(SwtColors.color(ColorType.GREEN));
					framesPerSecondTitle.setText("Animation ON: " + String.format("%,d", framesPerSecond) + " (Updates Per Sec)");
				} else {
					framesPerSecondTitle.setForeground(SwtColors.color(ColorType.RED));
					framesPerSecondTitle.setText("Animation OFF");
				}
				parent.layout();

				// Animate
				animate();
			}
		});
		formData = new FormData();
		formData.top = new FormAttachment(framesPerSecondTitle, Margins.TOP_PAD.margin());
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100, 0);
		framesPerSecondScale.setLayoutData(formData);

		// Min Frames Per Second Label
		CLabel minFramesPerSecondLabel = new CLabel(parent, SWT.NONE);
		minFramesPerSecondLabel.setText(String.format("%,d", MIN_FRAMES_PER_SECOND));
		minFramesPerSecondLabel.setBackground(SwtColors.color(ColorType.BACKGROUND_DARK));
		minFramesPerSecondLabel.setForeground(SwtColors.color(ColorType.WHITE));
		minFramesPerSecondLabel.setFont(SwtFonts.TEXT.font());
		minFramesPerSecondLabel.setLayout(new FormLayout());
		formData = new FormData();
		formData.top = new FormAttachment(framesPerSecondScale, Margins.SPACER.margin());
		formData.left = new FormAttachment(0, 0);
		minFramesPerSecondLabel.setLayoutData(formData);

		// Max Frames Per Second Label
		CLabel maxFramesPerSecondLabel = new CLabel(parent, SWT.NONE);
		maxFramesPerSecondLabel.setText(String.format("%,d", MAX_FRAMES_PER_SECOND));
		maxFramesPerSecondLabel.setBackground(SwtColors.color(ColorType.BACKGROUND_DARK));
		maxFramesPerSecondLabel.setForeground(SwtColors.color(ColorType.WHITE));
		maxFramesPerSecondLabel.setFont(SwtFonts.TEXT.font());
		maxFramesPerSecondLabel.setLayout(new FormLayout());
		formData = new FormData();
		formData.top = new FormAttachment(framesPerSecondScale, Margins.SPACER.margin());
		formData.right = new FormAttachment(100, 0);
		maxFramesPerSecondLabel.setLayoutData(formData);

		// Frame Index Scale Title Label
		frameIndexTitle = new CLabel(parent, SWT.NONE);
		frameIndexTitle.setText("Frame Index: " + String.format("%,d", frameIndex));
		frameIndexTitle.setBackground(SwtColors.color(ColorType.BACKGROUND_DARK));
		frameIndexTitle.setForeground(SwtColors.color(ColorType.WHITE));
		frameIndexTitle.setFont(SwtFonts.TEXT_BOLD.font());
		frameIndexTitle.setLayout(new FormLayout());
		formData = new FormData();
		formData.top = new FormAttachment(minFramesPerSecondLabel, Margins.TOP_MARGIN.margin());
		formData.left = new FormAttachment(0, 0);
		frameIndexTitle.setLayoutData(formData);

		// Frame Index Scale
		frameIndexScale = new Scale(parent, SWT.NONE);
		frameIndexScale.setBackground(SwtColors.color(ColorType.BACKGROUND_DARK));
		frameIndexScale.setForeground(SwtColors.color(ColorType.BLACK));
		frameIndexScale.setMinimum(MIN_FRAME_INDEX / FRAME_INDEX_INTERVAL);
		frameIndexScale.setMaximum(MAX_FRAME_INDEX / FRAME_INDEX_INTERVAL);
		frameIndexScale.setIncrement(1);
		frameIndexScale.setPageIncrement(1);
		frameIndexScale.setSelection(frameIndex / FRAME_INDEX_INTERVAL);
		frameIndexScale.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {
				frameIndex = frameIndexScale.getSelection() * FRAME_INDEX_INTERVAL;
				frameIndexTitle.setText("Frame Index: " + String.format("%,d", frameIndex));
				parent.layout();
				ref.refreshFrameIndex(frameIndex);
			}
		});
		formData = new FormData();
		formData.top = new FormAttachment(frameIndexTitle, Margins.TOP_PAD.margin());
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100, 0);
		frameIndexScale.setLayoutData(formData);

		// Min Frame Index Label
		CLabel minFrameIndexLabel = new CLabel(parent, SWT.NONE);
		minFrameIndexLabel.setText(String.format("%,d", MIN_FRAME_INDEX));
		minFrameIndexLabel.setBackground(SwtColors.color(ColorType.BACKGROUND_DARK));
		minFrameIndexLabel.setForeground(SwtColors.color(ColorType.WHITE));
		minFrameIndexLabel.setFont(SwtFonts.TEXT.font());
		minFrameIndexLabel.setLayout(new FormLayout());
		formData = new FormData();
		formData.top = new FormAttachment(frameIndexScale, Margins.SPACER.margin());
		formData.left = new FormAttachment(0, 0);
		minFrameIndexLabel.setLayoutData(formData);

		// Max Frame Index Label
		CLabel maxFrameIndexLabel = new CLabel(parent, SWT.NONE);
		maxFrameIndexLabel.setText(String.format("%,d", MAX_FRAME_INDEX));
		maxFrameIndexLabel.setBackground(SwtColors.color(ColorType.BACKGROUND_DARK));
		maxFrameIndexLabel.setForeground(SwtColors.color(ColorType.WHITE));
		maxFrameIndexLabel.setFont(SwtFonts.TEXT.font());
		maxFrameIndexLabel.setLayout(new FormLayout());
		formData = new FormData();
		formData.top = new FormAttachment(frameIndexScale, Margins.SPACER.margin());
		formData.right = new FormAttachment(100, 0);
		maxFrameIndexLabel.setLayoutData(formData);

		// Layout
		parent.layout();

		// Animate
		animate();
	}

	// Animates the frames
	private void animate() {
		// Return if animation is not enabled
		if (framesPerSecond == 0) {
			hasRunnableScheduled = false;
			return;
		}

		if (hasRunnableScheduled) {
			return;
		}

		// Timed refreshes
		hasRunnableScheduled = true;
		Runnable timer = new Runnable() {

			@Override
			public void run() {
				// Return if animation is not enabled
				if (framesPerSecond == 0) {
					return;
				}

				if (parent.isDisposed()) {
					// Return if parent is disposed
					return;
				}

				// Update the frame index
				frameIndex++;
				if (frameIndex + 1 == MAX_FRAME_INDEX) {
					frameIndex = 0;
				}
				frameIndexTitle.setText("Frame Index: " + String.format("%,d", frameIndex));
				frameIndexScale.setSelection(frameIndex / FRAME_INDEX_INTERVAL);
				parent.layout();

				// Refresh
				ref.refreshFrameIndex(frameIndex);

				// Continue Animation
				hasRunnableScheduled = false;
				animate();
			}
		};

		/*
		 * Schedule the time in terms of miliseconds: 1000 ms / number of frames
		 * per second
		 */
		PlatformUI.getWorkbench().getDisplay().timerExec(1000 / framesPerSecond, timer);
	}

	@Override
	public void dispose() {
		// Do Nothing.
	}
}
