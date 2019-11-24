package com.example.blogger.dao;


import com.example.blogger.model.Post;
import com.example.blogger.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PostRepository extends MongoRepository<Post, String> {
    Optional<Post> findByAuthor(User author);
}
