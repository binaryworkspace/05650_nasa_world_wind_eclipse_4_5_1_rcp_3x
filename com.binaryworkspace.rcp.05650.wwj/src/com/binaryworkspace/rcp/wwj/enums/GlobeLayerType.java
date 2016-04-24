package com.binaryworkspace.rcp.wwj.enums;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * An enumeration for NASA World Wind globe layer types. The globe image layer
 * is viewed above the map image layer. The globe image layer is used to depict
 * the aggregate features of Earth from a distant point in space. Once the Earth
 * is zoom in upon, the global image layer begins swapping for map image layer
 * data if available. In general only one type of globe layer can be viewed at
 * one time.
 * <p>
 * In general, layer types are defined external to World Wind in the
 * 'worldwind.layers.xml'. Note that all available data streams are specified in
 * the default file and can be added. When specified the properties
 * actuate="onLoad" and actuate="onRequest" to set the initial state of the
 * layer.isEnabled() to 'true' and 'false' respectively. This enum can be used
 * to override the xml file settings.
 * <p>
 * NOTE: The display name for this enums should not be changed as they match the
 * WWJ defined names. This forms an informal reference between the layer display
 * name and the named layer in the WWJ model that is useful in use case
 * implementations.
 * <p>
 * NOTE: Only one globe layer can be active at a time. Therefore, when setting
 * onLoad be sure that exactly 1 layer is set to 'true'.
 * 
 * <pre>
 * &#64;see http://forum.worldwindcentral.com/showthread.php?24609-programatically-adding-USGS-Digital-Ortho
 * </pre>
 * 
 * @author Chris Ludka
 * 
 */
public enum GlobeLayerType {
	/**
	 * The "NASA Blue Marble Image": Assumed to be the original Blue Marble
	 * Image.
	 * <p>
	 * "Blue Marble May 2004": Blue Marble Next Generation (BMNG) is a 500-meter
	 * image that was acquired in 2004.
	 * 
	 * <pre>
	 * &#64;see http://www.worldwindcentral.com/wiki/Blue_Marble_Next_Generation
	 * </pre>
	 */
	NASA_BLUE_MARBLE_IMAGE("NASA Blue Marble Image", false), //
	BLUE_MARBLE_MAY_2004("Blue Marble May 2004", true), //

	/**
	 * Earth Night Texture
	 */
	EARTH_AT_NIGHT("Earth at Night", false);

	private String displayName;

	private boolean onLoad;

	/**
	 * Provides the display name the layer.
	 * 
	 * @return
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Return true if the layer is loaded by default.
	 * 
	 * @return
	 */
	public boolean isOnLoad() {
		return onLoad;
	}

	private GlobeLayerType(String displayName, boolean onLoad) {
		this.displayName = displayName;
		this.onLoad = onLoad;
	}

	private static Map<String, GlobeLayerType> map;

	/**
	 * Returns the layer given the display name specified by the Enum:
	 * GlobeLayerType.XYZ.getDisplayName()
	 * 
	 * @param displayName
	 * @return
	 */
	public static GlobeLayerType fromDisplayName(String displayName) {
		if (map == null) {
			map = new LinkedHashMap<String, GlobeLayerType>();
			for (GlobeLayerType layerType : GlobeLayerType.values()) {
				map.put(layerType.getDisplayName(), layerType);
			}
		}
		return map.get(displayName);
	}
}
