package application.post;

import java.io.IOException;
import java.util.Scanner;

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
		Double addressX = null;
		Double addressY = null;
		String id = null;
		String password = null;
		int totalAmountCars = 0;

		do {

			repeatRegistration = false;

			try {

				System.out.println("Digite o nome do posto:");
				name = scanner.nextLine();

				System.out.println("Digite a latitude do posto:");
				addressX = scanner.nextDouble();

				System.out.println("Digite a longitude do posto:");
				addressY = scanner.nextDouble();

				System.out.println("Digite o id do posto:");
				id = scanner.nextLine();

				System.out.println("Digite a senha do posto:");
				password = scanner.nextLine();

				System.out.println("Digite a capacidade de veículos do posto:");
				totalAmountCars = scanner.nextInt();

			} catch (NumberFormatException e) {

				System.out.print("Algumas informações podem ter sido digitadas erradas, digite novamente");
				repeatRegistration = true;

			}

		} while (repeatRegistration);

		currentPost = new ChargingStation(name,addressX,addressY,totalAmountCars,id,password);

	}

	public void execPost() {

		while (connect) {

		}

	}

}
