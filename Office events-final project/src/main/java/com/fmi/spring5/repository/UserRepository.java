package com.fmi.spring5.repository;

import com.fmi.spring5.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends CrudRepository<User, String> {
     Optional<User> findUserByUsername(String username);
}
