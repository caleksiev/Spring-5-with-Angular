import {ComponentFixture, fakeAsync, TestBed} from '@angular/core/testing';
import {CreateNotesComponent} from './create-notes.component';
import {NotesService} from '../notes/notes.service';
import {ReactiveFormsModule} from '@angular/forms';
import {of, throwError} from 'rxjs';

describe('CreateNotesComponent', () => {
  let createNotesComponent: CreateNotesComponent;
  let fixture: ComponentFixture<CreateNotesComponent>;
  let hostElement;
  const mockNotesService = jasmine.createSpyObj('NotesService', ['createNote']);

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ReactiveFormsModule],
      declarations: [CreateNotesComponent],
      providers: [{provide: NotesService, useValue: mockNotesService}]
    });
    fixture = TestBed.createComponent(CreateNotesComponent);
    createNotesComponent = fixture.componentInstance;
    hostElement = fixture.nativeElement;
  });

  it('should create', () => {
    expect(createNotesComponent).toBeTruthy();
  });

  describe(' input DOM elements', () => {

    it('given correct input should create valid form as expected', () => {

      const testNote = {
        title: 'Some title',
        description: 'Some des',
        deadline: '2017-06-01',
        visibility: 'team'
      };

      fixture.detectChanges();


      const titleNoteInput: HTMLInputElement = hostElement.querySelector('#noteTitle > input');
      const descriptionNoteInput: HTMLInputElement = hostElement.querySelector('#noteDescription > input');
      const deadlineNoteInput: HTMLInputElement = hostElement.querySelector('#noteDeadline > input');
      const visibilityNoteInput: HTMLInputElement = hostElement.querySelector('#noteVisibility > #team');

      titleNoteInput.value = 'Some title';
      descriptionNoteInput.value = 'Some des';
      deadlineNoteInput.value = '2017-06-01';

      titleNoteInput.dispatchEvent(new Event('input'));
      descriptionNoteInput.dispatchEvent(new Event('input'));
      deadlineNoteInput.dispatchEvent(new Event('input'));
      visibilityNoteInput.dispatchEvent(new Event('change'));

      fixture.detectChanges();

      expect(createNotesComponent.noteForm.getRawValue()).toEqual(testNote);
      expect(createNotesComponent.noteForm.valid).toBeTruthy();
    });

    it('given invalid input should create incorrect form', () => {

      fixture.detectChanges();

      expect(createNotesComponent.noteForm.valid).toBeFalsy();

      const titleNoteInput: HTMLInputElement = hostElement.querySelector('#noteTitle > input');
      titleNoteInput.value = 'Some title';

      titleNoteInput.dispatchEvent(new Event('input'));

      fixture.detectChanges();

      expect(createNotesComponent.noteForm.valid).toBeFalsy();
    });
  });

  describe('button DOM element', () => {
    it('given invalid input when clicking button should get message for wrong input ', () => {

      fixture.detectChanges();

      const createButton = hostElement.querySelector('button');

      createButton.click();

      fixture.detectChanges();

      const responseFromCreate = hostElement.querySelector('p');

      expect(responseFromCreate.textContent).toEqual('Check your input! ' +
        'Еach field must be filled in. The minimum length of title is ' + 4 + ' symbols.');
    });

    it('given valid input when clicking button should create note', () => {
      mockNotesService.createNote.and.returnValue(of('success'));

      fixture.detectChanges();

      createNotesComponent.createNoteForm.clearValidators();
      createNotesComponent.createNoteForm.updateValueAndValidity();
      const titleNoteInput: HTMLInputElement = hostElement.querySelector('#noteTitle > input');
      const descriptionNoteInput: HTMLInputElement = hostElement.querySelector('#noteDescription > input');
      const deadlineNoteInput: HTMLInputElement = hostElement.querySelector('#noteDeadline > input');
      const visibilityNoteInput: HTMLInputElement = hostElement.querySelector('#noteVisibility > #team');

      titleNoteInput.value = 'Some title';
      descriptionNoteInput.value = 'Some des';
      deadlineNoteInput.value = '2017-06-01';

      titleNoteInput.dispatchEvent(new Event('input'));
      descriptionNoteInput.dispatchEvent(new Event('input'));
      deadlineNoteInput.dispatchEvent(new Event('input'));
      visibilityNoteInput.dispatchEvent(new Event('change'));

      fixture.detectChanges();

      const createButton = hostElement.querySelector('button');

      createButton.click();

      fixture.detectChanges();

      const responseFromCreate = hostElement.querySelector('p');

      expect(responseFromCreate.textContent).toEqual('The note is created!');

    });

    it('given valid input when clicking button should not create note because of "server error"', fakeAsync(() => {
      mockNotesService.createNote.and.returnValue(throwError({error: {message: 'error'}}));
      fixture.detectChanges();

      createNotesComponent.createNoteForm.clearValidators();
      createNotesComponent.createNoteForm.updateValueAndValidity();
      const titleNoteInput: HTMLInputElement = hostElement.querySelector('#noteTitle > input');
      const descriptionNoteInput: HTMLInputElement = hostElement.querySelector('#noteDescription > input');
      const deadlineNoteInput: HTMLInputElement = hostElement.querySelector('#noteDeadline > input');
      const visibilityNoteInput: HTMLInputElement = hostElement.querySelector('#noteVisibility > #team');

      titleNoteInput.value = 'Some title';
      descriptionNoteInput.value = 'Some des';
      deadlineNoteInput.value = '2017-06-01';

      titleNoteInput.dispatchEvent(new Event('input'));
      descriptionNoteInput.dispatchEvent(new Event('input'));
      deadlineNoteInput.dispatchEvent(new Event('input'));
      visibilityNoteInput.dispatchEvent(new Event('change'));

      fixture.detectChanges();

      const createButton = hostElement.querySelector('button');
      createButton.click();

      fixture.detectChanges();

      const responseFromCreate = hostElement.querySelector('p');


      expect(responseFromCreate.textContent).toEqual('error');

    }));
  });
});

