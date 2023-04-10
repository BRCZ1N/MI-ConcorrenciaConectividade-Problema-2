package application.car;

import java.util.Map;
import java.util.Scanner;
import java.io.IOException;
import java.util.HashMap;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import utilityclasses.BatteryConsumptionStatus;
import utilityclasses.BatteryLevel;
import utilityclasses.Http;
import utilityclasses.RequestHttp;

public class CarApp {

	private volatile int batteryCar;
	private BatteryConsumptionStatus currentDischargeLevel;
	private MqttClient clientMqtt;
	private boolean connected = true;

	private void generateClientMqtt(String addressBroker, String client, MemoryPersistence persistence) {

		try {

			clientMqtt = new MqttClient(addressBroker, client, persistence);

		} catch (MqttException e) {

			e.printStackTrace();

		}
	}

	private void connectMqtt() throws InterruptedException {

		MqttConnectOptions connOpts = new MqttConnectOptions();
		connOpts.setCleanSession(true);
		boolean notConnected = true;

		while (notConnected) {

			try {

				clientMqtt.connect(connOpts);
				notConnected = false;

			} catch (MqttException e) {

				System.out.println("Conexao nao encontrada, buscando nova conexao");

			}

			Thread.sleep(1000);

		}

	}

	private void generateRandomInitialConditions() {

		generateBatteryCar();
		generateCurrentDischargeLevel();

	}

	private void generateCurrentDischargeLevel() {

		BatteryConsumptionStatus[] batteryStatusEnum = BatteryConsumptionStatus.values();
		int randomArrayPos = (int) Math.random() * batteryStatusEnum.length;
		currentDischargeLevel = batteryStatusEnum[randomArrayPos];

	}

	private void generateBatteryCar() {

		BatteryLevel[] batteryLevelEnum = BatteryLevel.values();
		int randomArrayPos = (int) Math.random() * batteryLevelEnum.length;
		batteryCar = batteryLevelEnum[randomArrayPos].getBatteryLevel();

	}

	private void execCar() {

//		generateRandomInitialConditions();
//		listeningBatteryLevel();
//		reduceBatteryCar();
		try {
			menuClient();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void reduceBatteryCar() {

		new Thread(() -> {

			while (true) {

				try {

					batteryCar -= 1;
					Thread.sleep(currentDischargeLevel.getDischargeLevel());

				} catch (InterruptedException e) {

					// TODO Auto-generated catch block
					e.printStackTrace();

				}

			}

		});

	}

	public void listeningBatteryLevel() {

		new Thread(() -> {

			while (true) {

				if (batteryCar <= BatteryLevel.LOW.getBatteryLevel()) {

					if(clientMqtt.isConnected()) {
						
						
					}else {
						
						
						
					}

				}

			}

		});

	}

	private void menuClient() throws IOException {

		Scanner scanner = new Scanner(System.in);
		
		Map<String,String> header = new HashMap<String, String>();
		header.put("Host","localhost:8000");
		header.put("User-Agent", "insomnia/2023.1.0");
		header.put("Accept", "*/*");
		Http.sendHTTPRequestAndGetHttpResponse(new RequestHttp("GET", "/station/shorterQueue", "HTTP/1.1", header), "localhost");
		

		while (connected) {

			System.out.println("===================================================");
			System.out.println("========= Consumo de energia inteligente ==========");
			System.out.println("===================================================");
			System.out.println("================ Menu de cliente ==================");
			System.out.println("===================================================");
			System.out.println("====== (1) - Nivel de carga de energia");
			System.out.println("====== (3) - Menu do broker");
			System.out.println("====== (3) - Menu de requisicoes");
			System.out.println("====== (4) - Desconectar");
			System.out.println("=========== Digite a opcao desejada ===============");
			String opcao = scanner.next();

			switch (opcao) {

			case "1":

				System.out.println("O nivel de bateria atual é:" + batteryCar + "%");

			case "2":

				System.out.println("===================================================");
				System.out.println("========= Consumo de energia inteligente ==========");
				System.out.println("===================================================");
				System.out.println("======== Menu de requisicoes de cliente ===========");
				System.out.println("===================================================");
				System.out.println("====== (1) - Buscar posto com menor fila");
				System.out.println("====== (2) - Buscar posto mais proximo");
				System.out.println("====== (3) - Buscar todos os postos proximos");
//				System.out.println("====== (4) - Buscar melhor posto");
				System.out.println("=========== Digite a opcao desejada ===============");
				String opcaoMenuReq = scanner.next();

				switch (opcaoMenuReq) {

				case "1":

					break;

				case "2":

					break;

				default:

					System.out.println("Opcao não encontrada, tente novamente");
					break;

				}

				break;

			case "3":
				
				System.out.println("===================================================");
				System.out.println("========= Consumo de energia inteligente ==========");
				System.out.println("===================================================");
				System.out.println("=========== Menu secundario de cliente ============");
				System.out.println("===================================================");
				System.out.println("====== (1) - Conectar broker");
				System.out.println("====== (2) - Desconectar broker");
				break;

			default:
				
				System.out.println("Opcao não encontrada, tente novamente");
				break;

			}

		}

	}

	public static void main(String[] args) {

		CarApp car = new CarApp();
		car.execCar();

	}

}
