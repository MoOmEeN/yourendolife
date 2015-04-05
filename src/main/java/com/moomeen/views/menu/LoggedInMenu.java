package com.moomeen.views.menu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.moomeen.endo.EndomondoSessionHolder;
import com.moomeen.endo2java.model.AccountInfo;
import com.moomeen.utils.SpringContextHolder;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Image;

@SuppressWarnings("serial")
public class LoggedInMenu extends Menu {

	private static Logger LOG = LoggerFactory.getLogger(LoggedInMenu.class);

	private EndomondoSessionHolder session;

	public LoggedInMenu() {
		super();
		this.session = SpringContextHolder.lookupBean(EndomondoSessionHolder.class);

		addMenuItem("Statistics", com.moomeen.ViewChangeEvent.STATS_VIEW);
		addMenuItem("Places", com.moomeen.ViewChangeEvent.PLACES_VIEW);
		addMenuItem("Log Out", new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				getSession().close();
				getUI().getPage().setLocation("/");
			}
			
		}, MenuItemFloat.RIGHT, "main-item-border");
		
		AccountInfo accountInfo = session.getAccountInfo();
		Image avatarImage = new Image(null, new ExternalResource(getPictureUrl(accountInfo)));
		avatarImage.setStyleName("avatar-img");
		addComponent(avatarImage);
	}

	private String getPictureUrl(AccountInfo info){
			Long pcitureId = info.getPictureId();
			return String.format("https://www.endomondo.com/resources/gfx/picture/%d/thumbnail.jpg", pcitureId);
	}

}
