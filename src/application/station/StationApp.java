package application.station;
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

public class StationApp {

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

	public StationApp() {

		this.executor = Executors.newScheduledThreadPool(2);
		this.mqttMessage = configureMessageMqtt(MqttQoS.QoS_2.getQos());
		this.mqttOptions = configureConnectionOptionsMqtt();
		this.idClientMqtt = "STA-" + UUID.randomUUID().toString();

	}

	public void queueRefresh() {
		
		amountCars = (int) (Math.random()*30);
		
	}

	public static void main(String[] args) {

		StationApp station = new StationApp();
		station.execStation();

	}

	/**
	 * Método responsável por iniciar a execução da estação, realizando as
	 * configurações iniciais e gerando as threads para execução do cliente MQTT e
	 * publicação de mensagens MQTT.
	 */
	public void execStation() {

		initialConfigurationStation();
		generateThreads();

	}

	/**
	 * Método responsável por gerar as threads de execução para o cliente MQTT e
	 * publicação de mensagens MQTT.
	 */
	private void generateThreads() {

		executor.scheduleAtFixedRate(() -> configureAndExecClientMqtt(ServerConfig.REGIONAL_BROKER.getAddress(),currentStatusStation.getName(), mqttOptions), 0, 5, TimeUnit.SECONDS);
		executor.scheduleAtFixedRate(() -> queueRefresh(), 0, 15, TimeUnit.SECONDS);
		executor.scheduleAtFixedRate(() -> publishMessageMqtt(MqttGeneralTopics.MQTT_STATION.getTopic() + idClientMqtt),0, 5, TimeUnit.SECONDS);

	}

	/**
	 * Método responsável por publicar uma mensagem MQTT com as informações do
	 * status atual da estação em um tópico MQTT.
	 * 
	 * @param topic o tópico MQTT em que a mensagem será publicada
	 */
	public void publishMessageMqtt(String topic) {

		if (clientMqtt != null && clientMqtt.isConnected()) {

			try {

				currentStatusStation.setTotalAmountCars(amountCars);
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

	/**
	 * 
	 * Método responsável por configurar a mensagem MQTT com o QoS especificado.
	 * 
	 * @param qos o QoS a ser configurado na mensagem
	 * 
	 * @return a mensagem MQTT configurada
	 */
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

	/**
	 * 
	 * Método responsável por configurar as opções de conexão e conectar o MQTT.
	 * 
	 * @return as opções de conexão MQTT configuradas
	 */
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

	/**
	 * 
	 * Método responsável por configurar a estação de carregamento no momento de sua
	 * inicialização.
	 */
	public void initialConfigurationStation() {

		generatePosStation();

		System.out.println("Digite o nome do posto:");
		String name = scanner.nextLine();
		System.out.println();

		currentStatusStation = new ChargingStationModel(name, latitudeStation, longitudeStation, amountCars, idClientMqtt);
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

	/**
	 * responsável por gerar a posição aleatória de uma estação de carregamento de
	 * veículos elétricos
	 */
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
