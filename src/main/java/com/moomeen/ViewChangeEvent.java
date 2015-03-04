package com.moomeen;

public enum ViewChangeEvent {

	STATS_VIEW("stats"),
	PLACES_VIEW("places"),
	LOGIN("login");
	
	private String viewName;

	private ViewChangeEvent(String viewName) {
		this.viewName = viewName;
	}

	public String view(){
		return viewName;
	}

}
