package com.moomeen.views.workouts.details;

import java.util.ArrayList;
import java.util.List;

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
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapPolyline;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class WorkoutDetails extends HorizontalLayout {

	/**
	 *
	 */
	private static final long serialVersionUID = 9203596511419142742L;
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
		final GoogleMap googleMap = new GoogleMap(null, null, null);
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

	private VerticalLayout createBestsContent(final DetailedWorkout workout) {
		final VerticalLayout bests = new VerticalLayout();
		GoogleMap map = createRouteContent(workout);

		 CssLayout controls = new CssLayout();
		 controls.addStyleName("v-component-group");
		 final TextField tf = new TextField();
		 tf.setInputPrompt("Distance");
		 tf.addStyleName("inline-icon");
		 tf.setIcon(FontAwesome.TROPHY);
		 tf.setWidth("260px");
		 controls.addComponent(tf);
		 Button button = new Button("Calculate");
		 button.addStyleName("friendly");
		 controls.addComponent(button);

		 final Label text = new Label();
		 HorizontalLayout bottom = new HorizontalLayout();
		 bottom.setWidth("100%");
		 bottom.setHeight("50px");
		 bottom.addComponent(controls);
		 bottom.addComponent(text);
		 bottom.setComponentAlignment(controls, Alignment.BOTTOM_LEFT);
		 bottom.setComponentAlignment(text, Alignment.BOTTOM_RIGHT);

		 button.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				DistanceTime best = new BestDistanceTimeCalculator(workout).calculate(Double.parseDouble(tf.getValue()));
				String time = "";
				if (best.getTime() != null){
					time = LocaleHelper.getPeriodFormatter().print(best.getTime().toPeriod());
				}
				text.setValue(best.getDistance() + ": " + time);
			}
		});

		bests.addComponent(map);
		bests.addComponent(bottom);
		bests.setSizeFull();
//		bests.setExpandRatio(map, 0.8f);
//		bests.setExpandRatio(bottom, 0.2f);
		return bests;
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
