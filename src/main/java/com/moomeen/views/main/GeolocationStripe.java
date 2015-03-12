package com.moomeen.views.main;

import static com.moomeen.utils.ImageResourceLoader.*;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;

@SuppressWarnings("serial")
public class GeolocationStripe extends HorizontalLayout {
	
	public GeolocationStripe() {
		setWidth("100%");
		setStyleName("main-view-geo");
		Panel logoPanel = new Panel();
		Image logo = fromResourceImage("googlemaps.png");
		logo.setWidth("100%");
		logoPanel.setContent(logo);
		
		Panel textPanel = new Panel();
		Label text = new Label();
		text.setStyleName("h1");
		text.setValue("See your workouts on the map and identify places you visited");
		textPanel.setContent(text);
		
		addComponent(logoPanel);
		addComponent(textPanel);
		setComponentAlignment(logoPanel, Alignment.MIDDLE_LEFT);
		setComponentAlignment(textPanel, Alignment.MIDDLE_RIGHT);
		setExpandRatio(logoPanel, 1.0f);
		setExpandRatio(textPanel, 1.0f);
		
	}

}
