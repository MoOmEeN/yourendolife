package com.moomeen.views;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.UIScope;
import org.vaadin.spring.navigator.VaadinView;

import com.moomeen.endo.EndomondoSessionHolder;
import com.moomeen.endo2java.model.Workout;
import com.moomeen.views.helper.LazyLoadable;
import com.moomeen.views.helper.LoggedInContentView;
import com.moomeen.views.stats.ChartStripe;
import com.moomeen.views.stats.TextStripe;
import com.moomeen.views.workouts.list.WorkoutClickCallback;
import com.moomeen.views.workouts.list.WorkoutsList;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

@VaadinView(name = "stats")
@UIScope
public class StatsView extends LoggedInContentView {

	/**
	 *
	 */
	private static final long serialVersionUID = -5282768909040896555L;

	private static Logger LOG = LoggerFactory.getLogger(StatsView.class);

	@Autowired
	private EndomondoSessionHolder sessionHolder;

	@SuppressWarnings("serial")
	@Override
	public LazyLoadable content() {

		return new LazyLoadableContent() {

			@Override
			public Component content() {
				VerticalLayout layout = new VerticalLayout();
				layout.setStyleName("stats-view");
				TextStripe textStripe = new TextStripe();
				textStripe.setStyleName("stats-view-text");
				layout.addComponent(textStripe);

				List<Workout> workouts = sessionHolder.getWorkouts();
				WorkoutsList workoutsPanel =  new WorkoutsList(workouts, new WorkoutClickCallback());
				workoutsPanel.setStyleName("stats-view-workouts");
				layout.addComponent(workoutsPanel);

				Component chart = new ChartStripe();
				chart.setStyleName("stats-view-chart");
				layout.addComponent(chart);

				return layout;
			}
		};
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}



}