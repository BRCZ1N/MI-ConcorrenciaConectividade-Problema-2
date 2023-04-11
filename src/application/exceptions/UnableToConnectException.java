package application.exceptions;

public class UnableToConnectException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -533684294113102249L;

	public UnableToConnectException(String ip) {

		super("O servidor com ip:" + ip +" nao foi encontrado");

	}

}
