import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {NotesService} from '../notes/notes.service';

@Component({
  selector: 'app-create-notes',
  templateUrl: './create-notes.component.html',
  styleUrls: ['./create-notes.component.scss']
})
export class CreateNotesComponent implements OnInit {
  createNoteForm: FormGroup;
  private responseFromCreateNote: string;
  private minLengthOfTitle = 4;
  private color;

  constructor(private formBuilder: FormBuilder, private notesService: NotesService) {
  }

  ngOnInit() {
    this.createNoteForm = this.formBuilder.group({
      title: ['', [Validators.required, Validators.minLength(this.minLengthOfTitle)]],
      description: [''],
      deadline: ['', Validators.required],
      visibility: ['', Validators.required]
    });
  }

  create() {
    if (this.createNoteForm.valid) {
      this.notesService.createNote(this.createNoteForm.getRawValue(), this.createNoteForm.get('visibility').value).subscribe(() => {
        this.responseFromCreateNote = 'The note is created!';
        this.color = '#43ff54';
      }, error => {
        this.color = '#ff0000';
        this.responseFromCreateNote = error.error.message;
      });
    } else {
      this.responseFromCreateNote = 'Check your input! ' +
        'Еach field must be filled in. The minimum length of title is ' + this.minLengthOfTitle + ' symbols.';
      this.color = '#ff0000';
    }
  }

  get noteForm(): FormGroup {
    return this.createNoteForm;
  }

}
