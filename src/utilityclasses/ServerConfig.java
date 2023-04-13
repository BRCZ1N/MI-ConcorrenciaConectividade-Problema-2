package utilityclasses;

public enum ServerConfig {
	
	LOCALHOST("tcp://localhost:8100"),
	LARSID_3(""),
	LARSID_4(""),
	LARSID_5("");
	
	private String address;

	private ServerConfig(String string) {
		
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
