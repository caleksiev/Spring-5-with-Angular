package com.fmi.spring5.repository;

import com.fmi.spring5.model.Dev;
import com.fmi.spring5.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DevRepository extends CrudRepository<Dev,String> {

    void deleteByUser(User user);

    Optional<Dev> findByUser(User user);
}
