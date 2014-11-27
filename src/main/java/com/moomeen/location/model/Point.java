package com.moomeen.location.model;

public class Point {

	@SuppressWarnings("unused")
	private String type = "Point";
	
	private double[] coordinates;

	public Point(double latitude, double longitude) {
		coordinates = new double[2];
		coordinates[1] = latitude;
		coordinates[0] = longitude;
	}

	public double getLatitude() {
		return coordinates[1];
	}

	public double getLongitude() {
		return coordinates[0];
	}
}