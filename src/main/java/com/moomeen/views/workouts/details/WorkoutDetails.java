package com.moomeen.views.workouts.details;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.moomeen.bests.BestDistanceTimeCalculator;
import com.moomeen.bests.model.DistanceTime;
import com.moomeen.endo2java.model.DetailedWorkout;
import com.moomeen.endo2java.model.Point;
import com.moomeen.views.LocaleHelper;
import com.moomeen.views.workouts.details.timelapse.JsStreetView;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapInfoWindow;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapPolyline;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class WorkoutDetails extends HorizontalLayout {
	
	/**
	 *
	 */
	private static final long serialVersionUID = 9203596511419142742L;
	
	private final static Logger LOG = LoggerFactory.getLogger(WorkoutDetails.class);
	
	private final static String GOOGLE_MAPS_KEY = "AIzaSyDnDtkT3abR20ajxVlbYhI6uYKTXMgNJG4";
	
	private static final String ROUTE_COLOR = "#0072c6";
	private static final String BEST_COLOR = "#FF0E0E";
	
	private LatLon boundsNE;
	private LatLon boundsSW;

	private Button routeBtn;
	private Button streetViewBtn;
	private Button bestsBtn;

	public WorkoutDetails(final DetailedWorkout workout) {
		setSizeFull();
		addStyleName("workout-details");

		VerticalLayout menu = createMenu();
		final Panel contentPanel = createContentPanel();
		final GoogleMap route = createRouteContent(workout);
		final VerticalLayout streetView = createStreetViewContent(workout);
		final VerticalLayout bests = createBestsContent(workout);

		setMenuClickAction(routeBtn, contentPanel, route);
		setMenuClickAction(streetViewBtn, contentPanel, streetView);
		setMenuClickAction(bestsBtn, contentPanel, bests);
		contentPanel.setContent(route);

		addComponent(menu);
		addComponent(contentPanel);
		setComponentAlignment(menu, Alignment.TOP_LEFT);
		setComponentAlignment(contentPanel, Alignment.MIDDLE_RIGHT);
		setExpandRatio(menu, 0.3f);
		setExpandRatio(contentPanel, 1.2f);
	}

	private void setMenuClickAction(Button button, final Panel contentPanel, final Component content){
		button.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 4276024325003572024L;

			@Override
			public void buttonClick(ClickEvent event) {
				contentPanel.setContent(content);
			}
		});
	}

	private VerticalLayout createMenu() {
		routeBtn = menuButton(FontAwesome.GLOBE, "Route");
		streetViewBtn = menuButton(FontAwesome.PHOTO, "Street view");
		bestsBtn = menuButton(FontAwesome.TACHOMETER, "Bests");

		VerticalLayout menu = new VerticalLayout();
		menu.addComponent(routeBtn);
		menu.addComponent(streetViewBtn);
		menu.addComponent(bestsBtn);
		return menu;
	}

	private Button menuButton(Resource icon, String caption) {
		Button btn = new Button(caption);
		btn.setIcon(icon);
		btn.addStyleName("details-button");
		return btn;
	}

	private Panel createContentPanel() {
		final Panel contentPanel = new Panel();
		contentPanel.setSizeFull();
		return contentPanel;
	}

	private GoogleMap createRouteContent(final DetailedWorkout workout) {
		final GoogleMap googleMap = new GoogleMap(GOOGLE_MAPS_KEY, null, null);
		googleMap.setMinZoom(4);
		googleMap.setMaxZoom(16);
		googleMap.setSizeFull();

		drawWorkoutRoute(googleMap, workout);
		googleMap.fitToBounds(boundsNE, boundsSW);
		return googleMap;
	}

	private VerticalLayout createStreetViewContent(final DetailedWorkout workout) {
		final VerticalLayout streetView = new VerticalLayout();
		ProgressBar loadProgressBar = new ProgressBar();
		loadProgressBar.setId("street-view-progress");
		loadProgressBar.setStyleName("street-view-progress-bar");

		final JsStreetView streetViewAnimation = new JsStreetView(workout);
		streetView.addComponent(loadProgressBar);
		streetView.addComponent(streetViewAnimation);
		streetView.setExpandRatio(streetViewAnimation, 1f);
		streetView.setSizeFull();
		return streetView;
	}

	@SuppressWarnings("serial")
	private VerticalLayout createBestsContent(final DetailedWorkout workout) {
		final VerticalLayout bests = new VerticalLayout();
		bests.setSpacing(true);
		final GoogleMap map = createRouteContent(workout);

		CssLayout controls = new CssLayout();
		controls.addStyleName("v-component-group");
		final TextField tf = new TextField();
		tf.setInputPrompt("Distance in km");
		tf.addStyleName("inline-icon");
		tf.setIcon(FontAwesome.TROPHY);
		tf.setWidth("260px");
		controls.addComponent(tf);
		Button button = new Button("Calculate");
		button.addStyleName("friendly");
		controls.addComponent(button);

		HorizontalLayout bottom = new HorizontalLayout();
		bottom.setWidth("100%");
		bottom.addComponent(controls);
		bottom.setComponentAlignment(controls, Alignment.BOTTOM_LEFT);

		class BestContent {
			GoogleMapPolyline line;
			GoogleMapInfoWindow info;
		}
		
		final BestContent best = new BestContent();
		
		button.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				if (best.line != null){
					map.removePolyline(best.line);
					map.closeInfoWindow(best.info);
				}
				
				Double distanceToCalculate = Double.parseDouble(tf.getValue());
				DistanceTime bestDistance = new BestDistanceTimeCalculator(workout).calculate(distanceToCalculate);
				if (bestDistance.getPoints() == null){
					LOG.warn("Couldn't calculate best {} for workout with length {}", bestDistance.getDistance(), workout.getDistance());
					return;
				}
				best.line = drawLine(map, bestDistance.getPoints(), BEST_COLOR);
				
				if (bestDistance.getTime() != null) {
					Point firstPoint = bestDistance.getPoints().get(0);
					Point lastPoint = bestDistance.getPoints().get(bestDistance.getPoints().size()-1);
					String time = LocaleHelper.getPeriodFormatter().print(bestDistance.getTime().toPeriod());
					String bestTimePoints = firstPoint.getDistance() + " km - " + lastPoint.getDistance() + " km";
					GoogleMapInfoWindow infoWindow = new GoogleMapInfoWindow(distanceToCalculate + " km in " + time + "<br>" + bestTimePoints);
					best.info = infoWindow;
					infoWindow.setPosition(new LatLon(lastPoint.getLatitude(), lastPoint.getLongitude()));
					map.openInfoWindow(infoWindow);
				}
			}
		});

		bests.addComponent(map);
		bests.addComponent(bottom);
		bests.setSizeFull();
		bests.setExpandRatio(map, 0.9f);
		bests.setExpandRatio(bottom, 0.1f);
		return bests;
	}

	private void drawWorkoutRoute(GoogleMap map, DetailedWorkout workout) {
		if (workout.getPoints() == null || workout.getPoints().isEmpty()){
			return;
		}
		
		drawLine(map, workout.getPoints(), ROUTE_COLOR);
		findMapBounds(workout.getPoints());
	}
	
	private void findMapBounds(List<Point> workoutPoints){
		for (Point point : workoutPoints) {
			adjustBoundsIfNeeded(point);
		}
	}
	
	private GoogleMapPolyline drawLine(GoogleMap map, List<Point> workoutPoints, String color){
		List<LatLon> points = new ArrayList<LatLon>();
		
		for (Point workoutPoint : workoutPoints) {
			LatLon point = vaadinPoint(workoutPoint);
			points.add(point);
		}
		
		GoogleMapPolyline overlay = new GoogleMapPolyline(points, color, 0.8, 5);
		map.addPolyline(overlay);
		return overlay;
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

	private LatLon vaadinPoint(Point point){
		return new LatLon(point.getLatitude(), point.getLongitude());
	}

}
