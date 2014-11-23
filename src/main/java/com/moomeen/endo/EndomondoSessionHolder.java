package com.moomeen.endo;

import java.util.List;

import org.springframework.stereotype.Service;
import org.vaadin.spring.UIScope;

import com.moomeen.endo2java.error.InvocationException;
import com.moomeen.endo2java.model.DetailedWorkout;
import com.moomeen.endo2java.model.Workout;

@Service
@UIScope
public class EndomondoSessionHolder {

	private com.moomeen.endo2java.EndomondoSession internalSession;

	private List<Workout> workouts;

	public void init(com.moomeen.endo2java.EndomondoSession session) {
		this.internalSession = session;
	}

	public List<Workout> getWorkouts() throws InvocationException {
		checkInit();
		if (workouts == null){
			workouts = internalSession.getWorkouts(999);
		}
		return workouts;
	}

	public DetailedWorkout getWorkout(long workoutId) throws InvocationException {
		checkInit();
		return internalSession.getWorkout(workoutId);
	}

	private void checkInit(){
		if (internalSession == null){
			throw new RuntimeException("init first!");
		}
	}

}
