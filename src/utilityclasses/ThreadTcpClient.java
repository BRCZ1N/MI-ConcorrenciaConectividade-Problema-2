package utilityclasses;

import java.io.IOException;
import java.net.Socket;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.handler.codec.http.HttpResponse;
import reactor.core.publisher.Mono;

/**
 * Esta � a classe ThreadTcpClient, que � utilizada para representar e utilizar
 * de uma thread de um cliente TCP que se conecta ao servidor para auxiliar no
 * processamennto de dados
 *
 * @author Bruno Campos de Oliveira Rocha
 * @version 1.0
 */

public class ThreadTcpClient implements Runnable {

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

	public ThreadTcpClient(Socket socket) {

		this.socket = socket;
		this.connection = (socket.getInetAddress() + ":" + socket.getPort());

	}

	/**
	 * Esse � o m�todo, que utiliza sobrescrita e deve ser implementado atrav�s da
	 * interface runnable para a execu��o de threads, neste caso para a thread do
	 * cliente TCP
	 */
	@Override
	public void run() {

		try {

			RequestHttp http;

			while (true) {

				if (socket.getInputStream().available() > 0) {

					http = Http.readRequest(socket.getInputStream());

					if (http != null) {
						
						HttpMethod method = HttpMethod.resolve(http.getMethod());
						HttpHeaders headers = new HttpHeaders();
						headers.addAll(http.MapToMultiValueMap());
						String url = "http://localhost:8010" + http.getPath();
						WebClient webClient = WebClient.create();
						Mono<ResponseEntity<HttpResponse>> responseHttp = webClient.method(method).uri(url)
								.headers(h -> h.addAll(headers)).retrieve()
								.onStatus(HttpStatus::is4xxClientError, response -> Mono.empty())
								.toEntity(HttpResponse.class);
						ResponseEntity<HttpResponse> responseEntity = responseHttp.block();
						responseHttp.subscribe(response -> {
						    ResponseHttp httpResponse;
						    if (responseEntity.getBody() == null) {
						        httpResponse = new ResponseHttp(
						                HttpCodes.valueOf("HTTP_" + responseEntity.getStatusCodeValue()).getCodeHttp(),
						                responseEntity.getHeaders().toSingleValueMap());
						    } else {
						        httpResponse = new ResponseHttp(
						                HttpCodes.valueOf("HTTP_" + responseEntity.getStatusCodeValue()).getCodeHttp(),
						                responseEntity.getHeaders().toSingleValueMap(),
						                responseEntity.getBody().toString());
						    }
						    try {
								Http.sendResponse(socket.getOutputStream(), httpResponse.toString());
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						});

					}

				}

			}

		} catch (IOException e) {

			Thread.currentThread().interrupt();

		}

	}

}
