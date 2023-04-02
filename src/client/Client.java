package client;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;
import http.ProtocolHttp;
import http.RequestHttp;
import http.ResponseHttp;
import utilityclasses.HttpCodes;
import utilityclasses.HttpMethods;

/**
 * Esta � a classe Client, que representa a aplica��o do cliente HTTP TCP que se
 * conecta ao servidor.
 * 
 * @author Bruno Campos de Oliveira Rocha
 * @version 1.0
 */
public class Client {

	private Socket clientSocket;
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

	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {

		Client client = new Client();
		client.generateSocketClient("172.16.103.3", 8000);
		client.clientExecution();

	}

	/**
	 * Esse � o m�todo que instancia um socket TCP para o cliente, para isso ele
	 * recebe como parametros o ip e a porta do servidor ao qual fica a aplica��o
	 * servidor.
	 *
	 * @param ip   - O ip do servidor.
	 * @param port - A porta do servidor.
	 */
	private void generateSocketClient(String ip, int port) {

		boolean connected = false;
		while (!connected) {

			try {

				clientSocket = new Socket(ip, port);
				connected = true;

			} catch (UnknownHostException e) {

				e.printStackTrace();

			} catch (ConnectException e) {

				System.out.println("Server ainda n�o foi iniciado, espere um tempo");

				try {

					Thread.sleep(1000);

				} catch (InterruptedException ex) {

					ex.printStackTrace();

				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Esse � o metodo de execu��o do menu de login dessa aplica��o.
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private void clientExecution() throws IOException, InterruptedException {

		String clientAuthentication = "";
		RequestHttp request;
		ResponseHttp resp;

		do {

			System.out.println("===================================================");
			System.out.println("========= Consumo de energia inteligente ==========");
			System.out.println("===================================================");
			System.out.println("Digite o id:");
			clientID = scan.next();
			System.out.println("Digite a senha:");
			clientPassword = scan.next();

			Map<String, String> mapHeaders = new HashMap<>();
			mapHeaders.put("Host", clientSocket.getLocalAddress().getHostAddress() + ":" + clientSocket.getLocalPort());
			request = new RequestHttp(HttpMethods.GET,
					"/user/auth/id:" + clientID.replace(" ", "") + "&password:" + clientPassword.replace(" ", ""),
					"HTTP/1.1", mapHeaders);
			ProtocolHttp.sendMessage(clientSocket.getOutputStream(), request.toString());
			Thread.sleep(100);
			resp = readResponse(clientSocket.getInputStream());

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
			System.out.println("====== (1) - Visualizar historico de consumo");
			System.out.println("====== (2) - Gerar fatura");
			System.out.println("====== (3) - Visualizar fatura");
			System.out.println("====== (4) - Visualizar todas as faturas geradas");
			System.out.println("====== (5) - Status de consumo do cliente");
			System.out.println("====== (6) - Desconectar");
			System.out.println("=========== Digite a opcao desejada ===============");

			String opcao = scan.next();

			RequestHttp request;
			ResponseHttp response;
			JSONObject jsonBody;
			Map<String, String> mapHeaders = new HashMap<>();

			switch (opcao) {

				case "1":

					mapHeaders = new HashMap<>();
					mapHeaders.put("Host",
							clientSocket.getLocalAddress().getHostAddress() + ":" + clientSocket.getLocalPort());
					request = new RequestHttp(HttpMethods.GET, "/consumption/historic/" + clientID, "HTTP/1.1",
							mapHeaders);
					ProtocolHttp.sendMessage(clientSocket.getOutputStream(), request.toString());
					Thread.sleep(100);
					response = readResponse(clientSocket.getInputStream());

					if (response.getStatusLine().equals(HttpCodes.HTTP_200.getCodeHttp())) {

						System.out.println("================HISTORICO DE CONSUMO===============");
						jsonBody = new JSONObject(response.getBody());
						System.out.println("Historico do cliente:");
						System.out.println("Idenficador do cliente: " + jsonBody.get("idClient"));
						System.out.println("Consumo acumulado do cliente: " + jsonBody.get("accumulatedConsumption"));
						JSONArray jsonArray = jsonBody.getJSONArray("historic");
						System.out.println("======================CONSUMOS=====================");

						if (!jsonArray.isEmpty()) {

							for (int i = 0; i < jsonArray.length(); i++) {

								JSONObject jsonObject = jsonArray.getJSONObject(i);
								System.out.println("Data e hora do consumo:" + jsonObject.get("dateTime"));
								System.out.println("Quantidade de consumo:" + jsonObject.get("amount"));
								System.out.println("Unidade de medida do consumo:" + jsonObject.get("unitMeasurement"));
								System.out.println("===================================================");

							}

						} else {

							System.out.println("Lista de consumos do cliente est� vazia");

						}
						System.out.println("===================================================");

					} else {

						System.out.println("ERRO:");
						System.out.println(response.getStatusLine());

					}

					System.out.println();
					break;

				case "2":

					mapHeaders = new HashMap<>();
					mapHeaders.put("Host",
							clientSocket.getLocalAddress().getHostAddress() + ":" + clientSocket.getLocalPort());
					request = new RequestHttp(HttpMethods.GET, "/invoice/newInvoice/" + clientID, "HTTP/1.1",
							mapHeaders);
					ProtocolHttp.sendMessage(clientSocket.getOutputStream(), request.toString());
					Thread.sleep(100);
					response = readResponse(clientSocket.getInputStream());

					if (response.getStatusLine().equals(HttpCodes.HTTP_201.getCodeHttp())) {

						jsonBody = new JSONObject(response.getBody());
						System.out.println(
								"=============================FATURA GERADA====================================");
						System.out.println("Identificador do cliente:" + jsonBody.get("idClient"));
						System.out.println(
								"Status de consumo atual do cliente:" + jsonBody.get("currentStatusConsumption"));
						System.out.println("Nome do cliente:" + jsonBody.get("nameClient"));
						System.out.println("Consumo total da fatura:" + jsonBody.get("consumption"));
						System.out.println("Valor da fatura:" + jsonBody.get("invoiceValue"));
						System.out.println("Tarifa da fatura:" + jsonBody.get("fare"));
						System.out.println("Data de gera��o da fatura:" + jsonBody.get("issuanceDate"));
						System.out.println("Data em que a fatura expira:" + jsonBody.get("expirationDate"));
						System.out.println(
								"==============================================================================");

					} else {

						System.out.println("ERRO:");
						System.out.println(response.getStatusLine());

					}
					System.out.println();
					break;

				case "3":

					System.out.println("Digite o id da fatura:");
					String idInvoice = scan.next();
					mapHeaders = new HashMap<>();
					mapHeaders.put("Host",
							clientSocket.getLocalAddress().getHostAddress() + ":" + clientSocket.getLocalPort());
					request = new RequestHttp(HttpMethods.GET, "/invoice/" + idInvoice, "HTTP/1.1", mapHeaders);
					ProtocolHttp.sendMessage(clientSocket.getOutputStream(), request.toString());
					Thread.sleep(100);
					response = readResponse(clientSocket.getInputStream());

					if (response.getStatusLine().equals(HttpCodes.HTTP_200.getCodeHttp())) {

						jsonBody = new JSONObject(response.getBody());
						System.out.println(
								"====================================FATURA===================================");
						System.out.println("Identificador do cliente:" + jsonBody.get("idClient"));
						System.out.println(
								"Status de consumo atual do cliente:" + jsonBody.get("currentStatusConsumption"));
						System.out.println("Nome do cliente:" + jsonBody.get("nameClient"));
						System.out.println("Consumo total da fatura:" + jsonBody.get("consumption"));
						System.out.println("Valor da fatura:" + jsonBody.get("invoiceValue"));
						System.out.println("Tarifa da fatura:" + jsonBody.get("fare"));
						System.out.println("Data de gera��o da fatura:" + jsonBody.get("issuanceDate"));
						System.out.println("Data em que a fatura expira:" + jsonBody.get("expirationDate"));
						System.out.println(
								"==============================================================================");

					} else {

						System.out.println("ERRO:");
						System.out.println(response.getStatusLine());

					}
					System.out.println();
					break;

				case "4":

					mapHeaders = new HashMap<>();
					mapHeaders.put("Host",
							clientSocket.getLocalAddress().getHostAddress() + ":" + clientSocket.getLocalPort());
					request = new RequestHttp(HttpMethods.GET, "/invoice/all/" + clientID, "HTTP/1.1", mapHeaders);
					ProtocolHttp.sendMessage(clientSocket.getOutputStream(), request.toString());
					Thread.sleep(100);
					response = readResponse(clientSocket.getInputStream());

					if (response.getStatusLine().equals(HttpCodes.HTTP_200.getCodeHttp())) {

						jsonBody = new JSONObject(response.getBody());
						JSONArray jsonArray = jsonBody.getJSONArray("invoices");
						System.out.println("=================LISTA DE FATURAS==================");

						if (!jsonArray.isEmpty()) {

							for (int i = 0; i < jsonArray.length(); i++) {

								JSONObject jsonObject = jsonArray.getJSONObject(i);
								System.out.println("Identificador do cliente:" + jsonObject.get("idClient"));
								System.out.println("Status de consumo atual do cliente:"
										+ jsonObject.get("currentStatusConsumption"));
								System.out.println("Nome do cliente:" + jsonObject.get("nameClient"));
								System.out.println("Consumo total da fatura:" + jsonObject.get("consumption"));
								System.out.println("Valor da fatura:" + jsonObject.get("invoiceValue"));
								System.out.println("Tarifa da fatura:" + jsonObject.get("fare"));
								System.out.println("Data de gera��o da fatura:" + jsonObject.get("issuanceDate"));
								System.out.println("Data em que a fatura expira:" + jsonObject.get("expirationDate"));
								System.out.println("===================================================");

							}

						} else {

							System.out.println("Lista de faturas do cliente est� vazia");

						}
						System.out.println("===================================================");

					} else {

						System.out.println(response.getStatusLine());

					}
					System.out.println();
					break;

				case "5":

					mapHeaders = new HashMap<>();
					mapHeaders.put("Host",
							clientSocket.getLocalAddress().getHostAddress() + ":" + clientSocket.getLocalPort());
					request = new RequestHttp(HttpMethods.GET, "/user/statusConsumption/" + clientID, "HTTP/1.1",
							mapHeaders);
					ProtocolHttp.sendMessage(clientSocket.getOutputStream(), request.toString());
					Thread.sleep(100);
					response = readResponse(clientSocket.getInputStream());

					if (response.getStatusLine().equals(HttpCodes.HTTP_200.getCodeHttp())) {

						jsonBody = new JSONObject(response.getBody());
						System.out.println("=====================STATUS DE CONSUMO==================");
						System.out.println("Identificador do cliente:" + jsonBody.get("idClient"));
						System.out.println("Status de consumo:" + jsonBody.get("statusConsumption"));
						System.out.println("========================================================");

					} else {

						System.out.println("ERRO:");
						System.out.println(response.getStatusLine());

					}
					System.out.println();
					break;

				case "6":

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
