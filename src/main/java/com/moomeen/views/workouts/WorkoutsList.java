package com.moomeen.views.workouts;

import java.util.List;

import com.moomeen.endo2java.model.Workout;
import com.moomeen.views.workouts.list.WorkoutsTable;
import com.moomeen.views.workouts.list.WorkoutsTableControls;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.VerticalLayout;


public class WorkoutsList extends VerticalLayout {

	/**
	 *
	 */
	private static final long serialVersionUID = 5943429368424740640L;

	public WorkoutsList(List<Workout> workouts) {
		WorkoutsTable table = new WorkoutsTable(workouts);
		WorkoutsTableControls tableControls = new WorkoutsTableControls(table);

		addComponent(table);
		addComponent(tableControls);
		setComponentAlignment(table, Alignment.MIDDLE_CENTER);
		setComponentAlignment(tableControls, Alignment.BOTTOM_CENTER);
	}
}
