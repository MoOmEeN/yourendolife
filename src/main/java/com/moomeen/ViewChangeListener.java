package com.moomeen;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.spring.UIScope;
import org.vaadin.spring.events.Event;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventBusListener;

import com.vaadin.navigator.Navigator;

@Service
@UIScope
public class ViewChangeListener implements EventBusListener<ViewChangeEvent> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8446695067031763077L;

	@Autowired
	private EventBus eventBus;

	private Navigator navigator;

	@PostConstruct
	public void init(){
		eventBus.subscribe(this);
	}

	public void setNavigator(Navigator navigator){
		this.navigator = navigator;
	}

	@Override
	public void onEvent(Event<ViewChangeEvent> event) {
		navigator.navigateTo(event.getPayload().view());
	}

}
