import {TestBed} from '@angular/core/testing';

import {NotesService} from './notes.service';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {AuthService} from '../../auth.service';
import {HttpClient, HttpRequest} from '@angular/common/http';
import {Note} from './notes.interface';

describe('NotesService', () => {
  let httpClient: HttpClient;
  let httpTestingController: HttpTestingController;
  let notesService: NotesService;

  const testToken = btoa('pesho:1234');
  const mockAuthService = jasmine.createSpyObj('AuthService', ['getToken']);
  const mockGetTokenAuthService = mockAuthService.getToken.and.returnValue(testToken);

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [NotesService,
        {provide: AuthService, useValue: mockAuthService}]
    });

    httpClient = TestBed.get(HttpClient);
    httpTestingController = TestBed.get(HttpTestingController);
    notesService = TestBed.get(NotesService);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should be created', () => {
    expect(mockGetTokenAuthService).toBeTruthy();
  });

  describe('get all notes', () => {

    it('get all notes should be as expected', () => {

      const testNote: Note = {
        title: '',
        description: 'some',
        deadline: new Date()
      };

      notesService.getAllNotes('teams').subscribe((note) => {
        expect(note).toEqual(testNote);
      });

      const req = httpTestingController.expectOne('http://localhost:8080/notes/teams');
      expect(req.request.method).toEqual('GET');
      expect(req.request.withCredentials).toBeTruthy();
      expect(req.request.headers.get('Authorization') === 'Basic ' + testToken).toBeTruthy();

      req.flush(testNote);

    });
  });

  describe('delete team notes', () => {

    it('delete team notes should be as expected', () => {

      const testNote: Note = {
        title: 'some title',
        description: 'some',
        deadline: new Date()
      };

      notesService.deleteTeamNote(testNote.title).subscribe((note) => {
        expect(note).toEqual(testNote);
      });

      const params = {param: 'title', value: testNote.title};


      const deleteNoteRequest = httpTestingController
        .expectOne(req => req.url === 'http://localhost:8080/notes/team/delete');

      expect(deleteNoteRequest.request.method).toEqual('DELETE');
      expect(deleteNoteRequest.request.headers.get('Authorization')).toEqual('Basic ' + testToken);
      expect(deleteNoteRequest.request.withCredentials).toBeTruthy();
      expect(deleteNoteRequest.request.params.has('title')).toBeTruthy();
      expect(deleteNoteRequest.request.params.get('title')).toEqual(testNote.title);
      expect(mockGetTokenAuthService.calls.any()).toBeTruthy();

      deleteNoteRequest.flush(testNote);

    });
  });

  describe('create note', () => {

    it('create team note should be as expected', () => {

      const testNote: Note = {
        title: 'some title',
        description: 'some',
        deadline: new Date()
      };

      notesService.createNote(testNote, 'team').subscribe((note) => {
        expect(note).toEqual(testNote);
      });


      const createNoteRequest = httpTestingController
        .expectOne(req => req.url === 'http://localhost:8080/notes/team');

      expect(createNoteRequest.request.method).toEqual('POST');
      expect(createNoteRequest.request.headers.get('Authorization')).toEqual('Basic ' + testToken);
      expect(createNoteRequest.request.withCredentials).toBeTruthy();
      expect(createNoteRequest.request.body).toEqual(testNote);
      expect(mockGetTokenAuthService.calls.any()).toBeTruthy();

      createNoteRequest.flush(testNote);

    });
  });

  describe('edit note', () => {

    it('edit team note should be as expected', () => {

      const testNote: Note = {
        title: 'some title',
        description: 'some',
        deadline: new Date()
      };

      notesService.editNote(testNote).subscribe((note) => {
        expect(note).toEqual(testNote);
      });


      const editNoteRequest = httpTestingController
        .expectOne(req => req.url === 'http://localhost:8080/notes/team/edit');

      expect(editNoteRequest.request.method).toEqual('PUT');
      expect(editNoteRequest.request.headers.get('Authorization')).toEqual('Basic ' + testToken);
      expect(editNoteRequest.request.withCredentials).toBeTruthy();
      expect(editNoteRequest.request.body).toEqual(testNote);
      expect(mockGetTokenAuthService.calls.any()).toBeTruthy();

      editNoteRequest.flush(testNote);

    });
  });

});
