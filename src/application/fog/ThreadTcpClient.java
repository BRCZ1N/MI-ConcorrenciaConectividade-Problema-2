package application.fog;

import java.io.IOException;
import java.net.Socket;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import application.controllers.ChargingStationController;

/**
 * Esta � a classe ThreadTcpClient, que � utilizada para representar e utilizar
 * de uma thread de um cliente TCP que se conecta ao servidor para auxiliar no
 * processamennto de dados
 *
 * @author Bruno Campos de Oliveira Rocha
 * @version 1.0
 */

public class ThreadTcpClient implements Runnable {

	private final ChargingStationController controller;
	private final Socket socket;
	private final String connection;

	/**
	 * Esse � o m�todo, que retorna o ip e a porta em conjunto e em formato string
	 * para exibi��o no console do servidor
	 *
	 * @return Representa��o em conjunto em string do ip e da porta do servidor
	 */

	public String getConnection() {
		return connection;
	}

	/**
	 * Esse � o construtor da classe ThreadTcpClient, que constroe os objetos que
	 * representam as threads do cliente TCP
	 *
	 * @param socket - Socket TCP do cliente
	 */

	public ThreadTcpClient(Socket socket, ChargingStationController controller) {

		this.socket = socket;
		this.connection = (socket.getInetAddress() + ":" + socket.getPort());
		this.controller = controller;

	}

	/**
	 * Esse � o m�todo, que utiliza sobrescrita e deve ser implementado atrav�s da
	 * interface runnable para a execu��o de threads, neste caso para a thread do
	 * cliente TCP
	 */
	@Override
	public void run() {

		try {

			while (true) {

				if (socket.getInputStream().available() > 0) {

					RestTemplate restTemplate = new RestTemplate();
					RequestHttp http = Http.readRequest(socket.getInputStream());
					HttpHeaders headers = new HttpHeaders(http.MapToMultiValueMap());
					HttpEntity<String> requestEntity = new HttpEntity<String>(http.getBody(), headers);
					restTemplate.postForObject("http://localhost:8080" + http.getPath(), requestEntity, Void.class);

					
				}

			}

		} catch (IOException e) {

			Thread.currentThread().interrupt();

		}

	}

}
