package application.post;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

import org.apache.tomcat.jni.Thread;

import application.model.Users;

public class Post {

	private String idPost;
	private String namePost;
	private String addressPost;
	private boolean connect;
	private Integer qtdTotalCars = 0;
	private String optionMenu;
	private Queue<Users> currentQueue = new LinkedList<Users>();
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

		Post post = new Post();
		post.execPost();
	}

	public void execPost() {

		do {

			System.out.println("Digite o nome do posto:");
			namePost = scanner.nextLine();

			System.out.println("Digite o endereço do posto:");
			addressPost = scanner.nextLine();

			System.out.print("Digite a capacidade de veículos do posto:");
			qtdTotalCars = scanner.nextInt();

		} while (namePost == null || addressPost == null || connect == authenticator(namePost, addressPost) != true || !(qtdTotalCars instanceof Integer));

		while (connect) {

			System.out.println("(1) - Cadastrar usuario na fila \n (2) - Retirar usuário da fila \n (3) - Desligar sistema");
			optionMenu = scanner.nextLine();

			if (optionMenu.equals("1")) {

				currentQueue.add(new Users());

			} else if (optionMenu.equals("2")) {

				currentQueue.remove();

			} else if (optionMenu.equals("3")) {

				connect = false;

			} else {

				System.out.println("Opcao invalida");

			}

		}

	}
	
	@Override
	public void run() {

		boolean onMed = true;

		while (onMed) {

			try {

			} catch (InterruptedException e) {

				e.printStackTrace();

			} catch (IOException e) {

				e.printStackTrace();

			}
		}
	}

	private static boolean authenticator(String namePost, String addressPost) {

		return true;

	}

	private static String inOutQtd(String texto) {

		System.out.println("Digite a quantidade que deseja " + texto + ":");
		filaQtd = scanner.nextInt();

		if (texto == "INCREMENTAR") {
			if ((qtdTotalNow + filaQtd) >= qtdTotalCars) {
				System.out.println("Não é possivel adicionar essa quantidade de carros, não ha vagas");
				return "cheio";
			} else {
				qtdTotalNow += filaQtd;

				return "Quantidade de espaços Disponiveis:" + (qtdTotalCars - qtdTotalNow);
			}
		} else if (texto == "DECREMENTAR") {
			if ((qtdTotalNow - filaQtd) < 0) {
				System.out.println("Não é possivel reduzir essa quantidade de carros");
				return "vazio";
			} else {
				qtdTotalNow -= filaQtd;
				return "Quantidade de espaços Disponiveis:" + (qtdTotalCars - qtdTotalNow);
			}
		} else {
			return "Digite um numero valido";
		}

	}
}
