package com.moomeen.location.dao;

import java.net.UnknownHostException;
import java.util.List;

import javax.annotation.PostConstruct;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.util.JSON;
import com.moomeen.location.exception.PlaceNotFoundException;
import com.moomeen.location.model.Place;
import com.moomeen.location.model.Point;

@Service
public class PlaceDao {

	private static Logger LOG = LoggerFactory.getLogger(PlaceDao.class);

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
		ds.save(place);
	}

	public Place findWithinDistance(Point point, int distance) throws PlaceNotFoundException{
		DBObject query = getSearchQuery(point, distance);
		List<DBObject> list = executeQuery(query);
		if (list.isEmpty()){
			throw new PlaceNotFoundException(point);
		}
		return toPlace(point, list);
	}

	private List<DBObject> executeQuery(DBObject query) {
		List<DBObject> list = ds.getDB().getCollection("places").find(query).limit(1).toArray();
		return list;
	}
	
	private DBObject getSearchQuery(Point point, int distance){
		String queryString = getSearchQueryString(point, distance);
		return (DBObject)JSON.parse(queryString);
	}
	
	private String getSearchQueryString(Point point, int distance){
		return String.format("{ point : { "
				+ "$near : { "
					+ "$geometry: { "
						+ "type: \"Point\", "
						+ "coordinates: [%f, %f] "
					+ "}, "
					+ "$maxDistance: %d"
				+ "} "
			+ "} }", point.getLongitude(), point.getLatitude(), distance);
	}
	
	private Place toPlace(Point point, List<DBObject> list) {
		DBObject o = list.get(0);
		String country = (String) o.get("country");
		String name = (String) o.get("name");
		return new Place(name, country, point);
	}
}
