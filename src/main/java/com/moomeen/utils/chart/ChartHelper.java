package com.moomeen.utils.chart;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.vaadin.addon.JFreeChartWrapper;

import com.moomeen.endo2java.model.Sport;
import com.moomeen.endo2java.model.Workout;
import com.vaadin.ui.Component;

public class ChartHelper {
	
	@SuppressWarnings("deprecation")
	public static Component sportsPieChart(Map<Sport, List<Workout>> sports, Color foreground, Color background){
		DefaultPieDataset dataset = prepareDataSet(sports);
		JFreeChart chart = ChartFactory.createPieChart(null, dataset, false, false, false);
		chart.setBackgroundPaint(background);
		chart.setBorderVisible(false);

		PiePlot plot = (PiePlot) chart.getPlot();
		plot.setLabelPaint(foreground);
		plot.setLabelOutlinePaint(background);
		plot.setLabelBackgroundPaint(background);
		plot.setLabelFont(Font.decode(Font.MONOSPACED));
		plot.setLabelLinkPaint(foreground);
		plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}: {2}",
				NumberFormat.getNumberInstance(), NumberFormat.getPercentInstance()));
		
		plot.setSectionOutlinePaint(foreground);
		plot.setSectionOutlineStroke(new BasicStroke(2.0f));
		
		plot.setBackgroundPaint(background);
		plot.setOutlineVisible(false);
		plot.setDrawingSupplier(new ChartDrawingSupplier());

		return new JFreeChartWrapper(chart);
	}

	private static DefaultPieDataset prepareDataSet(Map<Sport, List<Workout>> sports) {
		DefaultPieDataset dataset = new DefaultPieDataset();
		for (Entry<Sport, List<Workout>> sport : sports.entrySet()) {
			dataset.setValue(sport.getKey().description(), sport.getValue().size());
		}
		return dataset;
	}
	
	public static Map<Sport, List<Workout>> groupWorkouts(List<Workout> workouts){
		Map<Sport, List<Workout>> sports = new HashMap<Sport, List<Workout>>();
		 for (Workout workout : workouts) {
			if (!sports.containsKey(workout.getSport())){
				sports.put(workout.getSport(), new ArrayList<Workout>());
			}
			sports.get(workout.getSport()).add(workout);
		}
		 return sortByWorkoutsCount(sports);
	}
	
	private static Map<Sport, List<Workout>> sortByWorkoutsCount(Map<Sport, List<Workout>> map) {
	     List<Entry<Sport, List<Workout>>> list = new ArrayList<Entry<Sport, List<Workout>>>(map.entrySet());
	     Collections.sort(list, new Comparator<Entry<Sport, List<Workout>>>() {

			@Override
			public int compare(Entry<Sport, List<Workout>> o1, Entry<Sport, List<Workout>> o2) {
				return  Integer.valueOf(o2.getValue().size()).compareTo(Integer.valueOf(o1.getValue().size()));
			}
	     });

	    Map<Sport, List<Workout>> result = new LinkedHashMap<Sport, List<Workout>>();
	    for (Iterator<Entry<Sport, List<Workout>>> it = list.iterator(); it.hasNext();) {
	        Map.Entry<Sport, List<Workout>> entry = it.next();
	        result.put(entry.getKey(), entry.getValue());
	    }
	    return result;
	} 

}
