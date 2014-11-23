package com.moomeen.views;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

public abstract class AbstractContentView extends VerticalLayout implements View {

	/**
	 *
	 */
	private static final long serialVersionUID = 9102807959577832012L;
	@Autowired
	protected EventBus eventBus;

	@PostConstruct
	public void init(){
		setSizeFull();
		setHeightUndefined();
		addComponent(new Menu(eventBus));

		Component content = content();
		content.setStyleName("content");
		addComponent(content);
	}

	public abstract Component content();

	@Override
	public void enter(ViewChangeEvent event) {
	}

}
