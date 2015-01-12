package com.moomeen.views;

import static com.moomeen.views.FeedbackForm.*;

import com.vaadin.server.ExternalResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class Footer extends VerticalLayout {
	
	public Footer() {
		Panel panel = new Panel();
		
		VerticalLayout footerLayout = new VerticalLayout();
		panel.setContent(footerLayout);
		
		setStyleName("footer");
		
		footerLayout.setSpacing(true);
		footerLayout.setMargin(true);
		
		Link email = new Link(CONTACT_EMAIL, new ExternalResource("mailto:" + CONTACT_EMAIL));
		 
		 footerLayout.addComponent(email);
		 footerLayout.setComponentAlignment(email, Alignment.MIDDLE_CENTER);
		 
		 Button feedback = new Button("feedback");
		 
		 footerLayout.addComponent(feedback);
		 feedback.setStyleName("link");
		 feedback.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				Window w = getFeedbackForm();
				UI.getCurrent().addWindow(w);
			}
		});
		 
		 addComponent(panel);
		 setComponentAlignment(panel, Alignment.MIDDLE_CENTER);
		 
		 Label copyright = new Label("© 2015 yourENDOlife.com");
		 copyright.setStyleName("footer-copyright");
		 addComponent(copyright);
		 setComponentAlignment(copyright, Alignment.BOTTOM_CENTER);
	}
	
	private Window getFeedbackForm(){
		Window w = new Window("Leave your feedback");
		w.center();
		w.setWidth("400px");
		w.setModal(true);
		w.setContent(new FeedbackForm());
		
		return w;
	}

}
