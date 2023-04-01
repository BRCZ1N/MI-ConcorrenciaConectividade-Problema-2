package application.post;

import java.io.IOException;
import java.util.Scanner;

public class Post extends Thread {

	private static String nomePosto;
	private static String bairroPosto;
	private static boolean connect;
	private static int filaQtd;
	private static String filaOpc;
	private static Integer qtdTotalCars = 0;
	private static int qtdTotalNow = 0;
	private static String respostaQtd;
	static Scanner scanner = new Scanner(System.in);

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

		// Loop para solicitar a entrada da matrícula do servidor até que ela seja
		// válida

		do {
			nomePosto = null;
			System.out.println("Digite o nome do posto:");
			nomePosto = scanner.nextLine();
			System.out.println("Digite o bairro:");
			bairroPosto = scanner.nextLine();
			System.out.print("Digite a quantidade carros que cabem no posto");
			qtdTotalCars = scanner.nextInt();
		} while (nomePosto == null || bairroPosto == null || connect == authenticator(nomePosto, bairroPosto) != true
				|| !(qtdTotalCars instanceof Integer));
		while (connect) {
			System.out.println("(1) - Aumentar fila \n" + "(2) - Diminuir fila \n" + "(3) - Desligar sistema");
			filaOpc = scanner.nextLine();
			if (filaOpc.equals("1")) {
				respostaQtd = inOutQtd("INCREMENTAR");
				System.out.println(respostaQtd);
			} else if (filaOpc.equals("2")) {
				respostaQtd = inOutQtd("DECREMENTAR");
				System.out.println(respostaQtd);
			}

			else {
				try {
					if (filaOpc.equals("3")) {
						connect = false;
					}

				} catch (NumberFormatException e) {
					// Caso a entrada não seja um número, o usuário é solicitado a inserir novamente
					System.out.println("Entrada invalida, Digite novamente:");
				}
			}
		}
	}

	// Runnable que envia a leitura do medidor para o servidor em intervalos
	// regulares
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

	private static boolean authenticator(String nomePosto2, String bairroPosto2) {
		return true;
		// TODO Auto-generated method stub

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
		}else if (texto == "DECREMENTAR") {
			if ((qtdTotalNow  - filaQtd) < 0 ) {
				System.out.println("Não é possivel reduzir essa quantidade de carros");
				return "vazio";
			} else {
				qtdTotalNow -= filaQtd;
				return "Quantidade de espaços Disponiveis:" + (qtdTotalCars - qtdTotalNow);
			}
		}else {
			return "Digite um numero valido";
		}
		
	}
}
