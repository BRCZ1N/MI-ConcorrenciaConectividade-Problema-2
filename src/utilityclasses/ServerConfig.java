package utilityclasses;

public enum ServerConfig {
	
	
	HTTP_LOCALHOST("http://192.168.56.1:8000"),
	LARSID_2("tcp://192.168.56.1:8100"),
	LARSID_3("tcp://192.168.56.1:8200"),
	LARSID_4("tcp://192.168.56.1:8100"),
	LARSID_5("tcp://192.168.56.1:8100");
	
	private String address;
	 /**
     * Construtor do enum ServerConfig.
     * @param address o endereço do servidor
     */
	private ServerConfig(String address) {
		
		this.address = address;
	}

    /**
     * Retorna o endereço do servidor.
     * @return o endereço do servidor
     */
	public String getAddress() {
		return address;
	}

    /**
     * Define o endereço do servidor.
     * @param address o endereço do servidor
     */
	public void setAddress(String address) {
		this.address = address;
	}

}
