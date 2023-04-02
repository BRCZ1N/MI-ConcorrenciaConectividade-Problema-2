package application.services;

import java.util.ArrayList;
import java.util.Optional;

import application.model.Post;

public class PostService {

	private ArrayList<Post> posts = new ArrayList<Post>();
	private long idPost = 0;

	public void addPost(Post post) {

		post.setId(Long.toString(idPost));
		idPost++;
		posts.add(post);

	}

	public Optional<Post> removePost(String id) {

		Optional<Post> post = Optional.ofNullable(getPost(id));
		if(post.isPresent()) {
			
			posts.remove(post.get());
			
		}
		
		return post;

	}

	public Post getPost(String id) {

		for (Post post : posts) {

			if (post.getId().equals(id)) {

				return post;

			}

		}

		return null;

	}

	public boolean authenticatePost(String id, String password) {

		for (Post post : posts) {

			if (post.getId().equals(id) && post.getPassword().equals(password)) {

				return true;

			}

		}

		return false;

	}

}
