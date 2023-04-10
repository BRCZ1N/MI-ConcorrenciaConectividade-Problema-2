package utilityclasses;

import java.util.Map;
import java.util.Map.Entry;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * Esta � a classe RequestHttp, que representa a requisi��o http do cliente
 * conectado ao servidor.
 *
 * @author Bruno Campos de Oliveira Rocha
 * @version 1.0
 */
public class RequestHttp {

	private String method;
	private String path;
	private String versionHttp;
	private Map<String, String> headers;
	private String body;

	/**
	 * Esse � o construtor da classe RequestHttp, que constroe o objeto http que
	 * representa a requisi��o do cliente em formato completo
	 *
	 * @param method      - Representa o m�todo da requisi��o http.
	 * @param path        - Armazena o caminho da requisi��o http.
	 * @param versionHttp - Representa a vers�o atual da requisi��o http.
	 * @param headers     - Representa os cabe�alhos da requisi��o http.
	 * @param body        - Representa o corpo da requisi��o http.
	 */
	public RequestHttp(String method, String path, String versionHttp, Map<String, String> headers, String body) {

		this.method = method;
		this.path = path;
		this.versionHttp = versionHttp;
		this.headers = headers;
		this.body = body;
	}
	
	public RequestHttp(String method, String path, String versionHttp, Map<String, String> headers) {

		this.method = method;
		this.path = path;
		this.versionHttp = versionHttp;
		this.headers = headers;
	
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getVersionHttp() {
		return versionHttp;
	}

	public void setVersionHttp(String versionHttp) {
		this.versionHttp = versionHttp;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	/**
	 * Esse � o m�todo, que formata os cabe�alhos da requisi��o no modelo padr�o de
	 * cabe�alhos http
	 *
	 * @return Os cabe�alhos da requisi��o http em string
	 */
	public String headersToString() {

		StringBuilder stringHeaders = new StringBuilder();
		for (Entry<String, String> header : headers.entrySet()) {

			stringHeaders.append(header.getKey() + ":" + " " + header.getValue() + "\r\n");

		}

		return stringHeaders.toString();

	}

	public MultiValueMap<String, String> MapToMultiValueMap() {

		MultiValueMap<String, String> headersConvert = new LinkedMultiValueMap<>();

		for (Map.Entry<String, String> mapContent : headers.entrySet()) {

			headersConvert.add(mapContent.getKey(),mapContent.getValue());

		}

		return headersConvert;

	}

	/**
	 * Esse � o m�todo, que formata todo o objeto da requisi��o no modelo padr�o de
	 * uma requisi��o http
	 *
	 * @return O objeto formatado em uma requisi��o http padr�o em formato String
	 */
	@Override
	public String toString() {

		if (body != null) {

			return this.method + " " + this.path + " " + this.versionHttp + "\r\n" + this.headersToString() + "\r\n"
					+ this.body;

		} else {

			return this.method + " " + this.path + " " + this.versionHttp + "\r\n" + this.headersToString() + "\r\n";

		}

	}

}
