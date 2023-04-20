package utilityclasses;

public enum ServerConfig {
	
	
	HTTP_LOCALHOST("http://localhost:8000"),
	lARSID_2("tcp://localhost:8100"),
	LARSID_3("tcp://localhost:8100"),
	LARSID_4("tcp://localhost:8100"),
	LARSID_5("tcp://localhost:8100");
	
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
