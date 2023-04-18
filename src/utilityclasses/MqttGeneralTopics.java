package utilityclasses;

public enum MqttGeneralTopics {

	MQTT_STATION("station/"), MQTT_FOG("fog/");

	public String topic;

	private MqttGeneralTopics(String topic) {

		this.topic = topic;

	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

}
