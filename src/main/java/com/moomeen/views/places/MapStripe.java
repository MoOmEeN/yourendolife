package com.moomeen.views.places;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.moomeen.endo.EndomondoSessionHolder;
import com.moomeen.endo2java.model.Workout;
import com.moomeen.location.LocationService;
import com.moomeen.location.model.Place;
import com.moomeen.location.model.Point;
import com.moomeen.utils.SpringContextHolder;
import com.moomeen.views.workouts.list.WorkoutsList;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.tapio.googlemaps.client.events.MarkerClickListener;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapMarker;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

public class MapStripe extends VerticalLayout {

	/**
	 *
	 */
	private static final long serialVersionUID = -1966025983114859352L;

	private static Logger LOG = LoggerFactory.getLogger(MapStripe.class);

	private EndomondoSessionHolder sessionHolder;

	private LocationService locationService;

	private LatLon boundsNE;
	private LatLon boundsSW;

	private WorkoutsList workoutsList;

	private Map<Place, List<Workout>> groupedByCity;
	private Map<String, List<Workout>> groupedByCcountry;

	public MapStripe(WorkoutsList workoutsList) {
		this.workoutsList = workoutsList;
		this.sessionHolder = SpringContextHolder.lookupBean(EndomondoSessionHolder.class);
		this.locationService = SpringContextHolder.lookupBean(LocationService.class);
		init();
	}

	public void init(){
		setHeightUndefined();
		setStyleName("places-view-map");

		HorizontalLayout labelAndMap = new HorizontalLayout();

		labelAndMap.setWidth("100%");

		List<Workout> workouts = sessionHolder.getWorkouts();
		groupedByCity = locationService.determineCities(workouts);

		Panel textPanel = createTextPanel(groupedByCity);
		Panel mapPanel = createMapPanel(groupedByCity);

		labelAndMap.addComponent(textPanel);
		labelAndMap.setComponentAlignment(textPanel, Alignment.MIDDLE_LEFT);
		labelAndMap.addComponent(mapPanel);
		labelAndMap.setComponentAlignment(mapPanel, Alignment.MIDDLE_RIGHT);
		labelAndMap.setExpandRatio(mapPanel, 1.0f);
		labelAndMap.setExpandRatio(textPanel, 1.0f);

		addComponent(labelAndMap);
		setComponentAlignment(labelAndMap, Alignment.MIDDLE_CENTER);
	}

	private Panel createTextPanel(Map<Place, List<Workout>> byCity) {
		Panel textPanel = new Panel();
		textPanel.setContent(getText(byCity));
		textPanel.setStyleName("places-view-map-half");
		return textPanel;
	}

	private Component getText(Map<Place, List<Workout>> byCity) {
		CssLayout layout = new CssLayout();
		layout.setStyleName("places-view-map-half-text");
		Label citiesLabel = getH1Label("You visited <p class=\"big-font\">" + byCity.size() + "</p> cities");
		layout.addComponent(citiesLabel);
		addLinkButtons(layout, toStringKeyMap(byCity));
		groupedByCcountry = groupByCountry(byCity);
		Label countriesLabel = getH1Label("in <p class=\"big-font\">" + groupedByCcountry.size() + "</p> countries");
		layout.addComponent(countriesLabel);
		addLinkButtons(layout, groupedByCcountry);
		return layout;
	}

	private Label getH1Label(String text){
		Label label = new Label(text, ContentMode.HTML);
		label.addStyleName("h1");
		return label;
	}

	private Map<String, List<Workout>> toStringKeyMap(Map<Place, List<Workout>> byCity){
		Map<String, List<Workout>> retMap = new HashMap<String, List<Workout>>();
		for (Entry<Place, List<Workout>> entry : byCity.entrySet()) {
			retMap.put(entry.getKey().getName(), entry.getValue());
		}
		return retMap;
	}

	private void addLinkButtons(CssLayout layout, Map<String, List<Workout>> workouts) {
		int counter = 0;
		for (Entry<String, List<Workout>> countryEntry : workouts.entrySet()) {
			boolean isLast = counter == workouts.size() -1;
			String link = countryEntry.getKey() + (isLast ? "" : ",");
			layout.addComponent(getLinkFor(link, countryEntry.getValue()));
			counter++;
		}
	}

	private Map<String, List<Workout>> groupByCountry(Map<Place, List<Workout>> byCity){
		Map<String, List<Workout>> byCountry = new HashMap<String, List<Workout>>();
		for (Entry<Place, List<Workout>> entry : byCity.entrySet()) {
			String country = entry.getKey().getCountry();
			if (!byCountry.containsKey(country)){
				byCountry.put(country, new ArrayList<Workout>());
			}
			byCountry.get(country).addAll(entry.getValue());
		}
		return byCountry;
	}

	private Button getLinkFor(String text, final List<Workout> workouts){
		Button linkButton = new Button(text);
		linkButton.addStyleName("link");
		linkButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 8896460096151887722L;

			@Override
			public void buttonClick(ClickEvent event) {
				workoutsList.setWorkouts(workouts);
			}

		});
		return linkButton;
	}

	private Panel createMapPanel(Map<Place, List<Workout>> byCities) {
		Panel mapPanel = new Panel();
		mapPanel.setStyleName("places-view-map-half");

		mapPanel.setSizeFull();
		GoogleMap googleMap = new GoogleMap(null, null, null);
		googleMap.setMinZoom(2);
		googleMap.setMaxZoom(7);
		googleMap.setSizeFull();
		googleMap.addStyleName("places-view-map-half-map");
		mapPanel.setContent(googleMap);

		final Map<GoogleMapMarker, List<Workout>> markers = addMarkers(byCities, googleMap);
		googleMap.addMarkerClickListener(new MarkerClickListener() {

			@Override
			public void markerClicked(GoogleMapMarker clickedMarker) {
				workoutsList.setWorkouts(markers.get(clickedMarker));
			}
		});

		googleMap.fitToBounds(boundsNE, boundsSW);
		return mapPanel;
	}

	private Map<GoogleMapMarker, List<Workout>> addMarkers(Map<Place, List<Workout>> byCities, GoogleMap googleMap) {
		Map<GoogleMapMarker, List<Workout>> markers = new HashMap<GoogleMapMarker, List<Workout>>();
		for (Entry<Place, List<Workout>> workout : byCities.entrySet()) {
			GoogleMapMarker marker = new GoogleMapMarker(workout.getKey().getName(), new LatLon(workout.getKey().getPoint().getLatitude(), workout.getKey().getPoint().getLongitude()), false);
			adjustBoundsIfNeeded(workout.getKey().getPoint());
			googleMap.addMarker(marker);
			markers.put(marker, workout.getValue());
		}
		return markers;
	}

	private void adjustBoundsIfNeeded(Point point) {
		initBoundsIfNeeded(point);
		adjustNEBoundIfNeeded(point);
		adjustSWBoundIfNeeded(point);
	}

	private void adjustSWBoundIfNeeded(Point point) {
		if (point.getLatitude() < boundsSW.getLat()){
			boundsSW.setLat(point.getLatitude());
		}
		if (point.getLongitude() < boundsSW.getLon()){
			boundsSW.setLon(point.getLongitude());
		}
	}

	private void adjustNEBoundIfNeeded(Point point) {
		if (point.getLatitude() > boundsNE.getLat()){
			boundsNE.setLat(point.getLatitude());
		}
		if (point.getLongitude() > boundsNE.getLon()){
			boundsNE.setLon(point.getLongitude());
		}
	}

	private void initBoundsIfNeeded(Point point) {
		if (boundsNE == null || boundsSW == null){
			boundsNE = new LatLon(point.getLatitude(), point.getLongitude());
			boundsSW = new LatLon(point.getLatitude(), point.getLongitude());
		}
	}

	public Map<Place, List<Workout>> getGroupedByCity(){
		return groupedByCity;
	}

	public Map<String, List<Workout>> getGroupedByCountry(){
		return groupedByCcountry;
	}

}
