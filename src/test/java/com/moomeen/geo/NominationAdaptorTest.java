package com.moomeen.geo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.junit.Test;

import com.moomeen.endo2java.error.InvocationException;

import fr.dudie.nominatim.client.JsonNominatimClient;
import fr.dudie.nominatim.model.Address;
import fr.dudie.nominatim.model.AddressElement;

public class NominationAdaptorTest {

	@Test
	public void getAdressesTest() throws IOException, InvocationException, URISyntaxException, InterruptedException {
		URL url = NominationAdaptorTest.class.getClassLoader().getResource("coordinates");
		File f= new File(url.toURI());
		List<P> points = new ArrayList<P>();
		BufferedReader br = new BufferedReader(new FileReader(f));
		String line;
		while ((line = br.readLine()) != null) {
		   String latitude = line.split(",")[0];
		   String longitude = line.split(",")[1];
			points.add(new P(new Double(latitude), new Double(longitude)));
		}
		br.close();

		final PoolingHttpClientConnectionManager connexionManager = new PoolingHttpClientConnectionManager();
		connexionManager.setMaxTotal(50);
		connexionManager.setDefaultMaxPerRoute(50);
		 HttpClientBuilder.create().setConnectionManager(connexionManager).build();
		 JsonNominatimClient client = new JsonNominatimClient("http://open.mapquestapi.com/nominatim/v1/", HttpClientBuilder.create().setConnectionManager(connexionManager).build(), "moomeen@gmail.com");
		 Vector<Address> list  = new Vector<Address>();
		 long geocodeStart = System.currentTimeMillis();

		 ExecutorService service = Executors.newFixedThreadPool(100);
		 for (P point : points) {
			 service.submit(new GetAddressJob(client, point, list));
			}
		 service.shutdown();
		 try {
			 service.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		 } catch (InterruptedException e) {

		 }

		Map<String, List<Address>> map = new HashMap<String, List<Address>>();
		for (Address address : list) {
			for (AddressElement elem : address.getAddressElements()) {
				if (elem.getKey().equals("country")){
					if (map.containsKey(elem.getValue())){
						map.get(elem.getValue()).add(address);
					} else {
						List<Address> ass = new ArrayList<Address>();
						ass.add(address);
						map.put(elem.getValue(), ass);
					}
				}
			}
		}

		for (String country : map.keySet()) {
			System.out.println(country + " " + map.get(country).size());
		}

		 System.out.println(System.currentTimeMillis() - geocodeStart);
	}

	static class P {
		private double latitude;
		private double longitude;

		public P(double latitude, double longitude) {
			this.latitude = latitude;
			this.longitude = longitude;
		}
	}

	static class GetAddressJob implements Runnable {

		JsonNominatimClient client;
		P point;
		Vector<Address> v;

		public GetAddressJob(JsonNominatimClient client, P point, Vector<Address> v) {
			this.client = client;
			this.point = point;
			this.v = v;
		}

		@Override
		public void run() {
			Address address;
			try {
				address = client.getAddress(point.longitude,point.latitude);
				 v.add(address);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

}
