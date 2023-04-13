package application.car;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONObject;

import application.exceptions.UnableToConnectException;
import utilityclasses.BatteryConsumptionStatus;
import utilityclasses.BatteryLevel;
import utilityclasses.Http;
import utilityclasses.HttpCodes;
import utilityclasses.RequestHttp;
import utilityclasses.ResponseHttp;

public class CarApp {

	private volatile int batteryCar;
	private ScheduledExecutorService executor;
	private BatteryConsumptionStatus currentDischargeLevel;
	private boolean connected = true;
	private String currentIpApi;
	private volatile double latitudeUser;
	private volatile double longitudeUser;
	private Scanner scanner = new Scanner(System.in);

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

	private void execCar() throws IOException, UnableToConnectException {

		generateRandomInitialConditions();
		configIpApi();
		generateThreads();
		menuClient();

	}

	public void generateThreads() {
		
		executor.scheduleAtFixedRate(() -> reduceBatteryCar(), 0 , currentDischargeLevel.getDischargeLevel(), TimeUnit.SECONDS);
		executor.scheduleAtFixedRate(() -> listeningBatteryLevel(), 0, 5, TimeUnit.SECONDS);
		generatePosUser();
		
		
	}

	public void reduceBatteryCar() {

		while (true) {

			batteryCar -= 1;

		}

	}

	public void generatePosUser() {

		double variationPercentage = 0.1;

		new Thread(() -> {

			latitudeUser = Math.random() * 100;
			longitudeUser = Math.random() * 100;

			while (true) {

				double minLat = latitudeUser - (latitudeUser * variationPercentage);
				double maxLat = latitudeUser + (latitudeUser * variationPercentage);

				double minLong = longitudeUser - (longitudeUser * variationPercentage);
				double maxLong = longitudeUser + (longitudeUser * variationPercentage);

				latitudeUser = Math.random() * (maxLat - minLat) + minLat;
				longitudeUser = Math.random() * (maxLong - minLong) + minLong;

			}

		});

	}

	public void listeningBatteryLevel() {

		new Thread(() -> {

			while (true) {

				if (batteryCar <= BatteryLevel.LOW.getBatteryLevel()) {

					try {

						Map<String, String> header = new HashMap<String, String>();
						header.put("Content-Lenght", "0");
						System.out.println();
						System.out.println("================================ALERTA===============================");
						System.out.println("=======================NÍVEL DE BATÉRIA BAIXO=========================");
						System.out.println("NIVEL DE BATERIA ATUAL: " + batteryCar + "%");
						System.out.println("================ POSTO MAIS PROXIMO DA LOCALIZAÇÃO ===================");
						ResponseHttp response = messageReturn("GET",
								"/station/bestLocation/location?x={" + latitudeUser + "}&y={" + longitudeUser + "}",
								"HTTP/1.1", header, currentIpApi);
						JSONObject jsonObject = new JSONObject(response.getBody());
						System.out.println("Nome do posto:" + jsonObject.get("name"));
						System.out.println("Latitude:" + jsonObject.get("addressX"));
						System.out.println("Longitude:" + jsonObject.get("addressY"));
						System.out.println("Quantidade de carros na fila:" + jsonObject.get("totalAmountCars"));

					} catch (IOException e) {
						e.printStackTrace();
					}

				}

			}

		});

	}

	public void configIpApi() throws UnableToConnectException {

		boolean unconnected = true;

		while (unconnected) {

			try {

				System.out.println("Digite o IP da conexao:");
				currentIpApi = scanner.next();

				Map<String, String> header = new HashMap<String, String>();
				header.put("Content-Lenght", "0");
				Http.sendHTTPRequestAndGetHttpResponse(new RequestHttp("GET", "/station/ping", "HTTP/1.1", header),
						currentIpApi);
				unconnected = false;

			} catch (IOException e) {

				unconnected = true;
				throw new UnableToConnectException(currentIpApi);

			}

		}

	}

	private void menuClient() throws IOException {

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
				Map<String, String> header = new HashMap<String, String>();
				header.put("Content-Lenght", "0");
				ResponseHttp response;
				JSONObject jsonObject;

				switch (opcaoMenuReq) {
				case "1":

					System.out.print("================ POSTO COM MENOR A FILA ==================");
					response = messageReturn("GET", "/station/shorterQueue", "HTTP/1.1", header, currentIpApi);
					jsonObject = new JSONObject(response.getBody());
					System.out.println("Nome do posto:" + jsonObject.get("name"));
					System.out.println("Latitude:" + jsonObject.get("addressX"));
					System.out.println("Longitude:" + jsonObject.get("addressY"));
					System.out.println("Quantidade de carros na fila:" + jsonObject.get("totalAmountCars"));
					break;

				case "2":

					System.out.println("================ POSTO MAIS PROXIMO DA LOCALIZAÇÃO ==================");
					response = messageReturn("GET",
							"/station/bestLocation/location?x={" + latitudeUser + "}&y={" + longitudeUser + "}",
							"HTTP/1.1", header, currentIpApi);
					jsonObject = new JSONObject(response.getBody());
					System.out.println("Nome do posto:" + jsonObject.get("name"));
					System.out.println("Latitude:" + jsonObject.get("addressX"));
					System.out.println("Longitude:" + jsonObject.get("addressY"));
					System.out.println("Quantidade de carros na fila:" + jsonObject.get("totalAmountCars"));
					break;

				case "3":

					System.out.println("================ TODOS OS POSTOS DISPONIVEIS ==================");
					response = messageReturn("GET", "/station/all", "HTTP/1.1", header, currentIpApi);

					if (response.getStatusLine().equals(HttpCodes.HTTP_200.getCodeHttp())) {

						JSONObject jsonBody = new JSONObject(response.getBody());
						JSONArray jsonArray = jsonBody.getJSONArray("postos");
						System.out.println("======================Postos=====================");

						if (!jsonArray.isEmpty()) {

							for (int i = 0; i < jsonArray.length(); i++) {

								jsonObject = jsonArray.getJSONObject(i);
								System.out.println("Nome do posto:" + jsonObject.get("name"));
								System.out.println("Latitude:" + jsonObject.get("addressX"));
								System.out.println("Longitude:" + jsonObject.get("addressY"));
								System.out.println("Quantidade de carros na fila:" + jsonObject.get("totalAmountCars"));
								System.out.println("===================================================");

							}

						}

					} else {

						System.out.println("Nao existe postos disponiveis");

					}
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

	public ResponseHttp messageReturn(String method, String endpoint, String httpVersion, Map<String, String> header,
			String currentIpApi) throws IOException {

		ResponseHttp response = Http.sendHTTPRequestAndGetHttpResponse(
				new RequestHttp(method, endpoint, httpVersion, header), currentIpApi);
		return response;

	}

	public static void main(String[] args) throws IOException, UnableToConnectException {

		CarApp car = new CarApp();
		car.execCar();

	}

}
