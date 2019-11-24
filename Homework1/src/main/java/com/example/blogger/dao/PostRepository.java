package com.example.blogger.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostRepository extends MongoRepository<PostRepository, String> {
}
