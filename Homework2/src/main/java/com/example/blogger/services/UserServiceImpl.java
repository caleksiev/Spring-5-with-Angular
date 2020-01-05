package com.example.blogger.services;

import com.example.blogger.dao.UserRepository;
import com.example.blogger.exception.InvalidEntityException;
import com.example.blogger.exception.NonexisitngEntityException;
import com.example.blogger.model.User;
import com.example.blogger.model.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    public User findById(String userId, UserPrincipal userPrincipal) throws NoPermissionException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = ((UserPrincipal) principal).getUser();
        if (!isOwner(userId, user)) {
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
    public User update(User user, UserPrincipal userPrincipal) throws NoPermissionException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userP = ((UserPrincipal) principal).getUser();
        if (!isOwner(user.getId(), userP)) {
            throw new NoPermissionException("The user can see and change only his own profile.");
        }
        userRepository.findById(user.getId())
                .orElseThrow(() -> new NonexisitngEntityException(String.format("User with ID='%s' does not exist.", user.getId())));
        return userRepository.save(user);
    }

    @Override
    public User remove(String userId, UserPrincipal userPrincipal) throws NoPermissionException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userP = ((UserPrincipal) principal).getUser();
        if (!isOwner(userId, userP)) {
            throw new NoPermissionException("The user can see and change only his own profile.");
        }
        User oldUser = userRepository.findById(userId)
                .orElseThrow(() -> new NonexisitngEntityException(String.format("User with ID='%s' does not exist.", userId)));

        userRepository.deleteById(userId);
        return oldUser;
    }

    @Override
    public User changeStatus(String userId) throws NoPermissionException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userP = ((UserPrincipal) principal).getUser();
        if (!isOwner(userId, userP)) {
            throw new NoPermissionException("The user can see and change only his own profile.");
        }
        User oldUser = userRepository.findById(userId)
                .orElseThrow(() -> new NonexisitngEntityException(String.format("User with ID='%s' does not exist.", userId)));
        oldUser.setActive(!oldUser.isActive());
        userRepository.save(oldUser);
        return oldUser;
    }

    @Override
    public User getCurrentLoggedInUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ((UserPrincipal) principal).getUser();
    }

    private boolean isOwner(String userId, User userPrincipal) {
        if (userPrincipal.getRole().equals("ADMIN")) {
            return true;
        }
        String userPrincipalId = userRepository.findByEmail(userPrincipal.getEmail()).get().getId();
        return userPrincipalId.equals(userId);
    }
}
