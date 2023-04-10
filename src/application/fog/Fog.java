package application.fog;

import java.io.IOException;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@SpringBootApplication
@ComponentScan("application")
@Configuration
@EnableConfigurationProperties
@Component
public class Fog {

	private MqttClient clientMqtt;
	private String client = "Fog";
	private MemoryPersistence persistence;

	private void generateClientMqtt(String addressBroker, String client, MemoryPersistence persistence) {

		try {

			clientMqtt = new MqttClient(addressBroker, client, persistence);

		} catch (MqttException e) {

			e.printStackTrace();

		}
	}

	private void connectMqtt() throws InterruptedException {

		MqttConnectOptions connOpts = new MqttConnectOptions();
		connOpts.setCleanSession(true);
		boolean notConnected = true;

		while (notConnected) {

			try {

				clientMqtt.connect(connOpts);
				notConnected = false;

			} catch (MqttException e) {

				System.out.println("Broker nao encontrado");

			}

			Thread.sleep(1000);

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
	private void execFog(String adressBroker) throws IOException, InterruptedException {

		generateClientMqtt(adressBroker, client, persistence);
		connectMqtt();

	}

	/**
	 * Este é o metodo principal dessa aplicação que inicia a mesma. Ele recebe um
	 * array de argumentos de linha de comando como entrada.
	 *
	 * @param args - O array de argumentos de linhas de comando.
	 * @throws IOException Erro de entrada e saida
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws IOException, InterruptedException {

		SpringApplication.run(Fog.class, args);
		Fog gateway = new Fog();
		gateway.execFog("tcp://localhost:8100");

	}

}
