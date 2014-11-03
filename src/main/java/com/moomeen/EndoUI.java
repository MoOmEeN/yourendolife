package com.moomeen;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.VaadinUI;
import org.vaadin.spring.navigator.SpringViewProvider;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;

@VaadinUI
@Theme("mytheme")
public class EndoUI extends UI {

	@Autowired
	private SpringViewProvider viewProvider;

	@Autowired
	private ViewChangeListener viewChangeListener;

	@Override
	protected void init(VaadinRequest vaadinRequest) {
		setWidth("1280px");
		setStyleName("ui");
		Navigator navigator = new Navigator(this, this);
		navigator.addProvider(viewProvider);
		setNavigator(navigator);
		viewChangeListener.setNavigator(navigator);
		navigator.navigateTo("login");
	}

}
