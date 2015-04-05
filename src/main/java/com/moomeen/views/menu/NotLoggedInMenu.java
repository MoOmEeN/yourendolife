package com.moomeen.views.menu;

import static com.moomeen.utils.ImageResourceLoader.*;

import com.moomeen.views.FeedbackForm;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Image;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class NotLoggedInMenu extends Menu {
	
	public NotLoggedInMenu() {
		Image image = fromResourceImage("logo_white.png");
		image.setStyleName("logo");
		addComponent(image);
		
		addStyleName("menu-bar-not-logged");
		addMenuItem("Log in", com.moomeen.ViewChangeEvent.LOGIN, MenuItemFloat.RIGHT, "main-item-border");
		addMenuItem("Contact", new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				Window w = new Window("Contact us");
				w.center();
				w.setWidth("400px");
				w.setModal(true);
				w.setContent(new FeedbackForm());
				UI.getCurrent().addWindow(w);
			}
		}, MenuItemFloat.RIGHT);
		
	}

}
