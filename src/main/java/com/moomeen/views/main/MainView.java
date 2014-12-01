package com.moomeen.views.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.UIScope;
import org.vaadin.spring.navigator.VaadinView;

import com.moomeen.location.LocationService;
import com.moomeen.views.AbstractContentView;
import com.vaadin.lazyloadwrapper.LazyLoadWrapper;
import com.vaadin.lazyloadwrapper.widgetset.client.ui.LLWRpc;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

@VaadinView(name = "main")
@UIScope
public class MainView extends AbstractContentView {

	/**
	 *
	 */
	private static final long serialVersionUID = -5282768909040896555L;

	private static Logger LOG = LoggerFactory.getLogger(MainView.class);

	@Autowired
	private LocationService locationService;

	@Override
	public Component content() {
		VerticalLayout layout = new VerticalLayout();
		addLazyLoadedContent(layout, new WorkoutsStripe(sessionHolder), "workouts-stripe-loader");
		addLazyLoadedContent(layout, new PlacesStripe(sessionHolder, locationService), "places-stripe-loader");

		return layout;
	}

	@Override
	public void enter(ViewChangeEvent event) {

	}
	
	private void addLazyLoadedContent(AbstractOrderedLayout layout, final LazyLoadable content, String styleName){
		layout.addComponent(new FixedLazyLoadWrapper(
				new LazyLoadWrapper.LazyLoadComponentProvider() {
					private static final long serialVersionUID = -2518774996039022517L;

					@Override
					public Component onComponentVisible() {
						content.init();
						return (Component) content;
					}
				}, "100%", styleName));
	}

	class FixedLazyLoadWrapper extends LazyLoadWrapper {
	    private LLWRpc serverRpc = new LLWRpc() {

	        @Override
	        public void onWidgetVisible() {
	            setClientSideIsVisible(true);
	        }
	    };

		public FixedLazyLoadWrapper(LazyLoadComponentProvider childProvider, String width, String styleName) {
			super(childProvider);
			registerRpc(serverRpc);
			setStyleName(styleName);
			setWidth(width);
		}
	}

}