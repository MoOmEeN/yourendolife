package com.moomeen.views.menu;

import static com.moomeen.utils.ImageResourceLoader.*;

import org.vaadin.spring.events.EventBus;

import com.moomeen.utils.SpringContextHolder;
import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;

@SuppressWarnings("serial")
public abstract class Menu extends HorizontalLayout implements View {
	
	protected EventBus eventBus;
	
	public Menu() {
		this.eventBus = SpringContextHolder.lookupBean(EventBus.class);
		Image image = fromResourceImage("logo_white.png");
		image.addClickListener(new ClickListener() {
			
			@Override
			public void click(com.vaadin.event.MouseEvents.ClickEvent event) {
				
			}
		});
		image.setStyleName("logo");
		addComponent(image);
		addStyleName("menu-bar");
	}

	protected void addMenuItem(String text, final com.moomeen.ViewChangeEvent viewChangeEvent) {
		addMenuItem(text, viewChangeEvent, MenuItemFloat.LEFT);
	}
	
	protected void addMenuItem(String text, Button.ClickListener clickListener) {
		addMenuItem(text, clickListener, MenuItemFloat.LEFT);
	}
	
	protected void addMenuItem(String text, Button.ClickListener clickListener, MenuItemFloat itemFloat) {
		addMenuItem(text, clickListener, itemFloat, null);
	}
	
	protected void addMenuItem(String text, Button.ClickListener clickListener, MenuItemFloat itemFloat, String styleName) {
		Button button = createButton(text, itemFloat);
		if (styleName != null){
			button.addStyleName(styleName);
		}
		button.addClickListener(clickListener);
	}
	
	protected void addMenuItem(String text, final com.moomeen.ViewChangeEvent viewChangeEvent, MenuItemFloat itemFloat) {
		addMenuItem(text,  viewChangeEvent, itemFloat, null);
	}
	
	protected void addMenuItem(String text, final com.moomeen.ViewChangeEvent viewChangeEvent, MenuItemFloat itemFloat, String styleName) {
		Button button = createButton(text, itemFloat);
		if (styleName != null){
			button.addStyleName(styleName);
		}
		button.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
					eventBus.publish(this, viewChangeEvent);
			}
		});
	}

	private Button createButton(String text, MenuItemFloat itemFloat) {
		Button button = new Button(text);
		button.addStyleName("menu-item");
		if (itemFloat == MenuItemFloat.RIGHT){
			button.addStyleName("menu-item-right");
		}
		addComponent(button);
		return button;
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
	}
}
