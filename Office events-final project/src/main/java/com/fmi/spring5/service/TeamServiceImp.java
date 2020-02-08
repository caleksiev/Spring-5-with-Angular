package com.fmi.spring5.service;

import com.fmi.spring5.exceptions.EntityAlreadyExistsException;
import com.fmi.spring5.exceptions.InvalidArgumentException;
import com.fmi.spring5.exceptions.NoSuchEntityException;
import com.fmi.spring5.model.Dev;
import com.fmi.spring5.model.Manager;
import com.fmi.spring5.model.Team;
import com.fmi.spring5.model.User;
import com.fmi.spring5.repository.DevRepository;
import com.fmi.spring5.repository.ManagerRepository;
import com.fmi.spring5.repository.TeamRepository;
import com.fmi.spring5.repository.UserRepository;
import com.fmi.spring5.utils.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.NoPermissionException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@Service
public class TeamServiceImp implements TeamService {


    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private ManagerRepository managerRepository;
    @Autowired
    private DevRepository devRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public Team addTeam(Team team) throws InvalidArgumentException, EntityAlreadyExistsException {
        if (team.getManager() != null) {
            throw new InvalidArgumentException("When the team is created, the manager must not be set.");
        }
        Optional<Team> foundTeam = teamRepository.findByName(team.getName());

        if (foundTeam.isPresent()) {
            throw new EntityAlreadyExistsException(String.format("There is already a team with name: '%s'.", team.getName()));
        }


        return teamRepository.save(team);

    }

    @Override
    public Team update(Team team) throws NoSuchEntityException {
        Team foundTeam = teamRepository.findByName(team.getName()).
                orElseThrow(() -> new NoSuchEntityException(String.format("The team with name: '%s' doesn't exists.", team.getName())));
        team.setId(foundTeam.getId());
        return teamRepository.save(team);
    }

    @Override
    @Transactional
    //??????????????/ check if work
    public void removeTeam(String teamName) throws NoSuchEntityException {
        Team foundTeam = teamRepository.findByName(teamName).
                orElseThrow(() -> new NoSuchEntityException(String.format("The team with name: '%s' doesn't exists.", teamName)));

        teamRepository.delete(foundTeam);
    }

    @Override
    public void addTeamManager(String managerName, String teamName) throws NoSuchEntityException, NoPermissionException, EntityAlreadyExistsException {
        User foundUser = userRepository.findUserByUsername(managerName).
                orElseThrow(() -> new NoSuchEntityException(String.format("The user with name: '%s' doesn't exists.", managerName)));

        if (!foundUser.getRole().equals(Role.MANAGER.getStringRole())) {
            throw new NoPermissionException(String.format("The user: '%s' is not a manager", managerName));
        }

        Team foundTeam = teamRepository.findByName(teamName).
                orElseThrow(() -> new NoSuchEntityException(String.format("The team with name: '%s' doesn't exists.", teamName)));

        if (foundTeam.getManager() != null) {
            throw new EntityAlreadyExistsException(String.format("The team: '%s' already have a manager.", teamName));
        }

        Manager manager = managerRepository.findByUser(foundUser).get();

        foundTeam.setManager(manager);
        teamRepository.save(foundTeam);
    }


    @Override
    public void removeTeamManager(String teamName) throws NoSuchEntityException {
        Team team = teamRepository.findByName(teamName).
                orElseThrow(() -> new NoSuchEntityException("The team with that name doesn't exists."));

        Manager manager = team.getManager();

        if (manager != null) {
            team.setManager(null);
            teamRepository.save(team);
        }
    }

    @Override
    public Manager getTeamManager(String teamName) throws NoSuchEntityException {
        Team foundTeam = teamRepository.findByName(teamName).
                orElseThrow(() -> new NoSuchEntityException("Team with that name doesn't exists."));

        Manager manager = foundTeam.getManager();

        if (manager != null) {
            return manager;
        } else {
            throw new NoSuchEntityException("Team doesn't has a manager.");
        }
    }

    @Override
    public Iterable<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    @Override
    public void addDevToTeam(String userName, String teamName) throws NoSuchEntityException, NoPermissionException, EntityAlreadyExistsException {
        Team foundTeam = teamRepository.findByName(teamName).
                orElseThrow(() -> new NoSuchEntityException("The team with that name doesn't exists."));

        User foundUser = userRepository.findUserByUsername(userName).
                orElseThrow(() -> new NoSuchEntityException("The dev with that name doesn't exists."));

        if (!foundUser.getRole().equals(Role.DEV.getStringRole())) {
            throw new NoPermissionException(String.format("The user: '%s' is not a dev", userName));
        }

        Dev dev = devRepository.findByUser(foundUser).get();

        if (dev.getTeam() != null) {
            throw new EntityAlreadyExistsException("The dev already have a team.");
        }

        dev.setTeam(foundTeam);
        devRepository.save(dev);

    }

    @Override
    public void removeDevFromTeam(String userName, String teamName) throws NoSuchEntityException, NoPermissionException {
        teamRepository.findByName(teamName).
                orElseThrow(() -> new NoSuchEntityException("The team with that name doesn't exists."));

        User foundUser = userRepository.findUserByUsername(userName).
                orElseThrow(() -> new NoSuchEntityException("The dev with that name doesn't exists."));

        if (!foundUser.getRole().equals(Role.DEV.getStringRole())) {
            throw new NoPermissionException(String.format("The user: '%s' is not a dev", userName));
        }

        Dev dev = devRepository.findByUser(foundUser).get();


        if (dev.getTeam() != null && !dev.getTeam().getName().equals(teamName)) {
            throw new IllegalArgumentException(String.format("The dev is not in team: '%s'", teamName));
        }

        dev.setTeam(null);
        devRepository.save(dev);
    }
}
