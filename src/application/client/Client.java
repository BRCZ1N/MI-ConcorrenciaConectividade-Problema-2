package application.client;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import application.fog.RequestHttp;

/**
 * Esta � a classe Client, que representa a aplica��o do cliente HTTP TCP que se
 * conecta ao servidor.
 * 
 * @author Bruno Campos de Oliveira Rocha
 * @version 1.0
 */
public class Client {

	private MqttClient clientMqtt;
	private Scanner scan = new Scanner(System.in);
	private String clientID;
	private String clientPassword;

	/**
	 * Este � o metodo principal dessa aplica��o que inicia a mesma. Ele recebe um
	 * array de argumentos de linha de comando como entrada.
	 *
	 * @param args - O array de argumentos de linhas de comando.
	 * @throws UnknownHostException Exception de conex�o de rede
	 * @throws IOException          Exception de entrada e saida
	 * @throws InterruptedException Exception de thread interrompido
	 */

	public static void main(String[] args) throws IOException, InterruptedException {

		String adress_broker = "tcp:127.0.0.1:8000";
		String clientCar = "Car";
		String topicServer = "postosDisponiveisServidor";
		String topicCar = "postosDisponiveisCarros";
		MemoryPersistence persistenceConnect = new MemoryPersistence();

		Client client = new Client();
		client.generateClientConnect(adress_broker, clientCar, persistenceConnect, topicServer, topicCar);
		client.clientExecution(topicServer, topicCar);
	}

	/**
	 * Esse � o m�todo que instancia um socket TCP para o cliente, para isso ele
	 * recebe como parametros o ip e a porta do servidor ao qual fica a aplica��o
	 * servidor.
	 *
	 * @param ip       - O ip do servidor.
	 * @param topicTwo
	 * @param topic
	 * @param port     - A porta do servidor.
	 * @return
	 */
	private void generateClientConnect(String addressBroker, String clientCar, MemoryPersistence persistence,
			String topicServer, String topicCar) {
		try {
			// Cria um novo cliente MQTT
			clientMqtt = new MqttClient(addressBroker , clientCar, persistence);
			clientMqtt.connect();

			clientMqtt.subscribe(topicServer);
			clientMqtt.subscribe(topicCar);

		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Esse � o metodo de execu��o do menu de login dessa aplica��o.
	 * 
	 * @param mqttClient
	 * @param topicTwo
	 * @param topic
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private void clientExecution(String topicServer, String topicCar) throws IOException, InterruptedException {
		
		String clientAuthentication = "";
		RequestHttp request;
		ResponseHttp resp;

		do {

			System.out.println("===================================================");
			System.out.println("========= Sistema de Postos de recarga  ==========");
			System.out.println("===================================================");
			System.out.println("Digite o id do automovel:");
			clientID = scan.next();

			Map<String, String> mapHeaders = new HashMap<>();
			mapHeaders.put("Host", clientSocket.getLocalAddress().getHostAddress() + ":" + clientSocket.getLocalPort());
			request = new RequestHttp(HttpMethods.GET, "/user/auth/id:" + clientID.replace(" ", ""), "HTTP/1.1",
					mapHeaders);
			clientMqtt.publish(topicCar, new MqttMessage(request.getBytes()));

		} while (!resp.getStatusLine().equals(HttpCodes.HTTP_200.getCodeHttp()));

		clientMenu();

	}

	/**
	 * Esse � o metodo de execu��o do menu de cliente dessa aplica��o.
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private void clientMenu() throws IOException, InterruptedException {

		boolean connection = true;

		while (connection) {

			System.out.println("===================================================");
			System.out.println("========= Consumo de energia inteligente ==========");
			System.out.println("===================================================");
			System.out.println("================ Menu de cliente ==================");
			System.out.println("===================================================");
			System.out.println("====== (1) - Nivel de carga de energia");
			System.out.println("====== (2) - Buscar postos disponiveis");
			System.out.println("====== (3) - Entrar na fila");
			System.out.println("====== (4) - Sair da fila");
			System.out.println("====== (5) - Desconectar");
			System.out.println("=========== Digite a opcao desejada ===============");
			// o alerta de nivel de energia devera esta aqui
			String opcao = scan.next();

			JSONObject jsonBody;
			Map<String, String> mapHeaders = new HashMap<>();

			switch (opcao) {

			case "1":
				// fazer uma classe para gerenciar o nivel de energia dos carros junto com o id
				// dele
			case "2":

				mapHeaders = new HashMap<>();
				mapHeaders.put("Host",
						clientSocket.getLocalAddress().getHostAddress() + ":" + clientSocket.getLocalPort());
				request = new RequestHttp(HttpMethods.GET, "/battery/nivel/" + clientID, "HTTP/1.1", mapHeaders);
				ProtocolHttp.sendMessage(clientSocket.getOutputStream(), request.toString());
				Thread.sleep(100);
				response = readResponse(clientSocket.getInputStream());

				if (response.getStatusLine().equals(HttpCodes.HTTP_200.getCodeHttp())) {

					System.out.println("================POSTOS DISPONIVEIS===============");
					jsonBody = new JSONObject(response.getBody());
					System.out.println("Idenficador do cliente: " + jsonBody.get("idClient"));
					JSONArray jsonArray = jsonBody.getJSONArray("postos");
					System.out.println("======================Postos=====================");

					if (!jsonArray.isEmpty()) {

						for (int i = 0; i < jsonArray.length(); i++) {

							JSONObject jsonObject = jsonArray.getJSONObject(i);
							System.out.println("Nome do posto:" + jsonObject.get("nomePosto"));
							System.out.println("Endereço:" + jsonObject.get("endereco"));
							System.out.println("Quantidade de carros na fila:" + jsonObject.get("quantidadeCarros"));
							System.out.println("===================================================");

						}

					} else {

						System.out.println("Postos indisponiveis");

					}
					System.out.println("===================================================");

				} else {

					System.out.println("ERRO:");
					System.out.println(response.getStatusLine());

				}

				System.out.println();
				break;

			case "3":

				System.out.println("Digite o posto que deseja ir:");

				mapHeaders = new HashMap<>();
				mapHeaders.put("Host",
						clientSocket.getLocalAddress().getHostAddress() + ":" + clientSocket.getLocalPort());
				request = new RequestHttp(HttpMethods.GET, "/queue/enter/" + clientID, "HTTP/1.1", mapHeaders);
				String topicRequisição = "Postos";
				client.publish(topic, new MqttMessage(message.getBytes()));
				ProtocolHttp.sendMessage(clientSocket.getOutputStream(), request.toString());
				Thread.sleep(100);
				response = readResponse(clientSocket.getInputStream());

				if (response.getStatusLine().equals(HttpCodes.HTTP_200.getCodeHttp())) {

					System.out.println("================POSTOS DISPONIVEIS===============");
					jsonBody = new JSONObject(response.getBody());
					System.out.println("Idenficador do cliente: " + jsonBody.get("idClient"));
					JSONArray jsonArray = jsonBody.getJSONArray("postos");
					System.out.println("======================Postos=====================");

					if (!jsonArray.isEmpty()) {

						for (int i = 0; i < jsonArray.length(); i++) {

							JSONObject jsonObject = jsonArray.getJSONObject(i);
							System.out.println("Nome do posto:" + jsonObject.get("nomePosto"));
							System.out.println("Endereço:" + jsonObject.get("endereco"));
							System.out.println("Quantidade de carros na fila:" + jsonObject.get("quantidadeCarros"));
							System.out.println("===================================================");

						}

					} else {

						System.out.println("Postos indisponiveis");

					}
					System.out.println("===================================================");

				} else {

					System.out.println("ERRO:");
					System.out.println(response.getStatusLine());

				}

				System.out.println();
				break;

			case "4":

			case "5":

				connection = false;
				break;

			default:

				System.out.println("Opção invalida");
				break;
			}
		}

		System.out.println("Sessão encerrada");

	}

	/**
	 * Esse � o metodo que vai ler a resposta http enviada pelo servidor.
	 * 
	 * @param input - O InputStream do socket que cont�m a resposta advinda do
	 *              servidor.
	 * @return A resposta http enviada pelo servidor formatada e colocada em um
	 *         objeto que a representa. * @throws UnknownHostException
	 * @throws IOException Erro de entrada e saida
	 */
	public ResponseHttp readResponse(InputStream input) throws IOException {

		ResponseHttp req = new ResponseHttp();
		Queue<String> httpData = new LinkedList<String>();
		String reqLine = null;
		String responseHeaders;
		Map<String, String> mapHeaders = null;
		StringBuilder str = new StringBuilder();
		String[] linesReq;

		BufferedInputStream buffer = new BufferedInputStream(input);

		if (buffer.available() > 0) {

			while (buffer.available() > 0) {

				str.append((char) buffer.read());

			}

			linesReq = str.toString().split("\r\n");

			for (String line : linesReq) {

				httpData.add(line);

			}

			responseHeaders = httpData.poll();
			mapHeaders = new HashMap<String, String>();

			while (!httpData.isEmpty() && !(reqLine = httpData.poll()).isBlank()) {

				String[] header = reqLine.split(":\s");
				mapHeaders.put(header[0], header[1]);

			}

			StringBuilder bodyJson = new StringBuilder();
			String bodyLine;

			while ((bodyLine = httpData.poll()) != null) {

				bodyJson.append(bodyLine);

			}

			req = new ResponseHttp(responseHeaders, mapHeaders, bodyJson.toString());

		}

		return req;
	}

}