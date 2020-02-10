import {Component, OnInit} from '@angular/core';
import {AuthenticationComponent} from '../../authentication/authentication.component';
import {AuthService} from '../../auth.service';
import {EventsService} from './events.service';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {TeamsService} from '../teams/teams.service';
import {Team} from '../teams/teams.interface';
import {RoomsService} from '../rooms/rooms.service';
import {TeamNotePage} from '../../../../e2e/src/notes/team-note.po';
import DateTimeFormat = Intl.DateTimeFormat;
import {DatePipe} from '@angular/common';
import {FromToSlot} from '../rooms/room/from-to-slot.interface';
import {Room} from '../rooms/room/room.interface';
import {dependenciesFromGlobalMetadata} from '@angular/compiler/src/render3/r3_factory';
import {UserProfileService} from '../user-profile/user-profile.service';
import {Dev} from '../../authentication/register/dev.interface';
import {TeamEvent} from './team-event.interface';

@Component({
  selector: 'app-events',
  templateUrl: './events.component.html',
  styleUrls: ['./events.component.scss'],
  providers: [DatePipe]
})
export class EventsComponent implements OnInit {

  isManager = this.authService.getRole() === 'MANAGER';
  managersTeam: Team[];
  currentUserName = this.authService.getUsername();
  createEventForm: FormGroup;
  canCreateEvent = false;
  message;
  freeRooms: Room[];
  hasFreeRooms = false;
  showDev = false;
  dev: Dev;

  constructor(private authService: AuthService,
              private eventsService: EventsService,
              private formBuilder: FormBuilder,
              private teamService: TeamsService,
              private roomsService: RoomsService,
              private userService: UserProfileService) {
  }

  ngOnInit() {

    this.createEventForm = this.formBuilder.group({
      title: ['', [Validators.required]],
      description: [''],
      date: ['', Validators.required],
      timeFrom: ['', Validators.required],
      timeTo: ['', Validators.required],
      room: ['', Validators.required],
      type: ['', Validators.required],

    });

    if (this.isManager) {
      this.teamService.getAllTeams().subscribe((teams: Team[]) => {
        this.managersTeam = teams.filter((t) => t.manager && t.manager.user.username === this.currentUserName);

        if (!this.isManager) {
          this.message = 'You are not in any team';
        }


        if (this.managersTeam.length === 0) {
          this.message = 'You are not in any team';
        }
      });
    } else {
      let hasTeam = true;
      this.userService.getDevs().subscribe((dev: Dev[]) => {
        const a: Dev[] = dev.filter((d) => d.team && d.user.username === this.currentUserName);
        this.dev = a[0];
        if (!a || (a && a.length === 0)) {
          hasTeam = false;
        }
        if (!hasTeam) {
          this.message = 'You are not in any team';
        } else {
          this.showDev = true;
        }
      });
    }
  }

  searchFreeRoom() {

    if (!this.createEventForm.get('date').value || !this.createEventForm.get('timeFrom').value
      || !this.createEventForm.get('timeTo').value) {

      this.message = 'Please fill the time and date correctly';
    } else {

      const fromDate = new Date(this.createEventForm.get('date').value + ' ' + this.createEventForm.get('timeFrom').value);
      const toDate = new Date(this.createEventForm.get('date').value + ' ' + this.createEventForm.get('timeTo').value);

      if (fromDate > toDate) {
        this.message = 'The start date must be before the end date';
        return;
      } else {
        this.message = '';
      }
      const slot: FromToSlot = {
        fromDate,
        toDate
      };

      this.roomsService.getFreeRooms(slot).subscribe((room: Room[]) => {
        this.freeRooms = room;
        this.hasFreeRooms = true;
      });
    }
  }

  somethingChanged() {
    this.hasFreeRooms = false;
    this.freeRooms = [];
  }

  createEvent() {
    let team = null;
    if (this.isManager) {
      team = this.createEventForm.get('team').value;
    } else {
      team = this.dev.team.name;
    }

    const res: TeamEvent = {
      description: this.createEventForm.get('description').value,
      title: this.createEventForm.get('title').value,
      fromDate: new Date(this.createEventForm.get('date').value + ' ' + this.createEventForm.get('timeFrom').value),
      toDate: new Date(this.createEventForm.get('date').value + ' ' + this.createEventForm.get('timeTo').value),
      room: this.freeRooms.filter((r) => r.roomName === this.createEventForm.get('room').value)[0]
    };
    this.eventsService.createTeamEvent(team, res)
      .subscribe(() => {
        this.message = 'The event was added.';
      }, error => {
        this.message = error.error.message;

      });
  }
}
