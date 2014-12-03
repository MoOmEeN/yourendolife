package com.moomeen.views;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.DefaultDrawingSupplier;
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
import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.PointClickEvent;
import com.vaadin.addon.charts.PointClickListener;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.Cursor;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.DataSeriesItem;
import com.vaadin.addon.charts.model.Labels;
import com.vaadin.addon.charts.model.PlotOptionsPie;
import com.vaadin.addon.charts.model.style.SolidColor;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
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
					layout.addComponent(createChart(workouts));

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

		 JFreeChart chart = ChartFactory.createPieChart(null, dataset, false, false, false);
		 chart.setBackgroundPaint(new Color(221, 95, 50)); // transparent black
		 chart.setBorderVisible(false);

		 PiePlot plot = (PiePlot) chart.getPlot();
		 plot.setLabelPaint(Color.WHITE);

		 plot.setLabelOutlinePaint(new Color(221, 95, 50));
		 plot.setSectionOutlinePaint(Color.WHITE);
//		 plot.setSectionOutlinePaint(new Color(221, 95, 50));
		 plot.setSectionOutlineStroke(new BasicStroke(2.0f));
		 plot.setLabelBackgroundPaint(new Color(221, 95, 50));
		 plot.setBackgroundPaint(new Color(221, 95, 50));
		 plot.setLabelFont(Font.decode(Font.MONOSPACED));
		 plot.setDrawingSupplier(new ChartDrawingSupplier());
		 plot.setOutlineVisible(false);
		 plot.setLabelLinkPaint(Color.WHITE);
		 plot.setLabelGenerator(new StandardPieSectionLabelGenerator(
		            "{0}: {2}", NumberFormat.getNumberInstance(), NumberFormat.getPercentInstance()
		        ));

		 return new JFreeChartWrapper(chart);
	}

	public static Chart createChart(List<Workout> workouts) {
		Chart chart = new Chart(ChartType.PIE);

		Configuration conf = chart.getConfiguration();

		PlotOptionsPie plotOptions = new PlotOptionsPie();
		plotOptions.setCursor(Cursor.POINTER);
		Labels dataLabels = new Labels();
		dataLabels.setEnabled(true);
		dataLabels.setColor(new SolidColor(0, 0, 0));
		dataLabels.setConnectorColor(new SolidColor(0, 0, 0));
		dataLabels
		.setFormatter("'<b>'+ this.point.name +'</b>: '+ this.percentage +' %'");
		plotOptions.setDataLabels(dataLabels);
		conf.setPlotOptions(plotOptions);

		final DataSeries series = new DataSeries();
		 Map<Sport, Integer> sports = new HashMap<Sport, Integer>();
		 for (Workout workout : workouts) {
			if (sports.containsKey(workout.getSport())){
				sports.put(workout.getSport(), sports.get(workout.getSport()) + 1);
			} else {
				sports.put(workout.getSport(), 1);
			}
		}

		 for (Entry<Sport, Integer> sport : sports.entrySet()) {
			 series.add(new DataSeriesItem(sport.getKey().description(), sport.getValue()));
		}


		conf.setSeries(series);
		chart.addPointClickListener(new PointClickListener() {
		@Override
		public void onClick(PointClickEvent event) {
		Notification.show("Click: " + series.get(event.getPointIndex()).getName());
		}
		});

		chart.drawChart(conf);

		return chart;
		}

	class ChartDrawingSupplier extends DefaultDrawingSupplier  {

	    public Paint[] paintSequence;
	    public int paintIndex;
	    public int fillPaintIndex;

	    {
	        paintSequence =  new Paint[] {
	                new Color(74,112, 147),
	                new Color(73,207, 229),
	                new Color(255,188,65),
	                new Color(194,71,88),
	                new Color(119,170,84),
	                new Color(112,113,200),
	                new Color(197,143,186),
	                new Color(181,185,198),
	        };
	    }

	    @Override
	    public Paint getNextPaint() {
	        Paint result
	        = paintSequence[paintIndex % paintSequence.length];
	        paintIndex++;
	        return result;
	    }


	    @Override
	    public Paint getNextFillPaint() {
	        Paint result
	        = paintSequence[fillPaintIndex % paintSequence.length];
	        fillPaintIndex++;
	        return result;
	    }
	}

}