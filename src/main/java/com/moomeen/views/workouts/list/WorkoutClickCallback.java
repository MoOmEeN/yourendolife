package com.moomeen.views.workouts.list;

import com.moomeen.endo2java.error.InvocationException;

public interface WorkoutClickCallback {

	void clicked(long workoutId) throws InvocationException;

}
