package com.fmi.spring5.repository;

import com.fmi.spring5.model.Manager;
import com.fmi.spring5.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ManagerRepository extends CrudRepository<Manager, String> {

    void deleteByUser(User user);

    Optional<Manager> findByUser(User user);

}

