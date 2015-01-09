package com.moomeen.views;



import com.vaadin.server.ExternalResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class Footer extends VerticalLayout {
	
	private static final String CONTACT_EMAIL = "contact@yourendolife.com";
	
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
		 
		 addComponent(panel);
		 setComponentAlignment(panel, Alignment.MIDDLE_CENTER);
		 
		 Label l = new Label("© 2015 yourENDOlife.com");
		 l.setStyleName("footer-copyright");
		 addComponent(l);
		 setComponentAlignment(l, Alignment.BOTTOM_CENTER);
	}

}
