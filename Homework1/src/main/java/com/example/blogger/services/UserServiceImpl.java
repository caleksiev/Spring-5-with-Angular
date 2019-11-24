package com.example.blogger.services;

import com.example.blogger.dao.UserRepository;
import com.example.blogger.exception.InvalidEntityException;
import com.example.blogger.exception.NonexisitngEntityException;
import com.example.blogger.model.User;
import com.example.blogger.model.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.NoPermissionException;
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
    public User findById(String userId, UserPrincipal principal) throws NoPermissionException {
        if (!isOwner(userId, principal)) {
            throw new NoPermissionException("The user can see and change only his own profile.");
        }
        return userRepository.findById(userId).orElseThrow(() -> new NonexisitngEntityException(
                String.format("User with ID='%s' does not exist.", userId)));
    }

    @Override
    public User add(User user) {

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new InvalidEntityException(String.format("User with email '%s' already exists", user.getEmail()));
        }

        user.setPassword(new BCryptPasswordEncoder(10).encode(user.getPassword()));
        return userRepository.insert(user);
    }

    @Override
    public User update(User user, UserPrincipal principal) throws NoPermissionException {
        if (!isOwner(user.getId(), principal)) {
            throw new NoPermissionException("The user can see and change only his own profile.");
        }
        userRepository.findById(user.getId())
                .orElseThrow(() -> new NonexisitngEntityException(String.format("User with ID='%s' does not exist.", user.getId())));

        return userRepository.save(user);
    }

    @Override
    public User remove(String userId, UserPrincipal principal) throws NoPermissionException {

        if (!isOwner(userId, principal)) {
            throw new NoPermissionException("The user can see and change only his own profile.");
        }
        User oldUser = userRepository.findById(userId)
                .orElseThrow(() -> new NonexisitngEntityException(String.format("User with ID='%s' does not exist.", userId)));

        userRepository.deleteById(userId);
        return oldUser;
    }

    private boolean isOwner(String userId, UserPrincipal userPrincipal) {
        if (userPrincipal.getUser().getRole().equals("ADMIN")) {
            return true;
        }
        String userPrincipalId = userRepository.findByEmail(userPrincipal.getUser().getEmail()).get().getId();
        return userPrincipalId.equals(userId);
    }
}
