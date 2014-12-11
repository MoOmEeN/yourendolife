package com.moomeen.views.workouts.list;

import com.moomeen.endo.EndomondoSessionHolder;
import com.moomeen.endo2java.error.InvocationException;
import com.moomeen.endo2java.model.DetailedWorkout;
import com.moomeen.utils.SpringContextHolder;
import com.moomeen.views.LocaleHelper;
import com.moomeen.views.workouts.details.WorkoutDetails;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

public class WorkoutClickCallback implements ItemClickCallback {

	private EndomondoSessionHolder session;
	
	public WorkoutClickCallback() {
		this.session = SpringContextHolder.lookupBean(EndomondoSessionHolder.class);
	}
	
	@SuppressWarnings("serial")
	@Override
	public void clicked(long workoutId) throws InvocationException {
		final DetailedWorkout workout = session.getWorkout(workoutId);
		final Window window = new Window(getWindowDescription(workout));
		window.setWidth("60%");
		window.setHeight("60%");
		window.center();
		
		window.setContent(new WorkoutDetails(workout));
		UI.getCurrent().addWindow(window);
	}
	
	private String getWindowDescription(DetailedWorkout workout){
		StringBuilder sb = new StringBuilder();
		sb.append(workout.getSport().description());
		sb.append(" - ");
		sb.append(workout.getStartTime().toString(LocaleHelper.getDateFormat()));
		sb.append(" - ");
		sb.append(LocaleHelper.getPeriodFormatter().print(workout.getDuration().toPeriod()));
		sb.append(" - ");
		sb.append(workout.getDistance() + "km");
		return sb.toString();
	}

}
