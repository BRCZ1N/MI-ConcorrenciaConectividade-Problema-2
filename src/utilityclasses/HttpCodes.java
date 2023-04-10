package utilityclasses;

/**
 * Esta � a enumera��o HttpCodes, que possui os status de resposta da requisi��o
 * 
 * @author Bruno Campos de Oliveira Rocha
 * @version 1.0
 */
public enum HttpCodes {

	HTTP_200("HTTP/1.1 200 OK"), HTTP_201("HTTP/1.1 201 Created"), HTTP_204("HTTP/1.1 204 No Content"),
	HTTP_400("HTTP/1.1 400 Bad Request"), HTTP_401("HTTP/1.1 401 Unauthorized"), HTTP_403("HTTP/1.1 403 Forbidden"),
	HTTP_404("HTTP/1.1 404 Not Found"), HTTP_405("HTTP/1.1 405 Method Not Allowed"),
	HTTP_500("HTTP/1.1 500 Internal Server Error"), HTTP_501("HTTP/1.1 501 Not Implemented");

	private String codeHttp;

	/**
	 * Esse � o construtor da enumera��o HttpCodes, que constroe o objeto que
	 * representa os possiveis codigos de status http
	 * 
	 * @param String codeHttp
	 */
	private HttpCodes(String codeHttp) {

		this.codeHttp = codeHttp;

	}

	/**
	 * Esse � o m�todo, que retorna c�digo do status http
	 * 
	 * @return Status http
	 */
	public String getCodeHttp() {
		return codeHttp;
	}

}
