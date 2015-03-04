package com.moomeen.views;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.moomeen.utils.login.EndoLoginForm;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class LoginStripe extends EndoLoginForm {

	private static Logger LOG = LoggerFactory.getLogger(LoginView.class);
	private static final String ERROR_MSG = "Please make sure your email and password are correct.";
	
	private Label errorLabel = new Label();
	
	@Override
	protected void showLoginError() {
		errorLabel.setVisible(true);
		errorLabel.setValue(ERROR_MSG);
	}

	@Override
	protected Component createContent(TextField userNameField, PasswordField passwordField, Button loginButton) {
		errorLabel.setVisible(false);
		errorLabel.setStyleName("login-error");
		
		Label bigHeader = new Label("Bring more value to your Endomondo workouts.");
		bigHeader.setStyleName("big");
		Label smallHeader = new Label("Geolocation, street view, custom best distances, export to Excel and more!");
		smallHeader.setStyleName("small");
		
		VerticalLayout layout = new VerticalLayout();
		layout.setSpacing(true);
		layout.setStyleName("main-view-login");
		
		layout.addComponent(bigHeader);
		layout.addComponent(smallHeader);
		
		HorizontalLayout loginLayout = new HorizontalLayout();
		loginLayout.setStyleName("login-controls");
		userNameField.setCaption(null);
		passwordField.setCaption(null);
		userNameField.setInputPrompt("EMail");
		passwordField.setInputPrompt("Password");
		userNameField.setHeight("100%");
		passwordField.setHeight("100%");
		loginLayout.addComponent(userNameField);
		loginLayout.addComponent(passwordField);
		loginButton.setCaption("LOG IN WITH ENDOMONDO");
		loginButton.setHeight("100%");
		loginButton.setStyleName("login-button");
		loginLayout.addComponent(loginButton);
		layout.addComponent(errorLabel);
		
		layout.addComponent(loginLayout);
		return layout;
	}
	
}
