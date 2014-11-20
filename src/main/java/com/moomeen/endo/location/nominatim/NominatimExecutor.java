package com.moomeen.endo.location.nominatim;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import fr.dudie.nominatim.client.JsonNominatimClient;
import fr.dudie.nominatim.model.Address;

@Service
public class NominatimExecutor {
	
	private final static Logger LOG = LoggerFactory.getLogger(NominatimExecutor.class);
	
	private final static String NOMINATION_BASE_URL = "http://open.mapquestapi.com/nominatim/v1/";
	private final static String EMAIL = "moomeen@gmail.com";
	
	private JsonNominatimClient nominatimClient;
	
	@PostConstruct
	public void init(){
		final PoolingHttpClientConnectionManager connexionManager = new PoolingHttpClientConnectionManager();
		connexionManager.setMaxTotal(50);
		connexionManager.setDefaultMaxPerRoute(50);
		HttpClient httpClient = HttpClientBuilder.create().setConnectionManager(connexionManager).build();
		nominatimClient = new JsonNominatimClient(NOMINATION_BASE_URL, httpClient, EMAIL);
	}
	
	public Address reverse(double latitude, double longitude) {
		try {
			return nominatimClient.getAddress(longitude, latitude);
		} catch (IOException e) {
			LOG.error("Couldn't reverse geolocate point: {}, {}", latitude, longitude, e);
		}
		return null;
	}
	
	public Address locate(String query) {
		try {
			List<Address> addresses = nominatimClient.search(query);
			Address addressToReturn = addresses.get(0); // TODO
			return addressToReturn;
		} catch (IOException e) {
			LOG.error("Couldn't geolocate address: {}", query, e);
		}
		return null;
	}
}
