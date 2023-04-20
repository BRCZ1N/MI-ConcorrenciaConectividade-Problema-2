package application.model;

import org.json.JSONException;
import org.json.JSONObject;

public class FogModel {

	private String id;
	private ChargingStationModel bestStation;

	public FogModel(String id, ChargingStationModel bestStation) {

		this.id = id;
		this.bestStation = bestStation;

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ChargingStationModel getBestStation() {
		return bestStation;
	}

	public void setBestStation(ChargingStationModel bestStation) {
		this.bestStation = bestStation;
	}
	
	public static FogModel JsonToFogModel(String stationJson) throws JSONException {

		JSONObject json = new JSONObject(stationJson);

		ChargingStationModel bestStation = (ChargingStationModel) json.get("bestStation");
		String id = json.getString("id");

		FogModel fog = new FogModel(id,bestStation);
		return fog;

	}

}
