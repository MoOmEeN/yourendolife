package com.moomeen.location;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity(value = "places", noClassnameStored = true)
public class Place {

	@Id
	private ObjectId id;

	private String name;

	private String country;

	private Point point;

	public Place(String name, String country, Point point) {
		super();
		this.name = name;
		this.country = country;
		this.point = point;
	}

	public String getName() {
		return name;
	}

	public String getCountry() {
		return country;
	}

	public Point getPoint(){
		return point;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Place other = (Place) obj;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
