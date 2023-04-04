package application.mqtt;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

public class HttpService {

	private WebClient webClient;

	public HttpService(String url) {

		this.webClient = WebClient.builder().baseUrl(url).build();

	}

	public Mono<String> getHttp(String path) {

		return webClient.get()
				.uri(path)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE, null)
				.retrieve()
				.bodyToMono(String.class);

	}

	public Mono<String> postHttp(String path) {

		return webClient.post()
				.uri(path)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.retrieve()
				.bodyToMono(String.class);

	}

}
