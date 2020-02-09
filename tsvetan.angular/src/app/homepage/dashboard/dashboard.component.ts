import {Component, OnInit} from '@angular/core';
import {NotesService} from "../notes/notes.service";
import {DatePipe} from "@angular/common";
import {Note} from "../notes/notes.interface";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  private dueCompanyNotes: number;
  private dueTeamNotes: number;

  constructor(private notesService: NotesService, private datePipe: DatePipe) {
  }

  ngOnInit() {
    this.notesService.getAllNotes('team').subscribe((notes: Note[]) => {
      let todayDate = this.datePipe.transform(new Date(), 'yyyy-MM-dd');
      this.dueTeamNotes = notes.filter((note) => note.deadline.toString() >= todayDate).length.valueOf();
    });

    this.notesService.getAllNotes('company').subscribe((notes: Note[]) => {
      let todayDate = this.datePipe.transform(new Date(), 'yyyy-MM-dd');
      this.dueCompanyNotes = notes.filter((note) => note.deadline.toString() >= todayDate).length.valueOf();
    });
  }


}
