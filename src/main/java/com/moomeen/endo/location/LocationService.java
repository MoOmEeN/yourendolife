package com.moomeen.endo.location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moomeen.EndomondoSessionHolder;
import com.moomeen.endo.location.nominatim.NominatimBulkExecutor;
import com.moomeen.endo2java.error.InvocationException;
import com.moomeen.endo2java.model.DetailedWorkout;
import com.moomeen.endo2java.model.Workout;

import fr.dudie.nominatim.model.Address;
import fr.dudie.nominatim.model.AddressElement;

@Service
public class LocationService {
	
	private static Logger LOG = LoggerFactory.getLogger(LocationService.class);
	
	@Autowired
	private NominatimBulkExecutor nominatimExecutor;

	@Autowired
	private EndomondoSessionHolder endomondoSession;
	
	public Map<City, List<Workout>> determineCities(List<Workout> workouts){
		Map<Point, Workout> coordinatesOfWorkouts = determineCoordinates(workouts);
		Map<Point, Address> coordinatesAddresses = nominatimExecutor.reverse(coordinatesOfWorkouts.keySet());
		
		Map<City, List<Workout>> workoutsCities = groupByCities(coordinatesOfWorkouts, coordinatesAddresses);
		
		enrichCitiesWithCoordinates(workoutsCities);
		
		return workoutsCities;
	}
	
	private Map<City, List<Workout>> groupByCities(Map<Point, Workout> coordinates, Map<Point, Address> addresses) {
		Map<City, List<Workout>> workoutsCities = new HashMap<City, List<Workout>>();
		
		for (Entry<Point, Address> address : addresses.entrySet()) {
			City city = fromAddress(address.getValue());
			Workout workout = coordinates.get(address.getKey());
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
	
	private City fromAddress(Address address){
		String city = null;
		String country = null;
		for (AddressElement element : address.getAddressElements()) {
			switch(element.getKey()){
			case "city":
				city = element.getValue();
				break;
			case "country":
				country = element.getValue();
				break;
			}
		}
		return new City(city, country);
	}

	private Map<Point, Workout> determineCoordinates(List<Workout> workouts) {
		Map<Point, Workout> points = new HashMap<Point, Workout>();
		for (Workout workout : workouts) {
			Point workoutInitialPoint = getInitialPoint(workout);
			if (workoutInitialPoint != null){
				points.put(workoutInitialPoint, workout);
			}
		}
		return points;
	}

	private Point getInitialPoint(Workout workout) {
		Point workoutInitialPoint;
		if (workout.getPolyLineEncoded() != null && !workout.getPolyLineEncoded().isEmpty()){
			workoutInitialPoint = getPointWithPolyline(workout);
		} else {
			workoutInitialPoint = getPointWithoutPolyline(workout);
		}
		return workoutInitialPoint;
	}

	private Point getPointWithPolyline(Workout workout) {
		Point point = PolylineDecoder.decodeFirst(workout.getPolyLineEncoded());
		return point;
	}

	private Point getPointWithoutPolyline(Workout workout) {
		try {
			DetailedWorkout detailedWorkout = endomondoSession.getWorkout(workout.getId());
			if (detailedWorkout.getPoints() != null && !detailedWorkout.getPoints().isEmpty()){
				return fromEndoPoint(detailedWorkout.getPoints().get(0));
			}
		} catch (InvocationException e) {
			LOG.error("Couldn't get workout details for workoutId: {}", workout.getId(), e);
		}
		return null;
	}
	
	private Point fromEndoPoint(com.moomeen.endo2java.model.Point endoPoint){
		if (endoPoint.getLatitude() == null || endoPoint.getLongitude() == null){
			return null;
		}
		return new Point(endoPoint.getLatitude(), endoPoint.getLongitude());
	}
	
	private void enrichCitiesWithCoordinates(Map<City, List<Workout>> workoutsCities) {
		Map<String, City> cityLocateQueries = prepareLocateQueries(workoutsCities);
		applyCoordinates(cityLocateQueries);
	}
	
	private Map<String, City> prepareLocateQueries(Map<City, List<Workout>> workoutsCities) {
		Map<String, City> cityLocateQueries = new HashMap<String, City>();
		for (City city : workoutsCities.keySet()) {
			cityLocateQueries.put(city.getName()+","+city.getCountry(), city);
		}
		return cityLocateQueries;
	}

	private void applyCoordinates(Map<String, City> cityLocateQueries) {
		Map<String, Address> locatedCities = nominatimExecutor.locate(cityLocateQueries.keySet());
		for (Entry<String, Address> locatedCity : locatedCities.entrySet()) {
			City city = cityLocateQueries.get(locatedCity.getKey());
			city.setLatitude(locatedCity.getValue().getLatitude());
			city.setLongitude(locatedCity.getValue().getLongitude());
		}
	}
}
