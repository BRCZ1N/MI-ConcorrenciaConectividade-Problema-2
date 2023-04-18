package utilityclasses;

public enum ServerConfig {
	
	Norte_LOCALHOST("tcp://localhost:8100"),
	Oeste_LARSID_3("tcp://localhost:8100"),
	Leste_LARSID_4("tcp://localhost:8100"),
	Sul_LARSID_5("tcp://localhost:8100");
	
	private String address;

	private ServerConfig(String address) {
		
		this.address = address;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
