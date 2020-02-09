package com.fmi.spring5.web;

import com.fmi.spring5.exceptions.EntityAlreadyExistsException;
import com.fmi.spring5.model.Room;
import com.fmi.spring5.service.RoomServiceImpl;
import com.fmi.spring5.utils.FromToSlot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    @Autowired
    RoomServiceImpl roomService;

    @GetMapping
    @PostAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Iterable<Room>> getAllRooms() {
        return ResponseEntity.ok().body(roomService.getAll());
    }

    @GetMapping("/occupied/{room}")
    public ResponseEntity<Iterable<FromToSlot>> getAllRooms(@PathVariable String room) throws EntityAlreadyExistsException {
        return ResponseEntity.ok().body(roomService.getOccupiedSlots(room));
    }

    @GetMapping("/free")
    public ResponseEntity<Iterable<Room>> getAllRooms(@RequestBody FromToSlot fromToSlot) throws EntityAlreadyExistsException {
        return ResponseEntity.ok().body(roomService.getFreeRooms(fromToSlot));
    }

    @PostMapping
    @PostAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Room> createRoom(@Valid @RequestBody Room room) throws EntityAlreadyExistsException {
        Room addedRoom = roomService.addRoom(room);
        return ResponseEntity.created(
                ServletUriComponentsBuilder.fromCurrentRequest().pathSegment("{id}").build(addedRoom.getId()))
                .body(addedRoom);
    }

    @PutMapping
    @PostAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Room> updateRoom(@Valid @RequestBody Room room) throws EntityAlreadyExistsException {
        return ResponseEntity.ok().body(roomService.updateRoom(room));
    }

    @DeleteMapping("{room}")
    @PostAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Room> deleteRoom(@PathVariable String room) throws EntityAlreadyExistsException {
        roomService.removeRoom(room);
        return ResponseEntity.noContent().build();
    }
}

