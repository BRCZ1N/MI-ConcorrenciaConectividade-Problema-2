package application.post;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.regex.Pattern;

import application.model.User;
import application.utilities.ProtocolHttp;
import application.utilities.RequestHttp;
import application.utilities.ResponseHttp;

public class PostApp {

	private User currentUser;
	private String namePost;
	private String addressPost;
	private boolean connect;
	private String qtdTotalCars;
	private String optionMenu;
	private Queue<User> currentQueue = new LinkedList<User>();
	private Scanner scanner = new Scanner(System.in);
	private String patternEnterQueue = "/user/enter";
	private String patternExitQueue = "/user/remove/(\\w+)";

	/**
	 * Metodo principal da classe UserEnergyGaugeThread, esta classe ira fazer a
	 * conexão do medidor com o sistema e permitira a passagem das medições para o
	 * servidor por meio das threads
	 *
	 * @param args - argumentos de linha de comando (não utilizados)
	 * @throws InterruptedException se a thread for interrompida enquanto estiver
	 *                              dormindo
	 * @throws IOException          se ocorrer um erro de entrada/saída
	 */
	public static void main(String[] args) {

		PostApp post = new PostApp();
		post.execPost();
	}

	public void execPost() {

		boolean repeatRegistration;

		do {

			repeatRegistration = false;

			try {

				System.out.println("Digite o nome do posto:");
				namePost = scanner.nextLine();

				System.out.println("Digite o endereço do posto:");
				addressPost = scanner.nextLine();

				System.out.print("Digite a capacidade de veículos do posto:");
				qtdTotalCars = scanner.nextLine();

			} catch (NumberFormatException e) {

				System.out.print("Capacidade de veículos do invalida, faça o cadastro novamente do posto");
				repeatRegistration = true;

			}

		} while (namePost == null || addressPost == null || repeatRegistration == true);
		
		listenerRequisitons();
		queueAction();

	}

	private void listenerRequisitons() {

		new Thread(() -> {

			while (connect) {
				
				currentRequest = ProtocolHttp.readRequest();

			}

		}).start();

	}

	private void queueAction() {

		new Thread(() -> {

			while (connect) {

				if (currentQueue.isEmpty() || currentUser != null) {

					if (Pattern.matches(patternEnterQueue, currentRequest.getPath())) {

						currentQueue.add(currentUser);

					} else if (Pattern.matches(patternExitQueue, currentRequest.getPath())) {

						currentQueue.remove();

					}

				}

			}

		}).start();

	}
}
