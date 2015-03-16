package com.moomeen.views.main;

import static com.moomeen.utils.ImageResourceLoader.*;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;

@SuppressWarnings("serial")
public class DistancesStripe extends HorizontalLayout {
	
	public DistancesStripe() {
		setWidth("100%");
		setStyleName("main-view-distances");
		Panel logoPanel = new Panel();
		Image logo = fromResourceImage("timer.png");
		logoPanel.setContent(logo);
		logoPanel.setStyleName("main-view-distances-logo");
		
		Panel textPanel = new Panel();
		Label text = new Label();
		text.setStyleName("h1");
		text.setValue("Calculate your best result in any distance you like");
		textPanel.setContent(text);
		
		addComponent(logoPanel);
		addComponent(textPanel);
		setComponentAlignment(logoPanel, Alignment.MIDDLE_LEFT);
		setComponentAlignment(textPanel, Alignment.MIDDLE_RIGHT);
		setExpandRatio(logoPanel, 1.0f);
		setExpandRatio(textPanel, 1.0f);
	}

}
