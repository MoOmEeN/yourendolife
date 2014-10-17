package com.moomeen;

public enum ViewChangeEvent {

	WORKOUTS_LIST("workoutsList");

	private String viewName;

	private ViewChangeEvent(String viewName) {
		this.viewName = viewName;
	}

	public String view(){
		return viewName;
	}

}
