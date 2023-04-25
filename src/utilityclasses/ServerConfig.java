package utilityclasses;

public enum ServerConfig {
	
	HTTP_FOG("http://192.168.56.1:8000"),
	REGIONAL_BROKER("tcp://192.168.56.1:8100"),
	GLOBAL_BROKER("tcp://172.16.103.3:8200");
	
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
