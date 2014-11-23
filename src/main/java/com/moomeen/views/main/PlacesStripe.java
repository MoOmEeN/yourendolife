package com.moomeen.views.main;


import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.moomeen.endo.EndomondoSessionHolder;
import com.moomeen.endo2java.error.InvocationException;
import com.moomeen.endo2java.model.DetailedWorkout;
import com.moomeen.endo2java.model.Workout;
import com.moomeen.location.LocationService;
import com.moomeen.location.Place;
import com.moomeen.views.workouts.details.WorkoutDetails;
import com.moomeen.views.workouts.list.WorkoutClickCallback;
import com.moomeen.views.workouts.list.WorkoutsList;
import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapMarker;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class PlacesStripe extends VerticalLayout {

	/**
	 *
	 */
	private static final long serialVersionUID = -1966025983114859352L;

	private static Logger LOG = LoggerFactory.getLogger(PlacesStripe.class);

	private EndomondoSessionHolder sessionHolder;

	private LocationService locationService;

	private LatLon boundsNE;
	private LatLon boundsSW;

	private boolean workoutsListVisible = false;
	private Panel workoutsPanel;

	public PlacesStripe(EndomondoSessionHolder session, LocationService locatorService) {
		this.sessionHolder = session;
		this.locationService = locatorService;
		init();
	}

	private void init(){
		try {
			setHeightUndefined();

			HorizontalLayout labelAndMap = new HorizontalLayout();

			labelAndMap.setHeight("600px");
			labelAndMap.setWidth("100%");
			List<Workout> workouts = sessionHolder.getWorkouts();
			Map<Place, List<Workout>> byCities = locationService.determineCities(workouts);

			Panel textPanel = createTextPanel(byCities);
			Panel mapPanel = createMapPanel(byCities);

			labelAndMap.addComponent(textPanel);
			labelAndMap.setComponentAlignment(textPanel, Alignment.MIDDLE_LEFT);
			labelAndMap.addComponent(mapPanel);
			labelAndMap.setComponentAlignment(mapPanel, Alignment.MIDDLE_RIGHT);
			labelAndMap.setExpandRatio(mapPanel, 1.0f);
			labelAndMap.setExpandRatio(textPanel, 1.0f);

			addComponent(labelAndMap);
			setComponentAlignment(labelAndMap, Alignment.MIDDLE_CENTER);
		} catch (InvocationException e) {
			LOG.error("Error during workouts retrieving", e);
		}
	}

	private Panel createTextPanel(Map<Place, List<Workout>> byCities) {
		Panel textPanel = new Panel();
		textPanel.setContent(getText(byCities));
		textPanel.setStyleName("stripe-half");
		textPanel.addStyleName("places-stripe-text-half");
		return textPanel;
	}

	private Component getText(Map<Place, List<Workout>> byCities) {
		HorizontalLayout layout = new HorizontalLayout();
		Label text = new Label("You visited " + byCities.size() + " cities!");
		text.addStyleName("h1");
		layout.addComponent(text);
		for (Entry<Place, List<Workout>> placeEntry : byCities.entrySet()) {
			layout.addComponent(getLinkFor(placeEntry.getKey().getName(), placeEntry.getValue()));
		}
		return layout;
	}

	private Button getLinkFor(String text, final List<Workout> workouts){
		Button linkButton = new Button(text);
		linkButton.addStyleName("link");
		linkButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 8896460096151887722L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (workoutsListVisible){
					removeComponent(workoutsPanel);
				}
				workoutsPanel = new Panel();
				workoutsPanel.setStyleName("places-stripe-workouts");
				workoutsPanel.setContent(new WorkoutsList(workouts, new WorkoutClickCallback() {

					@Override
					public void clicked(long workoutId) throws InvocationException {
						DetailedWorkout workout = sessionHolder.getWorkout(workoutId);
						final Window window = new Window(workout.getSport().description() + " - " + workout.getStartTime()); // TODO
						window.setWidth("60%");
						window.setHeight("60%");
						window.center();
						window.setContent(new WorkoutDetails(workout));
						UI.getCurrent().addWindow(window);
					}
				}));
				workoutsListVisible = true;
				addComponent(workoutsPanel);
				setComponentAlignment(workoutsPanel, Alignment.BOTTOM_CENTER);
			}
		});
		return linkButton;
	}

	private Panel createMapPanel(Map<Place, List<Workout>> byCities) {
		Panel mapPanel = new Panel();
		mapPanel.setStyleName("stripe-half");

		mapPanel.setSizeFull();
		GoogleMap googleMap = new GoogleMap(null, null, null);
		googleMap.setMinZoom(2);
		googleMap.setMaxZoom(7);
		googleMap.setSizeFull();
		googleMap.addStyleName("places-stripe-map");
		mapPanel.setContent(googleMap);

		addMarkers(byCities, googleMap);

		googleMap.fitToBounds(boundsNE, boundsSW);
		return mapPanel;
	}

	private void addMarkers(Map<Place, List<Workout>> byCities, GoogleMap googleMap) {
		for (Entry<Place, List<Workout>> workout : byCities.entrySet()) {
			GoogleMapMarker marker = new GoogleMapMarker(workout.getKey().getName(), new LatLon(workout.getKey().getLatitude(), workout.getKey().getLongitude()), false);
			adjustBoundsIfNeeded(workout.getKey());
			googleMap.addMarker(marker);
		}
	}

	private void adjustBoundsIfNeeded(Place point) {
		initBoundsIfNeeded(point);
		adjustNEBoundIfNeeded(point);
		adjustSWBoundIfNeeded(point);
	}

	private void adjustSWBoundIfNeeded(Place point) {
		if (point.getLatitude() < boundsSW.getLat()){
			boundsSW.setLat(point.getLatitude());
		}
		if (point.getLongitude() < boundsSW.getLon()){
			boundsSW.setLon(point.getLongitude());
		}
	}

	private void adjustNEBoundIfNeeded(Place point) {
		if (point.getLatitude() > boundsNE.getLat()){
			boundsNE.setLat(point.getLatitude());
		}
		if (point.getLongitude() > boundsNE.getLon()){
			boundsNE.setLon(point.getLongitude());
		}
	}

	private void initBoundsIfNeeded(Place point) {
		if (boundsNE == null || boundsSW == null){
			boundsNE = new LatLon(point.getLatitude(), point.getLongitude());
			boundsSW = new LatLon(point.getLatitude(), point.getLongitude());
		}
	}

}
