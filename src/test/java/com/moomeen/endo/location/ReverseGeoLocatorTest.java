package com.moomeen.endo.location;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.moomeen.location.ReverseGeoLocator;
import com.moomeen.location.dao.PlaceDao;
import com.moomeen.location.exception.PlaceNotFoundException;
import com.moomeen.location.model.Place;
import com.moomeen.location.model.Point;
import com.moomeen.location.nominatim.NominatimAdaptor;

@RunWith(MockitoJUnitRunner.class)
public class ReverseGeoLocatorTest {
	
	@Mock
	private NominatimAdaptor nominatimExecutor;
	
	@Mock
	private PlaceDao placeDao;
	
	@InjectMocks
	private ReverseGeoLocator locator = new ReverseGeoLocator();
	
	public ReverseGeoLocatorTest() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void reverseGeoLocate_cached() throws PlaceNotFoundException{
		// givem
		Point point = new Point(41.38658, 2.18645);
		Place place = new Place("Barcelona", "Spain", point);
		setPointCashed(point, place);
		
		// when
		Map<Point, Place> result = locator.reverse(asSet(point));
		
		// then
		verify(nominatimExecutor, times(0)).reverse(point);
		assertContains(result, point, place);
	}

	private void setPointCashed(Point point, Place place) throws PlaceNotFoundException {
		when(placeDao.findWithinDistance(eq(point), anyInt())).thenReturn(place);
	}

	@Test
	public void reverseGeoLocate_notCached() throws PlaceNotFoundException{
		// givem
		Point point = new Point(41.38658, 2.18645);
		setPointNotCashed(point);
		
		// when
		locator.reverse(asSet(point));
		
		// then
		verify(nominatimExecutor, times(1)).reverse(point);
	}
	
	@SuppressWarnings("unchecked")
	private void setPointNotCashed(Point point) throws PlaceNotFoundException {
		when(placeDao.findWithinDistance(eq(point), anyInt())).thenThrow(PlaceNotFoundException.class);
	}
	
	private void assertContains(Map<Point, Place> result, Point point, Place place) {
		boolean found = false;
		for (Entry<Point, Place> entry : result.entrySet()) {
			if (entry.getKey().equals(point) && entry.getValue().equals(place)){
				found = true;
				break;
			}
		}
		assertTrue(found);
	}
	
	private static <T> Set<T> asSet(T elem){
		Set<T> set = new HashSet<T>();
		set.add(elem);
		return set;
	}

}
