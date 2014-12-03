package com.moomeen.endo;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.vaadin.spring.UIScope;

import com.moomeen.endo2java.error.InvocationException;
import com.moomeen.endo2java.model.AccountInfo;
import com.moomeen.endo2java.model.DetailedWorkout;
import com.moomeen.endo2java.model.Workout;

@Service
@UIScope
public class EndomondoSessionHolder {

	private final static Logger LOG = LoggerFactory.getLogger(EndomondoSessionHolder.class);

	private com.moomeen.endo2java.EndomondoSession internalSession;

	private List<Workout> workouts;
	
	private AccountInfo accountInfo;

	public void init(com.moomeen.endo2java.EndomondoSession session) {
		this.internalSession = session;
	}

	public synchronized List<Workout> getWorkouts() {
		checkInit();
		if (workouts == null){
			long start = System.currentTimeMillis();
			try {
				workouts = internalSession.getAllWorkouts();
			} catch (InvocationException e) {
				LOG.error("Error while trying to get workouts", e);
				throw new RuntimeException(e);
			}
			if (LOG.isDebugEnabled()){
				LOG.debug("Retrieved {} workouts in {} ms", workouts.size(), System.currentTimeMillis() - start);
			}
		}
		return workouts;
	}

	public DetailedWorkout getWorkout(long workoutId) {
		checkInit();
		try {
			return internalSession.getWorkout(workoutId);
		} catch (InvocationException e) {
			LOG.error("Error while trying to get workout", e);
			throw new RuntimeException(e);
		}
	}
	
	public synchronized AccountInfo getAccountInfo() {
		checkInit();
		if (accountInfo == null){
			try {
				this.accountInfo = internalSession.getAccountInfo();
			} catch (InvocationException e) {
				LOG.error("Error while trying to get account info", e);
				throw new RuntimeException(e);
			}
		}
		return accountInfo;
	}

	private void checkInit(){
		if (internalSession == null){
			throw new RuntimeException("init first!");
		}
	}

}
