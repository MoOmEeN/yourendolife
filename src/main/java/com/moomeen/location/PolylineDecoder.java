package com.moomeen.location;

import java.util.ArrayList;
import java.util.List;

public class PolylineDecoder {
	
	public static Point decodeFirst(String encoded){
		List<Point> oneListPoint = decode(encoded, true);
		return oneListPoint.get(0);
	}
	
	public static List<Point> decode(String encoded){
		return decode(encoded, false);
	}
	
	private static List<Point> decode(String encoded, boolean onlyFirst) {
		List<Point> track = new ArrayList<Point>();
		int index = 0;
		int lat = 0, lng = 0;
		while (index < encoded.length()) {
			int b, shift = 0, result = 0;
			do {
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lat += dlat;
			shift = 0;
			result = 0;
			do {
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lng += dlng;
			Point p = new Point(lat / 1E5, lng / 1E5);
			track.add(p);
			if (onlyFirst){
				return track;
			}
		}
		return track;
	}
}
