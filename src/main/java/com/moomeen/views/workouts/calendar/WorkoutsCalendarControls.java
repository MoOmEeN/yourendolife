package com.moomeen.views.workouts.calendar;

import static com.moomeen.views.workouts.calendar.CalendarUtils.*;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Calendar;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

public class WorkoutsCalendarControls extends HorizontalLayout {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8637355242960199345L;
	
	private static final String MONTH = "Month";
	private static final String WEEK = "Week";
	private static final String DAY = "Day";
	
	private Calendar calendar;
	
	public WorkoutsCalendarControls(Calendar calendar) {
		this.calendar = calendar;
		
        HorizontalLayout rangeMode = new HorizontalLayout();
        HorizontalLayout rangeManagement = new HorizontalLayout();
		
        ComboBox rangeComboBox = initRangeComboBox();
        Label rangeLabel = new Label("Calendar range: ");
        
        rangeMode.addComponent(rangeLabel);
        rangeMode.addComponent(rangeComboBox);
        rangeMode.setComponentAlignment(rangeLabel, Alignment.MIDDLE_LEFT);
        rangeMode.setComponentAlignment(rangeComboBox, Alignment.MIDDLE_LEFT);
        
        addComponent(rangeMode);
        setWidth("100%");
        setExpandRatio(rangeMode, 1);
	}


	private ComboBox initRangeComboBox() {
		final ComboBox rangeComboBox = new ComboBox();

        rangeComboBox.addItem(MONTH);
        rangeComboBox.addItem(WEEK);
        rangeComboBox.addItem(DAY);
        rangeComboBox.setImmediate(true);
        rangeComboBox.setNullSelectionAllowed(false);
        rangeComboBox.setWidth("100px");
        
        rangeComboBox.addValueChangeListener(new RangeChangeListener());
        rangeComboBox.select(MONTH);
        rangeComboBox.addStyleName("pagedtable-itemsperpagecombobox");
        rangeComboBox.addStyleName("pagedtable-combobox");
        return rangeComboBox;
	}

	
	private class RangeChangeListener implements ValueChangeListener {

		/**
		 * 
		 */
		private static final long serialVersionUID = -3206401772199938835L;

		@Override
		public void valueChange(ValueChangeEvent event) {
        	if (event.getProperty().getValue().equals(MONTH)){
        		setMonthView(calendar);
        	} else if (event.getProperty().getValue().equals(WEEK)){
        		setWeekView(calendar);
        	} else if (event.getProperty().getValue().equals(DAY)){
        		setDayView(calendar);
        	}
		}
	}

}
