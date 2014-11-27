package com.moomeen.location.nominatim;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.moomeen.location.model.Place;
import com.moomeen.location.model.Point;

import fr.dudie.nominatim.client.JsonNominatimClient;
import fr.dudie.nominatim.client.request.NominatimReverseRequest;
import fr.dudie.nominatim.model.Address;
import fr.dudie.nominatim.model.AddressElement;

@Service
public class NominatimAdaptor {

	private final static Logger LOG = LoggerFactory.getLogger(NominatimAdaptor.class);

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

	public Place reverse(Point point) {
		try {
			NominatimReverseRequest request = new NominatimReverseRequest();
			request.setQuery(point.getLongitude(), point.getLatitude());
			request.setAcceptLanguage("en");
			Address address = nominatimClient.getAddress(request);
			return fromAddress(address);
		} catch (IOException e) {
			LOG.error("Couldn't reverse geolocate point: {}, {}", point.getLatitude(), point.getLongitude(), e);
		}
		return null;
	}

	private Place fromAddress(Address address){
		String city = getValue("city", address);
		String town = getValue("town", address);
		String village = getValue("village", address);
		String hamlet = getValue("hamlet", address);
		String state = getValue("state", address);
		String country = getValue("country", address);

		String name = firstNotNull(city, town, village, hamlet, state);

		return new Place(name, country, new Point(address.getLatitude(), address.getLongitude()));
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

}
