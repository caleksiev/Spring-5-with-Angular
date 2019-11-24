package com.example.blogger.services;

import com.example.blogger.model.User;
import com.example.blogger.model.UserPrincipal;

import javax.naming.NoPermissionException;
import java.security.Principal;
import java.util.List;

interface UserService {
    List<User> findAll();

    User findById(String userId, UserPrincipal userPrincipal) throws NoPermissionException;

    User add(User user);

    User update(User user, UserPrincipal userPrincipal) throws NoPermissionException;

    User remove(String userId, UserPrincipal userPrincipal) throws NoPermissionException;
}
