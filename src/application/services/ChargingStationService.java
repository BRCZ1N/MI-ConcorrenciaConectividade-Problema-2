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

	/**
	 * Construtor padrão da classe, que inicializa o mapa de estações.
	 */
	public ChargingStationService() {

		ChargingStationService.stations = new ConcurrentHashMap<String, ChargingStationModel>();

	}

	/**
	 * Retorna o mapa de estações.
	 * 
	 * @return o mapa de estações.
	 */
	public static Map<String, ChargingStationModel> getStations() {
		return stations;
	}

	/**
	 * Configura o mapa de estações.
	 * 
	 * @param stations o mapa de estações.
	 */
	public static void setStations(Map<String, ChargingStationModel> stations) {
		ChargingStationService.stations = stations;
	}

	/**
	 * Adiciona uma estação ao mapa.
	 * 
	 * @param station a estação a ser adicionada.
	 */
	public static void addStation(ChargingStationModel station) {

		stations.put(station.getId(), station);

	}

	/**
	 * Retorna a estação com a menor fila de carros aguardando carregamento.
	 * 
	 * @return a estação com a menor fila de carros aguardando carregamento.
	 */
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

	/**
	 * Retorna a estação mais próxima de uma determinada localização.
	 * 
	 * @param locationX a coordenada X da localização.
	 * @param locationY a coordenada Y da localização.
	 * @return a estação mais próxima de uma determinada localização.
	 */
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
	 /**
     * Obtém todas as estações de carregamento cadastradas.
     *
     * @return Optional contendo ArrayList de ChargingStationModel, vazio se não houver estações cadastradas.
     */
	public static Optional<ArrayList<ChargingStationModel>> getAllStations() {

		if (stations.isEmpty()) {

			return Optional.empty();

		}

		ArrayList<ChargingStationModel> stationsList = new ArrayList<>();
		stationsList.addAll(stations.values());
		return Optional.of(stationsList);

	}
	 /**
     * Edita uma estação de carregamento cadastrada.
     *
     * @param station ChargingStationModel contendo as informações atualizadas da estação.
     */
	public static void editStation(ChargingStationModel station) {

		if (stations.containsKey(station.getId())) {

			stations.replace(station.getId(), station);

		}

	}
	/**
     * Remove uma estação de carregamento cadastrada.
     *
     * @param id String contendo o ID da estação a ser removida.
     */
	public static void removeStation(String id) {

		if (stations.containsKey(id)) {

			stations.remove(id);

		}

	}
	/**
     * Calcula a distância entre duas coordenadas geográficas.
     *
     * @param locationX Double contendo a coordenada X do ponto 1.
     * @param locationY Double contendo a coordenada Y do ponto 1.
     * @param currentStationLocationX Double contendo a coordenada X do ponto 2.
     * @param currentStationLocationY Double contendo a coordenada Y do ponto 2.
     * @return Double contendo a distância entre os pontos.
     */
	public static double distanceValue(Double locationX, Double locationY, Double currentStationLocationX,
			Double currentStationLocationY) {

		double distance = Point2D.distance(locationX, locationY, currentStationLocationX, currentStationLocationY);

		return distance;
	}

}
