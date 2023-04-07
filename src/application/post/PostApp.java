package application.post;

import java.io.IOException;
import java.util.Scanner;

import application.controllers.UserController;
import application.model.ChargingStation;

public class PostApp {

	private ChargingStation currentPost;
	private boolean connect;
	private String optionMenu;
	private Scanner scanner = new Scanner(System.in);

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
		post.menuPost();
	}

	public void menuPost() {

		System.out.println("(1) Cadastrar posto");
		System.out.println("(2) Logar posto cadastrado");
		System.out.println("(3) Desconectar");

		optionMenu = scanner.nextLine();

		if (optionMenu == "1") {

			registrationPost();

		} else if (optionMenu == "2") {

			execPost();

		} else {

			connect = false;

		}

	}

	public void registrationPost() {

		boolean repeatRegistration;
		String name = null;
		String address = null;
		String login = null;
		String password = null;
		int amountCars = 0;

		do {

			repeatRegistration = false;

			try {

				System.out.println("Digite o nome do posto:");
				name = scanner.nextLine();

				System.out.println("Digite o endereço do posto:");
				address = scanner.nextLine();

				System.out.println("Digite login do posto:");
				login = scanner.nextLine();

				System.out.println("Digite a senha do posto:");
				password = scanner.nextLine();

				System.out.println("Digite a capacidade de veículos do posto:");
				amountCars = scanner.nextInt();

			} catch (NumberFormatException e) {

				System.out.print("Capacidade de veículos do invalida, faça o cadastro novamente do posto");
				repeatRegistration = true;

			}

		} while (repeatRegistration == true);

		currentPost = new ChargingStation(name, address, amountCars, login, password);

	}

	public void execPost() {

		while (connect) {

		}

	}

}
