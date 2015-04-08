package com.moomeen.views.main;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;

@SuppressWarnings("serial")
public class GeolocationStripe extends HorizontalLayout {
	
	public GeolocationStripe() {
		setWidth("100%");
		setStyleName("main-view-geo");
		
		Panel textPanel = new Panel();
		Label text = new Label();
		text.setStyleName("h1");
		text.setValue("See your workouts on the map and identify places you visited");
		textPanel.setContent(text);
		
		addComponent(textPanel);
		setComponentAlignment(textPanel, Alignment.MIDDLE_RIGHT);
	}

}
