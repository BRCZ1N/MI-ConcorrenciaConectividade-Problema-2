package application.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import application.model.User;
import application.services.ChargingStationService;
import application.services.UserService;

@RestController
@RequestMapping("/station")
public class ChargingStationController {
	
	@Autowired
	private ChargingStationService chargingStationService;
	private ResponseEntity<String> response;

	@GetMapping("/info/location?x={locationX}&y={locationY}")
	public ResponseEntity<String> getInfoStationNear(@PathVariable double locationX, @PathVariable double locationY) {

		JSONObject json = new JSONObject();
		json.put("name",);
		json.put("addressX",);
		json.put("addressY",);
		json.put("totalAmountCars",);
		return new ResponseEntity<String>(json.toString(), HttpStatus.OK);
	}
	
	@GetMapping("/info/all/location?x={locationX}&y={locationY}")
	public ResponseEntity<String> getInfoStationAll(@PathVariable double locationX, @PathVariable double locationY) {

		JSONObject json = new JSONObject();
		json.put("name",);
		json.put("addressX",);
		json.put("addressY",);
		json.put("totalAmountCars",);
		return new ResponseEntity<String>(json.toString(), HttpStatus.OK);
	}

	
}
