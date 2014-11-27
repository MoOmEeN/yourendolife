package com.moomeen.location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moomeen.endo2java.model.Workout;
import com.moomeen.location.model.Place;
import com.moomeen.location.model.Point;

@Service
public class LocationService {

	private static Logger LOG = LoggerFactory.getLogger(LocationService.class);

	@Autowired
	private ReverseGeoLocator executor;
	
	public Map<Place, List<Workout>> determineCities(List<Workout> workouts){
		Map<Point, Workout> coordinatesOfWorkouts = determineCoordinates(workouts);
		long millis = System.currentTimeMillis();
		Map<Point, Place> coordinatesPlaces = executor.reverse(coordinatesOfWorkouts.keySet());
		if (LOG.isDebugEnabled()){
			LOG.debug("Localized {} points in {} ms",coordinatesPlaces.size(), System.currentTimeMillis() - millis);
		}
		Map<Place, List<Workout>> workoutsCities = groupByCities(coordinatesOfWorkouts, coordinatesPlaces);
		return workoutsCities;
	}

	private Map<Place, List<Workout>> groupByCities(Map<Point, Workout> coordinates, Map<Point, Place> addresses) {
		Map<Place, List<Workout>> workoutsCities = new HashMap<Place, List<Workout>>();

		for (Entry<Point, Place> placeEntry : addresses.entrySet()) {
			Place city = placeEntry.getValue();
			Workout workout = coordinates.get(placeEntry.getKey());
			if (workoutsCities.containsKey(city)){
				workoutsCities.get(city).add(workout);
			} else {
				List<Workout> workoutOfCity = new ArrayList<Workout>();
				workoutOfCity.add(workout);
				workoutsCities.put(city, workoutOfCity);
			}
		}
		return workoutsCities;
	}

	private Map<Point, Workout> determineCoordinates(List<Workout> workouts) {
		Map<Point, Workout> coordinates = new HashMap<Point, Workout>();
		for (Workout workout : workouts) {
			if (containsPolyline(workout)){
				Point workoutInitialPoint = getPointFromPolyline(workout);
				if (workoutInitialPoint != null){
					coordinates.put(workoutInitialPoint, workout);
				}
			}
		}
		return coordinates;
	}

	private boolean containsPolyline(Workout workout) {
		return workout.getPolyLineEncoded() != null && !workout.getPolyLineEncoded().isEmpty();
	}

	private Point getPointFromPolyline(Workout workout) {
		Point point = PolylineDecoder.decodeFirst(workout.getPolyLineEncoded());
		return point;
	}
}
