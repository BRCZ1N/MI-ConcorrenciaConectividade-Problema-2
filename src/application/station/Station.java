package application.station;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;
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

import application.model.ChargingStationModel;
import utilityclasses.MqttGeneralTopics;
import utilityclasses.MqttQoS;
import utilityclasses.ServerConfig;

public class Station {

	private ScheduledExecutorService executor;
	private MqttMessage mqttMessage;
	private MqttConnectOptions mqttOptions;
	private ChargingStationModel currentStatusStation;
	private MqttClient clientMqtt;
	private Scanner scanner = new Scanner(System.in);
	private String message;

	/**
	 * Metodo principal da classe UserEnergyGaugeThread, esta classe ira fazer a
	 * conexão do medidor com o sistema e permitira a passagem das medições para o
	 * servidor por meio das threads
	 *
	 * @param args - argumentos de linha de comando (não utilizados)
	 * @throws InterruptedException se a thread for interrompida enquanto estiver
	 *                              dormindo
	 * @throws IOException          se ocorrer um erro de entrada/saída
	 */

	public Station() {

		this.executor = Executors.newScheduledThreadPool(3);
		this.mqttMessage = configureMessageMqtt(MqttQoS.QoS_2.getQos());
		this.mqttOptions = configureConnectionOptionsMqtt();

	}

	public static void main(String[] args) {

		Station station = new Station();
		station.execStation();

	}

	public void execStation() {

		configureStation();
		configureAndExecClientMqtt(ServerConfig.Norte_LOCALHOST.getAddress() + ":8100", currentStatusStation.getName(),
				mqttOptions);
		generateThreads();

	}

	private void generateThreads() {

		executor.scheduleAtFixedRate(() -> refreshStatusStation(), 0, 5, TimeUnit.SECONDS);
		executor.scheduleAtFixedRate(() -> refreshMessage(), 0, 5, TimeUnit.SECONDS);
		executor.scheduleAtFixedRate(
				() -> publishMessageMqtt(MqttGeneralTopics.MQTT_STATION.getTopic() + currentStatusStation), 0, 5,
				TimeUnit.SECONDS);

	}

	private void refreshMessage() {

		while (clientMqtt.isConnected()) {

			message = new JSONObject(currentStatusStation).toString();

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

	public void configureAndExecClientMqtt(String broker, String nameStation, MqttConnectOptions mqttOptions) {

		boolean connected = false;

		while (!connected) {

			try {

				clientMqtt = new MqttClient(broker, nameStation, new MemoryPersistence());

				clientMqtt.connect(mqttOptions);
				connected = true;

			} catch (MqttException e) {

				System.out.println("Broker nao encontrado");

			}
		}

	}
	
	public void configureStation() {

		boolean repeatRegistration;
		String name = null;
		Double addressX = null;
		Double addressY = null;
		int totalAmountCars = 0;

		do {

			repeatRegistration = false;

			try {

				System.out.println("Digite o nome do posto:");
				name = scanner.nextLine();

				System.out.println("Digite a latitude do posto:");
				addressX = scanner.nextDouble();

				System.out.println("Digite a longitude do posto:");
				addressY = scanner.nextDouble();

			} catch (NumberFormatException e) {

				System.out.print("Algumas informações podem ter sido digitadas erradas, digite novamente");
				repeatRegistration = true;

			}

		} while (repeatRegistration);

		currentStatusStation = new ChargingStationModel(name, addressX, addressY, totalAmountCars,
				"STA" + UUID.randomUUID().toString());

	}

	public void desconnectMqtt() {

		try {

			clientMqtt.disconnect();

		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

	}

}