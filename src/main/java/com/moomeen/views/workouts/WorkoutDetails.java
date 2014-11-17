package com.moomeen.views.workouts;

import java.util.ArrayList;
import java.util.List;

import com.moomeen.endo2java.model.DetailedWorkout;
import com.moomeen.endo2java.model.Point;
import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapPolyline;
import com.vaadin.ui.VerticalLayout;

public class WorkoutDetails extends VerticalLayout {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9203596511419142742L;
	private LatLon boundsNE;
	private LatLon boundsSW;
	
	public WorkoutDetails(DetailedWorkout workout) {
		setSizeFull();

		GoogleMap googleMap = new GoogleMap(null, null, null);
		googleMap.setMinZoom(4);
		googleMap.setMaxZoom(16);
		googleMap.setSizeFull();
		
		drawWorkoutRoute(googleMap, workout);
		googleMap.fitToBounds(boundsNE, boundsSW);
		
		addComponent(googleMap);
		setExpandRatio(googleMap, 1.0f);
	}

	private void drawWorkoutRoute(GoogleMap map, DetailedWorkout workout) {
		if (workout.getPoints() == null || workout.getPoints().isEmpty()){
			return;
		}
		List<LatLon> points = new ArrayList<LatLon>();
		
		for (Point workoutPoint : workout.getPoints()) {			
			LatLon point = vaadinPoint(workoutPoint);
			adjustBoundsIfNeeded(point);
			points.add(point);
		}
		GoogleMapPolyline overlay = new GoogleMapPolyline(points, "#0072c6", 0.8, 5);
		map.addPolyline(overlay);
	}

	private void adjustBoundsIfNeeded(LatLon point) {
		initBoundsIfNeeded(point);
		adjustNEBoundIfNeeded(point);
		adjustSWBoundIfNeeded(point);
	}

	private void adjustSWBoundIfNeeded(LatLon point) {
		if (point.getLat() < boundsSW.getLat()){
			boundsSW.setLat(point.getLat());
		}
		if (point.getLon() < boundsSW.getLon()){
			boundsSW.setLon(point.getLon());
		}
	}

	private void adjustNEBoundIfNeeded(LatLon point) {
		if (point.getLat() > boundsNE.getLat()){
			boundsNE.setLat(point.getLat());
		}
		if (point.getLon() > boundsNE.getLon()){
			boundsNE.setLon(point.getLon());
		}
	}

	private void initBoundsIfNeeded(LatLon point) {
		if (boundsNE == null || boundsSW == null){
			boundsNE = new LatLon(point.getLat(), point.getLon());
			boundsSW = new LatLon(point.getLat(), point.getLon());
		}
	}

	private LatLon vaadinPoint(Point point){
		return new LatLon(point.getLatitude(), point.getLongitude());
	}

}
