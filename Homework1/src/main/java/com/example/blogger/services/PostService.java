package com.example.blogger.services;

import com.example.blogger.model.Post;
import com.example.blogger.model.UserPrincipal;

import javax.naming.NoPermissionException;
import java.util.List;

public interface PostService {
    List<Post> findAll();

    Post findById(String postId);

    Post add(Post post,UserPrincipal principal);

    Post update(Post post, UserPrincipal userPrincipal) throws NoPermissionException;

    Post remove(String postId, UserPrincipal userPrincipal) throws NoPermissionException;
}
