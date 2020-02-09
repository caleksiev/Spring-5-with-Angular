import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../auth.service';
import {Room} from './room/room.interface';
import {RoomsService} from './rooms.service';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-rooms',
  templateUrl: './rooms.component.html',
  styleUrls: ['./rooms.component.scss']
})
export class RoomsComponent implements OnInit {
  rooms: Room[];
  message;

  showAdd = false;
  addRoomForm: FormGroup;


  constructor(private roomsService: RoomsService,
              private formBuilder: FormBuilder
  ) {
  }


  ngOnInit() {
    this.addRoomForm = this.formBuilder.group({
      roomName: ['', [Validators.required]],
      seatCount: ['', [Validators.required]]
    });

    this.roomsService.getRooms().subscribe((rooms: Room[]) => {
      this.rooms = rooms;
    });
  }


  editRoom(room: Room) {
    this.roomsService.updateRoom(room).subscribe(() => {
      this.message = 'The room was updated';
    }, error => {
      this.message = error.error.message;
    });

  }

  createRoom() {
    this.showAdd = false;
    this.roomsService.createRooms(this.addRoomForm.getRawValue()).subscribe(() => {
      this.message = 'The room was created';
      this.roomsService.getRooms().subscribe((rooms: Room[]) => {
        this.rooms = rooms;
      });
    }, error => {
      this.message = error.error.message;
    });
  }

  showAddForm() {
    this.showAdd = !this.showAdd;
  }
}
