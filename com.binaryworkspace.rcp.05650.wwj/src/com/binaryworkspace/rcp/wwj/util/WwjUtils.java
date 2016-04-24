package com.binaryworkspace.rcp.wwj.util;

import gov.nasa.worldwind.Model;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.geom.Vec4;
import gov.nasa.worldwind.layers.CompassLayer;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.layers.LayerList;
import gov.nasa.worldwind.layers.placename.PlaceNameLayer;
import gov.nasa.worldwind.terrain.SectorGeometryList;
import com.binaryworkspace.rcp.wwj.enums.GlobeLayerType;
import com.binaryworkspace.rcp.wwj.enums.MapLayerType;
import com.binaryworkspace.rcp.wwj.enums.OverlayLayerType;

/**
 * A collection of useful NASA World Wind utility functions.
 * <p>
 * <b>Notes:</b>
 * <ul>
 * <li>Many of these utility functions were duplicated and modified from
 * ApplicationTemplate and other examples that were originally provided in the
 * World Wind SDK xwordwind package.
 * </ul>
 * 
 * @author Chris Ludka
 *         <p>
 * @see <a href=
 *      "https://github.com/nasa/World-Wind-Java/blob/master/WorldWind/src/gov/nasa/worldwindx/examples/ApplicationTemplate.java">
 *      https://github.com/nasa/World-Wind-Java/blob/master/WorldWind/src/gov/
 *      nasa/worldwindx/examples/ApplicationTemplate.java</a>
 * @see <a href=
 *      "https://github.com/nasa/World-Wind-Java/blob/master/WorldWind/src/gov/nasa/worldwindx/examples/util/SurfaceImageEditor.java">
 *      https://github.com/nasa/World-Wind-Java/blob/master/WorldWind/src/gov/
 *      nasa/worldwindx/examples/util/SurfaceImageEditor.java</a>
 */
public class WwjUtils {

	/**
	 * Inserts the layer before the Compass layer.
	 * 
	 * @param wwjModel
	 * @param layer
	 */
	public static void insertBeforeCompass(Model wwjModel, Layer layer) {
		int compassPosition = 0;
		LayerList layers = wwjModel.getLayers();
		for (Layer l : layers) {
			if (l instanceof CompassLayer)
				compassPosition = layers.indexOf(l);
		}
		layers.add(compassPosition, layer);
	}

	/**
	 * Inserts the layer before the PlaceName layer.
	 * 
	 * @param wwjModel
	 * @param layer
	 */
	public static void insertBeforePlacenames(Model wwjModel, Layer layer) {
		int compassPosition = 0;
		LayerList layers = wwjModel.getLayers();
		for (Layer l : layers) {
			if (l instanceof PlaceNameLayer)
				compassPosition = layers.indexOf(l);
		}
		layers.add(compassPosition, layer);
	}

	/**
	 * Inserts the layer after the PlaceName layer.
	 * 
	 * @param wwjModel
	 * @param layer
	 */
	public static void insertAfterPlacenames(Model wwjModel, Layer layer) {
		int compassPosition = 0;
		LayerList layers = wwjModel.getLayers();
		for (Layer l : layers) {
			if (l instanceof PlaceNameLayer)
				compassPosition = layers.indexOf(l);
		}
		layers.add(compassPosition + 1, layer);
	}

	/**
	 * Inserts the layer before the target layer.
	 * 
	 * @param wwjModel
	 * @param layer
	 * @param targetName
	 */
	public static void insertBeforeLayerName(Model wwjModel, Layer layer, String targetName) {
		int targetPosition = 0;
		LayerList layers = wwjModel.getLayers();
		for (Layer l : layers) {
			if (l.getName().indexOf(targetName) != -1) {
				targetPosition = layers.indexOf(l);
				break;
			}
		}
		layers.add(targetPosition, layer);
	}

	/**
	 * Replaces the target layer with the replacement.
	 * 
	 * @param wwjModel
	 * @param oldLayer
	 * @param newLayer
	 */
	public static void replaceLayers(Model wwjModel, Layer oldLayer, Layer newLayer) {
		// Check that a new layer was provided
		if (newLayer == null) {
			return;
		}

		// Add the new layer before removing the target to reduce flickering
		insertBeforeCompass(wwjModel, newLayer);

		// Check that an old layer was provided
		if (oldLayer == null) {
			return;
		}

		// Remove the old layer
		wwjModel.getLayers().remove(oldLayer);
	}

	/**
	 * Initialize the layers based on the LayerType which is a Enum used to
	 * enable/disable layers as opposed to using the worldwind.layers.xml
	 * configuration file.
	 * 
	 * @param wwjModel
	 */
	public static void initLayerTypes(Model wwjModel) {
		// Initialize the layers based on the LayerType settings
		for (Layer layer : wwjModel.getLayers()) {
			// Globe Layer
			GlobeLayerType globeLayerType = GlobeLayerType.fromDisplayName(layer.getName());
			if (globeLayerType != null) {
				layer.setEnabled(globeLayerType.isOnLoad());
				if(!globeLayerType.isOnLoad()){
					wwjModel.getLayers().remove(layer);
				}
				continue;
			}

			// Map Layer
			MapLayerType mapLayerType = MapLayerType.fromDisplayName(layer.getName());
			if (mapLayerType != null) {
				layer.setEnabled(mapLayerType.isOnLoad());
				if(!mapLayerType.isOnLoad()){
					wwjModel.getLayers().remove(layer);
				}
				continue;
			}

			// Overlay Layer
			OverlayLayerType overlayLayerType = OverlayLayerType.fromDisplayName(layer.getName());
			if (overlayLayerType != null) {
				layer.setEnabled(overlayLayerType.isOnLoad());
				if(!overlayLayerType.isOnLoad()){
					wwjModel.getLayers().remove(layer);
				}
			}
		}
	}

	/**
	 * Update (replaces) an active layer in the WWJ Model only if the update
	 * layer is not equal to the active layer.
	 * <p>
	 * The updated layer will be inserted before the compass in the WWJ model.
	 * 
	 * @param wwjModel
	 * @param updateLayer
	 */
	public static void updateLayer(Model wwjModel, Layer updateLayer) {
		Layer oldLayer = wwjModel.getLayers().getLayerByName(updateLayer.getName());
		if (oldLayer == null) {
			// No old Layer
			WwjUtils.insertBeforeCompass(wwjModel, updateLayer);
		} else if (!oldLayer.equals(updateLayer)) {
			// Remove the old layer
			wwjModel.getLayers().remove(oldLayer);

			// Update
			WwjUtils.insertBeforeCompass(wwjModel, updateLayer);
		} else {
			// Update layer matches the active layer so do nothing
		}
	}

	/**
	 * Provides the surface elevation given a particular lat-lon.
	 * <p>
	 * This code is duplicated/modified from the World Wind example
	 * SurfaceImageEditor. SurfaceImageEditor was refactored to support resizing
	 * and moving surface images as well as surface quadrilaterals (a surface
	 * polygon with four sides).
	 * 
	 * @see <a href=
	 *      "https://github.com/nasa/World-Wind-Java/blob/master/WorldWind/src/gov/nasa/worldwindx/examples/util/SurfaceImageEditor.java">
	 *      https://github.com/nasa/World-Wind-Java/blob/master/WorldWind/src/
	 *      gov/nasa/worldwindx/examples/util/SurfaceImageEditor.java</a>
	 * 
	 * @param wwd
	 * @param latLon
	 * @return
	 */
	public static double computeSurfaceElevation(WorldWindow wwd, LatLon latLon) {
		// Terrain reference
		SectorGeometryList sgl = wwd.getSceneController().getTerrain();
		if (sgl != null) {
			// Point on the terrain
			Vec4 point = sgl.getSurfacePoint(latLon.getLatitude(), latLon.getLongitude(), 0.0);
			if (point != null) {
				// Get position in lat-lon-elev
				Position pos = wwd.getModel().getGlobe().computePositionFromPoint(point);
				return pos.getElevation();
			}
		}
		return wwd.getModel().getGlobe().getElevation(latLon.getLatitude(), latLon.getLongitude());
	}

	public static LatLon getBoundedLatLon(double lat, double lon) {
		double boundedLat = lat;
		double boundedLon = lon;

		// Unwind the excess latitude degrees
		int pointer = 0;
		while (Math.abs(boundedLat / 90.0) > 1.0) {
			if (boundedLat > 0.0) {
				boundedLat = boundedLat - 90.0;
				if (pointer == 0) {
					pointer = 90;
				} else if (pointer == 90) {
					pointer = 180;
				} else if (pointer == 180) {
					pointer = 270;
				} else {
					pointer = 0;
				}
			} else {
				boundedLat = boundedLat + 90.0;
				if (pointer == 0) {
					pointer = 270;
				} else if (pointer == 270) {
					pointer = 180;
				} else if (pointer == 180) {
					pointer = 90;
				} else {
					pointer = 0;
				}
			}
		}

		/**
		 * <pre>
		 * From Equator, Facing North, Pointer = 0
		 * Walk +15 degrees = +15 degrees
		 * Walk -15 degrees = -15 degrees
		 * boundedLat = boundedLat (e.g. Do Nothing)
		 * 
		 * From North Pole, Facing South, Pointer = 90
		 * Walk +15 degrees = 75 degrees
		 * Walk -15 degrees = 75 degrees
		 * boundedLat = 90 - Math.abs(boundedLat)
		 * 
		 * From Equator, Facing South, Pointer = 180
		 * Walk +15 degrees = -15 degrees
		 * Walk -15 degrees = +15 degrees
		 * boundedLat = -1 * boundedLat
		 * 
		 * From South Pole, Facing North, Pointer = 270
		 * Walk +15 degrees = -75 degrees
		 * Walk -15 degrees = -75 degrees
		 * boundedLat = -1 * Math.abs(boundedLat)
		 * </pre>
		 */
		if (pointer == 90) {
			boundedLat = 90 - Math.abs(boundedLat);
		} else if (pointer == 180) {
			boundedLat = -1 * boundedLat;
		} else if (pointer == 270) {
			boundedLat = -1 * Math.abs(boundedLat);
		}

		// Unwind the excess longitude degrees
		boolean isPrimeMeridian = true;
		while (Math.abs(boundedLon / 180.0) > 1.0) {
			if (boundedLon > 0.0) {
				boundedLon = boundedLon - 180.0;
			} else {
				boundedLon = boundedLon + 180.0;
			}
			isPrimeMeridian = !isPrimeMeridian;
		}

		/**
		 * <pre>
		 * From Prime Meridian, Facing East, Pointer = 0
		 * Walk +15 degrees = +15 degrees
		 * Walk -15 degrees = -15 degrees
		 * boundedLon = boundedLon (e.g. Do Nothing)
		 * 
		 * From Antimeridian, Facing East, Pointer = 180
		 * Walk +15 degrees = -165 degrees
		 * Walk -15 degrees = 165 degrees
		 * </pre>
		 */
		if (!isPrimeMeridian) {
			if (boundedLon > 0.0) {
				boundedLon = boundedLon - 180.0;
			} else {
				boundedLon = boundedLon + 180.0;
			}
		}
		return LatLon.fromDegrees(boundedLat, boundedLon);
	}
}
