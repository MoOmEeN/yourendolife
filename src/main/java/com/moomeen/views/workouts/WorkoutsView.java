package com.moomeen.views.workouts;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.UIScope;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.navigator.VaadinView;

import com.moomeen.EndomondoSessionHolder;
import com.moomeen.endo2java.error.InvocationException;
import com.moomeen.views.AbstractContentView;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;

@VaadinView(name = "workouts")
@UIScope
public class WorkoutsView extends AbstractContentView {

	@Autowired
	private EventBus eventBus;

	@Autowired
	private EndomondoSessionHolder sessionHolder;

	@Override
	public Component content() {
		TabSheet ts = new TabSheet();

		try {
			ts.addTab(new WorkoutsList(sessionHolder.getWorkouts()), "List");
			ts.addTab(new WorkoutsMap(), "Map");
			ts.addTab(new WorkoutsCalendar(sessionHolder.getWorkouts()), "Calendar");
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