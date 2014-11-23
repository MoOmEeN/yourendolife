package com.moomeen;

public enum ViewChangeEvent {

	MAIN_VIEW("main");

	private String viewName;

	private ViewChangeEvent(String viewName) {
		this.viewName = viewName;
	}

	public String view(){
		return viewName;
	}

}
