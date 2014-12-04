package com.moomeen.views.workouts.list;

import static com.moomeen.views.workouts.list.TableColumnEnum.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.PeriodFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jensjansson.pagedtable.PagedTable;
import com.moomeen.endo2java.error.InvocationException;
import com.moomeen.endo2java.model.Sport;
import com.moomeen.endo2java.model.Workout;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Table;

public class WorkoutsTable extends PagedTable {

	/**
	 *
	 */
	private static final long serialVersionUID = 4523530118134513505L;

	private static Logger LOG = LoggerFactory.getLogger(WorkoutsTable.class);

	private Locale browserLocale = Page.getCurrent().getWebBrowser().getLocale();
	private String dateFormat = DateTimeFormat.patternForStyle("MM", browserLocale);
	private PeriodFormatter periodFormatter = PeriodFormatterHelper.getForLocale(browserLocale);

	private List<Workout> workouts;
	private ItemClickCallback workoutClickCallback;

	public WorkoutsTable(List<Workout> workouts, ItemClickCallback clickCallback) {
		this(clickCallback);
		setWorkouts(workouts);
	}
	
	public WorkoutsTable(ItemClickCallback clickCallback) {
		initTable();
		setStyleName("v-table-empty");
		this.workoutClickCallback = clickCallback;
	}

	@SuppressWarnings("unchecked")
	private void fillWithData() {
		for (Workout workout : workouts) {
			Object item = addItem();
			Item i = getItem(item);
			i.getItemProperty(DURATION).setValue(workout.getDuration());
			i.getItemProperty(DISTANCE).setValue(workout.getDistance());
			i.getItemProperty(SPORT).setValue(workout.getSport());
			i.getItemProperty(START_DATE).setValue(workout.getStartTime());
			i.getItemProperty(CALORIES).setValue(workout.getCalories());
			i.getItemProperty(BURGERS).setValue(workout.getBurgersBurned());
			i.getItemProperty(DESCENT).setValue(workout.getDescent());
			i.getItemProperty(ASCENT).setValue(workout.getAscent());
			i.getItemProperty(AVG_SPEED).setValue(workout.getSpeedAvg());
			i.getItemProperty(MAX_SPEED).setValue(workout.getSpeedMax());
			i.getItemProperty(MIN_ALTITUDE).setValue(workout.getAltitudeMin());
			i.getItemProperty(MAX_ALTITUDE).setValue(workout.getAltitudeMax());
			i.getItemProperty(PEPTALKS).setValue(workout.getPeptalks());
			i.getItemProperty(LIKES).setValue(workout.getLikes());
			i.getItemProperty(COMMENTS).setValue(workout.getComments());
			i.getItemProperty(ID).setValue(workout.getId());
		}
	}
	
	public void setWorkouts(List<Workout> workouts){
		this.workouts = workouts;
		removeAllItems();
		fillWithData();
		refreshRowCache();
		removeStyleName("v-table-empty");
	}

	private void initTable() {
		setSelectable(false);
		setMultiSelect(false);
		setSortEnabled(true);
		setColumnCollapsingAllowed(true);
		setColumnReorderingAllowed(true);
		setWidth("100%");

		setColumns();
		setHeaders();
		colapseColumns(ID, BURGERS, DESCENT, ASCENT, MAX_SPEED, MIN_ALTITUDE, MAX_ALTITUDE, PEPTALKS, LIKES, COMMENTS);
	}

	private void setColumns() {
		addContainerProperty(VIEW, Button.class, null);
		addGeneratedColumn(VIEW, new ColumnGenerator() {
			@Override
			public Object generateCell(final Table source, final Object itemId, Object columnId) {
				 Button b = new Button();
				 b.setIcon(FontAwesome.SEARCH);
				 b.addClickListener(new ClickListener() {

					@Override
					public void buttonClick(ClickEvent event) {
						try {
							Long id = (Long) source.getItem(itemId).getItemProperty(ID).getValue();
							workoutClickCallback.clicked(id);
						} catch (InvocationException e) {
							LOG.error("Couldn't process click event", e);
						}
					}
				});
				 return b;
			}
		});
		setColumnCollapsible(VIEW, false);

		addContainerProperty(SPORT, Sport.class, null);
		addContainerProperty(START_DATE, DateTime.class, null);
		addContainerProperty(DURATION, Duration.class, null);
		addContainerProperty(DISTANCE, Double.class, 0.0);
		addContainerProperty(AVG_SPEED, Double.class, 0.0);
		addContainerProperty(MAX_SPEED, Double.class, 0.0);
		addContainerProperty(CALORIES, Double.class, 0.0);
		addContainerProperty(BURGERS, Double.class, 0.0);
		addContainerProperty(DESCENT, Integer.class, 0.0);
		addContainerProperty(ASCENT, Integer.class, 0.0);
		addContainerProperty(MIN_ALTITUDE, Double.class, 0.0);
		addContainerProperty(MAX_ALTITUDE, Double.class, 0.0);
		addContainerProperty(PEPTALKS, Integer.class, 0);
		addContainerProperty(LIKES, Integer.class, 0);
		addContainerProperty(COMMENTS, Integer.class, 0);
		addContainerProperty(ID, Long.class, null);
	}

	private void colapseColumns(TableColumnEnum... columns){
		for (TableColumnEnum tableColumn : columns) {
			setColumnCollapsed(tableColumn, true);
		}
	}

	private void setHeaders() {
		for (Object column : getContainerPropertyIds()) {
			TableColumnEnum col = (TableColumnEnum) column;
			setColumnHeader(col, col.description());
		}
	}

	@Override
	protected String formatPropertyValue(Object rowId, Object colId, Property<?> property) {
		TableColumnEnum column = (TableColumnEnum) colId;
		Object propertyId = property.getValue();
		if (propertyId == null){
			return defaultPropertyValue(property);
		}
		switch (column) {
			case DURATION:
				return periodFormatter.print(((Duration) propertyId).toPeriod());
			case SPORT:
				return ((Sport) propertyId).description();
			case START_DATE:
				return ((DateTime) propertyId).toString(dateFormat);
			default:
				return defaultPropertyValue(property);
		}
	}

	private String defaultPropertyValue(Property<?> property){
		if (property.getValue() instanceof Double){
			return formatDouble((Double) property.getValue());
		}
		return property.getValue() == null ? "" : property.getValue().toString();
	}

	private String formatDouble(double d){
		BigDecimal bd = BigDecimal.valueOf(d).stripTrailingZeros();
		if (bd.scale() > 2){
			bd = bd.setScale(2, RoundingMode.HALF_UP);
		}
		return bd.toString();
	}

}
