package application.cloud;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.JSONObject;
import application.model.FogModel;
import application.services.FogService;
import utilityclasses.MqttGeneralTopics;
import utilityclasses.MqttQoS;
import utilityclasses.ServerConfig;

public class CloudApp {

	private ScheduledExecutorService executor;
	private MqttClient clientMqtt = null;
	private String idClientMqtt;
	private MqttMessage mqttMessage;
	private MqttConnectOptions mqttOptions;

	public CloudApp() {

			this.executor = Executors.newScheduledThreadPool(2);
			this.mqttMessage = configureMessageMqtt(MqttQoS.QoS_2.getQos());
			this.mqttOptions = configureConnectionOptionsMqtt();
			this.idClientMqtt = "CLOUD-" + UUID.randomUUID().toString();

		}

	public static void main(String[] args) throws IOException, InterruptedException {

		CloudApp cloud = new CloudApp();
		cloud.execCloud(ServerConfig.lARSID_2.getAddress());

	}

	private void execCloud(String addressBroker) throws IOException, InterruptedException {

		generateThreads();

	}

	public void inscribeTopics() throws MqttException {

		clientMqtt.subscribe(MqttGeneralTopics.MQTT_FOG.getTopic() + "#");

	}

	private void generateThreads() {

		executor.scheduleAtFixedRate(() -> configureAndExecClientMqtt(ServerConfig.LARSID_3.getAddress(), idClientMqtt, mqttOptions),0, 10, TimeUnit.SECONDS);
		executor.scheduleAtFixedRate(() -> publishMessageMqtt(MqttGeneralTopics.MQTT_CLOUD.getTopic() + idClientMqtt), 0, 5, TimeUnit.SECONDS);

	}

	public void generateCallBackMqttClient() {

		clientMqtt.setCallback(new MqttCallback() {
			@Override
			public void connectionLost(Throwable cause) {
			}

			@Override
			public void messageArrived(String topic, MqttMessage message) {

				String payload = new String(message.getPayload());
				FogService.addFog(FogModel.JsonToFogModel(payload));

			}

			@Override
			public void deliveryComplete(IMqttDeliveryToken token) {
			}

		});

	}

	public void publishMessageMqtt(String topic) {

		if (clientMqtt != null && clientMqtt.isConnected() && !FogService.getAllStations().isEmpty()) {

			try {

				String message = new JSONObject(FogService.getAllStations().get()).toString();
				mqttMessage.setPayload(message.getBytes("UTF-8"));
				clientMqtt.publish(topic, mqttMessage);

			} catch (MqttException e) {

				// TODO Auto-generated catch block
				e.printStackTrace();

			} catch (UnsupportedEncodingException e) {

				// TODO Auto-generated catch block
				e.printStackTrace();

			}

		}

	}

	public MqttMessage configureMessageMqtt(int qos) {

		MqttMessage mqttMessage = new MqttMessage();
		mqttMessage.setQos(qos);

		return mqttMessage;

	}

	public MqttConnectOptions configureConnectionOptionsMqtt() {

		MqttConnectOptions options = new MqttConnectOptions();
		options.setCleanSession(true);

		return options;

	}

	public void configureAndExecClientMqtt(String broker, String idFog, MqttConnectOptions mqttOptions) {

		if (clientMqtt == null || !clientMqtt.isConnected()) {

			try {

				clientMqtt = new MqttClient(broker, idFog, new MemoryPersistence());
				clientMqtt.connect(mqttOptions);
				inscribeTopics();
				generateCallBackMqttClient();

			} catch (MqttException e) {

				System.out.println("=====================");
				System.out.println("Broker nao encontrado");
				System.out.println("=====================");

			}
		}

	}

	/**
	 * Esse é o método que executa o servidor desde as proprias threads do servidor
	 * até as proprios sockets UDP e TCP
	 *
	 * @param portServerSocket   - Porta TCP para o servidor
	 * @param portDatagramSocket - Porta UDP para o servidor
	 * @throws IOException
	 * @throws InterruptedException
	 */

	/**
	 * Este é o metodo principal dessa aplicação que inicia a mesma. Ele recebe um
	 * array de argumentos de linha de comando como entrada.
	 *
	 * @param args - O array de argumentos de linhas de comando.
	 * @throws IOException          Erro de entrada e saida
	 * @throws InterruptedException
	 */

	public void desconnectMqtt() {

		try {

			clientMqtt.disconnect();

		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

	}

}
