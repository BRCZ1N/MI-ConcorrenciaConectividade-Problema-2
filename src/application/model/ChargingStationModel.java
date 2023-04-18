package application.model;

import org.json.JSONException;
import org.json.JSONObject;

public class ChargingStationModel {

	private String name;
	private Double latitude;
	private Double longitude;
	private int totalAmountCars;
	private String id;

	public ChargingStationModel(String name, Double addressX, Double addressY, int totalAmountCars, String id) {

		this.name = name;
		this.latitude = addressX;
		this.longitude = addressY;
		this.totalAmountCars = totalAmountCars;
		this.id = id;

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public int getTotalAmountCars() {
		
		return totalAmountCars;
		
	}

	public void setTotalAmountCars(int totalAmountCars) {
		
		this.totalAmountCars = totalAmountCars;
		
	}

	public String getId() {
		
		return id;
		
	}

	public void setId(String id) {
		
		this.id = id;
		
	}
	
	public static ChargingStationModel JsonToChargingStationModel(String stationJson) throws JSONException {
		
		JSONObject json = new JSONObject(stationJson);
		
		String name = json.getString("name");
		Double addressX = json.getDouble("addressX");
		Double addressY = json.getDouble("addressY");
		int totalAmountCars = json.getInt("totalAmountCars");
		String id = json.getString("id");

		ChargingStationModel station = new ChargingStationModel(name, addressX, addressY, totalAmountCars, id);
		return station;
		
	}
	
}
