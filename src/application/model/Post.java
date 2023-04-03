package application.model;

public class Post {

	private String name;
	private String address;
	private int totalAmountCars;
	private String id;
	private String password;

	public Post(String name, String address, int totalAmountCars, String id, String password) {

		this.name = name;
		this.address = address;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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
