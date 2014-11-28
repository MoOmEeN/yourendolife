package com.moomeen.views.workouts.list;



public enum TableColumnEnum {

	DISTANCE("Distance (km)"),
	DURATION("Duration"),
	SPORT("Sport"),
	START_DATE("Start Date"),
	CALORIES("Calories"),
	BURGERS("Burgers Burned"),
	DESCENT("Descent (m)"),
	ASCENT("Ascent (m)"),
	AVG_SPEED("Average Speed (km/h)"),
	MAX_SPEED("Maximum Speed (km/h)"),
	MIN_ALTITUDE("Minimum Altitude (m)"),
	MAX_ALTITUDE("Maximum Altitude (m)"),
	PEPTALKS("Peptalks"),
	LIKES("Likes"),
	COMMENTS("Comments"),
	VIEW("");

	private String desc;

	private TableColumnEnum(String desc) {
		this.desc = desc;
	}

	public String description(){
		return desc;
	}

}
