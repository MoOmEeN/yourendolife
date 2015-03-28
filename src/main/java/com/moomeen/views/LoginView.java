package com.moomeen.views;

import static com.moomeen.utils.ImageResourceLoader.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.spring.UIScope;
import org.vaadin.spring.navigator.VaadinView;

import com.moomeen.utils.login.EndoLoginForm;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
@VaadinView(name = "login")
@UIScope
public class LoginView extends EndoLoginForm implements View {

	private static Logger LOG = LoggerFactory.getLogger(LoginView.class);
	private static final String ERROR_MSG = "Login failed. Please make sure your email and password are correct.";

	private Label errorLabel = new Label();

	@Override
	public void enter(ViewChangeEvent event) {
	}

	@Override
	protected Component createContent(TextField userNameField, PasswordField passwordField, Button loginButton) {
		VerticalLayout layout = new VerticalLayout();

		layout.setSizeFull();
//
//		Image logo = fromResourceImage("logo.jpg");
//		logo.setWidth("200px");
//		layout.addComponent(logo);
//		layout.setComponentAlignment(logo, Alignment.TOP_CENTER);

		errorLabel.setVisible(false);
		errorLabel.setStyleName("login-error");
		layout.addComponent(errorLabel);
		layout.setComponentAlignment(errorLabel, Alignment.MIDDLE_CENTER);

		Panel panel = new Panel();
		layout.addComponent(panel);
		panel.setStyleName("login-panel");
		layout.setComponentAlignment(panel, Alignment.BOTTOM_CENTER);
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

		loginButton.setCaption("LOG IN WITH ENDOMONDO");
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

				Image image = fromResourceImage("dontKnowPassword.png");
				VerticalLayout layout = new VerticalLayout();
				layout.addComponent(image);
				layout.setComponentAlignment(image, Alignment.MIDDLE_CENTER);

				window.setContent(layout);
				UI.getCurrent().addWindow(window);

			}
		});
		loginLayout.addComponent(dontKnow);
		loginLayout.setComponentAlignment(dontKnow, Alignment.BOTTOM_CENTER);

		return layout;
	}

	@Override
	protected void showLoginError(){
		errorLabel.setVisible(true);
		errorLabel.setValue(ERROR_MSG);
	}

}