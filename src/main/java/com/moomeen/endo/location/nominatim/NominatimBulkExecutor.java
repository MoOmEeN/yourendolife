package com.moomeen.endo.location.nominatim;

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

import com.moomeen.endo.location.Point;

import fr.dudie.nominatim.model.Address;

@Service
public class NominatimBulkExecutor {

	private final static Logger LOG = LoggerFactory.getLogger(NominatimBulkExecutor.class);

	private ExecutorService executorService = Executors.newFixedThreadPool(100);

	@Autowired
	private NominatimExecutor executor;

	public Map<Point, Address> reverse(Set<Point> points){
		Map<Point, Future<Address>> mapWithFutures = new HashMap<Point, Future<Address>>();
		for (final Point point : points) {
			Future<Address> addressFuture = submitReverseTask(point);
			mapWithFutures.put(point, addressFuture);
		}

		return extractValues(mapWithFutures);
	}

	private Future<Address> submitReverseTask(final Point point){
		return executorService.submit(new Callable<Address>() {

			@Override
			public Address call() throws Exception {
				return executor.reverse(point.getLatitude(), point.getLongitude());
			}
		});
	}

	private <T extends Object> Map<T, Address> extractValues(Map<T, Future<Address>> mapWithFutures) {
		Map<T, Address> mapWithValues = new HashMap<T, Address>();
		for (Map.Entry<T, Future<Address>> latLng : mapWithFutures.entrySet()) {
			try {
				mapWithValues.put(latLng.getKey(), latLng.getValue().get());
			} catch (InterruptedException | ExecutionException e) {
				LOG.error("Error during retrieving value from future object", e);
			}
		}
		return mapWithValues;
	}

}
