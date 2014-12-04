package com.moomeen.views.workouts.list;

import com.moomeen.endo.EndomondoSessionHolder;
import com.moomeen.endo2java.error.InvocationException;
import com.moomeen.endo2java.model.DetailedWorkout;
import com.moomeen.utils.SpringContextHolder;
import com.moomeen.views.workouts.details.WorkoutDetails;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

public class WorkoutClickCallback implements ItemClickCallback {

	private EndomondoSessionHolder session;
	
	public WorkoutClickCallback() {
		this.session = SpringContextHolder.lookupBean(EndomondoSessionHolder.class);
	}
	
	@Override
	public void clicked(long workoutId) throws InvocationException {
		DetailedWorkout workout = session.getWorkout(workoutId);
		final Window window = new Window(workout.getSport().description() + " - " + workout.getStartTime()); // TODO
		window.setWidth("60%");
		window.setHeight("60%");
		window.center();
		window.setContent(new WorkoutDetails(workout));
		UI.getCurrent().addWindow(window);
	}

}
