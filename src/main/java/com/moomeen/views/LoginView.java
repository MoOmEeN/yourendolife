package com.moomeen.views;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.UIScope;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.navigator.VaadinView;

import com.ejt.vaadin.loginform.LoginForm;
import com.moomeen.endo.EndomondoSessionHolder;
import com.moomeen.endo2java.EndomondoSession;
import com.moomeen.endo2java.error.InvocationException;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FileResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Image;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
@VaadinView(name = "login")
@UIScope
public class LoginView extends LoginForm implements View {

	private static Logger LOG = LoggerFactory.getLogger(LoginView.class);
	
	@Autowired
	private EventBus eventBus;

	@Autowired
	private EndomondoSessionHolder sessionHolder;
	
	@Override
	public void enter(ViewChangeEvent event) {
	}

	@Override
	protected Component createContent(TextField userNameField, PasswordField passwordField, Button loginButton) {
		VerticalLayout layout = new VerticalLayout();
		
		layout.setSizeFull();

		Panel panel = new Panel();
		layout.addComponent(panel);
		panel.setStyleName("login-panel");
		layout.setComponentAlignment(panel, Alignment.TOP_CENTER);
		panel.setWidth("350px");

		VerticalLayout loginLayout = new VerticalLayout();
		panel.setContent(loginLayout);
		loginLayout.setSpacing(true);
		loginLayout.setStyleName("loginForm");
		loginLayout.setMargin(true);

		loginLayout.addComponent(userNameField);
		userNameField.setCaption("Email");
		userNameField.addStyleName("large");
		userNameField.setWidth("324px");
		userNameField.setHeight("50px");
		userNameField.addStyleName("inline-icon");
		userNameField.setIcon(FontAwesome.USER);

		loginLayout.addComponent(passwordField);
		passwordField.setCaption("Password");
		passwordField.addStyleName("inline-icon");
		passwordField.addStyleName("large");
		passwordField.setIcon(FontAwesome.LOCK);
		passwordField.setWidth("324px");
		passwordField.setHeight("50px");

		loginButton.setCaption("LOG IN");
		loginButton.setClickShortcut(KeyCode.ENTER);
		loginButton.setWidth("324px");
		loginButton.setHeight("50px");
		loginButton.setStyleName("login-button");
		loginLayout.addComponent(loginButton);

		Button dontKnow = new Button("Don't know password?");
		dontKnow.addStyleName("link");
		dontKnow.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				final Window window = new Window();
        		window.center();
        		
        		try {
        			Image image = new Image(
					        null, new FileResource(streamTwoFile(this.getClass().getClassLoader().getResourceAsStream("VAADIN/themes/mytheme/img/dontKnowPassword.png"))));
        			VerticalLayout layout = new VerticalLayout();
        			layout.addComponent(image);
        			layout.setComponentAlignment(image, Alignment.MIDDLE_CENTER);
        			
					window.setContent(layout);
				} catch (IOException e) {
					LOG.error("Something went wrong while trying to show password help image", e);
				}
        		UI.getCurrent().addWindow(window);
            	
			}
		});
		loginLayout.addComponent(dontKnow);
		loginLayout.setComponentAlignment(dontKnow, Alignment.BOTTOM_CENTER);
		
		return layout;
	}

	 private static File streamTwoFile (InputStream in) throws IOException {
	        final File tempFile = File.createTempFile("tmpFile", ".tmp");
	        tempFile.deleteOnExit();
	        try (FileOutputStream out = new FileOutputStream(tempFile)) {
	            IOUtils.copy(in, out);
	        }
	        return tempFile;
	    }
	
	@Override
	protected void login(String userName, String password) {
		EndomondoSession session = new EndomondoSession(userName, password);
		try {
			session.login();
			LOG.debug("Logged in: {}", userName);
			sessionHolder.init(session);
			eventBus.publish(this, com.moomeen.ViewChangeEvent.STATS_VIEW);
		} catch (InvocationException e) {
			LOG.error("exception while trying to login user: {}", userName, e);
		}
		
	}

}