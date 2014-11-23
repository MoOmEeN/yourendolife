package com.moomeen.views;

import org.vaadin.spring.events.EventBus;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Link;

public class Menu extends HorizontalLayout implements View {

	public Menu(final EventBus eventBus) {
		//setRows(1);
		//setColumns(3); // TODO
		setStyleName("menu-bar");

		Link workouts = new Link("Workouts", new ExternalResource("https://vaadin.com"));
		workouts.addStyleName("large");
		workouts.addStyleName("menu-item");
		addComponent(workouts);
		Link places = new Link("Places", new ExternalResource("https://vaadin.com"));
		addComponent(places);
		places.addStyleName("large");
		places.addStyleName("menu-item");
		Link bests = new Link("Bests", new ExternalResource("https://vaadin.com"));
		addComponent(bests);
		bests.addStyleName("large");
		bests.addStyleName("menu-item");

//		Button button = new Button("Workouts");
//		button.setStyleName("menu-item");
//		button.setIcon(FontAwesome.HISTORY);
//		addComponent(button);
//		button.addClickListener(new Button.ClickListener() {
//			@Override
//			public void buttonClick(ClickEvent event) {
//					eventBus.publish(this, com.moomeen.ViewChangeEvent.WORKOUTS_LIST);
//			}
//		});
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}

}
