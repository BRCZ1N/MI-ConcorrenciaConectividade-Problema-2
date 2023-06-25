/**

Esta classe é responsável por definir os endpoints para as requisições relacionadas às estações de carregamento de veículos elétricos.
Os endpoints implementam os métodos HTTP GET para retornar informações sobre a estação de carregamento, como a fila mais curta, a melhor localização e todas as estações disponíveis.
Os métodos desta classe utilizam a classe ChargingStationService para acessar os dados das estações de carregamento e retornar as informações solicitadas em formato JSON.
*/
package application.controllers;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import application.model.ChargingStationModel;
import application.services.ChargingStationService;

@RestController
@ComponentScan("application.services")
@RequestMapping("/station")
public class ChargingStationController {

	@Autowired
	private static ChargingStationService chargingStationService;
	/**
	 * Método para obter a instância do serviço ChargingStationService.
	 * 
	 * @return a instância do ChargingStationService.
	 */
	public static ChargingStationService getChargingStationService() {
		
		return chargingStationService;
		
	}
	/**
	 * Método para adicionar uma estação de carregamento.
	 * 
	 * @param station a estação de carregamento a ser adicionada.
	 */
	public static void addStationLocal(ChargingStationModel station) {
	
		ChargingStationService.addStationLocal(station);
	
	}
	
	public static void addStationGlobal(String id, ChargingStationModel station) {
		
		ChargingStationService.addStationGlobal(id,station);
	
	}
	/**
	 * Endpoint para obter a estação de carregamento com a fila mais curta.
	 * 
	 * @return uma HttpEntity contendo as informações da estação de carregamento com a fila mais curta em formato JSON.
	 */
	@GetMapping("/shorterQueue")
	public HttpEntity<String> getShorterQueueStation() {

		return ChargingStationService.getShorterQueueStation().map(station -> ResponseEntity.ok(new JSONObject(station).toString())).orElse(ResponseEntity.notFound().build());

	}
	/**
	 * Endpoint para obter a estação de carregamento com a melhor localização em relação a um ponto (x,y) especificado.
	 * 
	 * @param locationX a coordenada x do ponto de referência.
	 * @param locationY a coordenada y do ponto de referência.
	 * @return uma HttpEntity contendo as informações da estação de carregamento com a melhor localização em formato JSON.
	 */
	@GetMapping("/bestLocation/locationX={locationX}&locationY={locationY}")
	public HttpEntity<String> getBestLocationStation(@PathVariable double locationX, @PathVariable double locationY) {

		return ChargingStationService.getBestLocationStation(locationX, locationY).map(station -> ResponseEntity.ok(new JSONObject(station).toString())).orElse(ResponseEntity.notFound().build());

	}
	
	@GetMapping("/global/bestStations")
	public HttpEntity<String> getBestLocationStation() {

		return ChargingStationService.getAllGlobalBestStations().map(station -> ResponseEntity.ok(new JSONArray(station).toString())).orElse(ResponseEntity.notFound().build());

	}

}
