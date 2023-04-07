package application.fog;

import java.io.IOException;
import java.net.Socket;

import org.eclipse.paho.client.mqttv3.MqttClient;

/**
 * Esta � a classe ThreadTcpClient, que � utilizada para representar e utilizar
 * de uma thread de um cliente TCP que se conecta ao servidor para auxiliar no
 * processamennto de dados
 * 
 * @author Bruno Campos de Oliveira Rocha
 * @version 1.0
 */
public class ThreadTcpClient implements Runnable {

	private Socket socket;
	private String connection;
	private MqttClient mqttClient;
	private int qos = 2;

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
	 * Esse � o m�todo, que seta o ip e a porta em conjunto e em formato string para
	 * exibi��o no console do servidor
	 * 
	 * @param connection - Representa��o em conjunto usando string do ip e da porta
	 *                   do servidor
	 */
	public void setConnection(String connection) {
		this.connection = connection;
	}

	/**
	 * Esse � o construtor da classe ThreadTcpClient, que constroe os objetos que
	 * representam as threads do cliente TCP
	 * 
	 * @param socket - Socket TCP do cliente
	 */

	public ThreadTcpClient(Socket socket, MqttClient mqttClient) {

		this.socket = socket;
		this.mqttClient = mqttClient;
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

			while (true) {

				if (socket.getInputStream().available() > 0) {

					RequestHttp http = Http.readRequest(socket.getInputStream());

				}

			}

		} catch (IOException e) {

			Thread.currentThread().interrupt();
		
		}

	}

}
