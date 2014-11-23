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

	public WorkoutsList(final List<Workout> workouts, WorkoutClickCallback clickCallback) {
		WorkoutsTable table = new WorkoutsTable(workouts, clickCallback);
		WorkoutsTableControls tableControls = new WorkoutsTableControls(table);

		addComponent(table);
		addComponent(tableControls);
		setComponentAlignment(table, Alignment.MIDDLE_CENTER);
		setComponentAlignment(tableControls, Alignment.BOTTOM_CENTER);
	}

}
