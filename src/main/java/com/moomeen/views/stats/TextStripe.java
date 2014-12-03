package com.moomeen.views.stats;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.moomeen.endo.EndomondoSessionHolder;
import com.moomeen.endo2java.model.Workout;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

public class TextStripe extends VerticalLayout {

	/**
	 *
	 */
	private static final long serialVersionUID = -3978592040065693945L;

	private static Logger LOG = LoggerFactory.getLogger(TextStripe.class);

	private EndomondoSessionHolder sessionHolder;
	
	public TextStripe(EndomondoSessionHolder session) {
		this.sessionHolder = session;
		init();
	}

	public void init() {
			List<Workout> workouts = sessionHolder.getWorkouts();

			Panel textPanel = new Panel();
			textPanel.setContent(getText(workouts));

			addComponent(textPanel);
			setComponentAlignment(textPanel, Alignment.MIDDLE_CENTER);

	}

	private Label getText(List<Workout> workouts) {
		String text = String
				.format("You've done <p class=\"big-font\">%d</p> workouts.<br>During <p class=\"big-font\">%s</p> of trainig, you went <p class=\"big-font\">%s</p> kilometres.<br> You burned <p class=\"big-font\">%s</p> calories, received <p class=\"big-font\">%d</p> likes and <p class=\"big-font\">%d</p> comments.",
						getNumberOfWorkouts(workouts), getTotalTime(workouts),
						getTotalDistance(workouts), getTotalCalories(workouts),
						getTotalLikes(workouts), getTotalComments(workouts));

		Label l = new Label(text, ContentMode.HTML);
//		l.setHeight("600px");
		l.setStyleName("h1");
		return l;
	}

	private int getNumberOfWorkouts(List<Workout> workouts){
		return workouts.size();
	}

	private String getTotalDistance(List<Workout> workouts){
		BigDecimal result = BigDecimal.ZERO;
		for (Workout workout : workouts) {
			if (workout.getDistance() != null){
				result = result.add(BigDecimal.valueOf(workout.getDistance()));
			}
		}
		return toString(result);
	}

	private String getTotalTime(List<Workout> workouts){
		Duration result = new Duration(0);
		for (Workout workout : workouts) {
			result = result.plus(workout.getDuration());
		}
		long millis = result.getMillis();

		return String.format("%d hours and %d minutes",  TimeUnit.MILLISECONDS.toHours(millis), TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.MINUTES.toMinutes(TimeUnit.MILLISECONDS.toHours(millis) * 60));
	}

	private String getTotalCalories(List<Workout> workouts){
		BigDecimal result = BigDecimal.ZERO;
		for (Workout workout : workouts) {
			if (workout.getCalories() != null){
				result = result.add(BigDecimal.valueOf(workout.getCalories()));
			}
		}
		return toString(result);
	}

	private int getTotalLikes(List<Workout> workouts){
		int result = 0;
		for (Workout workout : workouts) {
			result = result + workout.getLikes();
		}
		return result;
	}

	private int getTotalComments(List<Workout> workouts){
		int result = 0;
		for (Workout workout : workouts) {
			result = result + workout.getComments();
		}
		return result;
	}

	private String toString(BigDecimal number){
		return number.setScale(0, RoundingMode.HALF_UP).toPlainString();
	}

}
