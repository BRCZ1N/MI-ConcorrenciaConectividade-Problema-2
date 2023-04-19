package application.controllers;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import application.model.ChargingStationModel;
import application.services.ChargingStationService;

@RestController
@Component
@RequestMapping("/station")
public class ChargingStationController {

	private ChargingStationService chargingStationService;
	
	public ChargingStationController(ChargingStationService service) {
		
		this.chargingStationService = service;
		
	}

	public ChargingStationService getChargingStationService() {
		return chargingStationService;
	}

	public void setChargingStationService(ChargingStationService chargingStationService) {
		this.chargingStationService = chargingStationService;
	}
	
	public void addStation(ChargingStationModel station) {
		
		chargingStationService.addStation(station);
	
	}

	@GetMapping("/ping")
	public HttpEntity<String> getPingServer() {

		return ResponseEntity.ok("Conexao aceita");

	}

	@GetMapping("/shorterQueue")
	public HttpEntity<String> getShorterQueueStation() {

		return chargingStationService.getShorterQueueStation().map(station -> ResponseEntity.ok(station.toString())).orElse(ResponseEntity.notFound().build());

	}

	@GetMapping("/bestLocation/location?x={locationX}&y={locationY}")
	public HttpEntity<String> getBestLocationStation(@RequestParam double locationX, @RequestParam double locationY) {

		return chargingStationService.getBestLocationStation(locationX, locationY)
				.map(station -> ResponseEntity.ok(station.toString())).orElse(ResponseEntity.notFound().build());

	}

	@GetMapping("/all")
	public HttpEntity<String> getAllStations() {

		return chargingStationService.getAllStations().map(stations -> ResponseEntity.ok(stations.toString()))
				.orElse(ResponseEntity.notFound().build());

	}

}
