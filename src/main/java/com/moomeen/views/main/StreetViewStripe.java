package com.moomeen.views.main;

import static com.moomeen.utils.ImageResourceLoader.*;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;

@SuppressWarnings("serial")
public class StreetViewStripe extends HorizontalLayout {
	
	public StreetViewStripe() {
		setWidth("100%");
		setStyleName("main-view-street");
		Panel logoPanel = new Panel();
		Image logo = fromResourceImage("streetview.png");
		logoPanel.setContent(logo);
		logoPanel.setStyleName("main-view-street-logo");
		
		Panel textPanel = new Panel();
		Label text = new Label();
		text.setStyleName("h1");
		text.setValue("Replay your workout using street view images");
		textPanel.setContent(text);
		
		addComponent(textPanel);
		addComponent(logoPanel);
		setComponentAlignment(logoPanel, Alignment.MIDDLE_LEFT);
		setComponentAlignment(textPanel, Alignment.MIDDLE_RIGHT);
		setExpandRatio(logoPanel, 1.0f);
		setExpandRatio(textPanel, 1.0f);
	}
	
	

}
