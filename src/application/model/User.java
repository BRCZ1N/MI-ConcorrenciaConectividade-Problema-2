package application.model;

public class User {

	private String id;
	private String name;
	private String location;

	public User(String name, String location) {

		this.name = name;
		this.location = location;

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

}
