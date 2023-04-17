package application.fog;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import application.controllers.ChargingStationController;
import application.model.ChargingStationModel;
import application.services.ChargingStationService;
import utilityclasses.MqttQoS;
import utilityclasses.ServerConfig;

@SpringBootApplication
@ComponentScan("application")
@Configuration
@EnableConfigurationProperties
@Component
public class Fog {

	@Autowired
	private ChargingStationController controller;
	@Autowired
	private ChargingStationService service;
	private ScheduledExecutorService executor;
	private ChargingStationModel bestChargingStation;
	private String message;
	private MqttClient clientMqtt;
	private String idClientMqtt = "FOG" + UUID.randomUUID().toString();
	private MqttMessage mqttMessage;
	private MqttConnectOptions mqttOptions;

	public Fog() {

		this.executor = Executors.newScheduledThreadPool(2);
		this.mqttMessage = configureMessageMqtt(MqttQoS.QoS_2.getQos());
		this.mqttOptions = configureConnectionOptionsMqtt();
		this.idClientMqtt = "FOG" + UUID.randomUUID().toString();

	}

	public static void main(String[] args) throws IOException, InterruptedException {

		SpringApplication.run(Fog.class, args);
		Fog gateway = new Fog();
		gateway.execFog(ServerConfig.Norte_LOCALHOST.getAddress());

	}

	private void execFog(String addressBroker) throws IOException, InterruptedException {

		configureAndExecClientMqtt(addressBroker, idClientMqtt, mqttOptions);
		generateThreads();

	}

	private void generateThreads() {

		executor.scheduleAtFixedRate(() -> refreshStatusStation(), 0, 5, TimeUnit.SECONDS);
		executor.scheduleAtFixedRate(() -> refreshMessage(), 0, 5, TimeUnit.SECONDS);
		executor.scheduleAtFixedRate(() -> publishMessageMqtt(message), 0, 5, TimeUnit.SECONDS);

	}

	private void refreshMessage() {

		while (clientMqtt.isConnected()) {

			message = new JSONObject(bestChargingStation).toString();

		}

	}

	private void refreshStatusStation() {

		while (clientMqtt.isConnected()) {

		}

	}

	public void publishMessageMqtt(String topic) {

		while (clientMqtt.isConnected()) {

			if (!message.isEmpty()) {

				try {

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

	public void configureAndExecClientMqtt(String broker, String nameFog, MqttConnectOptions mqttOptions) {

		boolean connected = false;

		while (!connected) {

			try {

				clientMqtt = new MqttClient(broker, nameFog, new MemoryPersistence());

				clientMqtt.connect(mqttOptions);
				connected = true;

			} catch (MqttException e) {

				System.out.println("Broker nao encontrado");

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
