package utilityclasses;

import java.util.Map;
import java.util.Map.Entry;

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
	/**

	Construtor da classe.
	@param method o método HTTP da requisição
	@param path o caminho do recurso solicitado
	@param versionHttp a versão do protocolo HTTP
	@param headers um mapa com os cabeçalhos da requisição
	*/	
	public RequestHttp(String method, String path, String versionHttp, Map<String, String> headers) {

		this.method = method;
		this.path = path;
		this.versionHttp = versionHttp;
		this.headers = headers;
	
	}
	/**

	Retorna o método HTTP da requisição.
	@return o método HTTP da requisição
	*/
	public String getMethod() {
		return method;
	}
	/**

	Define o método HTTP da requisição.
	@param method o método HTTP da requisição
	*/
	public void setMethod(String method) {
		this.method = method;
	}
	/**

	Retorna o caminho do recurso solicitado.
	@return o caminho do recurso solicitado
	*/
	public String getPath() {
		return path;
	}
	/**

	Define o caminho do recurso solicitado.
	@param path o caminho do recurso solicitado
	*/
	public void setPath(String path) {
		this.path = path;
	}
	/**

	Retorna a versão do protocolo HTTP.
	@return a versão do protocolo HTTP
	*/
	public String getVersionHttp() {
		return versionHttp;
	}
	/**

	Define a versão do protocolo HTTP.
	@param versionHttp a versão do protocolo HTTP
	*/
	public void setVersionHttp(String versionHttp) {
		this.versionHttp = versionHttp;
	}
	/**

	Retorna um mapa com os cabeçalhos da requisição.
	@return um mapa com os cabeçalhos da requisição
	*/
	public Map<String, String> getHeaders() {
		return headers;
	}
	/**

	Define os cabeçalhos da requisição.
	@param headers um mapa com os cabeçalhos da requisição
	*/
	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}
	/**

	Retorna o corpo da requisição.
	@return o corpo da requisição
	*/

	public String getBody() {
		return body;
	}
	/**

	Define o corpo da requisição.
	@param body o corpo da requisição
	*/
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
