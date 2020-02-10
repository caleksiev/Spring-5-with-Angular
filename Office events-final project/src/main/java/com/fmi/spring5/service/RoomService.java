package com.fmi.spring5.service;

import com.fmi.spring5.exceptions.EntityAlreadyExistsException;
import com.fmi.spring5.model.Room;
import com.fmi.spring5.utils.FromToSlot;
import com.fmi.spring5.utils.FromToSlotRequest;

import java.math.RoundingMode;
import java.text.ParseException;
import java.util.List;

public interface RoomService {

    Room addRoom(Room room) throws EntityAlreadyExistsException;

    void removeRoom(String roomName) throws EntityAlreadyExistsException;

    Room updateRoom(Room room) throws EntityAlreadyExistsException;

    Room findRoom(String roomName) throws EntityAlreadyExistsException;

    Iterable<Room> getAll();

    List<FromToSlot> getOccupiedSlots(String roomName) throws EntityAlreadyExistsException;

    List<Room> getFreeRooms(FromToSlotRequest fromToSlot) throws ParseException;
}
