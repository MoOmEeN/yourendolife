package com.moomeen.views;

import com.moomeen.endo2java.EndomondoSession;
import com.moomeen.endo2java.error.InvocationException;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Link;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class LoginView extends VerticalLayout implements View {

	public LoginView() {
		setSizeFull();

		Panel panel = new Panel();
		addComponent(panel);
		panel.setStyleName("login-panel");
		setComponentAlignment(panel, Alignment.TOP_CENTER);
		panel.setWidth("350px");

		VerticalLayout loginLayout = new VerticalLayout();
		panel.setContent(loginLayout);
		loginLayout.setSpacing(true);
		loginLayout.setStyleName("loginForm");
		loginLayout.setMargin(true);

		final TextField login = new TextField();
		loginLayout.addComponent(login);
		login.setInputPrompt("Email");
		login.addStyleName("large");
		login.setWidth("324px");
		login.setHeight("50px");
		login.addStyleName("inline-icon");
		login.setIcon(FontAwesome.USER);

		final PasswordField password = new PasswordField();
		loginLayout.addComponent(password);
		password.addStyleName("inline-icon");
		password.addStyleName("large");
		password.setInputPrompt("Password");
		password.setIcon(FontAwesome.LOCK);
		password.setWidth("324px");
		password.setHeight("50px");

		Button button = new Button("LOG IN");
		button.setWidth("324px");
		button.setHeight("50px");
		button.setStyleName("login-button");
		loginLayout.addComponent(button);
		button.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				EndomondoSession session = new EndomondoSession(login
						.getValue(), password.getValue());
				try {
					session.login();
					System.out.println(session.getWorkouts().size());
				} catch (InvocationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		Link dontKnow = new Link();
		loginLayout.addComponent(dontKnow);
		loginLayout.setComponentAlignment(dontKnow, Alignment.BOTTOM_CENTER);
		dontKnow.setCaption("Don't know password?");
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}

}
