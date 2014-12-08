package com.moomeen.views.places;

import java.awt.Color;
import java.util.List;
import java.util.Map;

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



	public PlacesChartStripe(Map<String, List<Workout>> byCountry) {

		Component chart = ChartHelper.placesStackedBarChart(byCountry, FOREGROUND, BACKGROUND);

		Label textLabel = new Label("LALA", ContentMode.HTML);
		textLabel.setStyleName("h2");

		setWidth("100%");
		addComponent(chart);
		addComponent(textLabel);
		setComponentAlignment(chart, Alignment.MIDDLE_LEFT);
		setComponentAlignment(textLabel, Alignment.MIDDLE_RIGHT);

		setExpandRatio(chart, 1.0f);
		setExpandRatio(textLabel, 1.0f);
	}



}
