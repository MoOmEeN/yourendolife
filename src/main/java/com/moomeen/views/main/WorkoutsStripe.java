package com.moomeen.views.main;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.moomeen.endo.EndomondoSessionHolder;
import com.moomeen.endo2java.error.InvocationException;
import com.moomeen.endo2java.model.Workout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

public class WorkoutsStripe extends VerticalLayout implements LazyLoadable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3978592040065693945L;

	private static Logger LOG = LoggerFactory.getLogger(WorkoutsStripe.class);

	private EndomondoSessionHolder sessionHolder;
	
	public WorkoutsStripe(EndomondoSessionHolder session) {
		this.sessionHolder = session;
	}

	@Override
	public void init() {
		try {
			List<Workout> workouts = sessionHolder.getWorkouts();
			
			Panel textPanel = new Panel();
			textPanel.setContent(getText(workouts));
			
			addComponent(textPanel);
			setComponentAlignment(textPanel, Alignment.MIDDLE_CENTER);
			
			WorkoutsListView workoutsPanel = new WorkoutsListView(workouts, sessionHolder);

			addComponent(workoutsPanel);
			setComponentAlignment(workoutsPanel, Alignment.BOTTOM_CENTER);
			
		} catch (InvocationException e) {
			LOG.error("Error during workouts retrieving", e);
		}
	}

	private Label getText(List<Workout> workouts) {
		// TODO Auto-generated method stub
		return null;
	}

}
