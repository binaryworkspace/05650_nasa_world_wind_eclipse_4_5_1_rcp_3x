package com.binaryworkspace.rcp.wwj.enums;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * An enumeration for NASA World Wind network mode type.
 * <p>
 * The network mode is either offline or online.
 * 
 * @author Chris Ludka
 * 
 */
public enum NetworkModeType {
	/**
	 * The network mode is either offline or online.
	 */
	OFFLINE_MODE("OFFLINE MODE", "OFFLINE", true), //
	ONLINE_MODE("ONLINE MODE", "ONLINE", false); //
	
	private String displayName;
	
	private String mode;
	
	private boolean isOffline;
	
	/**
	 * Provides the display name the network mode state.
	 * 
	 * @return
	 */
	public String getDisplayName() {
		return displayName;
	}
	
	/**
	 * Provides the network mode.
	 * 
	 * @return
	 */
	public String getMode() {
		return mode;
	}
	
	/**
	 * Return true if the network is in offline mode
	 * 
	 * @return
	 */
	public boolean isOffline(){
		return isOffline;
	}
	
	private NetworkModeType(String displayName, String mode, boolean isOffline) {
		this.displayName = displayName;
		this.mode = mode;
		this.isOffline = isOffline;
	}
	
	private static Map<String, NetworkModeType> displayNameMap;
	
	private static Map<String, NetworkModeType> modeMap;
	
	/**
	 * Returns the network mode given the display name specified by the Enum:
	 * NetworkModeType.XYZ.getDisplayName()
	 * 
	 * @param displayName
	 * @return
	 */
	public static NetworkModeType fromDisplayName(String displayName) {
		if (displayNameMap == null) {
			displayNameMap = new LinkedHashMap<String, NetworkModeType>();
			for (NetworkModeType type : NetworkModeType.values()) {
				displayNameMap.put(type.getDisplayName(), type);
			}
		}
		return displayNameMap.get(displayName);
	}
	
	/**
	 * Returns the network mode given the mode specified by the Enum:
	 * NetworkModeType.XYZ.getMode()
	 * 
	 * @param displayName
	 * @return
	 */
	public static NetworkModeType fromModeName(String mode) {
		if (modeMap == null) {
			modeMap = new LinkedHashMap<String, NetworkModeType>();
			for (NetworkModeType type : NetworkModeType.values()) {
				modeMap.put(type.getMode(), type);
			}
		}
		return modeMap.get(mode);
	}

}
