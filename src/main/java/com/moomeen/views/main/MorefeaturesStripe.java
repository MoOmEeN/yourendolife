package com.moomeen.views.main;

import static com.moomeen.utils.ImageResourceLoader.*;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class MorefeaturesStripe extends HorizontalLayout {
	
	public MorefeaturesStripe() {
		setWidth("100%");
		setStyleName("main-view-distances");
		
		VerticalLayout distances = getDistancesPart();
		VerticalLayout stats = getStatsPart();
		VerticalLayout export = getExportPart();
		
		addComponent(distances);
		addComponent(stats);
		addComponent(export);
		setExpandRatio(distances, 1.0f);
		setExpandRatio(stats, 1.0f);
		setExpandRatio(export, 1.0f);
	}

	private VerticalLayout getStatsPart() {
		VerticalLayout distances = new VerticalLayout();
		Panel distancesLogoPanel = new Panel();
		Image distancesLogo = fromResourceImage("stats.png");
		distancesLogoPanel.setContent(distancesLogo);
		distancesLogoPanel.setStyleName("main-view-distances-logo");
		
		Panel distancesTextPanel = new Panel();
		Label distancestext = new Label();
		distancestext.setStyleName("h1");
		distancestext.setValue("View statistics of your workouts");
		distancesTextPanel.setContent(distancestext);
		distances.addComponent(distancesLogoPanel);
		distances.addComponent(distancesTextPanel);
		return distances;
	}

	private VerticalLayout getExportPart() {
		VerticalLayout distances = new VerticalLayout();
		Panel distancesLogoPanel = new Panel();
		Image distancesLogo = fromResourceImage("Excel.png");
		distancesLogoPanel.setContent(distancesLogo);
		distancesLogoPanel.setStyleName("main-view-distances-logo");
		
		Panel distancesTextPanel = new Panel();
		Label distancestext = new Label();
		distancestext.setStyleName("h1");
		distancestext.setValue("Export your workouts to Excel with one click");
		distancesTextPanel.setContent(distancestext);
		distances.addComponent(distancesLogoPanel);
		distances.addComponent(distancesTextPanel);
		return distances;
	}

	private VerticalLayout getDistancesPart() {
		VerticalLayout distances = new VerticalLayout();
		Panel distancesLogoPanel = new Panel();
		Image distancesLogo = fromResourceImage("timer.png");
		distancesLogoPanel.setContent(distancesLogo);
		distancesLogoPanel.setStyleName("main-view-distances-logo");
		
		Panel distancesTextPanel = new Panel();
		Label distancestext = new Label();
		distancestext.setStyleName("h1");
		distancestext.setValue("Calculate your best result for any distance you like");
		distancesTextPanel.setContent(distancestext);
		distances.addComponent(distancesLogoPanel);
		distances.addComponent(distancesTextPanel);
		return distances;
	}

}
