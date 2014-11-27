package com.moomeen.location;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moomeen.location.dao.PlaceDao;
import com.moomeen.location.exception.PlaceNotFoundException;
import com.moomeen.location.model.Place;
import com.moomeen.location.model.Point;
import com.moomeen.location.nominatim.NominatimAdaptor;

@Service
public class ReverseGeoLocator {

	private final static Logger LOG = LoggerFactory.getLogger(ReverseGeoLocator.class);
	private static final int ONE_KM = 1000;

	@Autowired
	private NominatimAdaptor nominatimExecutor;
	
	@Autowired
	private PlaceDao placeDao;
	
	private ExecutorService executorService = Executors.newFixedThreadPool(50);

	public Map<Point, Place> reverse(Set<Point> points){
		Map<Point, Future<Place>> mapWithFutures = new HashMap<Point, Future<Place>>();
		for (final Point point : points) {
			Future<Place> addressFuture = submitReverseTask(point);
			mapWithFutures.put(point, addressFuture);
		}

		return extractValues(mapWithFutures);
	}

	private Future<Place> submitReverseTask(final Point point){
		return executorService.submit(new Callable<Place>() {

			@Override
			public Place call() throws Exception {
				Place place = null;
				try {
					place = placeDao.findWithinDistance(point, ONE_KM);
				} catch (PlaceNotFoundException e){
					place = nominatimExecutor.reverse(point);
					placeDao.save(place);
				}
				return place;
			}
		});
	}

	private <K extends Object, V extends Object> Map<K, V> extractValues(Map<K, Future<V>> mapWithFutures) {
		Map<K, V> mapWithValues = new HashMap<K, V>();
		for (Map.Entry<K, Future<V>> latLng : mapWithFutures.entrySet()) {
			try {
				mapWithValues.put(latLng.getKey(), latLng.getValue().get());
			} catch (InterruptedException | ExecutionException e) {
				LOG.error("Error during retrieving value from future object", e);
			}
		}
		return mapWithValues;
	}

}
