package com.moomeen.views.workouts;

import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.ui.VerticalLayout;

public class WorkoutsMap extends VerticalLayout {

	public WorkoutsMap() {
		setSizeFull();
		GoogleMap googleMap = new GoogleMap(null, null, null);

		googleMap.setMinZoom(4);
		googleMap.setMaxZoom(16);
		googleMap.setSizeFull();
		//addComponent(l);
		addComponent(googleMap);
		setExpandRatio(googleMap, 1.0f);
	}

}
