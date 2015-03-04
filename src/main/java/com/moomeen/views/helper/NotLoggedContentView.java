package com.moomeen.views.helper;

import com.moomeen.views.menu.NotLoggedInMenu;
import com.vaadin.ui.Component;

@SuppressWarnings("serial")
public abstract class NotLoggedContentView extends ContentView {

	@Override
	protected Component menu() {
		return new NotLoggedInMenu();
	}

}
