import {ComponentFixture, TestBed} from '@angular/core/testing';

import {NoteComponent} from './note.component';
import {FormsModule} from '@angular/forms';
import {Note} from '../notes.interface';
import {By} from '@angular/platform-browser';

describe('NoteComponent', () => {
  let hostElement;
  let noteComponent: NoteComponent;
  let fixture: ComponentFixture<NoteComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [FormsModule],
      declarations: [NoteComponent]
    });
    fixture = TestBed.createComponent(NoteComponent);
    noteComponent = fixture.componentInstance;
    hostElement = fixture.nativeElement;
  });

  it('should create', () => {
    expect(noteComponent).toBeTruthy();
  });

  describe('@input tests', () => {
    it('mock data supplied by the parent component should be as expected', () => {

      const testNote: Note = {
        title: 'Some title',
        description: 'Some des',
        createdAt: new Date(),
        deadline: new Date(),
        user: {
          username: 'misho',
          password: ''
        },
        visibleForDetails: true
      };

      noteComponent.classNote = testNote;
      fixture.detectChanges();

      expect(hostElement.querySelector('li').textContent).toEqual('Title note: ' + testNote.title);
      expect(hostElement.querySelector('ol > #noteDescription').textContent).toEqual('Description: ' + testNote.description);
      expect(hostElement.querySelector('ol > #noteCreator').textContent).toEqual('Creator(username): ' + testNote.user.username);

    });
  });

  describe('@output test', () => {
    it('should raise delete event when clicking button delete', () => {

      const testNote: Note = {
        title: 'Some title',
        description: 'Some des',
        deadline: new Date(),
      };
      noteComponent.classNote = testNote;
      noteComponent.isManagerLogged = true;
      noteComponent.notesPurpose = 'team';
      fixture.detectChanges();

      const removeButton = fixture.debugElement.query(By.css('#removeButton')).nativeElement;

      let deletedNoteTitle = null;
      noteComponent.deleteEvent.subscribe((note) => deletedNoteTitle = note);
      spyOn(window, 'confirm').and.returnValue(true);
      removeButton.click();
      expect(deletedNoteTitle).toBe(testNote.title);
    });
  });

});
