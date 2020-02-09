package com.fmi.spring5.service;

import com.fmi.spring5.exceptions.EntityAlreadyExistsException;
import com.fmi.spring5.model.CompanyEvent;
import com.fmi.spring5.model.Room;
import com.fmi.spring5.model.TeamEvent;
import com.fmi.spring5.repository.CompanyEventRepository;
import com.fmi.spring5.repository.RoomRepository;
import com.fmi.spring5.repository.TeamEventsRepository;
import com.fmi.spring5.utils.FromToSlot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    TeamEventsRepository teamEventsRepository;

    @Autowired
    CompanyEventRepository companyEventRepository;

    @Override
    public Room addRoom(Room room) throws EntityAlreadyExistsException {
        Optional<Room> foundRoom = roomRepository.findByRoomName(room.getRoomName());

        if (foundRoom.isPresent()) {
            throw new EntityAlreadyExistsException(String.format("There is already a room with name: '%s'.", room.getRoomName()));
        }

        return roomRepository.save(room);
    }

    @Override
    @Transactional
    //Check if works
    public void removeRoom(String roomName) throws EntityAlreadyExistsException {
        Room foundRoom = roomRepository.findByRoomName(roomName)
                .orElseThrow(() -> new EntityAlreadyExistsException(String.format("There is already a room with name: '%s'.", roomName)));

        roomRepository.delete(foundRoom);
    }

    @Override
    public Room updateRoom(Room room) throws EntityAlreadyExistsException {
        Room foundRoom = roomRepository.findByRoomName(room.getRoomName())
                .orElseThrow(() -> new EntityAlreadyExistsException(String.format("There is already a room with name: '%s'.", room.getRoomName())));

        room.setId(foundRoom.getId());
        return roomRepository.save(room);
    }

    @Override
    public Room findRoom(String roomName) throws EntityAlreadyExistsException {
        return roomRepository.findByRoomName(roomName)
                .orElseThrow(() -> new EntityAlreadyExistsException(String.format("There is already a room with name: '%s'.", roomName)));

    }

    @Override
    public Iterable<Room> getAll() {
        return roomRepository.findAll();
    }

    @Override
    public List<FromToSlot> getOccupiedSlots(String roomName) throws EntityAlreadyExistsException {

        List<FromToSlot> fromToSlots = new ArrayList<>();

        Room foundRoom = roomRepository.findByRoomName(roomName)
                .orElseThrow(() -> new EntityAlreadyExistsException(String.format("There is already a room with name: '%s'.", roomName)));

        List<TeamEvent> teamEventByRoom = teamEventsRepository.getAllByRoom(foundRoom);
        List<CompanyEvent> companyEventByRoom = companyEventRepository.getAllByRoom(foundRoom);

        for (TeamEvent teamEvent : teamEventByRoom) {
            fromToSlots.add(new FromToSlot(teamEvent.getFromDate(), teamEvent.getToDate()));
        }

        for (CompanyEvent companyEvent : companyEventByRoom) {
            fromToSlots.add(new FromToSlot(companyEvent.getFromDate(), companyEvent.getToDate()));
        }
        return fromToSlots;

    }

    @Override
    public List<Room> getFreeRooms(FromToSlot fromToSlot) {
        LocalDateTime from = fromToSlot.getFrom();
        LocalDateTime to = fromToSlot.getTo();
        Iterable<Room> rooms = roomRepository.findAll();
        List<Room> freeRooms = new ArrayList<>();
        boolean isRoomFree = false;

        for (Room room : rooms) {
            List<TeamEvent> teamEventByRoom = teamEventsRepository.getAllByRoom(room);
            List<CompanyEvent> companyEventByRoom = companyEventRepository.getAllByRoom(room);

            for (TeamEvent teamEvent : teamEventByRoom) {
                LocalDateTime fromDate = teamEvent.getFromDate();
                LocalDateTime toDate = teamEvent.getToDate();

                if (fromDate.isBefore(from) || toDate.isAfter(to)) {
                    isRoomFree = false;
                    break;
                } else {
                    isRoomFree = true;
                }
            }

            for (CompanyEvent companyEvent : companyEventByRoom) {
                LocalDateTime fromDate = companyEvent.getFromDate();
                LocalDateTime toDate = companyEvent.getToDate();

                if (fromDate.isBefore(from) || toDate.isAfter(to)) {
                    isRoomFree = false;
                    break;
                } else {
                    isRoomFree = true;
                }
            }

            if (isRoomFree) {
                freeRooms.add(room);
            }
        }
        return freeRooms;
    }
}
