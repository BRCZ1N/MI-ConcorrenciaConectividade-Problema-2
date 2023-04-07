package application.model;

public class ChargingStation {

	private String name;
	private long addressX;
	private long addressY;
	private int totalAmountCars;
	private String id;
	private String password;

	public ChargingStation() {

	}

	public ChargingStation(String name, long addressX, long addressY, int totalAmountCars, String id, String password) {

		this.name = name;
		this.addressX = addressX;
		this.addressY = addressY;
		this.totalAmountCars = totalAmountCars;
		this.id = id;
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getAddressX() {
		return addressX;
	}

	public void setAddressX(long addressX) {
		this.addressX = addressX;
	}

	public long getAddressY() {
		return addressY;
	}

	public void setAddressY(long addressY) {
		this.addressY = addressY;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
