package application.controllers;

import org.json.JSONObject;
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
import application.services.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;
	private ResponseEntity<String> response;

	@GetMapping("/amountQueue")
	public ResponseEntity<String> getSizeListPost() {

		JSONObject json = new JSONObject();
		json.put("amountCars", userService.getSizeList());
		return new ResponseEntity<String>(json.toString(), HttpStatus.OK);
	}

	@PostMapping("/queue/enter")
	public ResponseEntity<String> createUser(@RequestBody User user) {

		userService.addUser(user);
		return new ResponseEntity<String>("Usuario adicionado", HttpStatus.OK);
	}

	@DeleteMapping("/queue/leave/{id}")
	public ResponseEntity<String> deleteUser(@PathVariable String id) {

		if (userService.removeUser(id).isPresent()) {

			response = new ResponseEntity<String>("Usuario removido", HttpStatus.OK);

		} else {

			response = new ResponseEntity<String>("Nao encontrado", HttpStatus.NOT_FOUND);

		}

		return response;

	}
}
