package com.moomeen.views.workouts;

import java.util.List;
import java.util.Locale;

import com.moomeen.endo2java.model.Workout;
import com.moomeen.views.workouts.calendar.CalendarUtils;
import com.moomeen.views.workouts.calendar.WorkoutsCalendarControls;
import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Calendar;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.components.calendar.event.BasicEvent;
import com.vaadin.ui.components.calendar.event.CalendarEvent;

public class WorkoutsCalendar extends VerticalLayout {

	
	private Locale browserLocale = Page.getCurrent().getWebBrowser().getLocale();

	
	private Calendar calendar;

	public WorkoutsCalendar(List<Workout> workouts) {
		
		calendar = new Calendar();
		calendar.setLocale(browserLocale);
		for (Workout workout : workouts) {
			CalendarEvent event = new BasicEvent(workout.getSport().description(), "", workout.getStartTime().toDate(), workout.getStartTime().plus(workout.getDuration()).toDate());
			calendar.addEvent(event);
		}
		
		CalendarUtils.setMonthView(calendar);
		calendar.setWidth("100%");

		addComponent(calendar);
		WorkoutsCalendarControls controls = new WorkoutsCalendarControls(calendar);
		addComponent(controls);
		setComponentAlignment(controls, Alignment.BOTTOM_CENTER);
		setComponentAlignment(calendar, Alignment.MIDDLE_CENTER);
	}

	


}
