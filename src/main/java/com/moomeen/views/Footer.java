package com.moomeen.views;



import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class Footer extends VerticalLayout {
	
	public Footer() {
		
		Panel panel = new Panel();
		
		panel.setWidth("350px");
		
		VerticalLayout loginLayout = new VerticalLayout();
		panel.setContent(loginLayout);
		
		setStyleName("footer");
		loginLayout.setSpacing(true);
		loginLayout.setMargin(true);
		
		TextField name = new TextField();
		name.setInputPrompt("Your email or whatever");
		name.addStyleName("large");
		name.setWidth("324px");
		name.setHeight("50px");
		name.addStyleName("inline-icon");
		name.setIcon(FontAwesome.USER);
		
		 TextArea content = new TextArea();
		 content.setWidth("324px");
		 content.addStyleName("large");
		 content.setInputPrompt("Do you have any suggestion, want to report a problem or just share your feelings?");
		 content.setRows(5);
		 content.addStyleName("inline-icon");
		 content.setIcon(FontAwesome.COMMENT);
		 
		 Button send = new Button("SEND");
		 send.setWidth("324px");
		 send.setHeight("50px");
		 send.setStyleName("footer-send-button");
		 
		 
		 loginLayout.addComponent(content);
		 loginLayout.setComponentAlignment(content, Alignment.TOP_CENTER);
		 loginLayout.addComponent(name);
		 loginLayout.setComponentAlignment(name, Alignment.MIDDLE_CENTER);
		 loginLayout.addComponent(send);
		 loginLayout.setComponentAlignment(send, Alignment.BOTTOM_CENTER);
		 
		 addComponent(panel);
		 setComponentAlignment(panel, Alignment.MIDDLE_CENTER);
		 
		 Label l = new Label("© 2014 yourENDOlife.me");
		 l.setStyleName("footer-copyright");
		 addComponent(l);
		 setComponentAlignment(l, Alignment.BOTTOM_CENTER);
	}

}
