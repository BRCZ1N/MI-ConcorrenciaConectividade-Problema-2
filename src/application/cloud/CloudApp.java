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
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
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

	/**
	 * Construtor padrão da classe. Inicializa as variáveis executor, mqttMessage,
	 * mqttOptions e idClientMqtt.
	 */
	public CloudApp() {

			this.executor = Executors.newScheduledThreadPool(2);
			this.mqttMessage = configureMessageMqtt(MqttQoS.QoS_2.getQos());
			this.mqttOptions = configureConnectionOptionsMqtt();
			this.idClientMqtt = "CLOUD-" + UUID.randomUUID().toString();

		}
	/**
	 * Método principal da aplicação. Executa a aplicação Spring e o gateway de
	 * comunicação.
	 *
	 * @param args argumentos de entrada do programa
	 * @throws IOException          em caso de erro de entrada e saída
	 * @throws InterruptedException se a thread for interrompida
	 */
	public static void main(String[] args) throws IOException, InterruptedException {

		CloudApp cloud = new CloudApp();
		cloud.execCloud(ServerConfig.LARSID_3.getAddress());

	}
	/**
	 * Método responsável por executar o gateway de comunicação.
	 *
	 * @param addressBroker o endereço do broker MQTT
	 * @throws IOException          em caso de erro de entrada e saída
	 * @throws InterruptedException se a thread for interrompida
	 */

	private void execCloud(String addressBroker) throws IOException, InterruptedException {

		generateThreads();

	}
	/**
	 * 
	 * Inscreve o cliente MQTT em um tópico específico do broker MQTT para receber
	 * mensagens.
	 * 
	 * @throws MqttException caso ocorra um erro na subscrição do cliente MQTT.
	 */
	public void inscribeTopics() throws MqttException {

		clientMqtt.subscribe(MqttGeneralTopics.MQTT_FOG.getTopic() + "#");

	}
	
	public void publishTopics() throws MqttPersistenceException, MqttException {

		clientMqtt.publish(MqttGeneralTopics.MQTT_CLOUD.getTopic(), new MqttMessage(new byte[0]));
		
	}


	/**
	 * 
	 * Gera as threads que irão configurar e executar o cliente MQTT e publicar
	 * mensagens em tópicos específicos.
	 */
	private void generateThreads() {

		executor.scheduleAtFixedRate(() -> configureAndExecClientMqtt(ServerConfig.LARSID_3.getAddress(), idClientMqtt, mqttOptions),0, 10, TimeUnit.SECONDS);
		executor.scheduleAtFixedRate(() -> publishMessageMqtt(MqttGeneralTopics.MQTT_CLOUD.getTopic() + idClientMqtt), 0, 5, TimeUnit.SECONDS);

	}
	/**
	 * 
	 * Gera o callback do cliente MQTT, que será chamado quando uma mensagem for
	 * recebida.
	 */
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
	/**
	 * 
	 * Publica uma mensagem MQTT em um tópico específico do broker MQTT.
	 * 
	 * @param topico o tópico MQTT onde a mensagem será publicada.
	 */
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
	/**
	 * 
	 * Configura a mensagem MQTT com o nível de qualidade de serviço especificado.
	 * 
	 * @param qos o nível de qualidade de serviço da mensagem MQTT
	 * 
	 * @return a mensagem MQTT configurada
	 */
	public MqttMessage configureMessageMqtt(int qos) {

		MqttMessage mqttMessage = new MqttMessage();
		mqttMessage.setQos(qos);

		return mqttMessage;

	}
	/**
	 * 
	 * Configura as opções de conexão MQTT para limpar sessões anteriores.
	 * 
	 * @return as opções de conexão MQTT configuradas
	 */
	public MqttConnectOptions configureConnectionOptionsMqtt() {

		MqttConnectOptions options = new MqttConnectOptions();
		options.setCleanSession(true);

		return options;

	}
	/**
	 * 
	 * Configura e executa um cliente MQTT com o broker, o identificador do cliente
	 * e as opções de conexão especificados,
	 * 
	 * inscrevendo-o em tópicos relevantes e configurando um callback para tratar
	 * mensagens recebidas.
	 * 
	 * @param broker      o endereço do broker MQTT
	 * 
	 * @param idFog       o identificador do cliente MQTT
	 * 
	 * @param mqttOptions as opções de conexão MQTT a serem utilizadas
	 */
	public void configureAndExecClientMqtt(String broker, String idFog, MqttConnectOptions mqttOptions) {

		if (clientMqtt == null || !clientMqtt.isConnected()) {

			try {

				clientMqtt = new MqttClient(broker, idFog, new MemoryPersistence());
				clientMqtt.connect(mqttOptions);
				inscribeTopics();
				publishTopics();
				generateCallBackMqttClient();

			} catch (MqttException e) {

				System.out.println("=====================");
				System.out.println("Broker nao encontrado");
				System.out.println("=====================");

			}
		}

	}


	/*
	 * Desconecta o cliente MQTT, encerrando a conexão com o broker.
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
