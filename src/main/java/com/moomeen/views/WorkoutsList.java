package com.moomeen.views;

import org.vaadin.spring.UIScope;
import org.vaadin.spring.navigator.VaadinView;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@VaadinView(name = "workoutsList")
@UIScope
public class WorkoutsList extends VerticalLayout implements View {

	public WorkoutsList() {
		Label l = new Label("eloelo");
		addComponent(l);
	}

	@Override
	public void enter(ViewChangeEvent event) {


	}

}
