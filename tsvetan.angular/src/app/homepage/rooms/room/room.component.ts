import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Room} from './room.interface';
import {User} from '../../../authentication/register/user.interface';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-room',
  templateUrl: './room.component.html',
  styleUrls: ['./room.component.scss']
})
export class RoomComponent implements OnInit {

  @Input() room: Room;
  @Output() private roomEdit: EventEmitter<Room> = new EventEmitter();



  showEdit = false;

  constructor() {
  }

  ngOnInit() {
  }

  showEditForm() {
    this.showEdit = !this.showEdit;
  }

  editRoom() {
    this.showEdit = false;
    this.roomEdit.emit(this.room);
  }
}
