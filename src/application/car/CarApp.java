package application.car;

import java.util.Map;
import java.util.Scanner;
import java.io.IOException;
import java.util.HashMap;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.JSONArray;
import org.json.JSONObject;

import utilityclasses.BatteryConsumptionStatus;
import utilityclasses.BatteryLevel;
import utilityclasses.Http;
import utilityclasses.HttpCodes;
import utilityclasses.RequestHttp;
import utilityclasses.ResponseHttp;

public class CarApp {

	private volatile int batteryCar;
	private BatteryConsumptionStatus currentDischargeLevel;
	private MqttClient clientMqtt;
	private boolean connected = true;

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
				}

			}

		});

	}

	private void menuClient() throws IOException {

		Scanner scanner = new Scanner(System.in);

		Map<String, String> header = new HashMap<String, String>();
		header.put("Host", "localhost:8000");
		header.put("User-Agent", "insomnia/2023.1.0");
		header.put("Accept", "*/*");
		Http.sendHTTPRequestAndGetHttpResponse(new RequestHttp("GET", "/station/shorterQueue", "HTTP/1.1", header),
				"localhost");

		while (connected) {

			System.out.println("===================================================");
			System.out.println("========= Consumo de energia inteligente ==========");
			System.out.println("===================================================");
			System.out.println("================ Menu de cliente ==================");
			System.out.println("===================================================");
			System.out.println("====== (1) - Nivel de carga de energia");
			System.out.println("====== (2) - Menu de requisicoes");
			System.out.println("====== (3) - Desconectar");
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
					messageReturn("/shorterQueue","================ POSTOS DISPONIVEIS COM MENOR FILA ==================");
					break;

				case "2":
					System.out.println("Digite o seu ponto X:");
					String coordenadasX = scanner.next();
					System.out.println("Digite o seu ponto Y:");
					String coordenadasY = scanner.next();
					messageReturn("/bestLocation/location?x={"+ coordenadasX +"}&y={"+ coordenadasY + "}","================ POSTOS DISPONIVEIS NA PROXIMIDADE ==================");
					break;
				case "3":
					messageReturn("/all","================ TODOS OS POSTOS DISPONIVEIS ==================");
					break;
				default:

					System.out.println("Opcao não encontrada, tente novamente");
					break;

				}

				break;

			case "3":
				connected = false;

			default:

				System.out.println("Opcao não encontrada, tente novamente");
				break;

			}

		}

	}

	public static void messageReturn(String endpoint, String tipo) throws IOException {
		JSONObject jsonBody;
		Map<String, String> header = new HashMap<String, String>();
		header.put("Host", "localhost:8000");
		header.put("User-Agent", "insomnia/2023.1.0");
		header.put("Accept", "*/*");
		header.put("Content-Type", "application/json");
		ResponseHttp response = Http.sendHTTPRequestAndGetHttpResponse(
				new RequestHttp("GET", endpoint, "HTTP/1.1", header), "localhost");
		if (response.getStatusLine().equals(HttpCodes.HTTP_200.getCodeHttp())) {
			System.out.println(String.format(tipo));
			jsonBody = new JSONObject(response.getBody());
			System.out.println("Idenficador do cliente: " + jsonBody.get("idClient"));
			JSONArray jsonArray = jsonBody.getJSONArray("postos");
			System.out.println("======================Postos=====================");

			if (!jsonArray.isEmpty()) {

				for (int i = 0; i < jsonArray.length(); i++) {

					JSONObject jsonObject = jsonArray.getJSONObject(i);
					System.out.println("Nome do posto:" + jsonObject.get("nomePosto"));
					System.out.println("Endereço:" + jsonObject.get("endereco"));
					System.out.println("Quantidade de carros na fila:" + jsonObject.get("quantidadeCarros"));
					System.out.println("===================================================");

				}

			}
		}else {
			System.out.println("Não Existe postos disponiveis");
		}
	}

	public static void main(String[] args) {

		CarApp car = new CarApp();
		car.execCar();

	}

}
