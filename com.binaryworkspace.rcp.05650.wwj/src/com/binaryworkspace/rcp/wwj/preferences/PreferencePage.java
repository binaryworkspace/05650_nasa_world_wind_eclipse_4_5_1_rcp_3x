package com.binaryworkspace.rcp.wwj.preferences;

import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;

import com.binaryworkspace.rcp.wwj.enums.NetworkModeType;
import com.binaryworkspace.rcp.wwj.resources.Margins;

/**
 * Preferences page. All application settings are configured through this page.
 * 
 * @author Chris Ludka
 */
public class PreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	/**
	 * View identifier for the RCP Framework
	 */
	public static final String ID = PreferencePage.class.getName();

	private static final String WWJ_DATA_FILE_STORE_FIELD_LABEL = "WWJ Data File Store:";

	private static final String WWJ_NETWORK_MODE_FIELD_LABEL = "WWJ Network Mode:";

	private final Font TEXT_BOLD = JFaceResources.getFontRegistry().getBold(JFaceResources.DEFAULT_FONT);

	/**
	 * Default constructor.
	 */
	public PreferencePage() {
		// default
	}

	/**
	 * Constructs the instance with the given style.
	 * 
	 * @param style
	 */
	public PreferencePage(int style) {
		super(style);
	}

	/**
	 * Constructs the instance with the given title and style.
	 * 
	 * @param title
	 * @param style
	 */
	public PreferencePage(String title, int style) {
		super(title, style);
	}

	/**
	 * Constructs the instance with the given title, image and style.
	 * 
	 * @param title
	 * @param image
	 * @param style
	 */
	public PreferencePage(String title, ImageDescriptor image, int style) {
		super(title, image, style);
	}

	/**
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	@Override
	public void init(IWorkbench workbench) {
		IPreferenceStore preferenceStore = PlatformUI.getPreferenceStore();
		setPreferenceStore(preferenceStore);
	}

	/**
	 * @see org.eclipse.jface.preference.PreferencePage#createContents(Composite)
	 */
	protected Control createContents(Composite parent) {
		// Base Composite
		Composite baseComposite = new Composite(parent, SWT.NONE);
		baseComposite.setLayout(new FormLayout());
		FormData formData = new FormData();
		formData.top = new FormAttachment(0, 0);
		formData.bottom = new FormAttachment(100, 0);
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100, 0);
		baseComposite.setLayoutData(formData);

		// WWJ Label
		Label wwjLabel = new Label(baseComposite, SWT.LEFT);
		wwjLabel.setText("WWJ Settings:");
		wwjLabel.setFont(TEXT_BOLD);
		formData = new FormData();
		formData.top = new FormAttachment(0, Margins.TOP_MARGIN.margin());
		formData.left = new FormAttachment(0, Margins.LEFT_MARGIN.margin());
		formData.right = new FormAttachment(100, Margins.RIGHT_MARGIN.margin());
		wwjLabel.setLayoutData(formData);

		// WWJ Directory Composite
		Composite wwjDirectoryFieldEditorComposite = new Composite(baseComposite, SWT.NONE);
		formData = new FormData();
		formData.top = new FormAttachment(wwjLabel, Margins.TOP_MARGIN.margin());
		formData.left = new FormAttachment(0, Margins.LEFT_MARGIN.margin());
		formData.right = new FormAttachment(100, Margins.RIGHT_MARGIN.margin());
		wwjDirectoryFieldEditorComposite.setLayoutData(formData);
		wwjDirectoryFieldEditorComposite.setLayout(new GridLayout());

		// WWJ_DATA_FILE_STORE
		DirectoryFieldEditor directoryFieldEditor = new DirectoryFieldEditor(PreferenceKeys.WWJ_DATA_FILE_STORE.getKey(), WWJ_DATA_FILE_STORE_FIELD_LABEL, wwjDirectoryFieldEditorComposite);
		directoryFieldEditor.setPreferenceStore(getPreferenceStore());
		directoryFieldEditor.load();
		addField(directoryFieldEditor);

		// WWJ RadioGroupFieldEditor Composite
		Composite wwjRadioGroupFieldEditorComposite = new Composite(baseComposite, SWT.NONE);
		formData = new FormData();
		formData.top = new FormAttachment(wwjDirectoryFieldEditorComposite, Margins.TOP_MARGIN.margin());
		formData.left = new FormAttachment(0, Margins.LEFT_MARGIN.margin());
		formData.right = new FormAttachment(100, Margins.RIGHT_MARGIN.margin());
		formData.bottom = new FormAttachment(100, Margins.BOTTOM_MARGIN.margin());
		wwjRadioGroupFieldEditorComposite.setLayoutData(formData);
		wwjRadioGroupFieldEditorComposite.setLayout(new GridLayout());

		// WWJ_NETWORK_MODE
		String[][] radioValues = new String[2][2];
		radioValues[0][0] = NetworkModeType.ONLINE_MODE.getDisplayName();
		radioValues[0][1] = NetworkModeType.ONLINE_MODE.getMode();
		radioValues[1][0] = NetworkModeType.OFFLINE_MODE.getDisplayName();
		radioValues[1][1] = NetworkModeType.OFFLINE_MODE.getMode();
		RadioGroupFieldEditor radioGroupFieldEditor = new RadioGroupFieldEditor(PreferenceKeys.WWJ_NETWORK_MODE.getKey(), WWJ_NETWORK_MODE_FIELD_LABEL, 1, radioValues,
				wwjRadioGroupFieldEditorComposite);
		radioGroupFieldEditor.setPreferenceStore(getPreferenceStore());
		radioGroupFieldEditor.load();
		addField(radioGroupFieldEditor);

		return baseComposite;
	}

	/**
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
	 */
	@Override
	protected void createFieldEditors() {
		// Do nothing.
	}
}