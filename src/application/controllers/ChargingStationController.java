package application.controllers;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import application.model.ChargingStationModel;
import application.services.ChargingStationService;

@RestController
@ComponentScan("application.services")
@RequestMapping("/station")
public class ChargingStationController {

	@Autowired
	private static ChargingStationService chargingStationService;

	public static ChargingStationService getChargingStationService() {
		
		return chargingStationService;
		
	}
	
	public static void addStation(ChargingStationModel station) {
	
		ChargingStationService.addStation(station);
	
	}

	@GetMapping("/shorterQueue")
	public HttpEntity<String> getShorterQueueStation() {

		return ChargingStationService.getShorterQueueStation().map(station -> ResponseEntity.ok(new JSONObject(station).toString())).orElse(ResponseEntity.notFound().build());

	}

	@GetMapping("/bestLocation/location?x={locationX}&y={locationY}")
	public HttpEntity<String> getBestLocationStation(@RequestParam double locationX, @RequestParam double locationY) {

		return ChargingStationService.getBestLocationStation(locationX, locationY).map(station -> ResponseEntity.ok(new JSONObject(station).toString())).orElse(ResponseEntity.notFound().build());

	}

	@GetMapping("/all")
	public HttpEntity<String> getAllStations() {

		return ChargingStationService.getAllStations().map(stations -> ResponseEntity.ok(stations.toString())).orElse(ResponseEntity.notFound().build());

	}

}
