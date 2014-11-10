package com.moomeen.views.workouts;

import java.util.List;

import org.joda.time.DateTime;

import com.moomeen.endo2java.model.Workout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Calendar;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.components.calendar.event.BasicEvent;
import com.vaadin.ui.components.calendar.event.CalendarEvent;

public class WorkoutsCalendar extends VerticalLayout {

	Calendar c;

	public WorkoutsCalendar(List<Workout> workouts) {
		setSizeFull();

		DateTime now = DateTime.now();
		c = new Calendar();
		c.setStartDate(new DateTime(now.year().get(), now.monthOfYear().get(), 1, 0, 0).toDate());
		c.setEndDate(new DateTime(now.year().get(), now.monthOfYear().get(), now.dayOfMonth().withMaximumValue().dayOfMonth().get(), 0, 0).toDate());
		for (Workout workout : workouts) {
			CalendarEvent event = new BasicEvent(workout.getSport().description(), "", workout.getStartTime().toDate(), workout.getStartTime().plus(workout.getDuration()).toDate());
			c.addEvent(event);
		}
addComponent(c);
setComponentAlignment(c, Alignment.MIDDLE_CENTER);



Button b = new Button("forward");
b.addClickListener(new ClickListener() {

	@Override
	public void buttonClick(ClickEvent event) {
		c.setStartDate(new DateTime(c.getStartDate()).plusMonths(1).toDate());
		c.setEndDate(new DateTime(c.getEndDate()).plusMonths(1).toDate());
	}
});


Button b2 = new Button("backward");
b2.addClickListener(new ClickListener() {

	@Override
	public void buttonClick(ClickEvent event) {
		c.setStartDate(new DateTime(c.getStartDate()).minusMonths(1).toDate());
		c.setEndDate(new DateTime(c.getEndDate()).minusMonths(1).toDate());

	}
});

addComponent(b);
addComponent(b2);
	}


}
