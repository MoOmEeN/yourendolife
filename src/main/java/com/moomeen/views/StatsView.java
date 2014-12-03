package com.moomeen.views;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.UIScope;
import org.vaadin.spring.navigator.VaadinView;

import com.moomeen.endo.EndomondoSessionHolder;
import com.moomeen.endo2java.error.InvocationException;
import com.moomeen.endo2java.model.DetailedWorkout;
import com.moomeen.endo2java.model.Workout;
import com.moomeen.views.stats.ChartStripe;
import com.moomeen.views.stats.TextStripe;
import com.moomeen.views.workouts.details.WorkoutDetails;
import com.moomeen.views.workouts.list.WorkoutClickCallback;
import com.moomeen.views.workouts.list.WorkoutsList;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@VaadinView(name = "stats")
@UIScope
public class StatsView extends AbstractContentView {

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

				List<Workout> workouts;
					workouts = sessionHolder.getWorkouts();
					WorkoutsList workoutsPanel =  new WorkoutsList(workouts, new WorkoutClickCallback() {

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
					});
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