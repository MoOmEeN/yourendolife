package com.moomeen.endo.location;

import static java.util.Collections.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.moomeen.endo2java.model.Workout;
import com.moomeen.location.LocationService;
import com.moomeen.location.ReverseGeoLocator;
import com.moomeen.location.model.Place;
import com.moomeen.location.model.Point;

@RunWith(MockitoJUnitRunner.class)
public class LocationServiceTest {

	@Mock
	private ReverseGeoLocator locator;
	
	@InjectMocks
	private LocationService service = new LocationService();
	
	public LocationServiceTest() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void determineCities_noPolyline(){
		// given
		Workout workout = workoutWithPolyline();
		
		// when
		Map<Place, List<Workout>> result = service.determineCities(singletonList(workout));
		
		// then
		verify(locator, times(1)).reverse(argThat(hasSize(0)));
		assertTrue(result.isEmpty());
	}
	
	@Test
	public void determineCities_twoWorkoutsSameCity(){
		// given
		Workout workoutOne = workoutWithPolyline("gwr{FaoiLbMgP");
		Workout workoutTwo = workoutWithPolyline("ods{FcyfL_@uA");
		mockGeoLocation(
				asMap(
						asList(
								new Point(41.38658, 2.18645), 
								new Point(41.38884, 2.18369)), 
						asList(
								new Place("Barcelona", "Spain", null), 
								new Place("Barcelona", "Spain", null))
				)
		);
		
		// when
		Map<Place, List<Workout>> result = service.determineCities(asList(workoutOne, workoutTwo));
		
		// then
		verify(locator, times(1)).reverse(argThat(hasSize(2)));
		assertEquals(1, result.size());
	}

	@Test
	public void determineCities_twoWorkoutsDifferentCities(){
		// given
		Workout workoutOne = workoutWithPolyline("wb}}Hgui_CxD`M");
		Workout workoutTwo = workoutWithPolyline("ods{FcyfL_@uA");
		mockGeoLocation(
				asMap(
						asList(
								new Point(52.25532, 21.0262), 
								new Point(41.38884, 2.18369)), 
						asList(
								new Place("Poland", "Warsaw", null), 
								new Place("Barcelona", "Spain", null))
				)
		);
		
		// when
		Map<Place, List<Workout>> result = service.determineCities(asList(workoutOne, workoutTwo));
		
		// then
		verify(locator, times(1)).reverse(argThat(hasSize(2)));
		assertEquals(2, result.size());
	}
	
	@SuppressWarnings("unchecked")
	private void mockGeoLocation(Map<Point, Place> toReturn) {
		when(locator.reverse(any(Set.class))).thenReturn(toReturn);
	}
	
	Matcher<Set<Point>> hasSize(final int size) {
	    return new TypeSafeMatcher<Set<Point>>() {
	        @Override
			public boolean matchesSafely(Set<Point> item) {
	            return item.size() == size;
	        }
	        @Override
			public void describeTo(Description description) {
	        }
	    };
	}
	
	private Workout workoutWithPolyline(){
		Workout w = mock(Workout.class);
		return w;
	}
	
	private Workout workoutWithPolyline(String polyline){
		Workout w = mock(Workout.class);
		when(w.getPolyLineEncoded()).thenReturn(polyline);
		return w;
	}
	
	@SafeVarargs
	private static <T> List<T> asList(T... items){
		List<T> list = new ArrayList<T>();
		for (T item : items) {
			list.add(item);
		}
		return list;
	}
	
	private static <K,V> Map<K,V> asMap(List<K> keys, List<V> values){
		Map<K,V> map = new HashMap<K, V>();
		for (int i = 0; i < keys.size(); i++){
			map.put(keys.get(i), values.get(i));
		}
		return map;
	}
}
