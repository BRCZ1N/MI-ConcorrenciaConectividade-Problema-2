package application.model;

import org.json.JSONException;
import org.json.JSONObject;

public class ChargingStationModel {

	private String name;
	private Double latitude;
	private Double longitude;
	private int totalAmountCars;
	private String id;

	/**
	 * 
	 * Construtor da classe ChargingStationModel.
	 * 
	 * @param name            nome da estação
	 * 
	 * @param addressX        coordenada X da localização da estação
	 * 
	 * @param addressY        coordenada Y da localização da estação
	 * 
	 * @param totalAmountCars quantidade total de carros que a estação pode carregar
	 * 
	 * @param id              identificador único da estação
	 */
	public ChargingStationModel(String name, Double addressX, Double addressY, int totalAmountCars, String id) {

		this.name = name;
		this.latitude = addressX;
		this.longitude = addressY;
		this.totalAmountCars = totalAmountCars;
		this.id = id;

	}

	/**
	 * 
	 * Método que retorna o nome da estação.
	 * 
	 * @return nome da estação
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * Método que define o nome da estação.
	 * 
	 * @param name nome da estação
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * Método que retorna a coordenada X da localização da estação.
	 * 
	 * @return coordenada X da localização da estação
	 */
	public Double getLatitude() {

		return latitude;

	}

	/**
	 * 
	 * Método que define a coordenada X da localização da estação.
	 * 
	 * @param latitude coordenada X da localização da estação
	 */
	public void setLatitude(Double latitude) {

		this.latitude = latitude;

	}

	/**
	 * 
	 * Método que retorna a coordenada Y da localização da estação.
	 * 
	 * @return coordenada Y da localização da estação
	 */
	public Double getLongitude() {

		return longitude;

	}

	/**
	 * 
	 * Método que define a coordenada Y da localização da estação.
	 * 
	 * @param longitude coordenada Y da localização da estação
	 */
	public void setLongitude(Double longitude) {

		this.longitude = longitude;

	}

	/**
	 * 
	 * Método que retorna a quantidade total de carros que a estação pode carregar.
	 * 
	 * @return quantidade total de carros que a estação pode carregar
	 */
	public int getTotalAmountCars() {

		return totalAmountCars;

	}

	/**
	 * 
	 * Método que define a quantidade total de carros que a estação pode carregar.
	 * 
	 * @param totalAmountCars quantidade total de carros que a estação pode carregar
	 */
	public void setTotalAmountCars(int totalAmountCars) {

		this.totalAmountCars = totalAmountCars;

	}

	/**
	 * 
	 * Método que retorna o identificador único da estação.
	 * 
	 * @return identificador único da estação
	 */
	public String getId() {

		return id;

	}

	/**
	 * 
	 * Método que define o identificador único da estação.
	 * 
	 * @param id identificador único da estação
	 */
	public void setId(String id) {

		this.id = id;

	}

	public static ChargingStationModel JsonToChargingStationModel(String stationJson) throws JSONException {

		JSONObject json = new JSONObject(stationJson);

		String name = json.getString("name");
		Double latitude = json.getDouble("latitude");
		Double longitude = json.getDouble("longitude");
		int totalAmountCars = json.getInt("totalAmountCars");
		String id = json.getString("id");

		ChargingStationModel station = new ChargingStationModel(name, latitude, longitude, totalAmountCars, id);
		return station;

	}

}
