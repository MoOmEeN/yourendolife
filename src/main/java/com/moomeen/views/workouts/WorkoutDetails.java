package com.moomeen.views.workouts;

import com.moomeen.endo2java.model.DetailedWorkout;
import com.moomeen.endo2java.model.Point;
import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.tapio.googlemaps.client.LatLon;
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
		for (Point point : workout.getPoints()) {
			googleMap.addMarker("", new LatLon(point.getLatitude(), point.getLongitude()), false, null);
		}

		googleMap.setMinZoom(4);
		googleMap.setMaxZoom(16);
		addComponent(l);
		addComponent(googleMap);
		

	}

}
