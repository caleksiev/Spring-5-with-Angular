package com.example.blogger.services;

import com.example.blogger.dao.UserRepository;
import com.example.blogger.exception.InvalidEntityException;
import com.example.blogger.exception.NonexisitngEntityException;
import com.example.blogger.model.User;
import com.example.blogger.model.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(String userId, UserPrincipal principal) {
        return userRepository.findById(userId).orElseThrow(() -> new NonexisitngEntityException(
                String.format("User with ID='%s' does not exist.", userId)));
    }

    @Override
    public User add(User user) {

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new InvalidEntityException(String.format("User with email '%s' already exists", user.getEmail()));
        }
        return userRepository.insert(user);
    }

    @Override
    public User update(User user,UserPrincipal principal) {
        userRepository.findById(user.getId())
                .orElseThrow(() -> new NonexisitngEntityException(String.format("User with ID='%s' does not exist.", user.getId())));

        return userRepository.save(user);
    }

    @Override
    public User remove(String userId,UserPrincipal principal) {
        User oldUser = userRepository.findById(userId)
                .orElseThrow(() -> new NonexisitngEntityException(String.format("User with ID='%s' does not exist.", userId)));

        userRepository.deleteById(userId);
        return oldUser;
    }
}
