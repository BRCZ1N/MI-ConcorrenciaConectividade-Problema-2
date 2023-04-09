package application.post;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class ThreadEnvioPosto extends Thread {

    private String broker;
    private String topic;
    private String message;
    private int qos;

    public ThreadEnvioPosto(String broker, String topic, String message, int qos) {
        this.broker = broker;
        this.topic = topic;
        this.message = message;
        this.qos = qos;
    }

    @Override
    public void run() {

        String clientId = MqttClient.generateClientId();
        MemoryPersistence persistence = new MemoryPersistence();

        try {
            MqttClient client = new MqttClient(broker, clientId, persistence);

            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);

            client.connect(options);

            MqttMessage mqttMessage = new MqttMessage(message.getBytes());
            mqttMessage.setQos(qos);

            client.publish(topic, mqttMessage);

            client.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

}
