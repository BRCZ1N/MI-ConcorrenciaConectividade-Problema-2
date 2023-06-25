package application.model;

import org.json.JSONObject;

public class FogModel {

	private String id;
	private ChargingStationModel bestStation;
	
	public FogModel() {
		super();
	}

	public FogModel(String id, ChargingStationModel bestStation) {

		this.id = id;
		this.bestStation = bestStation;

	}
	
	public FogModel(JSONObject json) {

		this.id = json.getString("id");
		this.bestStation = new ChargingStationModel(json.getJSONObject("bestStation"));

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

}
