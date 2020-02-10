package com.fmi.spring5.service;

import com.fmi.spring5.exceptions.EntityAlreadyExistsException;
import com.fmi.spring5.exceptions.NoSuchEntityException;
import com.fmi.spring5.model.*;
import com.fmi.spring5.repository.*;
import com.fmi.spring5.utils.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.naming.NoPermissionException;
import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class EventServiceImpl implements EventService {

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    CompanyEventRepository companyEventRepository;

    @Autowired
    TeamEventsRepository teamEventsRepository;

    @Autowired
    DevRepository devRepository;

    @Autowired
    ManagerRepository managerRepository;

    @Autowired
    TeamRepository teamRepository;

    @Override
    public CompanyEvent addCompanyEvent(CompanyEvent companyEvent) throws EntityAlreadyExistsException {
        Optional<CompanyEvent> foundCompanyEvent = companyEventRepository.findByTitle(companyEvent.getTitle());

        if (foundCompanyEvent.isPresent()) {
            throw new EntityAlreadyExistsException("Company events with that title already exists.");
        }


        if (!isRoomFree(companyEvent.getRoom(), companyEvent.getFromDate(), companyEvent.getToDate())) {
            throw new IllegalArgumentException(String.format("The room is not free in the interval: ['%s'-'%s']",
                    companyEvent.getFromDate()
                    , companyEvent.getToDate()));
        }

        companyEvent.setOrganizer(getPrincipal());
        return companyEventRepository.save(companyEvent);
    }

    @Override
    public TeamEvent addTeamEvent(TeamEvent teamEvent, String teamName) throws EntityAlreadyExistsException, NoSuchEntityException, NoPermissionException {
        Optional<TeamEvent> foundTeamEvent = teamEventsRepository.findByTitle(teamEvent.getTitle());

        if (foundTeamEvent.isPresent()) {
            throw new EntityAlreadyExistsException("Team event with that title already exists.");
        }

        Team foundTeam = teamRepository.findByName(teamName)
                .orElseThrow(() -> new NoSuchEntityException("There is no team with that name"));

        if (!isRoomFree(teamEvent.getRoom(), teamEvent.getFromDate(), teamEvent.getToDate())) {
            throw new IllegalArgumentException(String.format("The room is not free in the interval: ['%s'-'%s']",
                    teamEvent.getFromDate()
                    , teamEvent.getToDate()));
        }

        User getCurrentUser = getPrincipal();
        teamEvent.setOrganizer(getCurrentUser);

        if (getCurrentUser.getRole().equals(Role.DEV.getStringRole())) {
            Dev dev = devRepository.findByUser(getCurrentUser).get();
            if (dev.getTeam() == null) {
                throw new IllegalArgumentException(String.format("The dev: '%s' is not  in any team",
                        getCurrentUser.getUsername()));
            } else {
                teamEvent.setTeam(dev.getTeam());

                return teamEventsRepository.save(teamEvent);
            }
        } else if (getCurrentUser.getRole().equals(Role.MANAGER.getStringRole())) {
            Manager manager = managerRepository.findByUser(getCurrentUser).get();

            List<Team> foundTeamsManager = teamRepository.findByManager(manager)
                    .stream()
                    .filter((event) -> event.getName().equals(foundTeam.getName()))
                    .collect(Collectors.toList());

            if (foundTeamsManager.isEmpty()) {
                throw new IllegalArgumentException(String.format("The manager: '%s' is not  in this team",
                        getCurrentUser.getUsername()));
            }

            teamEvent.setTeam(foundTeamsManager.get(0));
            return teamEventsRepository.save(teamEvent);
        } else {
            throw new NoPermissionException("Only managers and dev can have team events");
        }
    }

    @Override
    public Iterable<CompanyEvent> getAllCompanyEvents() {

        return companyEventRepository.findAll();
    }

    @Override
    public Iterable<TeamEvent> getAllTeamEvents(String teamName) throws NoSuchEntityException, NoPermissionException {
        User currentUser = getPrincipal();

        Team foundTeam = teamRepository.findByName(teamName)
                .orElseThrow(() -> new NoSuchEntityException("There is no team with that name"));


        if (currentUser.getRole().equals(Role.DEV.getStringRole())) {
            Dev dev = devRepository.findByUser(currentUser).get();
            if (dev.getTeam() == null) {
                throw new IllegalArgumentException(String.format("The dev: '%s' is not  in any team",
                        currentUser.getUsername()));
            }
            return teamEventsRepository.getAllByTeam(dev.getTeam());
        } else if (currentUser.getRole().equals(Role.MANAGER.getStringRole())) {
            Manager manager = managerRepository.findByUser(currentUser).get();

            List<Team> foundTeamsManager = teamRepository.findByManager(manager)
                    .stream()
                    .filter((event) -> event.getName().equals(foundTeam.getName()))
                    .collect(Collectors.toList());

            if (foundTeamsManager.isEmpty()) {
                throw new IllegalArgumentException(String.format("The manager: '%s' is not  in this team",
                        currentUser.getUsername()));
            }

            return teamEventsRepository.getAllByTeam(foundTeamsManager.get(0));
        } else {
            throw new NoPermissionException("Only managers and dev can create team events");
        }
    }

    @Override
    public void deleteTeamEvent(String title) throws NoSuchEntityException, NoPermissionException {
        TeamEvent foundTeamEvent = teamEventsRepository.findByTitle(title).
                orElseThrow(() -> new NoSuchEntityException("There isn't team event with that title"));

        User currentUser = getPrincipal();

        if (currentUser.getRole().equals(Role.DEV.getStringRole())) {
            if (!foundTeamEvent.getOrganizer().getUsername().equals(currentUser.getUsername())) {
                throw new NoPermissionException("The dev can delete only his own team event");
            }
        } else if (currentUser.getRole().equals(Role.MANAGER.getStringRole())) {
            Manager manager = managerRepository.findByUser(currentUser).get();
            List<Team> foundTeamsManager = teamRepository.findByManager(manager);
            if (!foundTeamsManager.contains(foundTeamEvent.getTeam())) {
                throw new NoPermissionException("The manager can delete only his teams event and his own.");
            }
        } else {
            throw new NoPermissionException("Only managers and dev can delete team events");
        }

        teamEventsRepository.delete(foundTeamEvent);
    }

    @Override
    public void deleteCompanyEvent(String title) throws NoSuchEntityException, NoPermissionException {
        CompanyEvent foundCompanyEvent = companyEventRepository.findByTitle(title).
                orElseThrow(() -> new NoSuchEntityException("There isn't company event with that title"));

        User currentUser = getPrincipal();

        if (currentUser.getRole().equals(Role.DEV.getStringRole()) || currentUser.getRole().equals(Role.MANAGER.getStringRole())) {
            if (!foundCompanyEvent.getOrganizer().getUsername().equals(currentUser.getUsername())) {
                throw new NoPermissionException("The dev and manager can delete only his own company event");
            }

        }

        companyEventRepository.delete(foundCompanyEvent);
    }

    @Override
    public CompanyEvent updateCompanyEvent(CompanyEvent companyEvent) throws NoSuchEntityException, NoPermissionException {

        User currentUser = getPrincipal();

        if (currentUser.getRole().equals(Role.DEV.getStringRole()) || currentUser.getRole().equals(Role.MANAGER.getStringRole())) {
            if (!companyEvent.getOrganizer().getUsername().equals(currentUser.getUsername())) {
                throw new NoPermissionException("The dev and manager can edit only his own company event");
            }

        }

        if (isRoomFree(companyEvent.getRoom(), companyEvent.getFromDate(), companyEvent.getToDate())) {
            throw new IllegalArgumentException("The room can not be change, because is not free for this interval.");
        }

        return companyEventRepository.save(companyEvent);
    }

    @Override
    public TeamEvent updateTeamEvent(TeamEvent teamEvent) throws NoPermissionException {
        if (isRoomFree(teamEvent.getRoom(), teamEvent.getFromDate(), teamEvent.getToDate())) {
            throw new IllegalArgumentException("The room can not be change, because is not free for this interval.");
        }

        User currentUser = getPrincipal();

        if (currentUser.getRole().equals(Role.DEV.getStringRole())) {
            if (!teamEvent.getOrganizer().getUsername().equals(currentUser.getUsername())) {
                throw new NoPermissionException("The dev can edit only his own team event");
            }
        } else if (currentUser.getRole().equals(Role.MANAGER.getStringRole())) {
            Manager manager = managerRepository.findByUser(currentUser).get();
            List<Team> foundTeamsManager = teamRepository.findByManager(manager);
            if (!foundTeamsManager.contains(teamEvent.getTeam())) {
                throw new NoPermissionException("The manager can edit only his teams event and his own.");
            }
        } else {
            throw new NoPermissionException("Only managers and dev can edit team events");
        }

        return teamEventsRepository.save(teamEvent);
    }

    private User getPrincipal() {
        return ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
    }

    private boolean isRoomFree(Room room, Date from, Date to) {
        List<TeamEvent> teamEventByRoom = teamEventsRepository.getAllByRoom(room);
        List<CompanyEvent> companyEventByRoom = companyEventRepository.getAllByRoom(room);


        for (TeamEvent teamEvent : teamEventByRoom) {
            Date fromDate = teamEvent.getFromDate();
            Date toDate = teamEvent.getToDate();

            if (fromDate.before(from) || toDate.after(to)) {
                return false;
            }
        }

        for (CompanyEvent companyEvent : companyEventByRoom) {
            Date fromDate = companyEvent.getFromDate();
            Date toDate = companyEvent.getToDate();

            if (fromDate.before(from) || toDate.after(to)) {
                return false;
            }
        }

        return true;
    }
}
