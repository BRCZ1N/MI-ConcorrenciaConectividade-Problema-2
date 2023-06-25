package application.model;

import org.json.JSONObject;

public class ChargingStationModel {

	private String name;
	private Double latitude;
	private Double longitude;
	private int totalAmountCars;
	private String id;
	private double queueWaitingTime;

	/**
	 * 
	 * Construtor da classe ChargingStationModel.
	 * 
	 * @param name            nome da estação
	 * 
	 * @param latitude        longitude da localização da estação
	 * 
	 * @param longitude       latitude da localização da estação
	 * 
	 * @param totalAmountCars quantidade total de carros na fila
	 * 
	 * @param queueWaitingTime tempo de espera na fila
	 * 
	 * @param id              identificador único da estação
	 */
	public ChargingStationModel(String name, Double latitude, Double longitude, int totalAmountCars, double queueWaitingTime, String id) {

		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
		this.totalAmountCars = totalAmountCars;
		this.queueWaitingTime = queueWaitingTime;
		this.id = id;

	}

	public ChargingStationModel(JSONObject json) {
		
		this.name = json.getString("name");
		this.latitude = json.getDouble("latitude");
		this.longitude = json.getDouble("longitude");
		this.totalAmountCars = json.getInt("totalAmountCars");
		this.queueWaitingTime = json.getDouble("queueWaitingTime");
		this.id = json.getString("id");
		
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
	 * Método que retorna a quantidade total de carros na fila.
	 * 
	 * @return quantidade total de carros na fila
	 */
	public int getTotalAmountCars() {

		return totalAmountCars;

	}

	/**
	 * 
	 * Método que define a quantidade total de carros na fila
	 * 
	 * @param totalAmountCars quantidade total de carros na fila
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

	/**
	 * 
	 * Método que retorna o identificador único da estação.
	 * 
	 * @return tempo de espera na fila
	 */
	public double getQueueWaitingTime() {
		return queueWaitingTime;
	}

	/**
	 * 
	 * Método que define tempo de espera na fila
	 * 
	 * @param queueWaitingTime tempo de espera na fila
	 */
	public void setQueueWaitingTime(double queueWaitingTime) {
		this.queueWaitingTime = queueWaitingTime;
	}

}
