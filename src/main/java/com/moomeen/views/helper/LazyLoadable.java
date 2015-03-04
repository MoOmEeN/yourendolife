package com.moomeen.views.helper;

import com.vaadin.ui.Component;

public interface LazyLoadable extends Component {
	
	void init();
	
	boolean isInited();
	
}
