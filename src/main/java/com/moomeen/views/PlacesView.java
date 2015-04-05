package com.moomeen.views;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.spring.UIScope;
import org.vaadin.spring.navigator.VaadinView;

import com.moomeen.views.helper.LazyLoadable;
import com.moomeen.views.helper.LoggedInContentView;
import com.moomeen.views.places.MapStripe;
import com.moomeen.views.places.PlacesChartStripe;
import com.moomeen.views.workouts.list.WorkoutClickCallback;
import com.moomeen.views.workouts.list.WorkoutsList;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

@VaadinView(name = "places")
@UIScope
public class PlacesView extends LoggedInContentView {

	private static final long serialVersionUID = -5282768909040896555L;

	private static Logger LOG = LoggerFactory.getLogger(PlacesView.class);

	@SuppressWarnings("serial")
	@Override
	public LazyLoadable content() {

		return new LazyLoadableContent() {

			@Override
			public Component content() {
				VerticalLayout layout = new VerticalLayout();
				layout.setStyleName("places-view");

				WorkoutsList workoutsPanel =  new WorkoutsList(new WorkoutClickCallback());
				workoutsPanel.setStyleName("places-view-workouts");

				MapStripe mapStripe = new MapStripe(workoutsPanel);
				mapStripe.setStyleName("places-view-map");

				Component chart = new PlacesChartStripe(mapStripe.getGroupedByCountry());
				chart.setStyleName("places-view-chart");

				layout.addComponent(mapStripe);
				layout.addComponent(workoutsPanel);
				layout.addComponent(chart);
				return layout;
			}
		};
	}


	@Override
	public void enter(ViewChangeEvent event) {

	}


	@Override
	protected String menuStyleName() {
		return "places-view-menu";
	}

}