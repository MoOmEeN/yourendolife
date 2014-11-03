package com.moomeen.views;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

public abstract class AbstractContentView extends VerticalLayout implements View {

	@Autowired
	protected EventBus eventBus;

	@PostConstruct
	public void init(){
		setSizeFull();
		Panel p = new Panel();
		p.setStyleName("menu-bar");
		addComponent(p);
		setComponentAlignment(p, Alignment.TOP_CENTER);

		p.setContent(new Menu(eventBus));

		Component content = content();
		addComponent(content);
		setComponentAlignment(content, Alignment.MIDDLE_CENTER);
	}

	public abstract Component content();

	@Override
	public void enter(ViewChangeEvent event) {
	}

}
