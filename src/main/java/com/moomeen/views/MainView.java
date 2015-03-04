package com.moomeen.views;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.UIScope;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.navigator.VaadinView;

import com.moomeen.views.helper.LazyLoadable;
import com.moomeen.views.helper.NotLoggedContentView;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

@VaadinView(name = "main")
@UIScope
@SuppressWarnings("serial")
public class MainView extends NotLoggedContentView {
	
	@Autowired
	private EventBus eventBus;

	@Override
	public LazyLoadable content() {
		
		return new LazyLoadableContent() {

			@Override
			public Component content() {
				VerticalLayout layout = new VerticalLayout();
				layout.setStyleName("main-view");

				LoginStripe loginStripe = new LoginStripe();
				loginStripe.setStyleName("main-login-stripe");
				
				layout.addComponent(loginStripe);
//				layout.addComponent(workoutsPanel);
//				layout.addComponent(chart);
				return layout;
			}
		};
	}

}
