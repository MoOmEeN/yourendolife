package com.moomeen.views.workouts;

import java.util.ArrayList;
import java.util.List;

import com.moomeen.endo2java.model.DetailedWorkout;
import com.moomeen.endo2java.model.Point;
import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapPolyline;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class WorkoutDetails extends VerticalLayout {

	/**
	 *
	 */
	private static final long serialVersionUID = -5396112433334146826L;

	public WorkoutDetails(DetailedWorkout workout) {
		Label l = new Label(workout.getId() + "");

		setSizeFull();

		GoogleMap googleMap = new GoogleMap(null, null, null);

		List<LatLon> points = new ArrayList<LatLon>();


		for (Point point : workout.getPoints()) {
			points.add(new LatLon(point.getLatitude(), point.getLongitude()));
		}
		GoogleMapPolyline overlay = new GoogleMapPolyline(points, "#0072c6", 0.8, 5);
		googleMap.addPolyline(overlay);

		googleMap.setMinZoom(4);
		googleMap.setMaxZoom(16);
		googleMap.setSizeFull();
		//addComponent(l);
		addComponent(googleMap);
		setExpandRatio(googleMap, 1.0f);
	}

}
