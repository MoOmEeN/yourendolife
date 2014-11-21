package com.moomeen.views.workouts;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.UIScope;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.navigator.VaadinView;

import com.moomeen.endo.EndomondoSessionHolder;
import com.moomeen.endo2java.error.InvocationException;
import com.moomeen.endo2java.model.Workout;
import com.moomeen.location.LocationService;
import com.moomeen.location.Place;
import com.moomeen.views.AbstractContentView;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;

@VaadinView(name = "workouts")
@UIScope
public class WorkoutsView extends AbstractContentView {

	@Autowired
	private EventBus eventBus;

	@Autowired
	private EndomondoSessionHolder sessionHolder;
	
	@Autowired
	LocationService locationService;

	@Override
	public Component content() {

		
		TabSheet ts = new TabSheet();

		try {
			// TODO remove
			Map<Place, List<Workout>> mappedWorkouts = locationService.determineCities(sessionHolder.getWorkouts());
			
			ts.addTab(new WorkoutsList(sessionHolder), "List", FontAwesome.LIST);
			ts.addTab(new WorkoutsMap(), "Map", FontAwesome.GLOBE);
			ts.addTab(new WorkoutsCalendar(sessionHolder.getWorkouts()), "Calendar", FontAwesome.CALENDAR);
		} catch (InvocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ts;
	}

	@Override
	public void enter(ViewChangeEvent event) {

	}



}