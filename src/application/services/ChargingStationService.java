package application.services;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import application.model.ChargingStationModel;

@Service
public class ChargingStationService {

	private Map<String, ChargingStationModel> stations;
	private Long currentId = 0L;

	public ChargingStationService() {

		stations = new HashMap<>();

	}

	public void addStation(ChargingStationModel station) {

		station.setId(Long.toString(currentId));
		currentId++;
		stations.put(station.getId(), station);

	}

	public Optional<ChargingStationModel> getShorterQueueStation() {

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

	public Optional<ChargingStationModel> getBestLocationStation(Double locationX, Double locationY) {

		ChargingStationModel stationShorterQueue = null;
		double currentDistance = 0;
		double previousDistance = 0;

		if (stations.isEmpty()) {

			return Optional.empty();

		}

		for (Map.Entry<String, ChargingStationModel> currentStation : stations.entrySet()) {

			if (stationShorterQueue == null) {

				stationShorterQueue = currentStation.getValue();
				previousDistance = distanceValue(locationX, locationY, stationShorterQueue.getAddressX(),
						stationShorterQueue.getAddressY());

			} else {

				currentDistance = distanceValue(locationX, locationY, currentStation.getValue().getAddressX(),
						currentStation.getValue().getAddressY());

				if (previousDistance > currentDistance) {

					stationShorterQueue = currentStation.getValue();
					previousDistance = currentDistance;

				}

			}

		}

		return Optional.ofNullable(stationShorterQueue);

	}

	public Optional<ArrayList<ChargingStationModel>> getAllStations() {

		if (stations.isEmpty()) {

			return Optional.empty();

		}

		ArrayList<ChargingStationModel> stationsList = new ArrayList<>();
		stationsList.addAll(stations.values());
		return Optional.of(stationsList);

	}

	public void editStation(ChargingStationModel station) {

		if (stations.containsKey(station.getId())) {

			stations.replace(station.getId(), station);

		}

	}

	public void removeStation(String id) {

		if (stations.containsKey(id)) {

			stations.remove(id);

		}

	}

//	public boolean authenticateStation(String id, String password) {
//
//		for (Map.Entry<String, ChargingStationModel> station : stations.entrySet()) {
//
//			if (station.getValue().getId().equals(id) && station.getValue().getPassword().equals(password)) {
//
//				return true;
//
//			}
//
//		}
//
//		return false;
//
//	}

	public double distanceValue(Double locationX, Double locationY, Double currentStationLocationX,
			Double currentStationLocationY) {

		double distance = Point2D.distance(locationX, locationY, currentStationLocationX, currentStationLocationY);

		return distance;
	}

}
