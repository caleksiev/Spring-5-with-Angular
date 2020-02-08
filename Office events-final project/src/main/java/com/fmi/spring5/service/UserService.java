package com.fmi.spring5.service;

import com.fmi.spring5.exceptions.EntityAlreadyExistsException;
import com.fmi.spring5.exceptions.InvalidArgumentException;
import com.fmi.spring5.exceptions.NoSuchEntityException;
import com.fmi.spring5.model.Dev;
import com.fmi.spring5.model.User;


public interface UserService {
    Dev addUser(User user) throws EntityAlreadyExistsException; // only users with role dev can be created, then the admins can change the role

    Iterable<User> getAllUsers();

    User getUser(String username) throws NoSuchEntityException;

    String getUserRole(String username) throws NoSuchEntityException;

    void changeUserRole(String username, String newRole) throws InvalidArgumentException, NoSuchEntityException;

    User update(User user ) throws NoSuchEntityException;
}
