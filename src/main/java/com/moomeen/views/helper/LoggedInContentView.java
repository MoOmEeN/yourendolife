package com.moomeen.views.helper;

import com.moomeen.views.menu.LoggedInMenu;
import com.vaadin.ui.Component;


@SuppressWarnings("serial")
public abstract class LoggedInContentView extends ContentView {

	@Override
	protected Component menu(){
		return new LoggedInMenu();
	}

}
