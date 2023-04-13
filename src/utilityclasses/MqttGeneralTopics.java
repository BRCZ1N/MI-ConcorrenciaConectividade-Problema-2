package utilityclasses;

public enum MqttGeneralTopics {

	MQTT_STATION("/station"), MQTT_FOG("/fog");

	public String topic;

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	private MqttGeneralTopics(String string) {

	}

}
