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
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.ui.TextAnchor;
import org.vaadin.addon.JFreeChartWrapper;

import com.moomeen.endo2java.model.Sport;
import com.moomeen.endo2java.model.Workout;
import com.vaadin.ui.Component;

public class ChartHelper {

	@SuppressWarnings("deprecation")
	public static Component sportsPieChart(Map<Sport, List<Workout>> sports, Color foreground, Color background){
		DefaultPieDataset dataset = sportsDataset(sports);
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


	private static DefaultPieDataset sportsDataset(Map<Sport, List<Workout>> sports) {
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

	public static <K> Map<K, List<Workout>> sortByWorkoutsCount(Map<K, List<Workout>> map) {
	     List<Entry<K, List<Workout>>> list = new ArrayList<Entry<K, List<Workout>>>(map.entrySet());
	     Collections.sort(list, new Comparator<Entry<K, List<Workout>>>() {

			@Override
			public int compare(Entry<K, List<Workout>> o1, Entry<K, List<Workout>> o2) {
				return  Integer.valueOf(o2.getValue().size()).compareTo(Integer.valueOf(o1.getValue().size()));
			}
	     });

	    Map<K, List<Workout>> result = new LinkedHashMap<K, List<Workout>>();
	    for (Iterator<Entry<K, List<Workout>>> it = list.iterator(); it.hasNext();) {
	        Map.Entry<K, List<Workout>> entry = it.next();
	        result.put(entry.getKey(), entry.getValue());
	    }
	    return result;
	}

	public static Component placesStackedBarChart(Map<String, List<Workout>> workoutsByCountry, Color foreground, Color background){
		JFreeChart chart = ChartFactory.createStackedBarChart(null, null, "Workouts", placesDataset(workoutsByCountry), PlotOrientation.VERTICAL, true	, false, false);
		chart.setBackgroundPaint(background);

		CategoryPlot categoryPlot = chart.getCategoryPlot();
		categoryPlot.setDomainGridlinePaint(background);
		categoryPlot.setBackgroundPaint(background);

		categoryPlot.setOutlineVisible(false);

		CategoryAxis domainAxis = categoryPlot.getDomainAxis();
		domainAxis.setLabelPaint(foreground);
		domainAxis.setLabelFont(Font.decode(Font.MONOSPACED));
		domainAxis.setTickLabelPaint(foreground);
		domainAxis.setTickLabelFont(Font.decode(Font.MONOSPACED));
		domainAxis.setAxisLinePaint(foreground);

		ValueAxis valueAxis = categoryPlot.getRangeAxis();
		valueAxis.setLabelPaint(foreground);
		valueAxis.setTickLabelPaint(foreground);
		valueAxis.setTickLabelFont(Font.decode(Font.MONOSPACED));
		valueAxis.setLabelFont(Font.decode(Font.MONOSPACED));

		valueAxis.setAxisLinePaint(foreground);

		StackedBarRenderer barrenderer = (StackedBarRenderer)categoryPlot.getRenderer();
		barrenderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		barrenderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BOTTOM_CENTER));
		barrenderer.setBaseItemLabelsVisible(true);
		barrenderer.setBaseItemLabelPaint(foreground);
		barrenderer.setBaseItemLabelFont(Font.decode(Font.MONOSPACED));
		barrenderer.setShadowVisible(false);

		categoryPlot.setDrawingSupplier(new ChartDrawingSupplier());
		((BarRenderer) categoryPlot.getRenderer()).setBarPainter(new StandardBarPainter());

		LegendTitle legend = chart.getLegend();
		legend.setBackgroundPaint(background);
		legend.setBorder(new BlockBorder(background));
		legend.setItemPaint(foreground);
		legend.setItemFont(Font.decode(Font.MONOSPACED));

		return new JFreeChartWrapper(chart);
	}

	private static CategoryDataset placesDataset(Map<String, List<Workout>> items) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		for (Entry<String, List<Workout>> item : items.entrySet()) {
			Map<Sport, List<Workout>> sports = ChartHelper.groupWorkouts(item.getValue());
			for (Entry<Sport, List<Workout>> sport : sports.entrySet()) {
				dataset.addValue(sport.getValue().size(), sport.getKey().description(), item.getKey());
			}
		}
		return dataset;
	}

}
