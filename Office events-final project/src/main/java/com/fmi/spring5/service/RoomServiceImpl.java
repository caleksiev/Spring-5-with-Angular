package com.fmi.spring5.service;

import com.fmi.spring5.exceptions.EntityAlreadyExistsException;
import com.fmi.spring5.model.CompanyEvent;
import com.fmi.spring5.model.Room;
import com.fmi.spring5.model.TeamEvent;
import com.fmi.spring5.repository.CompanyEventRepository;
import com.fmi.spring5.repository.RoomRepository;
import com.fmi.spring5.repository.TeamEventsRepository;
import com.fmi.spring5.utils.FromToSlot;
import com.fmi.spring5.utils.FromToSlotRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
        Optional<Room> foundRoom = roomRepository.findByRoomName(room.getRoomName());

        if (foundRoom.isPresent() && !foundRoom.get().getId().equals(room.getId())) {
            throw new EntityAlreadyExistsException(String.format("There is already a room with name: '%s'.", room.getRoomName()));
        }

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
    public List<Room> getFreeRooms(FromToSlotRequest fromToSlot) throws ParseException {


        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date from = dateFormat.parse(fromToSlot.getFromDate());
        Date to = dateFormat.parse(fromToSlot.getToDate());
        Iterable<Room> rooms = roomRepository.findAll();
        List<Room> freeRooms = new ArrayList<>();
        boolean isRoomFree = true;

        for (Room room : rooms) {
            List<TeamEvent> teamEventByRoom = teamEventsRepository.getAllByRoom(room);
            List<CompanyEvent> companyEventByRoom = companyEventRepository.getAllByRoom(room);

            for (TeamEvent teamEvent : teamEventByRoom) {
                Date fromDate = teamEvent.getFromDate();
                Date toDate = teamEvent.getToDate();

                if (fromDate.before(from) || toDate.after(to)) {
                    isRoomFree = false;
                    break;
                } else {
                    isRoomFree = true;
                }
            }

            for (CompanyEvent companyEvent : companyEventByRoom) {
                Date fromDate = companyEvent.getFromDate();
                Date toDate = companyEvent.getToDate();

                if (fromDate.before(from) || toDate.after(to)) {
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
