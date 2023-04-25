package application.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import application.model.FogModel;

public class FogService {

	private static Map<String, FogModel> fogs;

	public FogService() {

		FogService.fogs = new ConcurrentHashMap<String, FogModel>();

	}

	public static Map<String, FogModel> getStations() {
		return fogs;
	}

	public static void setFogs(Map<String, FogModel> stations) {
		FogService.fogs = stations;
	}

	public static void addFog(FogModel fog) {

		fogs.put(fog.getId(), fog);

	}

	public static Optional<ArrayList<FogModel>> getOrdersListAllRegionsForQueue() {

		if (fogs.isEmpty()) {

			return Optional.empty();

		}

		ArrayList<FogModel> stationsList = new ArrayList<>();
		stationsList.addAll(fogs.values());
		
		Comparator<FogModel> comparator = new Comparator<FogModel>() {

			@Override
			public int compare(FogModel f1, FogModel f2) {
				
				Integer queue1 = f1.getBestStation().getTotalAmountCars();
				Integer queue2 = f2.getBestStation().getTotalAmountCars();
				return queue1.compareTo(queue2);
				
			}
			
		};
		
		
		Collections.sort(stationsList,comparator);
		return Optional.of(stationsList);

	}

	public static void removeFog(String id) {

		if (fogs.containsKey(id)) {

			fogs.remove(id);

		}

	}

}
