package application.services;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import application.model.ChargingStation;

public class ChargingStationService {

	private Map<String, ChargingStation> stations;
	private long idPost = 0;

	public ChargingStationService() {

		stations = new HashMap<String, ChargingStation>();

	}

	public void addStation(ChargingStation post) {

		if (stations.containsValue(post)) {

			post.setId(Long.toString(idPost));
			idPost++;
			stations.put(post.getId(), post);

		}

	}

	public Optional<ChargingStation> getStationShorterQueue() {

		ChargingStation stationShorterQueue = null;

		for (Entry<String, ChargingStation> station : stations.entrySet()) {

			if (stationShorterQueue == null) {

				stationShorterQueue = station.getValue();

			} else {

				if (stationShorterQueue.getTotalAmountCars() > station.getValue().getTotalAmountCars()) {

					stationShorterQueue = station.getValue();

				}

			}

		}
		
		return Optional.ofNullable(stationShorterQueue);

	}

	public Optional<ChargingStation> getStationBestProximity(double locationX, double locationY) {

		ChargingStation stationShorterQueue = null;
		double currentDistance = 0;
		double previousDistance = 0;

		for (Entry<String, ChargingStation> currentStation : stations.entrySet()) {

			if (stationShorterQueue == null) {

				stationShorterQueue = currentStation.getValue();
				previousDistance = Point2D.distance(locationX, locationY, stationShorterQueue.getAddressX(), stationShorterQueue.getAddressY());
				
			} else {

				currentDistance = Point2D.distance(locationX, locationY, currentStation.getValue().getAddressX(), currentStation.getValue().getAddressY());
				
				if (previousDistance > currentDistance) {

					stationShorterQueue = currentStation.getValue();

				}

			}

		}
		
		return Optional.ofNullable(stationShorterQueue);


	}

	public Optional<ArrayList<ChargingStation>> getAllStations() {
		
		ArrayList<ChargingStation> stations;
		
		if(stations)
		
	}

	public void editStation(ChargingStation post) {

		if (stations.containsValue(post)) {

			stations.replace(post.getId(), post);

		}

	}

	public void removeStation(String id) {

		Optional<ChargingStation> post = Optional.ofNullable(getPost(id));
		if (post.isPresent()) {

			stations.remove(post.get().getId());

		}

	}

	public ChargingStation getPost(String id) {

		for (Entry<String, ChargingStation> post : stations.entrySet()) {

			if (post.getKey().equals(id)) {

				return post.getValue();

			}

		}

		return null;

	}

	public boolean authenticateStation(String id, String password) {

		for (Entry<String, ChargingStation> post : stations.entrySet()) {

			if (post.getValue().getId().equals(id) && post.getValue().getPassword().equals(password)) {

				return true;

			}

		}

		return false;

	}

}
