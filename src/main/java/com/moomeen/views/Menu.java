package com.moomeen.views;

import org.vaadin.spring.events.EventBus;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.GridLayout;

public class Menu extends GridLayout implements View {

	public Menu(final EventBus eventBus) {
		setRows(1);
		setColumns(5); // TODO
		setStyleName("menu-bar");

		Button button = new Button("Workouts");
		button.setStyleName("menu-item");
		button.setIcon(FontAwesome.HISTORY);
		addComponent(button);
		button.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
					eventBus.publish(this, com.moomeen.ViewChangeEvent.WORKOUTS_LIST);
			}
		});
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}

}
