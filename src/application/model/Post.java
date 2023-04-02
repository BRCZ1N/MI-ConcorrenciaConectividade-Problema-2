package application.model;

public class Post {

	private String namePost;
	private String addressPost;
	private String totalAmountCars;

	public Post(String namePost, String addressPost, String totalAmountCars) {

		this.namePost = namePost;
		this.addressPost = addressPost;
		this.totalAmountCars = totalAmountCars;
	}

	public String getNamePost() {
		return namePost;
	}

	public void setNamePost(String namePost) {
		this.namePost = namePost;
	}

	public String getAddressPost() {
		return addressPost;
	}

	public void setAddressPost(String addressPost) {
		this.addressPost = addressPost;
	}

	public String getTotalAmountCars() {
		return totalAmountCars;
	}

	public void setTotalAmountCars(String totalAmountCars) {
		this.totalAmountCars = totalAmountCars;
	}

}
