package com.moomeen.endo.location.nominatim;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.moomeen.location.Place;
import com.moomeen.location.Point;
import com.moomeen.location.nominatim.NominatimAdaptor;

@RunWith(Parameterized.class)
public class NominatimAdaptorTest {
	
	@Parameterized.Parameters
	public static Collection<Object[]> locations() {
		return Arrays.asList(new Object[][] { 
				{ new Point(52.21048005, 21.0084851808125), "Warsaw" },
				{ new Point(50.5698988, 22.0643200655906), "Stalowa Wola" }, 
				{ new Point(52.5005107, 13.5205721), "Berlin" },
				{ new Point(41.3911354, 2.1694735), "Barcelona" },
				{ new Point(52.1310306, 20.8214569), "Komorów-Wieś" },
				{ new Point(54.3854113, 16.3183244), "Dąbki" }
		});
	}
	
	private Point coordinates;
	private String expectedPlace;
	
	private NominatimAdaptor adaptor;
	
	public NominatimAdaptorTest(Point cooridnates, String expectedPlace) {
		this.coordinates = cooridnates;
		this.expectedPlace = expectedPlace;
	}
	
	@Before
	public void init() {
		adaptor = new NominatimAdaptor();
		adaptor.init();
	}

   @Test
   public void testReverseGeolocate() {
	   // when
	   Place place = adaptor.reverse(coordinates.getLatitude(), coordinates.getLongitude());
	   
	   // then
	   assertEquals(expectedPlace, place.getName());
   }

}
