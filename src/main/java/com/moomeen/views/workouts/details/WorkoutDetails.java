package com.moomeen.views.workouts.details;

import java.util.ArrayList;
import java.util.List;

import com.moomeen.endo2java.model.DetailedWorkout;
import com.moomeen.endo2java.model.Point;
import com.moomeen.views.workouts.details.timelapse.JsTimeLapse;
import com.vaadin.server.FontAwesome;
import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapPolyline;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

public class WorkoutDetails extends HorizontalLayout {

	/**
	 *
	 */
	private static final long serialVersionUID = 9203596511419142742L;
	private LatLon boundsNE;
	private LatLon boundsSW;

	public WorkoutDetails(final DetailedWorkout workout) {
		setSizeFull();
		addStyleName("workout-details");

		final Panel googleMapPanel = new Panel();
		Button route = new Button("Route");
		route.addStyleName("details-button");
		route.setIcon(FontAwesome.GLOBE);

		Button timeLapseBtn = new Button("Street view");
		timeLapseBtn.setIcon(FontAwesome.PHOTO);
		timeLapseBtn.addStyleName("details-button");

		Button bestsBtn = new Button("Bests");
		bestsBtn.setIcon(FontAwesome.TACHOMETER);
		bestsBtn.addStyleName("details-button");


		VerticalLayout menu = new VerticalLayout();

		menu.addComponent(route);
		menu.addComponent(timeLapseBtn);
		menu.addComponent(bestsBtn);

		final GoogleMap googleMap = new GoogleMap(null, null, null);
		googleMap.setMinZoom(4);
		googleMap.setMaxZoom(16);
		googleMap.setSizeFull();

		drawWorkoutRoute(googleMap, workout);
		googleMap.fitToBounds(boundsNE, boundsSW);

		googleMapPanel.setSizeFull();
		googleMapPanel.setContent(googleMap);

		route.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				googleMapPanel.setContent(googleMap);
			}
		});

		final JsTimeLapse timeLapse = new JsTimeLapse(workout);

		timeLapseBtn.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				googleMapPanel.setContent(timeLapse);
			}
		});

		addComponent(menu);
		addComponent(googleMapPanel);
		setComponentAlignment(menu, Alignment.TOP_LEFT);
		setComponentAlignment(googleMapPanel, Alignment.MIDDLE_RIGHT);
		setExpandRatio(menu, 0.3f);
		setExpandRatio(googleMapPanel, 1.2f);
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
