package com.moomeen.utils.login;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.spring.events.EventBus;

import com.ejt.vaadin.loginform.LoginForm;
import com.moomeen.endo.EndomondoSessionHolder;
import com.moomeen.endo2java.EndomondoSession;
import com.moomeen.endo2java.error.InvocationException;
import com.moomeen.utils.SpringContextHolder;

@SuppressWarnings("serial")
public abstract class EndoLoginForm extends LoginForm {
	
	private static Logger LOG = LoggerFactory.getLogger(EndoLoginForm.class);
	
	private EventBus eventBus;
	
	private EndomondoSessionHolder sessionHolder;
	
	public EndoLoginForm() {
		this.eventBus = SpringContextHolder.lookupBean(EventBus.class);
		this.sessionHolder = SpringContextHolder.lookupBean(EndomondoSessionHolder.class);
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
			showLoginError();
			LOG.error("exception while trying to login user: {}", userName, e);
		}
	}
	
	protected abstract void showLoginError();

}
