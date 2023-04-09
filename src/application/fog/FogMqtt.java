package application.fog;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class FogMqtt implements MqttCallback, Runnable {
    private MqttClient clientMqtt;
    private static String clientTopic = "postoDisponiveis";
    private MemoryPersistence persistence;

    public FogMqtt(String brokerUrl, String client) throws MqttException {
        this.clientMqtt = new MqttClient(brokerUrl, MqttClient.generateClientId(), new MemoryPersistence());
        this.clientMqtt.setCallback(this);
        this.clientMqtt.connect();
        this.clientMqtt.subscribe(this.clientTopic);
    }

    @Override
    public void connectionLost(Throwable throwable) {
        System.out.println("Conexão perdida com o broker MQTT!");
        throwable.printStackTrace();
    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        System.out.println("Mensagem: " + topic);
        System.out.println("Payload: " + new String(mqttMessage.getPayload()));
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        // TODO Auto-generated method stub
    }

    @Override
    public void run() {
        try {
            while (true) {
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
