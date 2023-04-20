package application.model;

public class UserModel {
	/**
	 * ID do usuário.
	 */
	private String id;

	/**
	 * Nome do usuário.
	 */
	private String name;

	/**
	 * Localização do usuário.
	 */
	private String location;

	/**
	 * Construtor da classe UserModel.
	 * 
	 * @param name     O nome do usuário.
	 * @param location A localização do usuário.
	 */
	public UserModel(String name, String location) {

		this.name = name;
		this.location = location;

	}

	/**
	 * Retorna o ID do usuário.
	 * 
	 * @return ID do usuário.
	 */
	public String getId() {
		return id;
	}

	/**
	 * Define o ID do usuário.
	 * 
	 * @param id ID do usuário.
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Retorna o nome do usuário.
	 * 
	 * @return Nome do usuário.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Define o nome do usuário.
	 * 
	 * @param name Nome do usuário.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Retorna a localização do usuário.
	 * 
	 * @return Localização do usuário.
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * Define a localização do usuário.
	 * 
	 * @param location Localização do usuário.
	 */
	public void setLocation(String location) {
		this.location = location;
	}
}
