package com.binaryworkspace.rcp.wwj.enums;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * An enumeration for NASA World Wind map layer types. The map layers provide
 * image tiles at varying levels of detail (LOD) and reside just below the image
 * layer. In general only one type of map layer can be viewed at one time. See
 * link below regarding the World Wind Tile system for more detail.
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
 * NOTE: Only one map layer can be active at a time. Therefore, when setting
 * onLoad be sure that exactly 1 layer is set to 'true'.
 * 
 * <pre>
 * &#64;see http://www.worldwindcentral.com/wiki/Image:Worldwindtilesystemtmr0.png
 * &#64;see http://forum.worldwindcentral.com/showthread.php?24609-programatically-adding-USGS-Digital-Ortho
 * </pre>
 * 
 * @author Chris Ludka
 * 
 */
public enum MapLayerType {
	/**
	 * Landsat 7
	 * <p>
	 * Maximum 15-meter resolution
	 * 
	 * <pre>
	 * &#64;see http://www.worldwindcentral.com/wiki/Landsat
	 * </pre>
	 */
	I_CUBED_LANDSAT("i-cubed Landsat", false), //

	/**
	 * National Agriculture Imagery Program (NAIP)
	 * <p>
	 * 6-meter horizontal accuracy with no more than 10% cloud cover per quarter
	 * quad tile.
	 * 
	 * <pre>
	 * &#64;see http://www.fsa.usda.gov/FSA/apfoapp?area=home&subject=prog&topic=nai
	 * </pre>
	 */
	USDA_NAIP("USDA NAIP", false), // Confirmed working.
	USDA_NAIP_USGS("USDA NAIP USGS", false), // working?

	/**
	 * High res imagery from Microsoft.
	 * 
	 * <pre>
	 * &#64;see http://www.worldwindcentral.com/wiki/Add-on:Virtual_Earth
	 * </pre>
	 */
	MS_VIRTUAL_EARTH_AERIAL("MS Virtual Earth Aerial", true), //

	/**
	 * High res imagery from bing.com.
	 */
	BING_IMAGERY("Bing Imagery", false), //

	/**
	 * USGS produced topographic maps as part of the the National Atlas project.
	 * Note that these are not DTED and do not extrude the terrain, but rather
	 * provide a topographic overlay image.
	 * 
	 * <pre>
	 * &#64;see http://nationalatlas.gov/articles/mapping/a_tnm.html
	 * </pre>
	 */
	USGS_TOPOGRAPHIC_MAPS_1_TO_250K("USGS Topographic Maps 1:250K", false), //
	USGS_TOPOGRAPHIC_MAPS_1_TO_100K("USGS Topographic Maps 1:100K", false), //
	USGS_TOPOGRAPHIC_MAPS_1_TO_24K("USGS Topographic Maps 1:24K", false); //

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

	private MapLayerType(String displayName, boolean onLoad) {
		this.displayName = displayName;
		this.onLoad = onLoad;
	}

	private static Map<String, MapLayerType> map;

	/**
	 * Returns the layer given the display name specified by the Enum:
	 * MapLayerType.XYZ.getDisplayName()
	 * 
	 * @param displayName
	 * @return
	 */
	public static MapLayerType fromDisplayName(String displayName) {
		if (map == null) {
			map = new LinkedHashMap<String, MapLayerType>();
			for (MapLayerType layerType : MapLayerType.values()) {
				map.put(layerType.getDisplayName(), layerType);
			}
		}
		return map.get(displayName);
	}
}
