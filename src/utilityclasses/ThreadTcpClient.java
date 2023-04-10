//package utilityclasses;
//
//import java.io.IOException;
//import java.net.Socket;
//
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.Response;
//
///**
// * Esta � a classe ThreadTcpClient, que � utilizada para representar e utilizar
// * de uma thread de um cliente TCP que se conecta ao servidor para auxiliar no
// * processamennto de dados
// *
// * @author Bruno Campos de Oliveira Rocha
// * @version 1.0
// */
//
//public class ThreadTcpClient implements Runnable {
//
//	private final Socket socket;
//	private final String connection;
//
//	/**
//	 * Esse � o m�todo, que retorna o ip e a porta em conjunto e em formato string
//	 * para exibi��o no console do servidor
//	 *
//	 * @return Representa��o em conjunto em string do ip e da porta do servidor
//	 */
//
//	public String getConnection() {
//		return connection;
//	}
//
//	/**
//	 * Esse � o construtor da classe ThreadTcpClient, que constroe os objetos que
//	 * representam as threads do cliente TCP
//	 *
//	 * @param socket - Socket TCP do cliente
//	 */
//
//	public ThreadTcpClient(Socket socket) {
//
//		this.socket = socket;
//		this.connection = (socket.getInetAddress() + ":" + socket.getPort());
//
//	}
//
//	/**
//	 * Esse � o m�todo, que utiliza sobrescrita e deve ser implementado atrav�s da
//	 * interface runnable para a execu��o de threads, neste caso para a thread do
//	 * cliente TCP
//	 */
//	@Override
//	public void run() {
//
//		RequestHttp http;
//
//		while (true) {
//
//			try {
//				if (socket.getInputStream().available() > 0) {
//
//					http = Http.readRequest(socket.getInputStream());
//					
//
//					if (http != null) {
//
//						OkHttpClient client = new OkHttpClient();
//						HttpMethod method = HttpMethod.resolve(http.getMethod());
//						HttpHeaders headers = new HttpHeaders();
//						headers.addAll(http.MapToMultiValueMap());
//						String url = "http://localhost:8000" + http.getPath();
//						Request request = new Request.Builder().url(url).build();
//						Response response = client.newCall(request).execute();
//
//						System.out.println(response.message());
//						
//						
//					}
//
//				}
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//		}
//
//	}
//
//}
