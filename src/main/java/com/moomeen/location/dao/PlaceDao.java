package com.moomeen.location.dao;

import java.net.UnknownHostException;

import javax.annotation.PostConstruct;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.Morphia;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.moomeen.location.Place;

@Service
public class PlaceDao {

	private static Logger LOG = LoggerFactory.getLogger(PlaceDao.class);

	private double EARTH_RADIUS = 6371.01;

	@Value("#{environment.MONGO_URL}")
    private String dbUri;

	private Datastore ds;

	@PostConstruct
	public void init() throws UnknownHostException {
		MongoClientURI uri = new MongoClientURI(dbUri);
		MongoClient client = new MongoClient(uri);
		ds = new Morphia().createDatastore(client, uri.getDatabase());
	}

	public void save(Place place) {
		Key<Place> key =  ds.save(place);
		LOG.debug("place saved with id: {}", key.getId().toString());
	}

	public Place findWithinDistance(double longitude, double latitude, double distance){
		return null; // TODO
	}

}
