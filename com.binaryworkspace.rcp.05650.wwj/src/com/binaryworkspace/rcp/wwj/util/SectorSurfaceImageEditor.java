package com.binaryworkspace.rcp.wwj.util;

import gov.nasa.worldwind.Movable;
import gov.nasa.worldwind.View;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.event.DragSelectEvent;
import gov.nasa.worldwind.event.SelectEvent;
import gov.nasa.worldwind.event.SelectListener;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.Intersection;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.geom.Line;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.geom.Vec4;
import gov.nasa.worldwind.globes.Globe;
import gov.nasa.worldwind.layers.LayerList;
import gov.nasa.worldwind.layers.MarkerLayer;
import gov.nasa.worldwind.pick.PickedObject;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.SurfaceImage;
import gov.nasa.worldwind.render.markers.BasicMarkerAttributes;
import gov.nasa.worldwind.render.markers.BasicMarkerShape;
import gov.nasa.worldwind.render.markers.Marker;
import gov.nasa.worldwind.render.markers.MarkerAttributes;
import gov.nasa.worldwind.util.Logging;

import java.awt.Component;
import java.awt.Cursor;
import java.util.ArrayList;
import java.util.List;

import com.binaryworkspace.rcp.wwj.enums.SurfaceEditorModeType;
import com.binaryworkspace.rcp.wwj.structures.IndexedMarker;

/**
 * A sector surface image that maintains a rectangular (or triangular
 * when close to a pole) shape on the spheroid of the Earth when stretched and
 * moved.
 * <p>
 * This code is duplicated/modified from the World Wind example
 * SurfaceImageEditor. SurfaceImageEditor was refactored to support resizing and
 * moving surface images as well as surface sectors (a surface polygon
 * with four sides).
 * <p>
 * This code also alters the cursor state checks to account for multiple surface
 * editors present in the system. For example, removed the check condition that
 * the cursor becomes the default cursor if 'this' editor's surface image is not
 * selected. When multiple editors were present the last editor could overwrite
 * the cursor state because that last editor's image was not being rolled over
 * even though other editor's images may be in a roll over state:
 * 
 * <pre>
 * if ((event.getTopObject() != null) &amp;&amp; !((event.getTopObject() == this.surfaceImage) || (event.getTopPickedObject().getParentLayer() == this.markerLayer))) {
 * 	((Component) wwd).setCursor(cursor);
 * 	return;
 * }
 * </pre>
 * 
 * @author Chris Ludka
 * 
 * @see <a
 *      href="https://github.com/nasa/World-Wind-Java/blob/master/WorldWind/src/gov/nasa/worldwindx/examples/util/SurfaceImageEditor.java">
 *      https://github.com/nasa/World-Wind-Java/blob/master/WorldWind/src/gov/nasa/worldwindx/examples/util/SurfaceImageEditor.java</a>
 * 
 */
public class SectorSurfaceImageEditor implements SelectListener {
	
	private final WorldWindow wwd;
	
	private SurfaceImage surfaceImage;
	
	private MarkerLayer markerLayer;
	
	private boolean isEditable;
	
	private boolean isEditing;
	
	private double markerSize;
	
	private SurfaceEditorModeType surfaceEditorModeType = SurfaceEditorModeType.NONE;
	
	private Position previousPosition = null;
	
	public SectorSurfaceImageEditor(WorldWindow wwd, SurfaceImage surfaceImage) {
		// Validation
		if (wwd == null) {
			String msg = Logging.getMessage("nullValue.WorldWindow");
			Logging.logger().log(java.util.logging.Level.FINE, msg);
			throw new IllegalArgumentException(msg);
		}
		if (surfaceImage == null) {
			String msg = Logging.getMessage("nullValue.Shape");
			Logging.logger().log(java.util.logging.Level.FINE, msg);
			throw new IllegalArgumentException(msg);
		}
		
		// Init
		this.wwd = wwd;
		this.surfaceImage = surfaceImage;
		markerLayer = new MarkerLayer();
	}
	
	// Helper function to respond to a drag of the whole shape
	private void dragWholeShape(DragSelectEvent dragEvent, Object topObject) {
		// Determine if the object is movable
		if (!(topObject instanceof Movable)) {
			return;
		}
		Movable dragObject = (Movable) topObject;
		
		/*
		 * Compute ref-point position in screen coordinates. Since the
		 * SufaceShape implicitly follows the surface geometry, override the
		 * reference elevation with the current surface elevation. This will
		 * improve cursor tracking in areas where the elevations are far from
		 * zero.
		 */
		Position refPos = dragObject.getReferencePosition();
		if (refPos == null) {
			return;
		}
		double refElevation = WwjUtils.computeSurfaceElevation(wwd, refPos);
		refPos = new Position(refPos, refElevation);
		View view = wwd.getView();
		Globe globe = wwd.getModel().getGlobe();
		Vec4 refPoint = globe.computePointFromPosition(refPos);
		Vec4 screenRefPoint = view.project(refPoint);
		
		// Compute screen-coord delta since last event
		int dx = dragEvent.getPickPoint().x - dragEvent.getPreviousPickPoint().x;
		int dy = dragEvent.getPickPoint().y - dragEvent.getPreviousPickPoint().y;
		
		// Find intersection of screen coord ref-point with globe
		double x = screenRefPoint.x + dx;
		double y = ((dragEvent.getMouseEvent().getComponent().getSize().height - screenRefPoint.y) + dy) - 1;
		Line ray = view.computeRayFromScreenPoint(x, y);
		Intersection inters[] = globe.intersect(ray, refPos.getElevation());
		
		/*
		 * If there is an intersection with globe then move reference point to
		 * the intersection point.
		 */
		if (inters != null) {
			Position p = globe.computePositionFromPoint(inters[0].getIntersectionPoint());
			/******************************************************************
			 * TODO: Appears to be a bug in SufaceImage.moveTo that inverts the
			 * image.
			 * 
			 * <pre>
			 * LatLon oldRef = this.getReferencePosition();
			 * if (oldRef == null)
			 * 	return;
			 * 
			 * for (int i = 0; i &lt; this.corners.size(); i++) {
			 * 	LatLon p = this.corners.get(i);
			 * 	double distance = LatLon.greatCircleDistance(oldRef, p).radians;
			 * 	double azimuth = LatLon.greatCircleAzimuth(oldRef, p).radians;
			 * 	LatLon pp = LatLon.greatCircleEndPosition(position, azimuth, distance);
			 * 	this.corners.set(i, pp);
			 * }
			 * this.setCorners(this.corners);
			 * 
			 * <pre>
			 ******************************************************************/
			// dragObject.moveTo(p);
			
			// Get old reference
			LatLon oldRef = surfaceImage.getReferencePosition();
			if (oldRef == null) {
				return;
			}
			
			// Compute new corners
			List<LatLon> newCorners = surfaceImage.getCorners();
			for (int i = 0; i < surfaceImage.getCorners().size(); i++) {
				LatLon latLon = surfaceImage.getCorners().get(i);
				double distance = LatLon.greatCircleDistance(oldRef, latLon).radians;
				double azimuth = LatLon.greatCircleAzimuth(oldRef, latLon).radians;
				LatLon pp = LatLon.greatCircleEndPosition(p, azimuth, distance);
				newCorners.set(i, pp);
			}
			
			// Update the surface image's position
			surfaceImage.setCorners(newCorners);
		}
	}
	
	/**
	 * Provides the surface image.
	 * 
	 * @return
	 */
	public SurfaceImage getSurfaceImage() {
		return surfaceImage;
	}
	
	/**
	 * Provides the world window.
	 * 
	 * @return
	 */
	public WorldWindow getWwd() {
		return wwd;
	}
	
	/**
	 * Indicates if the image is able to be edited.
	 * <p>
	 * Editable means the ability to select and drag the whole image as well as
	 * stretch/resizing the image by selecting and dragging the image's corners.
	 * <p>
	 * When the image is editable markers will appear on the image's corners and
	 * the mouse cursor will show 'cross hairs' when moused-over to indicate the
	 * image can be stretched/resized. The mouse cursor will also show a 'hand'
	 * when over the image indicating the whole image can be moved.
	 * 
	 * @return
	 */
	public boolean isEditable() {
		return isEditable;
	}
	
	/**
	 * Resizes the shape.
	 * <p>
	 * A resize typically occurs when a marker (located on the corners of the
	 * image) is clicked and dragged.
	 * 
	 * @param topObject
	 */
	private void resizeShape(Object topObject) {
		// Check for IndexedMarker
		if (!(topObject instanceof IndexedMarker)) {
			return;
		}
		
		/*
		 * If the terrain beneath the control point is null, then the user is
		 * attempting to drag the marker off the globe. This is not a valid
		 * state for SurfaceImage, so ignore this action but keep the drag
		 * operation in effect.
		 */
		PickedObject terrainObject = wwd.getObjectsAtCurrentPosition().getTerrainObject();
		if (terrainObject == null) {
			return;
		}
		
		// Get the delta position change
		Position p = terrainObject.getPosition();
		Angle dLat = p.getLatitude().subtract(previousPosition.getLatitude());
		Angle dLon = p.getLongitude().subtract(previousPosition.getLongitude());
		LatLon delta = new LatLon(dLat, dLon);
		previousPosition = p;
		
		// Move the selected marker
		List<LatLon> corners = new ArrayList<LatLon>(surfaceImage.getCorners());
		IndexedMarker marker = (IndexedMarker) topObject;
		LatLon markerPosition = corners.get(marker.getIndex()).add(delta);
		corners.set(marker.getIndex(), markerPosition);
		
		/**
		 * Maintain the sector rectangular (or triangular when close to
		 * the poles) shape by moving the adjacent vertices in conjunction with
		 * the selected marker. If this is not done then a free form
		 *  will be formed.
		 * <p>
		 * Zero indexing for the corners start in the SE corner and is
		 * incremented by one for each vertex in the clockwise direction.
		 * 
		 * <pre>
		 * 
		 * 2 ______ 3
		 *  |      |
		 *  |      |
		 *  |      |
		 *  |______|
		 * 1        0
		 * 
		 * </pre>
		 */
		if (marker.getIndex() == 0) {
			LatLon latLon = corners.get(3);
			corners.set(3, LatLon.fromDegrees(latLon.latitude.degrees, markerPosition.longitude.degrees));
			latLon = corners.get(1);
			corners.set(1, LatLon.fromDegrees(markerPosition.latitude.degrees, latLon.longitude.degrees));
		} else if (marker.getIndex() == 1) {
			LatLon latLon = corners.get(0);
			corners.set(0, LatLon.fromDegrees(markerPosition.latitude.degrees, latLon.longitude.degrees));
			latLon = corners.get(2);
			corners.set(2, LatLon.fromDegrees(latLon.latitude.degrees, markerPosition.longitude.degrees));
		} else if (marker.getIndex() == 2) {
			LatLon latLon = corners.get(1);
			corners.set(1, LatLon.fromDegrees(latLon.latitude.degrees, markerPosition.longitude.degrees));
			latLon = corners.get(3);
			corners.set(3, LatLon.fromDegrees(markerPosition.latitude.degrees, latLon.longitude.degrees));
		} else if (marker.getIndex() == 3) {
			LatLon latLon = corners.get(2);
			corners.set(2, LatLon.fromDegrees(markerPosition.latitude.degrees, latLon.longitude.degrees));
			latLon = corners.get(0);
			corners.set(0, LatLon.fromDegrees(latLon.latitude.degrees, markerPosition.longitude.degrees));
		}
		
		// Update the position of the surface image
		surfaceImage.setCorners(corners);
	}
	
	@Override
	public void selected(SelectEvent event) {
		// Validation: Event
		if (event == null) {
			String msg = Logging.getMessage("nullValue.EventIsNull");
			Logging.logger().log(java.util.logging.Level.FINE, msg);
			throw new IllegalArgumentException(msg);
		}
		
		// Validation: Component
		if (!(wwd instanceof Component)) {
			return;
		}
		
		/*
		 * Set the mouse to the default cursor if it is not over an
		 * editable/movable object.
		 */
		Cursor cursor = null;
		if ((event.getTopObject() == null) || event.getTopPickedObject().isTerrain()) {
			((Component) wwd).setCursor(cursor);
			return;
		}
		
		// Change mouse cursor based on what is selected
		if (event.getEventAction().equals(SelectEvent.DRAG_END)) {
			isEditing = false;
			surfaceEditorModeType = SurfaceEditorModeType.NONE;
			previousPosition = null;
		} else if (event.getEventAction().equals(SelectEvent.ROLLOVER)) {
			if (event.getTopObject() instanceof SurfaceImage) {
				cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
			} else if (event.getTopObject() instanceof Marker) {
				cursor = Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
			}
			((Component) wwd).setCursor(cursor);
		} else if (event.getEventAction().equals(SelectEvent.LEFT_PRESS)) {
			isEditing = true;
			previousPosition = wwd.getCurrentPosition();
		} else if (event.getEventAction().equals(SelectEvent.DRAG)) {
			// Validation: Edit state
			if (!isEditing) {
				return;
			}
			
			// Validation: Drag Event
			DragSelectEvent dragEvent = (DragSelectEvent) event;
			Object topObject = dragEvent.getTopObject();
			if (topObject == null) {
				return;
			}
			
			// Drag whole image or stretch/resize based on selection
			boolean isResizeMarkers = false;
			if ((topObject == surfaceImage) || (surfaceEditorModeType == SurfaceEditorModeType.MOVING_SURFACE)) {
				surfaceEditorModeType = SurfaceEditorModeType.MOVING_SURFACE;
				dragWholeShape(dragEvent, topObject);
			} else if ((dragEvent.getTopPickedObject().getParentLayer() == markerLayer) || (surfaceEditorModeType == SurfaceEditorModeType.SIZING_SURFACE)) {
				surfaceEditorModeType = SurfaceEditorModeType.SIZING_SURFACE;
				resizeShape(topObject);
				isResizeMarkers = true;
			}
			
			// Update the markers and consume the event
			updateMarkers(isResizeMarkers);
			event.consume();
		}
	}
	
	/**
	 * Sets the editable state.
	 * <p>
	 * Editable means the ability to select and drag the whole image as well as
	 * stretch/resizing the image by selecting and dragging the image's corners.
	 * <p>
	 * When the image is editable markers will appear on the image's corners and
	 * the mouse cursor will show 'cross hairs' when moused-over to indicate the
	 * image can be stretched/resized. The mouse cursor will also show a 'hand'
	 * when over the image indicating the whole image can be moved.
	 * 
	 * @param isEditable
	 */
	public void setIsEditable(boolean isEditable) {
		if (!this.isEditable && isEditable) {
			// Enable Markers for Editing
			LayerList layers = wwd.getModel().getLayers();
			
			// Add Layer
			if (!layers.contains(markerLayer)) {
				layers.add(markerLayer);
			}
			
			// Enable Marker
			if (!markerLayer.isEnabled()) {
				markerLayer.setEnabled(true);
			}
			
			// Update Markers
			updateMarkers(true);
			wwd.addSelectListener(this);
		} else if (this.isEditable && !isEditable) {
			// Disable Markers for Editing
			LayerList layers = wwd.getModel().getLayers();
			layers.remove(markerLayer);
			wwd.removeSelectListener(this);
		}
		
		// Set state
		this.isEditable = isEditable;
	}
	
	/**
	 * Updates the position and attributes of the markers.
	 * <p>
	 * This method also resizes the markers. As the image gets smaller the
	 * markers will become smaller as well as to not over shadow the image.
	 * Likewise as the image the markers will proportionally become larger as
	 * well to make it easier to select for editing. stretched/resized.
	 * <p>
	 * If the whole image is being dragged/moved instead of stretched/resized
	 * then there is no reason to change the marker size and isResizeMarkers
	 * should be false.
	 * 
	 * @param isResizeMarkers
	 */
	private void updateMarkers(boolean isResizeMarkers) {
		// Resize the markers if needed
		List<LatLon> corners = surfaceImage.getCorners();
		if (isResizeMarkers) {
			markerSize = LatLon.getAverageDistance(corners).radians * wwd.getModel().getGlobe().getRadius() / 30.0;
		}
		MarkerAttributes markerAttrs = new BasicMarkerAttributes(Material.BLUE, BasicMarkerShape.SPHERE, 0.7, 10.0, 0.1, markerSize);
		
		// Marker locations
		ArrayList<LatLon> markerLatLonList = new ArrayList<LatLon>(8);
		for (LatLon corner : corners) {
			markerLatLonList.add(corner);
		}
		
		// Indexed markers
		ArrayList<Marker> indexedMarkerList = new ArrayList<Marker>(markerLatLonList.size());
		for (int i = 0; i < markerLatLonList.size(); i++) {
			indexedMarkerList.add(new IndexedMarker(new Position(markerLatLonList.get(i), 0), markerAttrs, i));
		}
		
		// Layer
		markerLayer.setOverrideMarkerElevation(true);
		markerLayer.setElevation(0);
		markerLayer.setKeepSeparated(false);
		markerLayer.setMarkers(indexedMarkerList);
	}
}
