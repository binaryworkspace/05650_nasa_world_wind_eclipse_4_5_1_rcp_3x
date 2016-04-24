package com.binaryworkspace.rcp.wwj.preferences;

import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.PlatformUI;

import com.binaryworkspace.rcp.wwj.enums.NetworkModeType;

/**
 * Enum for all preference keys.
 * 
 * @author Chris Ludka
 */
public enum PreferenceKeys {

	/**
	 * WWJ Data File Store.
	 * <p>
	 * Note the default value is set for Windows 7.
	 */
	WWJ_DATA_FILE_STORE("WWJ Data File Store", "C:\\ProgramData\\WorldWindData\\"),

	/**
	 * WWJ Network Mode.
	 */
	WWJ_NETWORK_MODE("WWJ Network Mode", NetworkModeType.ONLINE_MODE.getMode());

	private String key;

	private boolean isRecordRelationship;

	private final IPreferenceStore preferenceStore = PlatformUI.getPreferenceStore();

	private PreferenceKeys(String key, String defaultValue) {
		preferenceStore.setDefault(key, defaultValue);
		this.key = key;
	}

	private PreferenceKeys(String key, Boolean isRecordRelationship) {
		this.key = key;
		this.isRecordRelationship = isRecordRelationship;
		if (this.isRecordRelationship) {
			preferenceStore.setDefault(key, Boolean.FALSE.toString());
		}
	}

	public static Set<PreferenceKeys> getRecordRelationshipPreferenceKeys() {
		Set<PreferenceKeys> result = new LinkedHashSet<PreferenceKeys>();
		for (PreferenceKeys preferenceKey : PreferenceKeys.values()) {
			if (preferenceKey.isRecordRelationship) {
				result.add(preferenceKey);
			}
		}
		return result;
	}

	private PreferenceKeys(String key) {
		this.key = key;
	}

	/**
	 * Returns the key for this preference.
	 * 
	 * @return lookup key
	 */
	public String getKey() {
		return this.key;
	}
}
