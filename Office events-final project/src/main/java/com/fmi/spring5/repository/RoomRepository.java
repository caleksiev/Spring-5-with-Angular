package com.fmi.spring5.repository;

import com.fmi.spring5.model.Room;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomRepository extends CrudRepository<Room,String> {
    Optional<Room> findByRoomName(String roomName);
}
