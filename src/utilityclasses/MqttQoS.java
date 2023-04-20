package utilityclasses;

public enum MqttQoS {

	QoS_0(0), QoS_1(1), QoS_2(2);

	private int qos;
	/**
	 * Construtor do enum MqttQoS.
	 * 
	 * @param qos o nível de QoS a ser definido.
	 */
	private MqttQoS(int qos) {

		this.qos = qos;
		
	}
	/**
	 * Retorna o nível de QoS.
	 * 
	 * @return o nível de QoS.
	 */
	public int getQos() {
		return qos;
	}
	/**
	 * Define o nível de QoS.
	 * 
	 * @param qos o nível de QoS a ser definido.
	 */
	public void setQos(int qos) {
		this.qos = qos;
	}

}
