package com.moomeen.views;

import org.vaadin.spring.events.EventBus;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.GridLayout;

public class Menu extends GridLayout implements View {

	public Menu(final EventBus eventBus) {
		setRows(1);
		setColumns(5); // TODO
		setStyleName("menu-bar");
		setWidth("1280px");

		Button button = new Button("Workouts");
		button.setStyleName("menu-item");
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
