package com.fmi.spring5.repository;

import com.fmi.spring5.model.Manager;
import com.fmi.spring5.model.Team;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends CrudRepository<Team,String> {
    Optional<Team> findByName(String teamName);

    List<Team> findByManager(Manager manager);

}
