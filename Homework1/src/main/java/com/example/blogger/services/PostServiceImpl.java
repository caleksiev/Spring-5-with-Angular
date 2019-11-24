package com.example.blogger.services;


import com.example.blogger.dao.PostRepository;
import com.example.blogger.exception.NonexisitngEntityException;
import com.example.blogger.model.Post;
import com.example.blogger.model.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.NoPermissionException;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    PostRepository postRepository;

    @Override
    public List<Post> findAll() {
        return postRepository.findAll();
    }

    @Override
    public Post findById(String postId) {
        return postRepository.findById(postId).orElseThrow(() -> new NonexisitngEntityException(
                String.format("Post with ID='%s' does not exist.", postId)));
    }

    @Override
    public Post add(Post post, UserPrincipal userPrincipal) {
        post.setAuthor(userPrincipal.getUser());
        return postRepository.insert(post);
    }

    @Override
    public Post update(Post post, UserPrincipal userPrincipal) throws NoPermissionException {
        Post foundPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new NonexisitngEntityException(String.format("Post with ID='%s' does not exist.", post.getId())));

        if (foundPost.getAuthor().getEmail().equals(userPrincipal.getUser().getEmail())) {
            post.setAuthor(userPrincipal.getUser());
            return postRepository.save(post);
        } else {
            throw new NoPermissionException("The user can update only his posts");
        }
    }

    @Override
    public Post remove(String postId, UserPrincipal userPrincipal) throws NoPermissionException {

        Post oldPost = postRepository.findById(postId)
                .orElseThrow(() -> new NonexisitngEntityException(String.format("Post with ID='%s' does not exist.", postId)));

        if (!oldPost.getAuthor().getEmail().equals(userPrincipal.getUser().getEmail())) {
            throw new NoPermissionException("The user can remove only his posts");

        }

        postRepository.deleteById(postId);
        return oldPost;
    }

}
