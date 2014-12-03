package com.moomeen.views;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.spring.UIScope;
import org.vaadin.spring.navigator.VaadinView;

import com.moomeen.views.places.MapStripe;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

@VaadinView(name = "places")
@UIScope
public class PlacesView extends AbstractContentView {

	private static final long serialVersionUID = -5282768909040896555L;

	private static Logger LOG = LoggerFactory.getLogger(PlacesView.class);

	@Override
	public LazyLoadable content() {

		return new LazyLoadableContent() {

			@Override
			public Component content() {
				VerticalLayout layout = new VerticalLayout();
				layout.setStyleName("places-view");
				MapStripe mapStripe = new MapStripe();
				mapStripe.setStyleName("places-view-map");
				layout.addComponent(mapStripe);
				return layout;
			}
		};
	}



	@Override
	public void enter(ViewChangeEvent event) {

	}

}