package com.fmi.spring5.repository;

import com.fmi.spring5.model.CompanyEvent;
import com.fmi.spring5.model.Room;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyEventRepository extends CrudRepository<CompanyEvent, String> {

    List<CompanyEvent> getAllByRoom(Room room);

    Optional<CompanyEvent> findByTitle(String title);
}
