package com.moomeen.views;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.UIScope;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.navigator.VaadinView;

import com.moomeen.EndomondoSessionHolder;
import com.moomeen.endo2java.error.InvocationException;
import com.moomeen.endo2java.model.Sport;
import com.moomeen.endo2java.model.Workout;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;

@VaadinView(name = "workouts")
@UIScope
public class WorkoutsView extends AbstractContentView {

	@Autowired
	private EventBus eventBus;

	@Autowired
	private EndomondoSessionHolder sessionHolder;

	@Override
	public Component content() {
		TabSheet ts = new TabSheet();
		ts.addStyleName("framed");
		Table table = new Table();
		 table.setSelectable(true);
		 table.setMultiSelect(true);
		 table.setSortEnabled(true);
		 table.setColumnCollapsingAllowed(true);
		 table.setColumnReorderingAllowed(true);
		try {
			List<Workout> workouts = sessionHolder.getSession().getWorkouts();
			table.setContainerDataSource(convert(workouts));
		} catch (InvocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		table.setWidth("100%");
		ts.setWidth("90%");
		ts.addTab(table, "List");
		return ts;
	}

	@Override
	public void enter(ViewChangeEvent event) {

	}

	private Container convert(List<Workout> workouts){
		Container c = new IndexedContainer();
		c.addContainerProperty("duration", Duration.class, 0.0);
		c.addContainerProperty("distance", String.class, 0.0);
		c.addContainerProperty("burgersBurned", Double.class, 0.0);
		c.addContainerProperty("sport", Sport.class, 0.0);
		c.addContainerProperty("startDate", DateTime.class, 0.0);
		c.addContainerProperty("calories", Double.class, 0.0);
		c.addContainerProperty("live", Boolean.class, 0.0);

		for (Workout workout : workouts) {
			Object itemId = c.addItem();
			Item item = c.getItem(itemId);
			item.getItemProperty("duration").setValue(workout.getDuration());
			item.getItemProperty("distance").setValue(workout.getDistance());
			item.getItemProperty("burgersBurned").setValue(workout.getBurgersBurned());
			item.getItemProperty("sport").setValue(workout.getSport());
			item.getItemProperty("startDate").setValue(workout.getStartDate());
			item.getItemProperty("calories").setValue(workout.getCalories());
			item.getItemProperty("live").setValue(workout.getLive());
		}
		return c;
	}


}
