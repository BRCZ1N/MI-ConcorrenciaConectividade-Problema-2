package application.car;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONObject;

import application.exceptions.UnableToConnectException;
import application.model.ChargingStationModel;
import utilityclasses.BatteryConsumptionStatus;
import utilityclasses.BatteryLevel;
import utilityclasses.ConfigLarsidIpsHttp;
import utilityclasses.Http;
import utilityclasses.HttpCodes;
import utilityclasses.RequestHttp;
import utilityclasses.ResponseHttp;

public class CarApp {

	private volatile int batteryCar = BatteryLevel.DEFAULT.getBatteryLevel();
	private ScheduledExecutorService executor;
	private BatteryConsumptionStatus currentDischargeLevel;
	private volatile boolean connected = true;
	private volatile double latitudeCar;
	private volatile double longitudeCar;
	private Scanner scanner = new Scanner(System.in);
	private volatile String carArea;
	private volatile ChargingStationModel currentStationBest;
	private Future<?> future;

	/*
	 * Starta as Threads de execução da interface do cliente
	 */
	public CarApp() {

		this.executor = Executors.newScheduledThreadPool(5);

	}

	/*
	 * metodo que irá setar a Geração de um nivel de bateria e uma força de redução
	 * de bateria aleatoria
	 */
	private void generateRandomInitialConditions() {

		generateCurrentDischargeLevel();
		generatePosUser();

	}

	/*
	 * gera uma força de redução de bateria aleatoria
	 */
	private void generateCurrentDischargeLevel() {

		BatteryConsumptionStatus[] batteryStatusEnum = BatteryConsumptionStatus.values();
		int randomArrayPos = (int) (Math.random() * batteryStatusEnum.length);
		currentDischargeLevel = batteryStatusEnum[randomArrayPos];

	}

	/*
	 * reseta o nivel de bateria
	 */
	private void resetBatteryLevel() {

		batteryCar = BatteryLevel.DEFAULT.getBatteryLevel();

	}

	public Double approximateNumber(double num) {

		DecimalFormat df = new DecimalFormat("#.##");
		String formattedNum = df.format(num);
		return Double.parseDouble(formattedNum.replace(",", "."));

	}

	/*
	 * executa os metodos de funcionamento da classe, dentre threads, e as condições
	 * iniciais de funcionamento da interface
	 * 
	 * @throws IOException
	 * 
	 * @throws UnableToConnectException
	 */
	private void execCar() throws IOException, UnableToConnectException {

		generateRandomInitialConditions();
		generateThreads();
		menuClient();

	}

	/**
	 * 
	 * Gera as threads que irão configurar e executar o cliente carro, dentre nivel
	 * de bateria, nivel de redução e posição relativa do automovel
	 */
	public void generateThreads() {

		executor.scheduleAtFixedRate(() -> reduceBatteryCar(), 0, currentDischargeLevel.getDischargeLevel(),
				TimeUnit.SECONDS);
		executor.scheduleAtFixedRate(() -> listeningBatteryLevel(), 0, 5, TimeUnit.SECONDS);
		executor.scheduleAtFixedRate(() -> trackingAreaFog(), 0, 5, TimeUnit.SECONDS);
		executor.scheduleAtFixedRate(() -> autoOff(), 0, 5, TimeUnit.SECONDS);

	}

	/**
	 * 
	 * metodo de redução do nivel de bateria
	 */
	public void reduceBatteryCar() {

		batteryCar -= 1;

	}

	/**
	 * 
	 * Metodo que ira dar a posição relativa do automovel, gerada de forma aleatoria
	 */
	public void generatePosUser() {

		DecimalFormat df = new DecimalFormat("0.00");

		latitudeCar = Math.random() * 100;
		String latitudeFormatada = df.format(latitudeCar);
		latitudeCar = Double.parseDouble(latitudeFormatada.replace(",", "."));

		longitudeCar = Math.random() * 100;
		String longitudeFormatada = df.format(longitudeCar);
		longitudeCar = Double.parseDouble(longitudeFormatada.replace(",", "."));

	}

	/**
	 * 
	 * Metodo que servira de alerta para o nivel de bateria critico
	 */
	public void listeningBatteryLevel() {

		if (batteryCar <= BatteryLevel.LOW.getBatteryLevel()
				&& batteryCar != BatteryLevel.DISCHARGED.getBatteryLevel()) {

			try {

				System.out.println("===================== Alerta =======================");
				System.out.println("============= Nivel de bateria baixo ===============");
				System.out.println("Nivel de bateria atual: " + batteryCar + "%");
				System.out.println("======= Posto mais proximo da localizacao ==========");
				Map<String, String> header = new HashMap<String, String>();
				header.put("Content-Lenght", "0");
				ResponseHttp response = messageReturn("GET","/station/bestLocation/locationX=" + latitudeCar + "&locationY=" + longitudeCar, "HTTP/1.1",header, carArea);
				System.out.println("Buscando posto mais próximo");

				if (HttpCodes.HTTP_200.getCodeHttp().equals(response.getStatusLine())) {

					JSONObject jsonObject = new JSONObject(response.getBody());
					System.out.println("====================================================");
					System.out.println("Nome do posto:" + jsonObject.getString("name"));
					System.out.println("Latitude:" + jsonObject.getDouble("latitude"));
					System.out.println("Longitude:" + jsonObject.getDouble("longitude"));
					System.out.println("Quantidade de carros na fila:" + jsonObject.getInt("totalAmountCars"));
					System.out.println("Tempo de espera na fila em minutos:" + jsonObject.getDouble("queueWaitingTime"));
					currentStationBest = new ChargingStationModel(jsonObject);
					System.out.println("====================================================");
					
					System.out.println("====================================================");
					System.out.println("Posicao atual do carro:");
					System.out.println("Latitude do carro: "+approximateNumber(latitudeCar));
					System.out.println("Longitude do carro: "+approximateNumber(longitudeCar));
					System.out.println("====================================================");
					
					if (future == null || !future.isDone()) {

						future = executor.submit(() -> {

							simulateApproach(currentStationBest.getLatitude(), currentStationBest.getLongitude());

						});
					
					}else if((((Double)(approximateNumber(latitudeCar))).equals(currentStationBest.getLatitude())) && (((Double)(approximateNumber(longitudeCar))).equals(currentStationBest.getLongitude()))){
						
						future.cancel(true);
						resetBatteryLevel();
						generatePosUser();
						
					}

				} else {

					System.out.println("Erro:");
					System.out.println(response.toString());

				}

			} catch (IOException e) {

				e.printStackTrace();

			}

		}

	}

	/**
	 * 
	 * Metodo que servira para definir a zona de acesso do cliente
	 */

	private void trackingAreaFog() {

		if ((latitudeCar >= 0 && latitudeCar <= 50) && (longitudeCar >= 0 && longitudeCar <= 50)) {

			carArea = ConfigLarsidIpsHttp.HTTP_FOG_REGION_Q1.getAddress();

		} else if ((latitudeCar >= 0 && latitudeCar <= 50) && (longitudeCar >= 51 && longitudeCar <= 100)) {

			carArea = ConfigLarsidIpsHttp.HTTP_FOG_REGION_Q2.getAddress();

		} else if ((latitudeCar >= 51 && latitudeCar <= 100) && (longitudeCar >= 0 && longitudeCar <= 50)) {

			carArea = ConfigLarsidIpsHttp.HTTP_FOG_REGION_Q3.getAddress();

		} else if((latitudeCar >= 51 && latitudeCar <= 100) && (longitudeCar >= 51 && longitudeCar <= 100)) {

			carArea = ConfigLarsidIpsHttp.HTTP_FOG_REGION_Q4.getAddress();

		}

	}

	/**
	 * 
	 * Metodo que tera as opções de requisição do cliente
	 */
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
			System.out.println("====== (4) - Informações de conexão");
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
				System.out.println("====== (3) - Buscar os melhores postos de outras regiões");
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

						System.out.println("================ Posto com a menor fila ==================");
						jsonObject = new JSONObject(response.getBody());
						System.out.println("Nome do posto:" + jsonObject.getString("name"));
						System.out.println("Latitude:" + jsonObject.getDouble("latitude"));
						System.out.println("Longitude:" + jsonObject.getDouble("longitude"));
						System.out.println("Quantidade de carros na fila:" + jsonObject.getInt("totalAmountCars"));
						System.out.println(
								"Tempo de espera na fila em minutos:" + jsonObject.getDouble("queueWaitingTime"));

					} else {

						System.out.println("Erro:");
						System.out.println(response.toString());

					}
					break;

				case "2":

					response = messageReturn("GET",
							"/station/bestLocation/locationX=" + latitudeCar + "&locationY=" + longitudeCar, "HTTP/1.1",
							header, carArea);

					if (HttpCodes.HTTP_200.getCodeHttp().equals(response.getStatusLine())) {

						System.out.println("================ Posto mais proximo da localização ==================");
						jsonObject = new JSONObject(response.getBody());
						System.out.println("Nome do posto:" + jsonObject.getString("name"));
						System.out.println("Latitude:" + jsonObject.getDouble("latitude"));
						System.out.println("Longitude:" + jsonObject.getDouble("longitude"));
						System.out.println("Quantidade de carros na fila:" + jsonObject.getInt("totalAmountCars"));
						System.out.println("Tempo de espera na fila em minutos:" + jsonObject.getDouble("queueWaitingTime"));

					} else {

						System.out.println("Erro:");
						System.out.println(response.toString());

					}

					break;

				case "3":

					response = messageReturn("GET", "/station/global/bestStations", "HTTP/1.1", header, carArea);

					if (response.getStatusLine().equals(HttpCodes.HTTP_200.getCodeHttp())) {

						JSONObject jsonBody = new JSONObject(response.getBody());
						JSONArray jsonArray = jsonBody.getJSONArray("postos");
						System.out.println("================ Melhores postos de outras regiões ==================");

						if (!jsonArray.isEmpty()) {

							for (int i = 0; i < jsonArray.length(); i++) {

								jsonObject = jsonArray.getJSONObject(i);
								System.out.println("Nome do posto:" + jsonObject.getString("name"));
								System.out.println("Latitude:" + jsonObject.getDouble("latitude"));
								System.out.println("Longitude:" + jsonObject.getDouble("longitude"));
								System.out.println(
										"Quantidade de carros na fila:" + jsonObject.getInt("totalAmountCars"));
								System.out.println("Tempo de espera na fila em minutos:"
										+ jsonObject.getDouble("queueWaitingTime"));
								System.out.println("===================================================");

							}

						}

					} else {

						System.out.println("Erro:");
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
				
			case "4":
				
				System.out.println("Informações de conexão:"+carArea);

			default:

				System.out.println("Opcao não encontrada, tente novamente");
				break;

			}

		}

	}

	private void simulateApproach(Double latSta, Double longSta) {

		while (!(approximateNumber(latitudeCar).equals(latSta) && approximateNumber(longitudeCar).equals(longSta))) {

			if (approximateNumber(latitudeCar) < latSta) {

				latitudeCar += 0.01;

			} else if (approximateNumber(latitudeCar) > latSta) {

				latitudeCar -= 0.01;

			}

			if (approximateNumber(longitudeCar) < longSta) {

				longitudeCar += 0.01;

			} else if (approximateNumber(longitudeCar) > longSta) {

				longitudeCar -= 0.01;
			}

		}

	}

	/**
	 * 
	 * Metodo que ira receber a mensagem de retorno da nevoa
	 */
	public ResponseHttp messageReturn(String method, String endpoint, String httpVersion, Map<String, String> header,
			String currentIpApi) throws IOException {

		ResponseHttp response = Http.sendHTTPRequestAndGetHttpResponse(
				new RequestHttp(method, endpoint, httpVersion, header), currentIpApi);
		return response;

	}

	public void autoOff() {

		if (batteryCar == BatteryLevel.DISCHARGED.getBatteryLevel() && !executor.isShutdown()) {

			System.out.println("Bateria zerada programa encerrado");
			connected = false;
			executor.shutdown();

		}

	}

	public static void main(String[] args) throws IOException, UnableToConnectException {

		CarApp car = new CarApp();
		car.execCar();

	}

}
