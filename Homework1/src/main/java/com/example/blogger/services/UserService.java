package com.example.blogger.services;

import com.example.blogger.model.User;
import com.example.blogger.model.UserPrincipal;

import java.security.Principal;
import java.util.List;

interface UserService {
    List<User> findAll();

    User findById(String userId, UserPrincipal userPrincipal);

    User add(User user);

    User update(User user, UserPrincipal userPrincipal);

    User remove(String userId, UserPrincipal userPrincipal);
}
