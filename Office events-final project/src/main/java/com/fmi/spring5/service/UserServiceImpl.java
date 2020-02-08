package com.fmi.spring5.service;

import com.fmi.spring5.exceptions.EntityAlreadyExistsException;
import com.fmi.spring5.exceptions.InvalidArgumentException;
import com.fmi.spring5.exceptions.NoSuchEntityException;
import com.fmi.spring5.model.Admin;
import com.fmi.spring5.model.Dev;
import com.fmi.spring5.model.Manager;
import com.fmi.spring5.model.User;
import com.fmi.spring5.repository.AdminRepository;
import com.fmi.spring5.repository.DevRepository;
import com.fmi.spring5.repository.ManagerRepository;
import com.fmi.spring5.repository.UserRepository;
import com.fmi.spring5.utils.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private ManagerRepository managerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DevRepository devRepository;
    @Autowired
    private AdminRepository adminRepository;

    @Override
    public Dev addUser(User user) throws EntityAlreadyExistsException {
        userRepository.findUserByUsername(user.getUsername())
                .orElseThrow(() -> new EntityAlreadyExistsException(String.format("The user with name: '%s' already exist", user.getUsername())));

        return devRepository.save(new Dev(user));
    }

    @Override
    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUser(String username) throws NoSuchEntityException {
        return userRepository.findUserByUsername(username).
                orElseThrow(() -> new NoSuchEntityException(String.format("The user with name: '%s' doesn't exist", username)));
    }

    @Override
    public String getUserRole(String username) throws NoSuchEntityException {
        User foundUser = userRepository.findUserByUsername(username).
                orElseThrow(() -> new NoSuchEntityException(String.format("The user with name: '%s' doesn't exist", username)));

        return foundUser.getRole();
    }

    @Override
    @Transactional
    public void changeUserRole(String username, String newRole) throws InvalidArgumentException, NoSuchEntityException {
        if (!newRole.equals(Role.ADMIN.getStringRole()) && !newRole.equals(Role.MANAGER.getStringRole())
                && !newRole.equals(Role.DEV.getStringRole())) {
            throw new InvalidArgumentException(String.format("Role: '%s' doesn't exists", newRole));
        }

        User foundUser = userRepository.findUserByUsername(username).
                orElseThrow(() -> new NoSuchEntityException(String.format("User with ID='%s' does not exist.", username)));

        String userRole = foundUser.getRole();

        if (userRole.equals(Role.ADMIN.getStringRole())) {
            if (newRole.equals(Role.MANAGER.getStringRole())) {
                adminRepository.deleteAdminByUser(foundUser);
                managerRepository.save(new Manager(foundUser));
            } else if (newRole.equals(Role.DEV.getStringRole())) {
                adminRepository.deleteAdminByUser(foundUser);
                devRepository.save(new Dev(foundUser));
            }
        } else if (userRole.equals(Role.MANAGER.getStringRole())) {
            if (newRole.equals(Role.ADMIN.getStringRole())) {
                managerRepository.deleteByUser(foundUser);
                adminRepository.save(new Admin(foundUser));
            } else if (newRole.equals(Role.DEV.getStringRole())) {
                managerRepository.deleteByUser(foundUser);
                devRepository.save(new Dev(foundUser));
            }
        } else {
            if (newRole.equals(Role.ADMIN.getStringRole())) {
                devRepository.deleteByUser(foundUser);
                adminRepository.save(new Admin(foundUser));
            } else if (newRole.equals(Role.MANAGER.getStringRole())) {
                devRepository.deleteByUser(foundUser);
                managerRepository.save(new Manager(foundUser));
            }
        }
    }

    @Override
    public User update(User user) throws NoSuchEntityException {
        User foundUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new NoSuchEntityException(String.format("User with ID='%s' does not exist.", user.getId())));

        user.setId(foundUser.getId());
        user.setRole(foundUser.getRole());
        return userRepository.save(user);
    }

}
