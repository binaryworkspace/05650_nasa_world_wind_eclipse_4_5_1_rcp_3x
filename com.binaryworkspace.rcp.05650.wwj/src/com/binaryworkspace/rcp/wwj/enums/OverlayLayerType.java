package com.binaryworkspace.rcp.wwj.enums;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * An enumeration for NASA World Wind overlay layer types. Overlays provide
 * additional data or controls above the imagery data layers. For instance,
 * additional data overlays could include place names, street overlays and star
 * fields when zoomed out. Control overlays could include the compass and world
 * map overlays.
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
 * NOTE: Unlike the globe and map layers, there can be zero to many active
 * overlays at a time. Therefore, when setting onLoad there are no restrictions
 * to the number of 'true' or 'false' values allowed as the default setting.
 * 
 * <pre>
 * &#64;see http://forum.worldwindcentral.com/showthread.php?24609-programatically-adding-USGS-Digital-Ortho
 * </pre>
 * 
 * @author Chris Ludka
 * 
 */
public enum OverlayLayerType {
	/**
	 * USGS provided 3D Ortho data.
	 */
	USGS_URBAN_AREA_ORTHO("USGS Urban Area Ortho", false), // working?
	// No longer listed as a layer
	// USGS_DIGITAL_ORTHO("USGS Digital Ortho", false),
	
	/**
	 * Provides Open Street Map tiles.
	 */
	OPEN_STREET_MAP("Open Street Map", false), //

	/**
	 * Map Decorators:
	 * <ul>
	 * <li>Stars: Provides a star field when zoomed out.
	 * <li>Atmosphere: Provides a 'blue' ring and atmosphere when zoomed out.
	 * <li>Place Names: Naming service that provides place names.
	 * <li>Political Boundaries: Provides the outlines for country boundries.
	 * <li>World Map: Provides a 'pan' sized map view.
	 * <li>Scale bar: Provides a scale on the map.
	 * <li>Compass: Provides a compass.
	 * </ul>
	 */
	STARS("Stars", false), //
	ATMOSPHERE("Atmosphere", true), //
	PLACE_NAMES("Place Names", true), //
	POLITICAL_BOUNDARIES("Political Boundaries", false), //
	WORLD_MAP("World Map", true), //
	SCALE_BAR("Scale bar", true), //
	COMPASS("Compass", true), //

	/**
	 * Lat-Lon Graticule: Example documentaiton states: Displays the globe with
	 * a latitude and longitude graticule (latitude and longitude grid). The
	 * graticule is its own layer and can be turned on and off independent of
	 * other layers. As the view zooms in, the graticule adjusts to display a
	 * finer grid.
	 * <p>
	 * Code example modified from stackoverflow:
	 * 
	 * <pre>
	 * Layer graticuleLayer = null;
	 * try {
	 * 	graticuleLayer = (Layer) LatLonGraticuleLayer.class.newInstance();
	 * } catch (Exception e) {
	 * 	System.out.println(&quot;Can't get a graticule layer &quot; + e);
	 * }
	 * if (graticuleLayer != null) {
	 * 	graticuleLayer.setEnabled(true);
	 * }
	 * model.getLayers().add(graticuleLayer);
	 * 
	 * <pre>
	 * &#64;see https://github.com/nasa/World-Wind-Java/blob/master/WorldWind/src/gov/nasa/worldwindx/examples/Graticule.java
	 * &#64;see http://stackoverflow.com/questions/19193865/how-to-get-latitude-and-longitude-lines-in-world-wind
	 * </pre>
	 */
	LAT_LON_GRATICULE("Lat-Lon Graticule", false);

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

	private OverlayLayerType(String displayName, boolean onLoad) {
		this.displayName = displayName;
		this.onLoad = onLoad;
	}

	private static Map<String, OverlayLayerType> map;

	/**
	 * Returns the layer given the display name specified by the Enum:
	 * OverlayLayerType.XYZ.getDisplayName()
	 * 
	 * @param displayName
	 * @return
	 */
	public static OverlayLayerType fromDisplayName(String displayName) {
		if (map == null) {
			map = new LinkedHashMap<String, OverlayLayerType>();
			for (OverlayLayerType layerType : OverlayLayerType.values()) {
				map.put(layerType.getDisplayName(), layerType);
			}
		}
		return map.get(displayName);
	}
}
