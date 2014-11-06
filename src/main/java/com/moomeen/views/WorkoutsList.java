package com.moomeen.views;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import com.moomeen.endo2java.model.Sport;
import com.moomeen.endo2java.model.Workout;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;


public class WorkoutsList extends VerticalLayout {

	private List<Workout> workouts;

	public WorkoutsList(List<Workout> workouts) {
		this.workouts = workouts;

		Table table = new Table();
		 table.setSelectable(true);
		 table.setMultiSelect(true);
		 table.setSortEnabled(true);
		 table.setColumnCollapsingAllowed(true);
		 table.setColumnReorderingAllowed(true);
		table.setContainerDataSource(convert(workouts));
		table.setWidth("100%");
		addComponent(table);
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
