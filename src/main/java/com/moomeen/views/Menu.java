package com.moomeen.views;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.spring.events.EventBus;

import com.moomeen.endo.EndomondoSessionHolder;
import com.moomeen.endo2java.model.AccountInfo;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;

public class Menu extends HorizontalLayout implements View {

	/**
	 * 
	 */
	private static final long serialVersionUID = 880448738734609335L;
	
	private static Logger LOG = LoggerFactory.getLogger(Menu.class);
	
	private EndomondoSessionHolder session;
	
	private EventBus eventBus;
	
	public Menu(final EventBus eventBus, EndomondoSessionHolder session) {
		this.eventBus = eventBus;
		this.session = session;
		//setRows(1);
		//setColumns(3); // TODO
		setStyleName("menu-bar");
		
		AccountInfo accountInfo;
			accountInfo = session.getAccountInfo();
			Image avatarImage = new Image(null, new ExternalResource(getPictureUrl(accountInfo)));
			avatarImage.setStyleName("avatar-img");
			addComponent(avatarImage);

		
		createMenuItem("Statistics", com.moomeen.ViewChangeEvent.STATS_VIEW);
		createMenuItem("Places", com.moomeen.ViewChangeEvent.PLACES_VIEW);
		
//		Link workouts = new Link("Workouts", new ExternalResource("https://vaadin.com"));
//		workouts.addStyleName("large");
//		workouts.addStyleName("menu-item");
//		addComponent(workouts);
//		Link places = new Link("Places", new ExternalResource("https://vaadin.com"));
//		addComponent(places);
//		places.addStyleName("large");
//		places.addStyleName("menu-item");
//		Link bests = new Link("Bests", new ExternalResource("https://vaadin.com"));
//		addComponent(bests);
//		bests.addStyleName("large");
//		bests.addStyleName("menu-item");

		
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

	private void createMenuItem(String text, final com.moomeen.ViewChangeEvent viewChangeEvent) {
		Button button = new Button(text);
		button.setStyleName("menu-item");
		addComponent(button);
		button.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
					eventBus.publish(this, viewChangeEvent);
			}
		});
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}
	
	private String getPictureUrl(AccountInfo info){
			Long pcitureId = info.getPictureId();
			return String.format("https://www.endomondo.com/resources/gfx/picture/%d/thumbnail.jpg", pcitureId);
	}

}
