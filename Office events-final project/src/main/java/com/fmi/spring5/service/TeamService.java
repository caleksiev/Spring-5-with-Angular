package com.fmi.spring5.service;

import com.fmi.spring5.exceptions.EntityAlreadyExistsException;
import com.fmi.spring5.exceptions.InvalidArgumentException;
import com.fmi.spring5.exceptions.NoSuchEntityException;
import com.fmi.spring5.model.Manager;
import com.fmi.spring5.model.Team;

import javax.naming.NoPermissionException;

public interface TeamService {
    Team addTeam(Team team) throws InvalidArgumentException, EntityAlreadyExistsException;

    Team update(Team user) throws NoSuchEntityException;

    void removeTeam(String teamName) throws NoSuchEntityException;

    void addTeamManager(String managerName, String teamName) throws NoSuchEntityException, NoPermissionException, EntityAlreadyExistsException;

    void removeTeamManager(String teamName) throws NoSuchEntityException;

    Manager getTeamManager(String teamName) throws NoSuchEntityException;

    Iterable<Team> getAllTeams();

    void addDevToTeam(String devName, String teamName) throws NoSuchEntityException, NoPermissionException, EntityAlreadyExistsException;

    void removeDevFromTeam(String devName, String teamName) throws NoSuchEntityException, NoPermissionException;

}
