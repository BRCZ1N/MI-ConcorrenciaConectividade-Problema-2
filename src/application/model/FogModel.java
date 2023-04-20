package application.model;

import org.json.JSONException;
import org.json.JSONObject;

public class FogModel {

	private String id;
	private Double latitude;
	private Double longitude;
	private ChargingStationModel bestStation;

	public FogModel(String id, Double latitude, Double longitude, ChargingStationModel bestStation) {

		this.id = id;
		this.latitude = latitude;
		this.longitude = longitude;
		this.bestStation = bestStation;

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public ChargingStationModel getBestStation() {
		return bestStation;
	}

	public void setBestStation(ChargingStationModel bestStation) {
		this.bestStation = bestStation;
	}
	
	public static FogModel JsonToFogModel(String stationJson) throws JSONException {

		JSONObject json = new JSONObject(stationJson);

		Double latitude = json.getDouble("latitude");
		Double longitude = json.getDouble("longitude");
		ChargingStationModel bestStation = (ChargingStationModel) json.get("bestStation");
		String id = json.getString("id");

		FogModel fog = new FogModel(id, latitude, longitude,bestStation);
		return fog;

	}

}
