package com.example.blogger.services;

import com.example.blogger.model.User;
import com.example.blogger.model.UserPrincipal;
import org.springframework.security.access.prepost.PostFilter;

import javax.naming.NoPermissionException;
import java.security.Principal;
import java.util.List;

interface UserService {
    @PostFilter("hasRole('ADMIN') or (hasRole('BLOGGER') and filterObject.email == authentication.name)")
    List<User> findAll();

    User findById(String userId, UserPrincipal userPrincipal) throws NoPermissionException;

    User add(User user);

    User update(User user, UserPrincipal userPrincipal) throws NoPermissionException;

    User remove(String userId, UserPrincipal userPrincipal) throws NoPermissionException;

    User changeStatus(String userId) throws NoPermissionException;

    User getCurrentLoggedInUser();
}
