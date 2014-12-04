package com.moomeen.views.workouts.list;

import com.moomeen.endo2java.error.InvocationException;

public interface ItemClickCallback {

	void clicked(long workoutId) throws InvocationException;

}
