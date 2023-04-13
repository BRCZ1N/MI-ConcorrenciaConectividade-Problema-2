package utilityclasses;

public enum MqttQoS {

	QoS_0(0), QoS_1(1), QoS_2(2);

	private int qos;

	private MqttQoS(int i) {

	}

	public int getQos() {
		return qos;
	}

	public void setQos(int qos) {
		this.qos = qos;
	}

}
