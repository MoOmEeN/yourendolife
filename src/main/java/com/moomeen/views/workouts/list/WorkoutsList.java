package com.moomeen.views.workouts.list;

import java.util.List;

import com.moomeen.endo2java.model.Workout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.VerticalLayout;


public class WorkoutsList extends VerticalLayout {

	/**
	 *
	 */
	private static final long serialVersionUID = 5943429368424740640L;
	
	private WorkoutsTable table;
	WorkoutsTableControls tableControls;

	public WorkoutsList(final List<Workout> workouts, ItemClickCallback clickCallback) {
		table = new WorkoutsTable(workouts, clickCallback);
		setupList(table); 
	}
	
	public WorkoutsList(ItemClickCallback clickCallback) {
		table = new WorkoutsTable(clickCallback);
		setupList(table); 
	}

	public void setWorkouts(List<Workout> workouts){
		table.setWorkouts(workouts);
		tableControls.refreshControls();
	}
	
	private void setupList(WorkoutsTable table) {
		tableControls = new WorkoutsTableControls(table);
		
		addComponent(table);
		addComponent(tableControls);
		setComponentAlignment(table, Alignment.MIDDLE_CENTER);
		setComponentAlignment(tableControls, Alignment.BOTTOM_CENTER);
	}
}
