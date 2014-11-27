package com.moomeen.location.exception;

import com.moomeen.location.model.Point;

public class PlaceNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5234430027139688332L;
	private Point p;
	
	public PlaceNotFoundException(Point p) {
		this.p = p;
	}

	public Point getPoint() {
		return p;
	}
}
