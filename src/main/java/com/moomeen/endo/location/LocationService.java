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
		String city = getValue("city", address);
		String town = getValue("town", address);
		String village = getValue("village", address);
		String hamlet = getValue("hamlet", address);
		String state = getValue("state", address);
		String country = getValue("country", address);

		String name = firstNotNull(city, town, village, hamlet, state);

		return new City(name, country, address.getLatitude(), address.getLongitude());
	}

	private String firstNotNull(String... strings){
		for (String string : strings) {
			if (string != null){
				return string;
			}
		}
		throw new NullPointerException("only null strings provided");
	}

	private String getValue(String key, Address address){
		for (AddressElement element : address.getAddressElements()) {
			if (element.getKey().equals(key)){
				return element.getValue();
			}
		}
		return null;
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
