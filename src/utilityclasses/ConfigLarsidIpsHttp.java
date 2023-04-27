package utilityclasses;

public enum ConfigLarsidIpsHttp {
	
	HTTP_FOG_REGION_Q1("http://172.16.103.4:8000"),
	HTTP_FOG_REGION_Q2("http://172.16.103.5:8000"),
	HTTP_FOG_REGION_Q3("http://172.16.103.6:8000"),
	HTTP_FOG_REGION_Q4("http://172.16.103.7:8000");
	
	private String address;
	 /**
     * Construtor do enum ServerConfig.
     * @param address o endereço do servidor
     */
	private ConfigLarsidIpsHttp(String address) {
		
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
