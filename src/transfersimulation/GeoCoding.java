package transfersimulation;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;

public class GeoCoding {
 
	public static void main(String[] args) {
		// Replace the API key below with a valid API key.
		try {
			GeoApiContext context = new GeoApiContext()
				.setApiKey("AIzaSyAI9lwlk593ZOLjbd0XW-e9EMvQnxEVxtg").setQueryRateLimit(1, 1);
			GeocodingResult[] results = GeocodingApi.geocode(context,
				    "1600 Amphitheatre Parkway Mountain View, CA 94043").await();
			System.out.println(results[0].formattedAddress);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
}
