package com.moomeen.views.workouts.calendar;

import org.joda.time.DateTime;

import com.vaadin.ui.Calendar;

public class CalendarUtils {
	
	public static void setMonthView(Calendar calendar){
		DateTime now = DateTime.now();
		
		calendar.setStartDate(new DateTime(now.year().get(), now.monthOfYear().get(), 1, 0, 0).toDate());
		calendar.setEndDate(new DateTime(now.year().get(), now.monthOfYear().get(), now.dayOfMonth().withMaximumValue().dayOfMonth().get(), 0, 0).toDate());
	}
	
	public static void setDayView(Calendar calendar){
		DateTime now = DateTime.now();
		
		calendar.setStartDate(now.toDate());
		calendar.setEndDate(now.toDate());
	}

	public static void setWeekView(Calendar calendar){
		DateTime now = DateTime.now();
		
		calendar.setStartDate(new DateTime(now.year().get(), now.monthOfYear().get(), now.weekOfWeekyear().get() * 7, 0, 0).toDate());
		calendar.setEndDate(new DateTime(now.year().get(), now.monthOfYear().get(), now.weekOfWeekyear().get() * 7, 0, 0).toDate());
	}

}
