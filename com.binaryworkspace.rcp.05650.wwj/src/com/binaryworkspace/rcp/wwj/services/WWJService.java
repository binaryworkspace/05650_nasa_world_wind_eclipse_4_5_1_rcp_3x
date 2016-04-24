package com.binaryworkspace.rcp.wwj.services;

import gov.nasa.worldwind.WorldWind;

import java.io.File;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.binaryworkspace.rcp.wwj.enums.NetworkModeType;
import com.binaryworkspace.rcp.wwj.preferences.PreferenceKeys;

/**
 * This service provides access to WWJService to assist with global processes,
 * preferences and settings.
 * <p>
 * The service is registered service factory {@link WWJServiceFactory} and be
 * accessed after the RCP initialization by calling:
 * <tt>ServiceProvider.getService(WWJService.class);</tt>
 * <p>
 * Note: Service creation is a lazy loading process.
 * 
 * @author Chris Ludka
 * 
 */
public class WWJService {

	private IPreferenceStore preferencesStore;

	public WWJService() {
		// Set up the IPreferenceStore.
		preferencesStore = PlatformUI.getPreferenceStore();

		/*
		 * Initialize preferences. If this is the first time the user is running
		 * the Application or if a preset for a preference does not exist, then
		 * provide a dialog box to guide the user to a proper settings. That
		 * value will be stored in the preferences page for the user as well.
		 * Finally, to make sure the preferences was set correctly and that the
		 * application is using data out of the preferences.
		 */
		String scenarioInputDirectory = preferencesStore.getString(PreferenceKeys.WWJ_DATA_FILE_STORE.getKey());
		File scenarioInputDirectoryFile = new File(scenarioInputDirectory);
		if (!scenarioInputDirectoryFile.isDirectory()) {
			Shell activeShell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
			DirectoryDialog dirDialog = new DirectoryDialog(activeShell);
			dirDialog.setText("Set the OFFLINE Data File Store Directory:");
			String selectedDirectory = dirDialog.open();
			preferencesStore.setValue(PreferenceKeys.WWJ_DATA_FILE_STORE.getKey(), selectedDirectory);
		}

		// Update Preferences
		updatePreferences();

		/*
		 * Since preferences can change during initialization (for example the
		 * walk through of setting up folders the first time the application is
		 * run) do not add a property change listener until after all
		 * initializations have completed. After initialization, listen for
		 * changes that may require additional updates to other preference
		 * dependent resources.
		 */
		preferencesStore.addPropertyChangeListener(new IPropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent event) {
				// Update Preferences
				updatePreferences();
			}
		});
	}

	// Helper function to update preferences
	private void updatePreferences() {
		// Get preference values
		String dataFileStore = preferencesStore.getString(PreferenceKeys.WWJ_DATA_FILE_STORE.getKey());
		String networkMode = preferencesStore.getString(PreferenceKeys.WWJ_NETWORK_MODE.getKey());

		// Data File Store
		if (!WorldWind.getDataFileStore().getLocations().contains(new File(dataFileStore))) {
			/*
			 * Since the current data file store does not contain the new
			 * location, add the new location.
			 */
			WorldWind.getDataFileStore().addLocation(dataFileStore, true);
		}

		// Network Mode
		NetworkModeType networkModeType = NetworkModeType.fromModeName(networkMode);
		WorldWind.setOfflineMode(networkModeType.isOffline());
	}
}
