package com.moomeen.views.main;

import java.util.Collections;
import java.util.List;

import com.moomeen.endo.EndomondoSessionHolder;
import com.moomeen.endo2java.error.InvocationException;
import com.moomeen.endo2java.model.DetailedWorkout;
import com.moomeen.endo2java.model.Workout;
import com.moomeen.views.workouts.details.WorkoutDetails;
import com.moomeen.views.workouts.list.WorkoutClickCallback;
import com.moomeen.views.workouts.list.WorkoutsList;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ChameleonTheme;

public class WorkoutsListView extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4727313957446648131L;
	
	private List<Workout> workouts;
	
	private EndomondoSessionHolder endoSession;
	
	private String noWorkoutsMessage = "No workouts selected";
	
	private WorkoutsList workoutsList;
	
	private VerticalLayout layout;
	
	private Button toggleButton;
	
	@SuppressWarnings("unchecked")
	public WorkoutsListView(EndomondoSessionHolder session) {
		this.workouts = Collections.EMPTY_LIST;
		this.endoSession = session;
		init();
	}

	public WorkoutsListView(List<Workout> workouts, EndomondoSessionHolder session) {
		this.workouts = workouts;
		this.endoSession = session;
		init();
	}
	
	private void init() {
		layout = new VerticalLayout();
		Button hideButton = getToggleButton(layout);
		layout.addComponent(hideButton);
		layout.setComponentAlignment(hideButton, Alignment.TOP_RIGHT);
		
		drawList();
		hideList();
		
		setStyleName("stripe-workouts");
		setContent(layout);
	}
	
	private void drawList(){
		if (workoutsList != null){
			layout.removeComponent(workoutsList);
		}
		workoutsList = getWorkoutsList(workouts);
		layout.addComponent(workoutsList);
		layout.setComponentAlignment(workoutsList, Alignment.MIDDLE_CENTER);
	}
	
	private WorkoutsList getWorkoutsList(final List<Workout> workouts) {
		WorkoutsList workoutsList = new WorkoutsList(workouts, new WorkoutClickCallback() {

			@Override
			public void clicked(long workoutId) throws InvocationException {
				DetailedWorkout workout = endoSession.getWorkout(workoutId);
				final Window window = new Window(workout.getSport().description() + " - " + workout.getStartTime()); // TODO
				window.setWidth("60%");
				window.setHeight("60%");
				window.center();
				window.setContent(new WorkoutDetails(workout));
				UI.getCurrent().addWindow(window);
			}
		});
		return workoutsList;
	}

	private Button getToggleButton(VerticalLayout layout) {
		toggleButton = new Button();
		toggleButton.setWidth("100%");
		toggleButton.addStyleName(ChameleonTheme.BUTTON_ICON_ON_RIGHT);
		toggleButton.setIcon(FontAwesome.CARET_LEFT);
		toggleButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -3830368788156535945L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (!workoutsList.isVisible()){
					showList();
				} else {
					hideList();
				}
			}
		});
		return toggleButton;
	}
	
	public void hideList(){
		if (workoutsList.isVisible()){
			workoutsList.setVisible(false);
		}
		toggleButton.setIcon(FontAwesome.CARET_LEFT);
	}
	
	public void showList(){
		if (!workoutsList.isVisible()){
			workoutsList.setVisible(true);
		}
		toggleButton.setIcon(FontAwesome.CARET_DOWN);
	}

	public void setWorkouts(List<Workout> workouts){
		this.workouts = workouts;
		drawList();
		showList();
	}

}
