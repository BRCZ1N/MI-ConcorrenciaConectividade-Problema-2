package application.fog;

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
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import application.controllers.ChargingStationController;
import application.model.ChargingStationModel;
import application.model.FogModel;
import application.services.ChargingStationService;
import application.services.FogService;
import utilityclasses.MqttGeneralTopics;
import utilityclasses.MqttQoS;
import utilityclasses.ServerConfig;

@SpringBootApplication
@EnableConfigurationProperties
@Configuration
@EnableAutoConfiguration
@ComponentScan("application.controllers")
@ComponentScan("application.services")
@Component
public class FogApp {

	private ScheduledExecutorService executor;
	private MqttClient clientMqtt = null;
	private String idClientMqtt;
	private MqttMessage mqttMessage;
	private MqttConnectOptions mqttOptions;

	/**
	 * Construtor padrão da classe. Inicializa as variáveis executor, mqttMessage,
	 * mqttOptions e idClientMqtt.
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	public FogApp() throws IOException, InterruptedException {

		this.executor = Executors.newScheduledThreadPool(2);
		this.mqttMessage = configureMessageMqtt(MqttQoS.QoS_2.getQos());
		this.mqttOptions = configureConnectionOptionsMqtt();
		this.idClientMqtt = "FOG-" + UUID.randomUUID().toString();
		execFog();

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

		SpringApplication.run(FogApp.class, args);

	}

	/**
	 * Método responsável por executar o gateway de comunicação.
	 *
	 * @param addressBroker o endereço do broker MQTT
	 * @throws IOException          em caso de erro de entrada e saída
	 * @throws InterruptedException se a thread for interrompida
	 */
	private void execFog() throws IOException, InterruptedException {

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

		clientMqtt.subscribe(MqttGeneralTopics.MQTT_STATION.getTopic() + "#",MqttQoS.QoS_2.getQos());
		clientMqtt.subscribe(MqttGeneralTopics.MQTT_CLOUD.getTopic() + "#",MqttQoS.QoS_2.getQos());

	}
	

	/**
	 * 
	 * Gera as threads que irão configurar e executar o cliente MQTT e publicar
	 * mensagens em tópicos específicos.
	 */
	private void generateThreads() {

		executor.scheduleAtFixedRate(() -> configureAndExecClientMqtt(ServerConfig.LOCAL_FOG.getAddress(), idClientMqtt, mqttOptions), 0, 10,TimeUnit.SECONDS);
		executor.scheduleAtFixedRate(() -> publishMessageMqtt(MqttGeneralTopics.MQTT_FOG.getTopic() + idClientMqtt), 0,5, TimeUnit.SECONDS);

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
				if (topic.contains("station/")) {

					ChargingStationController.addStation(ChargingStationModel.JsonToChargingStationModel(payload));

				}else {
					
					FogService.addFog(FogModel.JsonToFogModel(payload));
					
				}

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

		if (clientMqtt != null && clientMqtt.isConnected()) {

			if(ChargingStationService.getShorterQueueStation().isPresent()) {
				
				try {

					String message = new JSONObject(ChargingStationService.getShorterQueueStation().get()).toString();
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
