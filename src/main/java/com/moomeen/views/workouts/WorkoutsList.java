package com.moomeen.views.workouts;

import java.util.List;

import org.joda.time.DateTime;

import com.jensjansson.pagedtable.PagedTable;
import com.jensjansson.pagedtable.PagedTable.PageChangeListener;
import com.jensjansson.pagedtable.PagedTable.PagedTableChangeEvent;
import com.moomeen.endo2java.model.Sport;
import com.moomeen.endo2java.model.Workout;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;


public class WorkoutsList extends VerticalLayout {

	private List<Workout> workouts;

	public WorkoutsList(List<Workout> workouts) {
		this.workouts = workouts;
		PagedTable table = new PagedTable();

		 table.setSelectable(true);
		 table.setMultiSelect(true);
		 table.setSortEnabled(true);
		 table.setColumnCollapsingAllowed(true);
		 table.setColumnReorderingAllowed(true);
		table.setContainerDataSource(convert(workouts));
		table.setWidth("100%");

		 HorizontalLayout tableControls = createControls(table);
	    //tableControls.setWidth("1030px");
		 table.setPageLength(10);
		addComponent(table);
		addComponent(tableControls);
		setComponentAlignment(table, Alignment.MIDDLE_CENTER);
		setComponentAlignment(tableControls, Alignment.BOTTOM_CENTER);
		//addComponent(tableControls);
	}


	private Container convert(List<Workout> workouts){
		Container c = new IndexedContainer();
		c.addContainerProperty("duration", String.class, 0.0);
		c.addContainerProperty("distance", Double.class, 0.0);
		c.addContainerProperty("burgersBurned", Double.class, 0.0);
		c.addContainerProperty("sport", Sport.class, 0.0);
		c.addContainerProperty("startDate", DateTime.class, 0.0);
		c.addContainerProperty("calories", Double.class, 0.0);
		//c.addContainerProperty("live", Boolean.class, 0.0);

		for (Workout workout : workouts) {
			Object itemId = c.addItem();
			Item item = c.getItem(itemId);
			item.getItemProperty("duration").setValue(workout.getDuration().getStandardMinutes() + " min");
			item.getItemProperty("distance").setValue(workout.getDistance());
			item.getItemProperty("burgersBurned").setValue(workout.getBurgersBurned());
			item.getItemProperty("sport").setValue(workout.getSport());
			item.getItemProperty("startDate").setValue(workout.getStartDate());
			item.getItemProperty("calories").setValue(workout.getCalories());
			//item.getItemProperty("live").setValue(workout.getLive());
		}
		return c;
	}

    public HorizontalLayout createControls(final PagedTable table) {
        Label itemsPerPageLabel = new Label("Items per page:");
        final ComboBox itemsPerPageSelect = new ComboBox();

        itemsPerPageSelect.addItem("5");
        itemsPerPageSelect.addItem("10");
        itemsPerPageSelect.addItem("25");
        itemsPerPageSelect.addItem("50");
        //itemsPerPageSelect.addItem("100");
        //itemsPerPageSelect.addItem("600");
        itemsPerPageSelect.setImmediate(true);
        itemsPerPageSelect.setNullSelectionAllowed(false);
        itemsPerPageSelect.setWidth("70px");
        itemsPerPageSelect.addValueChangeListener(new ValueChangeListener() {
            private static final long serialVersionUID = -2255853716069800092L;

            @Override
			public void valueChange(
                    com.vaadin.data.Property.ValueChangeEvent event) {
            	table.setPageLength(Integer.valueOf(String.valueOf(event
                        .getProperty().getValue())));
            }
        });
        itemsPerPageSelect.select("25");
        Label pageLabel = new Label("Page:&nbsp;", ContentMode.HTML);
        final TextField currentPageTextField = new TextField();
        currentPageTextField.setValue(String.valueOf(table.getCurrentPage()));
        currentPageTextField.setConverter(Integer.class);
     //   currentPageTextField.addValidator(new IntegerRangeValidator("Wrong page number", 1, table.getTotalAmountOfPages()));
        Label separatorLabel = new Label("&nbsp;/&nbsp;", ContentMode.HTML);
        final Label totalPagesLabel = new Label(
                String.valueOf(table.getTotalAmountOfPages()), ContentMode.HTML);
        //currentPageTextField.setStyleName(Reindeer.TEXTFIELD_SMALL);
        currentPageTextField.setImmediate(true);
        currentPageTextField.addValueChangeListener(new ValueChangeListener() {
            private static final long serialVersionUID = -2255853716069800092L;

            @Override
			public void valueChange(
                    com.vaadin.data.Property.ValueChangeEvent event) {
                if (currentPageTextField.isValid()
                        && currentPageTextField.getValue() != null) {
                    int page = Integer.valueOf(String
                            .valueOf(currentPageTextField.getValue()));
                    table.setCurrentPage(page);
                }
            }
        });
        pageLabel.setWidth(null);
        currentPageTextField.setWidth("20px");
        separatorLabel.setWidth(null);
        totalPagesLabel.setWidth(null);

        HorizontalLayout controlBar = new HorizontalLayout();
        HorizontalLayout pageSize = new HorizontalLayout();
        HorizontalLayout pageManagement = new HorizontalLayout();
        final Button first = new Button("<<", new ClickListener() {
            private static final long serialVersionUID = -355520120491283992L;

            @Override
			public void buttonClick(ClickEvent event) {
            	table.setCurrentPage(0);
            }
        });
        final Button previous = new Button("<", new ClickListener() {
            private static final long serialVersionUID = -355520120491283992L;

            @Override
			public void buttonClick(ClickEvent event) {
            	table.previousPage();
            }
        });
        final Button next = new Button(">", new ClickListener() {
            private static final long serialVersionUID = -1927138212640638452L;

            @Override
			public void buttonClick(ClickEvent event) {
            	table.nextPage();
            }
        });
        final Button last = new Button(">>", new ClickListener() {
            private static final long serialVersionUID = -355520120491283992L;

            @Override
			public void buttonClick(ClickEvent event) {
                table.setCurrentPage(table.getTotalAmountOfPages());
            }
        });
        first.setStyleName(Reindeer.BUTTON_LINK);
        previous.setStyleName(Reindeer.BUTTON_LINK);
        next.setStyleName(Reindeer.BUTTON_LINK);
        last.setStyleName(Reindeer.BUTTON_LINK);

        itemsPerPageLabel.addStyleName("pagedtable-itemsperpagecaption");
        itemsPerPageSelect.addStyleName("pagedtable-itemsperpagecombobox");
        pageLabel.addStyleName("pagedtable-pagecaption");
        currentPageTextField.addStyleName("pagedtable-pagefield");
        separatorLabel.addStyleName("pagedtable-separator");
        totalPagesLabel.addStyleName("pagedtable-total");
        first.addStyleName("pagedtable-first");
        previous.addStyleName("pagedtable-previous");
        next.addStyleName("pagedtable-next");
        last.addStyleName("pagedtable-last");

        itemsPerPageLabel.addStyleName("pagedtable-label");
        itemsPerPageSelect.addStyleName("pagedtable-combobox");
        pageLabel.addStyleName("pagedtable-label");
        currentPageTextField.addStyleName("pagedtable-label");
        separatorLabel.addStyleName("pagedtable-label");
        totalPagesLabel.addStyleName("pagedtable-label");
        first.addStyleName("pagedtable-button");
        previous.addStyleName("pagedtable-button");
        next.addStyleName("pagedtable-button");
        last.addStyleName("pagedtable-button");

        pageSize.addComponent(itemsPerPageLabel);
        pageSize.addComponent(itemsPerPageSelect);
        pageSize.setComponentAlignment(itemsPerPageLabel, Alignment.MIDDLE_LEFT);
        pageSize.setComponentAlignment(itemsPerPageSelect,
                Alignment.MIDDLE_LEFT);
        pageSize.setSpacing(true);
        pageManagement.addComponent(first);
        pageManagement.addComponent(previous);
        pageManagement.addComponent(pageLabel);
        pageManagement.addComponent(currentPageTextField);
        pageManagement.addComponent(separatorLabel);
        pageManagement.addComponent(totalPagesLabel);
        pageManagement.addComponent(next);
        pageManagement.addComponent(last);
        pageManagement.setComponentAlignment(first, Alignment.MIDDLE_LEFT);
        pageManagement.setComponentAlignment(previous, Alignment.MIDDLE_LEFT);
        pageManagement.setComponentAlignment(pageLabel, Alignment.MIDDLE_LEFT);
        pageManagement.setComponentAlignment(currentPageTextField,
                Alignment.MIDDLE_LEFT);
        pageManagement.setComponentAlignment(separatorLabel,
                Alignment.MIDDLE_LEFT);
        pageManagement.setComponentAlignment(totalPagesLabel,
                Alignment.MIDDLE_LEFT);
        pageManagement.setComponentAlignment(next, Alignment.MIDDLE_LEFT);
        pageManagement.setComponentAlignment(last, Alignment.MIDDLE_LEFT);
        pageManagement.setWidth(null);
        pageManagement.setSpacing(true);
        controlBar.addComponent(pageSize);
        controlBar.addComponent(pageManagement);
        controlBar.setComponentAlignment(pageManagement,
                Alignment.MIDDLE_CENTER);
        controlBar.setWidth("100%");
        controlBar.setExpandRatio(pageSize, 1);
        table.addListener(new PageChangeListener() {
            @Override
			public void pageChanged(PagedTableChangeEvent event) {
                first.setEnabled(table.getCurrentPage() > 1);
                previous.setEnabled(table.getCurrentPage() > 1);
                next.setEnabled(table.getCurrentPage() < table.getTotalAmountOfPages());
                last.setEnabled(table.getCurrentPage() < table.getTotalAmountOfPages());
                currentPageTextField.setValue(String.valueOf(table.getCurrentPage()));
                if (table.getCurrentPage() >= 10){
                	currentPageTextField.setWidth("35px");
                } else if (table.getCurrentPage() >= 100) {
                	currentPageTextField.setWidth("45px");
                } else {
                	currentPageTextField.setWidth("25px");
                }
                totalPagesLabel.setValue(String.valueOf(table.getTotalAmountOfPages()));
                itemsPerPageSelect.setValue(String.valueOf(table.getPageLength()));
            }
        });
        return controlBar;
    }

}
