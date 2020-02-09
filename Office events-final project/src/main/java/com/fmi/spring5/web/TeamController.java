package com.fmi.spring5.web;

import com.fmi.spring5.exceptions.EntityAlreadyExistsException;
import com.fmi.spring5.exceptions.InvalidArgumentException;
import com.fmi.spring5.exceptions.NoSuchEntityException;
import com.fmi.spring5.model.Team;
import com.fmi.spring5.service.TeamServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.naming.NoPermissionException;
import javax.validation.Valid;

@RestController
@RequestMapping("/teams")
public class TeamController {

    @Autowired
    TeamServiceImp teamService;

    @GetMapping
    @PostAuthorize("hasRole('ADMIN')")
    ResponseEntity<Iterable<Team>> getAllTeams() {
        return ResponseEntity.ok(teamService.getAllTeams());
    }

    @PostMapping
    @PostAuthorize("hasRole('ADMIN')")
    ResponseEntity<Team> addTeam(@Valid @RequestBody Team team) throws EntityAlreadyExistsException, InvalidArgumentException {

        Team addedTeam = teamService.addTeam(team);
        return ResponseEntity.created(
                ServletUriComponentsBuilder.fromCurrentRequest().pathSegment("{id}").build(addedTeam.getId()))
                .body(addedTeam);
    }

    @PutMapping
    @PostAuthorize("hasRole('ADMIN')")
    ResponseEntity<Team> updateTeam(@Valid @RequestBody Team team) throws NoSuchEntityException {
        return ResponseEntity.ok().body(teamService.update(team));
    }

    @DeleteMapping("/{team}")
    @PostAuthorize("hasRole('ADMIN')")
    ResponseEntity<Team> deleteTeam(@PathVariable String team) throws NoSuchEntityException {
        teamService.removeTeam(team);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/dev/{username}")
    @PostAuthorize("hasRole('MANAGER')")
    public ResponseEntity addDevToTeam(@PathVariable String username, @RequestParam("team") String teamName) throws NoPermissionException, NoSuchEntityException, EntityAlreadyExistsException {

        teamService.addDevToTeam(username, teamName);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/dev/{username}")
    @PostAuthorize("hasRole('MANAGER')")
    public ResponseEntity deleteDevFromTeam(@PathVariable String username, @RequestParam("team") String teamName) throws NoPermissionException, NoSuchEntityException {

        teamService.removeDevFromTeam(username, teamName);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/manager/{username}")
    @PostAuthorize("hasRole('ADMIN')")
    public ResponseEntity addManagerToTeam(@PathVariable String username, @RequestParam("team") String teamName) throws NoPermissionException, NoSuchEntityException, EntityAlreadyExistsException {

        teamService.addTeamManager(username, teamName);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/manager/")
    @PostAuthorize("hasRole('ADMIN')")
    public ResponseEntity deleteTeamManager(@RequestParam("team") String teamName) throws NoSuchEntityException {

        teamService.removeTeamManager(teamName);
        return ResponseEntity.noContent().build();
    }

}
