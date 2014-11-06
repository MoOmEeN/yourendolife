package com.moomeen.views.workouts;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.UIScope;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.navigator.VaadinView;

import com.moomeen.EndomondoSessionHolder;
import com.moomeen.endo2java.error.InvocationException;
import com.moomeen.views.AbstractContentView;
import com.moomeen.views.WorkoutsCalendar;
import com.moomeen.views.WorkoutsList;
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
		ts.addStyleName("framed");
		ts.setWidth("90%");
		try {
			ts.addTab(new WorkoutsList(sessionHolder.getSession().getWorkouts()), "List");
			ts.addTab(new WorkoutsMap(), "Map");
			ts.addTab(new WorkoutsCalendar(), "Calendar");
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