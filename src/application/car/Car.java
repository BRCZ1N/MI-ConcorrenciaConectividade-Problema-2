package application.car;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.Executors;
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
import utilityclasses.ServerConfig;

public class Car {

	private volatile int batteryCar;
	private ScheduledExecutorService executor;
	private BatteryConsumptionStatus currentDischargeLevel;
	private boolean connected = true;
	private volatile double latitudeUser;
	private volatile double longitudeUser;
	private Scanner scanner = new Scanner(System.in);
	private volatile String carArea;

	public Car() {

		this.executor = Executors.newScheduledThreadPool(3);

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

	private void execCar() throws IOException, UnableToConnectException {

		generateRandomInitialConditions();
		configArea();
		generateThreads();
		menuClient();

	}

	public void generateThreads() {

		executor.scheduleAtFixedRate(() -> reduceBatteryCar(), 0, currentDischargeLevel.getDischargeLevel(),
				TimeUnit.SECONDS);
		executor.scheduleAtFixedRate(() -> listeningBatteryLevel(), 0, 5, TimeUnit.SECONDS);
		executor.scheduleAtFixedRate(() -> generatePosUser(), 0, 5, TimeUnit.SECONDS);

	}

	public void reduceBatteryCar() {

		batteryCar -= 1;

	}

	public void generatePosUser() {

		double variationPercentage = 0.1;

		latitudeUser = Math.random() * 100;
		longitudeUser = Math.random() * 100;

		double minLat = latitudeUser - (latitudeUser * variationPercentage);
		double maxLat = latitudeUser + (latitudeUser * variationPercentage);

		double minLong = longitudeUser - (longitudeUser * variationPercentage);
		double maxLong = longitudeUser + (longitudeUser * variationPercentage);

		latitudeUser = Math.random() * (maxLat - minLat) + minLat;
		longitudeUser = Math.random() * (maxLong - minLong) + minLong;

	}

	public void listeningBatteryLevel() {

		if (batteryCar <= BatteryLevel.LOW.getBatteryLevel()) {

			try {

				Map<String, String> header = new HashMap<String, String>();
				header.put("Content-Lenght", "0");
				System.out.println("===============================Alerta=================================");
				System.out.println("=======================Nivel de bateria baixo=========================");
				System.out.println("Nivel de bateria atual: " + batteryCar + "%");
				System.out.println("================ Posto mais proximo da localizacao ===================");
				ResponseHttp response = messageReturn("GET","/station/bestLocation/location?x={" + latitudeUser + "}&y={" + longitudeUser + "}", "HTTP/1.1",header, carArea);
				JSONObject jsonObject = new JSONObject(response.getBody());
				System.out.println("Nome do posto:" + jsonObject.getString("name"));
				System.out.println("Latitude:" + jsonObject.getDouble("latitude"));
				System.out.println("Longitude:" + jsonObject.getDouble("longitude"));
				System.out.println("Quantidade de carros na fila:" + jsonObject.getString("totalAmountCars"));

			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	private void configArea() throws IOException {
		boolean configConnect = true;
		
		while (configConnect) {
			
			System.out.println("===================================================");
			System.out.println("============ Escolha a zona do carro ==============");
			System.out.println("===================================================");
			System.out.println("====== (1) - Norte");
			System.out.println("====== (2) - Leste");
			System.out.println("====== (3) - Oeste");
			System.out.println("====== (4) - Sul");
			System.out.println("=========== Digite a opcao desejada ===============");
			String opcao = scanner.next();
			
			switch (opcao) {
			
			case "1":
				
				carArea = ServerConfig.HTTP_LOCALHOST.getAddress();
				configConnect = false;
				break;
				
			case "2":
				
				carArea = ServerConfig.Leste_LARSID_4.getAddress();
				configConnect = false;
				break;
				
			case "3":
				
				carArea = ServerConfig.Oeste_LARSID_3.getAddress();
				configConnect = false;
				break;
				
			case "4":
				
				carArea = ServerConfig.Sul_LARSID_5.getAddress();
				configConnect = false;
				break;
				
			default:
				
				System.out.println("Digite uma zona valida");
				break;
				
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

				System.out.println("O nivel de bateria atual:" + batteryCar + "%");
				break;

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

					response = messageReturn("GET", "/station/shorterQueue", "HTTP/1.1", header, carArea);

					if (HttpCodes.HTTP_200.getCodeHttp().equals(response.getStatusLine())) {

						System.out.print("================ Posto com a menor fila ==================");
						jsonObject = new JSONObject(response.getBody());
						System.out.println("Nome do posto:" + jsonObject.getString("name"));
						System.out.println("Latitude:" + jsonObject.getDouble("latitude"));
						System.out.println("Longitude:" + jsonObject.getDouble("longitude"));
						System.out.println("Quantidade de carros na fila:" + jsonObject.getInt("totalAmountCars"));

					} else {

						System.out.println(response.toString());

					}
					break;

				case "2":

					response = messageReturn("GET","/station/bestLocation/location?x={" + latitudeUser + "}&y={" + longitudeUser + "}","HTTP/1.1", header, carArea);

					if (HttpCodes.HTTP_200.getCodeHttp().equals(response.getStatusLine())) {

						System.out.println("================ Posto mais proximo da localização ==================");
						jsonObject = new JSONObject(response.getBody());
						System.out.println("Nome do posto:" + jsonObject.getString("name"));
						System.out.println("Latitude:" + jsonObject.getDouble("latitude"));
						System.out.println("Longitude:" + jsonObject.getDouble("longitude"));
						System.out.println("Quantidade de carros na fila:" + jsonObject.getInt("totalAmountCars"));

					} else {

						System.out.println(response.toString());

					}

					break;

				case "3":

					response = messageReturn("GET", "/station/all", "HTTP/1.1", header, carArea);

					if (response.getStatusLine().equals(HttpCodes.HTTP_200.getCodeHttp())) {

						JSONObject jsonBody = new JSONObject(response.getBody());
						JSONArray jsonArray = jsonBody.getJSONArray("postos");
						System.out.println("================ TODOS OS POSTOS DISPONIVEIS NA REGIÃO ==================");

						if (!jsonArray.isEmpty()) {

							for (int i = 0; i < jsonArray.length(); i++) {

								jsonObject = jsonArray.getJSONObject(i);
								System.out.println("Nome do posto:" + jsonObject.getString("name"));
								System.out.println("Latitude:" + jsonObject.getDouble("latitude"));
								System.out.println("Longitude:" + jsonObject.getDouble("longitude"));
								System.out.println("Quantidade de carros na fila:" + jsonObject.getInt("totalAmountCars"));
								System.out.println("===================================================");

							}

						}

					} else {

						System.out.println(response.toString());

					}
					break;

				default:

					System.out.println("Opcao não encontrada, tente novamente");
					break;

				}

				break;

			case "3":

				connected = false;
				break;

			default:

				System.out.println("Opcao não encontrada, tente novamente");
				break;

			}

		}

	}

	public ResponseHttp messageReturn(String method, String endpoint, String httpVersion, Map<String, String> header, String currentIpApi) throws IOException {

		ResponseHttp response = Http.sendHTTPRequestAndGetHttpResponse(new RequestHttp(method, endpoint, httpVersion, header), currentIpApi);
		return response;

	}

	public static void main(String[] args) throws IOException, UnableToConnectException {

		Car car = new Car();
		car.execCar();

	}

}
