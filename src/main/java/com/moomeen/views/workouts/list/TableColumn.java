package com.moomeen.views.workouts.list;


public enum TableColumn {
	
	DISTANCE("Distance (km)"),
	DURATION("Duration"),
	SPORT("Sport"),
	START_DATE("Start Date"),
	CALORIES("Calories"),
	BURGERS("Burgers Burned");
	
	
	private String desc;
	
	private TableColumn(String desc) {	
		this.desc = desc;
	}
	
	public String description(){
		return desc;
	}

}
