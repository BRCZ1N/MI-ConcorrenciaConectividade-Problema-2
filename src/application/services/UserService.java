package application.services;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.stereotype.Service;

import application.model.User;

@Service
public class UserService {

	private ArrayList<User> users = new ArrayList<User>();
	private long idClient = 0;

	public void addUser(User user) {

		user.setId(Long.toString(idClient));
		idClient++;
		users.add(user);

	}

	public Optional<User> removerUser(String id) {

		return Optional.ofNullable(getUser(id));

	}

	public User getUser(String id) {

		for (User user : users) {

			if (user.getId().equals(id)) {

				return user;

			}

		}

		return null;

	}

	public int getSizeList() {

		return users.size() - 1;

	}

}
