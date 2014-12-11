package com.moomeen.views;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus;

import com.moomeen.utils.FixedLazyLoadWrapper;
import com.vaadin.lazyloadwrapper.LazyLoadWrapper;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

public abstract class AbstractContentView extends VerticalLayout implements View {

	private static final long serialVersionUID = 9102807959577832012L;

	@Autowired
	protected EventBus eventBus;

	@PostConstruct
	public void init(){
		setSizeFull();
		setHeightUndefined();
		addComponent(new Menu(eventBus));

		final VerticalLayout contentLayout = new VerticalLayout();
		final LazyLoadable content = content();
		contentLayout.addComponent(content);
		contentLayout.addComponent(new Footer());

		addComponent(new FixedLazyLoadWrapper(
				new LazyLoadWrapper.LazyLoadComponentProvider() {
					private static final long serialVersionUID = -2518774996039022517L;

					@Override
					public Component onComponentVisible() {
						if (!content.isInited()){
							content.init();
						}

						return contentLayout;
					}
				}));


	}

	public abstract LazyLoadable content();

	@Override
	public void enter(ViewChangeEvent event) {
	}



	protected abstract class LazyLoadableContent extends VerticalLayout implements LazyLoadable {
		private static final long serialVersionUID = 6782422350111874382L;

		private boolean inited = false;

		@Override
		public void init() {
			addComponent(content());
			inited = true;
		}

		public abstract Component content();

		@Override
		public boolean isInited() {
			return inited;
		}

	}

}
