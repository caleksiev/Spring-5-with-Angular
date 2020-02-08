package com.fmi.spring5.service;


import com.fmi.spring5.exceptions.EntityAlreadyExistsException;
import com.fmi.spring5.exceptions.NoSuchEntityException;
import com.fmi.spring5.model.CompanyEvent;
import com.fmi.spring5.model.TeamEvent;

import javax.naming.NoPermissionException;

public interface EventService {

    CompanyEvent addCompanyEvent(CompanyEvent companyEvent) throws EntityAlreadyExistsException;

    TeamEvent addTeamEvent(TeamEvent teamEvent,String teamName) throws EntityAlreadyExistsException, NoSuchEntityException, NoPermissionException;

    Iterable<CompanyEvent> getAllCompanyEvents();

    Iterable<TeamEvent> getAllTeamEvents(String teamName) throws NoSuchEntityException, NoPermissionException;

    void deleteTeamEvent(String title) throws NoSuchEntityException, NoPermissionException;

    void deleteCompanyEvent(String title) throws NoSuchEntityException, NoPermissionException;

    CompanyEvent updateCompanyEvent(CompanyEvent companyEvent) throws NoSuchEntityException, NoPermissionException;

    TeamEvent updateTeamEvent(TeamEvent teamEvent) throws NoPermissionException;

}
