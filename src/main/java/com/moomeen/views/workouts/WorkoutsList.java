package com.moomeen.views.workouts;

import org.vaadin.hene.popupbutton.PopupButton;

import com.moomeen.EndomondoSessionHolder;
import com.moomeen.endo2java.error.InvocationException;
import com.moomeen.views.workouts.list.WorkoutsTable;
import com.moomeen.views.workouts.list.WorkoutsTableControls;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;


public class WorkoutsList extends VerticalLayout {

	/**
	 *
	 */
	private static final long serialVersionUID = 5943429368424740640L;
	
	private Integer selectedItem;
	private PopupButton viewButton;
	
	public WorkoutsList(final EndomondoSessionHolder sessionHolder) throws InvocationException {
		HorizontalLayout listControls = new HorizontalLayout();
		viewButton = new PopupButton("View");
		viewButton.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				 final Window window = new Window("Window");
				 window.setWidth(300.0f, Unit.PIXELS);
				 try {
					window.setContent(new WorkoutDetails(sessionHolder.getWorkout(sessionHolder.getWorkouts().get(selectedItem).getId())));
				} catch (InvocationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 
				 UI.getCurrent().addWindow(window);
			}
		});
		viewButton.addStyleName("borderless-colored");
		viewButton.setContent(new Label(""));
		viewButton.setEnabled(false);
		listControls.addComponent(viewButton);
		
		Button exportButton = new Button("Export");
		exportButton.addStyleName("borderless-colored");
		listControls.addComponent(exportButton);
		exportButton.setEnabled(false);

		WorkoutsTable table = new WorkoutsTable(sessionHolder.getWorkouts());
		table.addValueChangeListener(new TableSelectionListener());
		WorkoutsTableControls tableControls = new WorkoutsTableControls(table);

		addComponent(listControls);
		addComponent(table);
		addComponent(tableControls);
		setComponentAlignment(listControls, Alignment.TOP_LEFT);
		setComponentAlignment(table, Alignment.MIDDLE_CENTER);
		setComponentAlignment(tableControls, Alignment.BOTTOM_CENTER);
	}
	
	private class TableSelectionListener implements ValueChangeListener {

		/**
		 * 
		 */
		private static final long serialVersionUID = -513576465910944568L;

		@Override
		public void valueChange(ValueChangeEvent event) {
			boolean valueEmpty = event.getProperty().getValue() == null;
			if (!valueEmpty){
				viewButton.setEnabled(true);
				setSelectedItem(event);
			} else {
				viewButton.setEnabled(false);
			}
		}

		private void setSelectedItem(ValueChangeEvent event) {
			selectedItem = (Integer) event.getProperty().getValue();
		}
		
	}
}
