package com.moomeen.views.workouts.calendar;

import org.joda.time.DateTime;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Calendar;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;

public class CopyOfWorkoutsCalendarControls extends HorizontalLayout {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8637355242960199345L;
	
	private static final String MONTH = "Month";
	private static final String WEEK = "Week";
	private static final String DAY = "Day";
	
	private Calendar calendar;
	private	DateTime now = DateTime.now();
	
	public CopyOfWorkoutsCalendarControls(Calendar calendar) {
		this.calendar = calendar;
		
        final ComboBox rangeComboBox = new ComboBox();

        rangeComboBox.addItem(MONTH);
        rangeComboBox.addItem(WEEK);
        rangeComboBox.addItem(DAY);
        rangeComboBox.setImmediate(true);
        rangeComboBox.setNullSelectionAllowed(false);
        rangeComboBox.setWidth("70px");
        
        rangeComboBox.addValueChangeListener(new RangeChangeListener());
        rangeComboBox.select(MONTH);
        rangeComboBox.addStyleName("pagedtable-itemsperpagecombobox");
        rangeComboBox.addStyleName("pagedtable-combobox");
		
        
	}
	
	private void setMonthView(){
		calendar.setStartDate(new DateTime(now.year().get(), now.monthOfYear().get(), 1, 0, 0).toDate());
		calendar.setEndDate(new DateTime(now.year().get(), now.monthOfYear().get(), now.dayOfMonth().withMaximumValue().dayOfMonth().get(), 0, 0).toDate());
	}
	
	private void setDayView(){
		calendar.setStartDate(now.toDate());
		calendar.setEndDate(now.toDate());
	}

	private void setWeekView(){
		calendar.setStartDate(new DateTime(now.year().get(), now.monthOfYear().get(), now.weekOfWeekyear().get() * 7 - 7, 0, 0).toDate());
		calendar.setEndDate(new DateTime(now.year().get(), now.monthOfYear().get(), now.weekOfWeekyear().get() * 7, 0, 0).toDate());
	}
	
	private class RangeChangeListener implements ValueChangeListener {

		/**
		 * 
		 */
		private static final long serialVersionUID = -3206401772199938835L;

		@Override
		public void valueChange(ValueChangeEvent event) {
        	if (event.getProperty().getValue().equals(MONTH)){
        		setMonthView();
        	} else if (event.getProperty().getValue().equals(WEEK)){
        		setWeekView();
        	} else if (event.getProperty().getValue().equals(DAY)){
        		setDayView();
        	}
		}
	}

}
