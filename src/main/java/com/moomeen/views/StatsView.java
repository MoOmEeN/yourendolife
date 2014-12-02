package com.moomeen.views;

import java.awt.Color;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.addon.JFreeChartWrapper;
import org.vaadin.spring.UIScope;
import org.vaadin.spring.navigator.VaadinView;

import com.moomeen.endo2java.error.InvocationException;
import com.moomeen.endo2java.model.DetailedWorkout;
import com.moomeen.endo2java.model.Sport;
import com.moomeen.endo2java.model.Workout;
import com.moomeen.location.LocationService;
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
	private LocationService locationService;

	@SuppressWarnings("serial")
	@Override
	public LazyLoadable content() {

		return new LazyLoadableContent() {

			@Override
			public Component content() {
				VerticalLayout layout = new VerticalLayout();
				layout.setStyleName("stats-view");
				TextStripe textStripe = new TextStripe(sessionHolder);
				textStripe.setStyleName("stats-view-text");
				layout.addComponent(textStripe);

				List<Workout> workouts;
				try {
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
					
					
					layout.addComponent(chart(workouts));
					
				} catch (InvocationException e) {
					LOG.error("Couldn't get workouts", e);
				}
				
				return layout;
			}
		};
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}
	
	private Component chart(List<Workout> workouts){
		 DefaultPieDataset dataset = new DefaultPieDataset();
		 
		 Map<Sport, Integer> sports = new HashMap<Sport, Integer>();
		 for (Workout workout : workouts) {
			if (sports.containsKey(workout.getSport())){
				sports.put(workout.getSport(), sports.get(workout.getSport()) + 1);
			} else {
				sports.put(workout.getSport(), 1);
			}
		}
		
		 for (Entry<Sport, Integer> sport : sports.entrySet()) {
			dataset.setValue(sport.getKey().description(), sport.getValue());
		}
		 JFreeChart chart = ChartFactory.createPieChart("Sports", dataset, false, false, false);
		 chart.setBackgroundPaint(new Color(221, 95, 50)); // transparent black
		 chart.setBorderVisible(false);
		 
		 PiePlot plot = (PiePlot) chart.getPlot();
		 plot.setLabelPaint(Color.WHITE);
		 plot.setLabelBackgroundPaint(new Color(0,0,0,0));
		 plot.setBackgroundPaint(new Color(221, 95, 50));
		 plot.setLabelGenerator(new StandardPieSectionLabelGenerator(
		            "{0} = {2}", NumberFormat.getNumberInstance(), NumberFormat.getPercentInstance()
		        ));

		 return new JFreeChartWrapper(chart);
	}

}