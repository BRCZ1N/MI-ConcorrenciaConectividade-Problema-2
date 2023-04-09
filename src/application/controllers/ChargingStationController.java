package application.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import application.services.ChargingStationService;

@RestController
@RequestMapping("/station")
public class ChargingStationController {

	@Autowired
	private ChargingStationService chargingStationService;

	@GetMapping("/shorterQueue")
	public ResponseEntity<String> getShorterQueueStation() {

		return chargingStationService.getShorterQueueStation().map(station -> ResponseEntity.ok(station.toString())).orElse(ResponseEntity.notFound().build());

	}

	@GetMapping("/bestLocation/location?x={locationX}&y={locationY}")
	public ResponseEntity<String> getBestLocationStation(@PathVariable double locationX,
			@PathVariable double locationY) {

		return chargingStationService.getBestLocationStation(locationX, locationY).map(station -> ResponseEntity.ok(station.toString())).orElse(ResponseEntity.notFound().build());

	}

	@GetMapping("/all")
	public ResponseEntity<String> getAllStations() {

		return chargingStationService.getAllStations().map(stations -> ResponseEntity.ok(stations.toString())).orElse(ResponseEntity.notFound().build());

	}

}
