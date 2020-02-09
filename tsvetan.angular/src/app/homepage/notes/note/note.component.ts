import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Note} from '../notes.interface';


@Component({
  selector: 'app-note',
  templateUrl: './note.component.html',
  styleUrls: ['./note.component.scss']
})
export class NoteComponent implements OnInit {
  @Input() private note: Note;
  @Input() notesPurpose: string;
  @Input() isManagerLogged: boolean;
  @Input() username: string;

  @Output() private noteDelete: EventEmitter<string> = new EventEmitter();
  @Output() private noteEdit: EventEmitter<Note> = new EventEmitter();


  constructor() {
  }

  ngOnInit() {

  }

  showHideNotesDetails() {
    this.note.visibleForDetails = !this.note.visibleForDetails;
  }

  showHideEditMenu() {
    this.note.visibleForEdit = !this.note.visibleForEdit;
  }

  deleteNote() {
    if (window.confirm('Are you sure you want to delete this note?!')) {
      this.noteDelete.emit(this.note.title);
    }
  }

  editNote() {
    this.showHideEditMenu();
    this.noteEdit.emit(this.note);
  }

  isMyNote(): boolean {


    if (this.isManagerLogged) {
      return true;
    }

    const currentUserUsername: string = this.username;
    const notesCreator: string = this.note.user.username;

    return currentUserUsername === notesCreator;
  }

  get classNote(): Note {
    return this.note;
  }

  // only for testing
  set classNote(value: Note) {
    this.note = value;
  }

  get deleteEvent() {
    return this.noteDelete;
  }
}
