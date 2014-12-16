package com.moomeen.views.places;

import java.awt.Color;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.moomeen.endo2java.model.Workout;
import com.moomeen.utils.chart.ChartHelper;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

@SuppressWarnings("serial")
public class PlacesChartStripe extends HorizontalLayout {

	private static final Color FOREGROUND = Color.WHITE;
	private static final Color BACKGROUND = new Color(146,176,106);

	private Map<String, List<Workout>> byCountry;

	public PlacesChartStripe(Map<String, List<Workout>> byCountry) {
		this.byCountry = byCountry;
		Component chart = ChartHelper.placesStackedBarChart(byCountry, FOREGROUND, BACKGROUND);

		Label textLabel = new Label(produceText(), ContentMode.HTML);
		textLabel.setStyleName("h2");

		setWidth("100%");
		addComponent(chart);
		addComponent(textLabel);
		setComponentAlignment(chart, Alignment.MIDDLE_LEFT);
		setComponentAlignment(textLabel, Alignment.MIDDLE_RIGHT);

		setExpandRatio(chart, 1.0f);
		setExpandRatio(textLabel, 1.0f);
	}

	private String produceText(){
		Map<String, List<Workout>> grouped = ChartHelper.sortByWorkoutsCount(byCountry);

		StringBuilder sb = new StringBuilder();
		sb.append("You've done ");
		if (byCountry.isEmpty()){
			sb.append("0 workouts");
		} else {
			int count = 0;
			for (Entry<String, List<Workout>> entry : grouped.entrySet()) {
				sb.append(String.format("<p class=\"big-font\">%d</p> %s in %s", entry.getValue().size(), count == 0 ? entry.getValue().size() == 1 ? "workout" : "workouts" : "", entry.getKey()));
				if (count == grouped.size() - 2){
					sb.append(" and");
				} else if (count != grouped.size() -1){
					sb.append(",");
				} else {
					sb.append(".");
				}
				count++;
			}
		}
		return sb.toString();
	}

}
