package com.moomeen.utils;

import com.vaadin.lazyloadwrapper.LazyLoadWrapper;
import com.vaadin.lazyloadwrapper.widgetset.client.ui.LLWRpc;

public class FixedLazyLoadWrapper extends LazyLoadWrapper {

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
//		setStyleName("content");
	}
}
