package application.fog;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

//import java.net.ServerSocket;
//import java.net.Socket;
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

	private ServerSocket socketServer;
	private MqttClient clientMqtt;
	private Socket clientSocket;
	private String client = "Fog";
	private MemoryPersistence persistence;

	/**
	 * Esse é o método que instancia sockets para o servidor para conexões TCP para
	 * clientes http e conexões UDP para os medidores, para isso ele recebe como
	 * parametros uma porta para o socket TCP e uma para o socket UDP
	 *
	 * @param portServerSocket   - Porta do servidor TCP
	 * @param portDatagramSocket - Porta do servidor UDP
	 * @throws IOException
	 */
	private void generateSocketServer(int portServerSocket) throws IOException {

		socketServer = new ServerSocket(portServerSocket);

	}

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
	 * Esse é o método que gera e inicia uma Thread para clientes TCP da aplicação
	 * servidor
	 *
	 * @param socketClientTCP - Socket do cliente TCP
	 */
//	private void generateAndStartThreadClientTCP(Socket socketClientTCP) {
//
//		ThreadTcpClient threadTcpClient = new ThreadTcpClient(socketClientTCP);
//		new Thread(threadTcpClient).start();
//
//	}

	/**
	 * Esse é o método que executa o servidor desde as proprias threads do servidor
	 * até as proprios sockets UDP e TCP
	 *
	 * @param portServerSocket   - Porta TCP para o servidor
	 * @param portDatagramSocket - Porta UDP para o servidor
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	private void execFog(int portServerSocket, String adressBroker) throws IOException, InterruptedException {
//	private void execFog(String adressBroker) throws IOException, InterruptedException {

		generateSocketServer(portServerSocket);
//		generateClientMqtt(adressBroker, client, persistence);
//		connectMqtt();
//		System.out.println("Server listening on port: " + socketServer.getLocalPort());
//		boolean connection = true;
		
//		while (connection) {
//
//			clientSocket = socketServer.accept();
//			System.out.println("Cliente conectado a partir da porta:" + clientSocket.getPort());
//			generateAndStartThreadClientTCP(clientSocket);
//
//		}
//
//		socketServer.close();

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
		gateway.execFog(8010,"tcp://localhost:8100");

	}

}
