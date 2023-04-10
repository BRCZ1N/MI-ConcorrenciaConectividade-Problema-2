package application.fog;

import java.util.Map;
import java.util.Map.Entry;

/**
 * Esta � a classe ResponseHttp, que representa a resposta da requisi��o que
 * ser� enviada ao cliente conectado ao servidor.
 * 
 * @author Bruno Campos de Oliveira Rocha
 * @version 1.0
 */
public class ResponseHttp {

	private String statusLine;
	private Map<String, String> headers;
	private String body;

	/**
	 * Esse � o construtor da classe ResponseHttp, que constroe o objeto http que
	 * representa a resposta http que ser� enviada para o cliente em formato
	 * completo.
	 * 
	 * @param statusLine - Representa o status da resposta da requisi��o.
	 * @param headers    - Representa os cabe�alhos da resposta http.
	 * @param body       - Representa o corpo da resposta http.
	 */
	public ResponseHttp(String statusLine, Map<String, String> headers, String body) {

		this.statusLine = statusLine;
		this.headers = headers;
		this.body = body;
	}

	/**
	 * Esse � o construtor da classe ResponseHttp, que constroe o objeto http que
	 * representa a resposta http que ser� enviada para o cliente em formato sem
	 * corpo.
	 * 
	 * @param statusLine - Representa o status da resposta da requisi��o.
	 * @param headers    - Representa os cabe�alhos da resposta http.
	 */

	public ResponseHttp(String statusLine, Map<String, String> headers) {

		this.statusLine = statusLine;
		this.headers = headers;

	}

	/**
	 * Esse � o construtor vazio da classe ResponseHttp, que constroe o objeto da
	 * classe ResponseHttp vazio
	 */
	public ResponseHttp() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Esse � o m�todo, que retorna o status da resposta http
	 * 
	 * @return Status da requisi��o http
	 */
	public String getStatusLine() {
		return statusLine;
	}

	/**
	 * Esse � o m�todo, que seta o status da resposta http
	 * 
	 * @param statusLine - O status da resposta http
	 */
	public void setStatusLine(String statusLine) {
		this.statusLine = statusLine;
	}

	/**
	 * Esse � o m�todo, que retorna os cabe�alhos da resposta http
	 * 
	 * @return Cabe�alhos da requisi��o http
	 */
	public Map<String, String> getHeaders() {
		return headers;
	}

	/**
	 * Esse � o m�todo, que seta os cabe�alhos da resposta http
	 * 
	 * @param headers - Os cabe�alhos da resposta http
	 */
	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	/**
	 * Esse � o m�todo, que retorna o corpo da resposta http
	 * 
	 * @return Corpo da requisi��o http
	 */
	public String getBody() {
		return body;
	}

	/**
	 * Esse � o m�todo, que seta o corpo da resposta http
	 * 
	 * @param body - O corpo da resposta http
	 */
	public void setBody(String body) {
		this.body = body;
	}

	/**
	 * Esse � o m�todo, que formata os cabe�alhos da resposta no modelo padr�o de
	 * cabe�alhos http
	 * 
	 * @return Os cabe�alhos da resposta http em string
	 */
	public String headersToString() {

		StringBuilder stringHeaders = new StringBuilder();
		for (Entry<String, String> header : headers.entrySet()) {

			stringHeaders.append(header.getKey() + ":" + " " + header.getValue() + "\r\n");

		}

		return stringHeaders.toString();

	}

	/**
	 * Esse � o m�todo, que formata todo o objeto da resposta no modelo padr�o de
	 * uma resposta http
	 * 
	 * @return O objeto formatado em uma resposta http padr�o em formato String
	 */
	@Override
	public String toString() {

		if (body != null) {

			return this.statusLine + "\r\n" + this.headersToString() + "\r\n" + this.body;

		} else {

			return this.statusLine + "\r\n" + this.headersToString() + "\r\n";

		}

	}

}
