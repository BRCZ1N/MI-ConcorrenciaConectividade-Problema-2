package application.gateway;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class Gateway {

	private ServerSocket socketServer;
	private MqttClient clientMqtt;
	private Socket clientSocket;
	private String clientCar = "Gateway";
	private String topicA = "sendRequisition";
	private String topicB = "catchResponse";
	private MemoryPersistence persistence = new MemoryPersistence();

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

	private void generateClientMqtt(String addressBroker, String clientCar, MemoryPersistence persistence,
			String topicA, String topicB) {

		try {
			// Cria um novo cliente MQTT
			clientMqtt = new MqttClient(addressBroker, clientCar, persistence);
			clientMqtt.connect();

			clientMqtt.subscribe(topicA);
			clientMqtt.subscribe(topicB);

		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Esse é o método que gera e inicia uma Thread para clientes TCP da aplicação
	 * servidor
	 *
	 * @param socketClientTCP - Socket do cliente TCP
	 */
	private void generateAndStartThreadClientTCP(Socket socketClientTCP, MqttClient clientMqtt) {

		ThreadTcpClient threadTcpClient = new ThreadTcpClient(socketClientTCP, clientMqtt, topicA, topicB);
		new Thread(threadTcpClient).start();

	}

	/**
	 * Esse é o método que executa o servidor desde as proprias threads do servidor
	 * até as proprios sockets UDP e TCP
	 * 
	 * @param portServerSocket   - Porta TCP para o servidor
	 * @param portDatagramSocket - Porta UDP para o servidor
	 * @throws IOException
	 */
	private void execServer(int portServerSocket, String adressBroker) throws IOException {

		boolean connection = true;

		generateSocketServer(portServerSocket);
		generateClientMqtt(adressBroker, clientCar, persistence, topicA, topicB);

		while (connection) {

			clientSocket = socketServer.accept();
			System.out.println("Cliente conectado a partir da porta:" + clientSocket.getPort());
			generateAndStartThreadClientTCP(clientSocket, clientMqtt);

		}

		socketServer.close();

	}

	/**
	 * Este é o metodo principal dessa aplicação que inicia a mesma. Ele recebe um
	 * array de argumentos de linha de comando como entrada.
	 *
	 * @param args - O array de argumentos de linhas de comando.
	 * @throws IOException Erro de entrada e saida
	 */
	public static void main(String[] args) throws IOException {

		Gateway gateway = new Gateway();
		gateway.execServer(8000, "tcp:127.0.0.1:8000");

	}

}
