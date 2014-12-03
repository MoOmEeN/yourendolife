package com.moomeen.views;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus;

import com.moomeen.endo.EndomondoSessionHolder;
import com.vaadin.lazyloadwrapper.LazyLoadWrapper;
import com.vaadin.lazyloadwrapper.widgetset.client.ui.LLWRpc;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

public abstract class AbstractContentView extends VerticalLayout implements View {

	private static final long serialVersionUID = 9102807959577832012L;
	
	@Autowired
	protected EventBus eventBus;
	
	@Autowired
	protected EndomondoSessionHolder sessionHolder;

	@PostConstruct
	public void init(){
		setSizeFull();
		setHeightUndefined();
		addComponent(new Menu(eventBus, sessionHolder));
		
		final LazyLoadable content = content();
		
		addComponent(new FixedLazyLoadWrapper(
				new LazyLoadWrapper.LazyLoadComponentProvider() {
					private static final long serialVersionUID = -2518774996039022517L;

					@Override
					public Component onComponentVisible() {
						if (!content.isInited()){
							content.init();
						}
						return content;
					}
				}));
		
		addComponent(new Footer());
	}

	public abstract LazyLoadable content();

	@Override
	public void enter(ViewChangeEvent event) {
	}

	private class FixedLazyLoadWrapper extends LazyLoadWrapper {
		
		private static final long serialVersionUID = 469079528082124329L;
		private LLWRpc serverRpc = new LLWRpc() {
	    	
			private static final long serialVersionUID = 5055880416669815652L;

			@Override
	        public void onWidgetVisible() {
	            setClientSideIsVisible(true);
	        }
	    };

		public FixedLazyLoadWrapper(LazyLoadComponentProvider childProvider) {
			super(childProvider);
			registerRpc(serverRpc);
//			setStyleName("content");
		}
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
