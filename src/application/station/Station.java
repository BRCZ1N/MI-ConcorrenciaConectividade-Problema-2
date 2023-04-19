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

	private Double latitudeStation;
	private Double longitudeStation;
	private int amountCars = 0;
	private ScheduledExecutorService executor;
	private MqttMessage mqttMessage;
	private MqttConnectOptions mqttOptions;
	private ChargingStationModel currentStatusStation;
	private MqttClient clientMqtt = null;
	private Scanner scanner = new Scanner(System.in);
	private String message;
	private String idClientMqtt;

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

		this.executor = Executors.newScheduledThreadPool(2);
		this.mqttMessage = configureMessageMqtt(MqttQoS.QoS_2.getQos());
		this.mqttOptions = configureConnectionOptionsMqtt();
		this.idClientMqtt = "STA-" + UUID.randomUUID().toString();

	}

	public static void main(String[] args) {

		Station station = new Station();
		station.execStation();

	}

	public void execStation() {

		initialConfigurationStation();
		generateThreads();

	}

	private void generateThreads() {

		executor.scheduleAtFixedRate(() -> configureAndExecClientMqtt(ServerConfig.Norte_LOCALHOST.getAddress(),currentStatusStation.getName(), mqttOptions), 0, 5, TimeUnit.SECONDS);
		executor.scheduleAtFixedRate(() -> publishMessageMqtt(MqttGeneralTopics.MQTT_STATION.getTopic() + idClientMqtt),0, 5, TimeUnit.SECONDS);

	}

	public void publishMessageMqtt(String topic) {

		if (clientMqtt != null && clientMqtt.isConnected()) {

			try {

				message = new JSONObject(currentStatusStation).toString();
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

	public void configureAndExecClientMqtt(String broker, String nameStation, MqttConnectOptions mqttOptions) {
	
		if (clientMqtt == null || !clientMqtt.isConnected()) {
			
			try {

				clientMqtt = new MqttClient(broker, nameStation, new MemoryPersistence());
				clientMqtt.connect(mqttOptions);

			} catch (MqttException e) {

				System.out.println("=====================");
				System.out.println("Broker nao encontrado");
				System.out.println("=====================");

			}

		}

	}

	public void initialConfigurationStation() {

		generatePosStation();

		System.out.println("Digite o nome do posto:");
		String name = scanner.nextLine();
		System.out.println();

		currentStatusStation = new ChargingStationModel(name, latitudeStation, longitudeStation, amountCars,
				idClientMqtt);
		System.out.println("=====================================================");
		System.out.println("===============Status inicial do posto===============");
		System.out.println("=====================================================");
		System.out.println("Nome do posto: " + currentStatusStation.getName());
		System.out.println("ID do posto: " + currentStatusStation.getId());
		System.out.println("Latitude do posto: " + currentStatusStation.getLatitude());
		System.out.println("Longitude do posto: " + currentStatusStation.getLongitude());
		System.out.println("Quantidade inicial de carros no posto: " + currentStatusStation.getTotalAmountCars());
		System.out.println("=====================================================");

	}

	public void generatePosStation() {

		latitudeStation = Math.random() * 100;
		longitudeStation = Math.random() * 100;

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
