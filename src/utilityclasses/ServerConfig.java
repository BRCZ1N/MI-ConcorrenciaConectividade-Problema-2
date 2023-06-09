package utilityclasses;

public enum ServerConfig {
	
	REGIONAL_BROKER("tcp://172.16.103.7:8100"),
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
