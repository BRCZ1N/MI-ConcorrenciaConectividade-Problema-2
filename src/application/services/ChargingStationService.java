package application.services;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;
import application.model.ChargingStationModel;

@Component
public class ChargingStationService {

	private static Map<String, ChargingStationModel> stations;

	public ChargingStationService() {

		ChargingStationService.stations = new ConcurrentHashMap<String, ChargingStationModel>();

	}
	
	public static Map<String, ChargingStationModel> getStations() {
		return stations;
	}

	public static void setStations(Map<String, ChargingStationModel> stations) {
		ChargingStationService.stations = stations;
	}

	public static void addStation(ChargingStationModel station) {

		stations.put(station.getId(), station);

	}

	public static Optional<ChargingStationModel> getShorterQueueStation() {

		ChargingStationModel stationShorterQueue = null;

		if (stations.isEmpty()) {

			return Optional.empty();

		}

		for (Map.Entry<String, ChargingStationModel> station : stations.entrySet()) {

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

	public static Optional<ChargingStationModel> getBestLocationStation(Double locationX, Double locationY) {

		ChargingStationModel stationShorterQueue = null;
		double currentDistance = 0;
		double previousDistance = 0;

		if (stations.isEmpty()) {

			return Optional.empty();

		}

		for (Map.Entry<String, ChargingStationModel> currentStation : stations.entrySet()) {

			if (stationShorterQueue == null) {

				stationShorterQueue = currentStation.getValue();
				previousDistance = distanceValue(locationX, locationY, stationShorterQueue.getLatitude(),
						stationShorterQueue.getLongitude());

			} else {

				currentDistance = distanceValue(locationX, locationY, currentStation.getValue().getLatitude(),
						currentStation.getValue().getLongitude());

				if (previousDistance > currentDistance) {

					stationShorterQueue = currentStation.getValue();
					previousDistance = currentDistance;

				}

			}

		}

		return Optional.ofNullable(stationShorterQueue);

	}

	public static Optional<ArrayList<ChargingStationModel>> getAllStations() {

		if (stations.isEmpty()) {

			return Optional.empty();

		}

		ArrayList<ChargingStationModel> stationsList = new ArrayList<>();
		stationsList.addAll(stations.values());
		return Optional.of(stationsList);

	}

	public static void editStation(ChargingStationModel station) {

		if (stations.containsKey(station.getId())) {

			stations.replace(station.getId(), station);

		}

	}

	public static void removeStation(String id) {

		if (stations.containsKey(id)) {

			stations.remove(id);

		}

	}

	public static double distanceValue(Double locationX, Double locationY, Double currentStationLocationX,Double currentStationLocationY) {

		double distance = Point2D.distance(locationX, locationY, currentStationLocationX, currentStationLocationY);

		return distance;
	}

}
