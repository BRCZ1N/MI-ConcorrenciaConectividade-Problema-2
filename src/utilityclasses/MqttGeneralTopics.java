package utilityclasses;


public enum MqttGeneralTopics {

	/**
	 * 
	 * Tópico utilizado para comunicação com as estações IoT.
	 */
	MQTT_STATION("station/"),
	/**
	 * 
	 * Tópico utilizado para comunicação com os nós FOG.
	 */
	MQTT_FOG("fog/");

	/**
	 * 
	 * Tópico MQTT.
	 */
	public String topic;

	/**
	 * 
	 * Construtor da enumeração MqttGeneralTopics.
	 * 
	 * @param topic tópico MQTT.
	 */
	private MqttGeneralTopics(String topic) {
		this.topic = topic;
	}

	/**
	 * 
	 * Retorna o tópico MQTT.
	 * 
	 * @return o tópico MQTT.
	 */
	public String getTopic() {
		return topic;
	}

	/**
	 * 
	 * Define o tópico MQTT.
	 * 
	 * @param topic o tópico MQTT a ser definido.
	 */
	public void setTopic(String topic) {
		this.topic = topic;
	}
}
