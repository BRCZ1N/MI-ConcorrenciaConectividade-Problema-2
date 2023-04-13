package utilityclasses;

public enum ServerConfig {
	
	Norte_LOCALHOST("tcp://localhost:8100"),
	Oeste_LARSID_3(""),
	Leste_LARSID_4(""),
	Sul_LARSID_5("");
	
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
