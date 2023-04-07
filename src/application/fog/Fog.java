package application.fog;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class Fog {

	private ServerSocket socketServer;
	private MqttClient clientMqtt;
	private Socket clientSocket;
	private String client = "Fog";
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

	private void generateClientMqtt(String addressBroker, String client, MemoryPersistence persistence) {

		try {

			clientMqtt = new MqttClient(addressBroker, client, persistence);
			clientMqtt.connect();

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

		ThreadTcpClient threadTcpClient = new ThreadTcpClient(socketClientTCP, clientMqtt);
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
	private void execFog(int portServerSocket, String adressBroker, double latitude, double longitude) throws IOException {

		boolean connection = true;

		generateSocketServer(portServerSocket);
		generateClientMqtt(adressBroker, client, persistence);

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

		Fog gateway = new Fog();
		gateway.execFog(8000, "tcp:127.0.0.1:8000",0.0,50.0);

	}

}
