package com.moomeen.views;

import static com.moomeen.views.FeedbackForm.*;

import com.vaadin.server.ExternalResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class Footer extends VerticalLayout {
	
	public Footer() {
		setStyleName("footer");
		
		Panel panel = new Panel();
		panel.setContent(getFooterMessage());
		addComponent(panel);
		setComponentAlignment(panel, Alignment.MIDDLE_CENTER);

		Label copyright = new Label("© 2015 yourENDOlife.com");
		copyright.setStyleName("footer-copyright");
		addComponent(copyright);
		setComponentAlignment(copyright, Alignment.BOTTOM_CENTER);
	}

	private Component getFooterMessage() {
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);

		Label textPartOne = new Label("Question? Suggestion? Any feedback is welcome! You can use our");
		Button feedback = getFeedbackWindowButton("contact form");
		Label textPartTwo = new Label("or send it directly to");
		Link email = getEmailLink();
	
		add(layout, textPartOne);
		add(layout, feedback);
		add(layout, textPartTwo);
		add(layout, email);

		return layout;
	}

	private void add(VerticalLayout layout, Component component) {
		layout.addComponent(component);
		layout.setComponentAlignment(component, Alignment.MIDDLE_CENTER);
	}

	private Button getFeedbackWindowButton(String text) {
		Button feedback = new Button(text);
		feedback.setStyleName("link");

		feedback.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				Window w = getFeedbackWindow();
				UI.getCurrent().addWindow(w);
			}
		});
		return feedback;
	}

	private Link getEmailLink() {
		Link email = new Link(CONTACT_EMAIL, new ExternalResource("mailto:" + CONTACT_EMAIL));
		return email;
	}
	
	private Window getFeedbackWindow(){
		Window w = new Window("Leave your feedback");
		w.center();
		w.setWidth("400px");
		w.setModal(true);
		w.setContent(new FeedbackForm());
		
		return w;
	}

}
