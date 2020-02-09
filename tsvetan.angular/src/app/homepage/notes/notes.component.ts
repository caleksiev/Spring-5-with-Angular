import {Component, OnInit} from '@angular/core';
import {Note} from './notes.interface';
import {NotesService} from './notes.service';
import {DatePipe} from '@angular/common';
import {ActivatedRoute} from '@angular/router';
import {MatSnackBar} from '@angular/material';
import {AuthService} from '../../auth.service';

@Component({
  selector: 'app-notes',
  templateUrl: './notes.component.html',
  styleUrls: ['./notes.component.scss']
})
export class NotesComponent implements OnInit {

  private notes: Note[];
  private page = 1;
  private limit = 5;
  private notesPurpose: string;
  private isManagerLogged: boolean;
  private currentUserName;
  private response: string;


  constructor(private notesService: NotesService,
              private authService: AuthService,
              private datePipe: DatePipe,
              private route: ActivatedRoute,
              private snackBar: MatSnackBar) {

  }

  ngOnInit() {
    this.currentUserName = this.authService.getUsername();
    this.route.url.subscribe((params) => {
      this.notesPurpose = params[0].toString();
      if (this.notesPurpose === 'manager') {
        this.notesPurpose = 'team';
        this.isManagerLogged = true;
      }
      this.notesService.getAllNotes(this.notesPurpose).subscribe((notes: Note[]) => {
          if (this.notesPurpose === 'company') {
            const todayDate = this.datePipe.transform(new Date(), 'yyyy-MM-dd');
            this.notes = notes.filter((note) => note.deadline.toString() >= todayDate);
          } else {
            this.notes = notes;
            this.response = 'Your team notes:!';

          }
        },
        error => {
          if (error.status === 404) {
            this.response = 'You still are not in any team. Contact your managers!';
          } else {
            this.response = 'Something went wrong. Check your connection.';
          }
        }
      );
    });
  }


  deleteTeamNote(noteTitle: string): void {
    this.notesService.deleteTeamNote(noteTitle).subscribe((notesAfterDelete) => {
      this.notes = notesAfterDelete;
    });
  }

  editTeamNote(note: Note): void {
    this.notesService.editNote(note).subscribe(() => {
    }, error => {
      this.snackBar.open(error.error.message, null, {
        duration: 2000
      });
    });
  }
}
