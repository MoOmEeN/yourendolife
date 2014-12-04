package com.moomeen.views.stats;

import java.awt.Color;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.moomeen.endo.EndomondoSessionHolder;
import com.moomeen.endo2java.model.Sport;
import com.moomeen.endo2java.model.Workout;
import com.moomeen.utils.SpringContextHolder;
import com.moomeen.utils.chart.ChartHelper;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

@SuppressWarnings("serial")
public class ChartStripe extends HorizontalLayout {

	private static final Color FOREGROUND = Color.WHITE;
	private static final Color BACKGROUND = new Color(221, 95, 50);
	
	private EndomondoSessionHolder session;
	
	public ChartStripe() {
		this.session = SpringContextHolder.lookupBean(EndomondoSessionHolder.class);
		Map<Sport, List<Workout>> sports = ChartHelper.groupWorkouts(session.getWorkouts());
		Component chart = ChartHelper.sportsPieChart(sports, FOREGROUND, BACKGROUND);
		
		String text = produceText(sports);
		Label textLabel = new Label(text, ContentMode.HTML);
		textLabel.setStyleName("h2");
		
		setWidth("100%");
		addComponent(chart);
		addComponent(textLabel);
		setComponentAlignment(chart, Alignment.MIDDLE_LEFT);
		setComponentAlignment(textLabel, Alignment.MIDDLE_RIGHT);
		
		setExpandRatio(chart, 1.0f);
		setExpandRatio(textLabel, 1.0f);
	}

	private String produceText(Map<Sport, List<Workout>> sports) {
		StringBuilder sb = new StringBuilder();
		if (sports.isEmpty()){
			sb.append("You've done 0 workouts");
		} else {
			sb.append("You've done<br>");
			int count = 0;
			for (Entry<Sport, List<Workout>> entry : sports.entrySet()) {
				if (count == sports.size() - 1){
					if (sports.size() == 1){
						// only one element
						sb.append(String.format(" <p class=\"big-font\">%d</p> %s workouts.", entry.getValue().size(), entry.getKey().description().toLowerCase()));
					} else {
						// last
						sb.append(String.format(" and <p class=\"big-font\">%d</p> %s workouts.", entry.getValue().size(), entry.getKey().description().toLowerCase()));
						
					}
				} else {
					if (count == sports.size() - 2){
						// one before last
						sb.append(String.format("<p class=\"big-font\">%d</p> %s<br>", entry.getValue().size(), entry.getKey().description().toLowerCase())); // no comma after
					} else {
						sb.append(String.format("<p class=\"big-font\">%d</p> %s,<br>", entry.getValue().size(), entry.getKey().description().toLowerCase()));
					}
					
				}
				count++;
			}

		}
		return sb.toString();
	}
	
}
