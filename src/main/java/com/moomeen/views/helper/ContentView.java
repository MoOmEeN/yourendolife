package com.moomeen.views.helper;

import javax.annotation.PostConstruct;

import com.moomeen.utils.FixedLazyLoadWrapper;
import com.moomeen.views.Footer;
import com.vaadin.lazyloadwrapper.LazyLoadWrapper;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public abstract class ContentView  extends VerticalLayout implements View {

	@PostConstruct
	public void init(){
		setSizeFull();
		setHeightUndefined();
		
		addMenuIfDefined();
		addLazyLoadableContent();
	}

	private void addMenuIfDefined() {
		Component menu = menu();
		menu.addStyleName(menuStyleName());
		if (menu != null){
			addComponent(menu);
		}
	}

	private void addLazyLoadableContent() {
		final VerticalLayout contentLayout = new VerticalLayout();
		final LazyLoadable content = content();
		contentLayout.addComponent(content);
		contentLayout.addComponent(new Footer());

		addComponent(wrapWithLazyLoad(contentLayout, content));
	}

	private FixedLazyLoadWrapper wrapWithLazyLoad(final VerticalLayout contentLayout, final LazyLoadable content) {
		return new FixedLazyLoadWrapper(
				new LazyLoadWrapper.LazyLoadComponentProvider() {

					@Override
					public Component onComponentVisible() {
						if (!content.isInited()){
							content.init();
						}

						return contentLayout;
					}
				});
	}

	protected abstract LazyLoadable content();
	
	protected abstract Component menu();
	
	protected abstract String menuStyleName();

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
