package com.moomeen.views.workouts.details.timelapse;

import com.moomeen.endo2java.model.DetailedWorkout;
import com.vaadin.annotations.JavaScript;
import com.vaadin.ui.AbstractJavaScriptComponent;

@JavaScript({ "https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js",
			"https://cdnjs.cloudflare.com/ajax/libs/underscore.js/1.6.0/underscore-min.js",
			"https://cdnjs.cloudflare.com/ajax/libs/gsap/latest/TweenLite.min.js", "js_time_lapse.js",
			"StreetviewSequence.js" })
public class JsTimeLapse extends AbstractJavaScriptComponent {

	private static final long serialVersionUID = 7082932209865633859L;

	public JsTimeLapse(DetailedWorkout workout) {
		setSizeFull();
		double[][] points = new double[workout.getPoints().size()][2];

		for (int i = 0; i < points.length; i ++){
			double[] coordinates = new double[2];
			coordinates[0] = workout.getPoints().get(i).getLatitude();
			coordinates[1] = workout.getPoints().get(i).getLongitude();

			points[i] = coordinates;
		}

		getState().points = points;
	}

	@Override
	protected JsTimeLapseState getState() {
		return (JsTimeLapseState) super.getState();
	}
}
