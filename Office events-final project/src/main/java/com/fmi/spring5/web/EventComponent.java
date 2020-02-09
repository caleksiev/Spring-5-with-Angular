package com.fmi.spring5.web;


import com.fmi.spring5.exceptions.EntityAlreadyExistsException;
import com.fmi.spring5.exceptions.NoSuchEntityException;
import com.fmi.spring5.model.CompanyEvent;
import com.fmi.spring5.model.Room;
import com.fmi.spring5.model.TeamEvent;
import com.fmi.spring5.service.EventServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.naming.NoPermissionException;
import javax.validation.Valid;

@RestController
@RequestMapping("/events")
public class EventComponent {
    @Autowired
    EventServiceImpl eventService;

    @GetMapping("/company")
    public ResponseEntity<Iterable<CompanyEvent>> getAllCompanyEvents() {
        return ResponseEntity.ok().body(eventService.getAllCompanyEvents());
    }

    @GetMapping("/{team}")
    public ResponseEntity<Iterable<TeamEvent>> getAllTeamEvents(@PathVariable String team) throws NoPermissionException, NoSuchEntityException {
        return ResponseEntity.ok().body(eventService.getAllTeamEvents((team)));
    }

    @PostMapping("/company")
    public ResponseEntity<CompanyEvent> createCompanyEvent(@Valid @RequestBody CompanyEvent companyEvent) throws EntityAlreadyExistsException {
        CompanyEvent addedCompanyEvent = eventService.addCompanyEvent(companyEvent);
        return ResponseEntity.created(
                ServletUriComponentsBuilder.fromCurrentRequest().pathSegment("{id}").build(addedCompanyEvent.getId()))
                .body(addedCompanyEvent);
    }

    @PostMapping("/{team}")
    //Team must be set for managers, becouse they have multiple teams
    public ResponseEntity<TeamEvent> createTeamEvent(@PathVariable String team, @Valid @RequestBody TeamEvent teamEvent) throws EntityAlreadyExistsException, NoPermissionException, NoSuchEntityException {
        TeamEvent addedTeamEvent = eventService.addTeamEvent(teamEvent, team);
        return ResponseEntity.created(
                ServletUriComponentsBuilder.fromCurrentRequest().pathSegment("{id}").build(addedTeamEvent.getId()))
                .body(addedTeamEvent);
    }

    @PutMapping("/company")
    public ResponseEntity<CompanyEvent> updateCompanyEvent(@Valid @RequestBody CompanyEvent companyEvent) throws NoPermissionException, NoSuchEntityException {
        return ResponseEntity.ok().body(eventService.updateCompanyEvent(companyEvent));
    }

    @PutMapping("/team")
    public ResponseEntity<TeamEvent> updateTeamEvent(@Valid @RequestBody TeamEvent teamEvent) throws NoPermissionException {
        return ResponseEntity.ok().body(eventService.updateTeamEvent(teamEvent));
    }

    @DeleteMapping("/{company}")
    public ResponseEntity deleteCompanyEvent(@PathVariable String company) throws NoPermissionException, NoSuchEntityException {
        eventService.deleteCompanyEvent(company);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{team}")
    public ResponseEntity deleteTeamEvent(@PathVariable String team) throws NoPermissionException, NoSuchEntityException {
        eventService.deleteTeamEvent(team);
        return ResponseEntity.noContent().build();

    }

}
