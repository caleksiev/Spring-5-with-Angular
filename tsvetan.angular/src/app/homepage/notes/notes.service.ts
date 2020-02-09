import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {AuthService} from '../../auth.service';
import {Note} from './notes.interface';

@Injectable({
  providedIn: 'root'
})
export class NotesService {

  constructor(private http: HttpClient, private authService: AuthService) {
  }

  private basicAuthorization(userDetails: string): any {
    return {
      Authorization: 'Basic ' + userDetails
    };
  }

  getAllNotes(notesType: string): Observable<any> {
    const headers = this.basicAuthorization(this.authService.getToken());
    return this.http.get(`http://localhost:8080/notes/${notesType}`, {headers, withCredentials: true});
  }

  deleteTeamNote(title: string): Observable<any> {
    const headers = this.basicAuthorization(this.authService.getToken());

    return this.http.delete('http://localhost:8080/notes/team/delete',
      {headers, withCredentials: true, params: {title}});
  }

  createNote(teamNote: Note, notesType: string): Observable<any> {
    const headers = this.basicAuthorization(this.authService.getToken());

    return this.http.post(`http://localhost:8080/notes/${notesType}`,
      teamNote,
      {headers, withCredentials: true});
  }


  editNote(teamNote: Note): Observable<any> {
    const headers = this.basicAuthorization(this.authService.getToken());

    return this.http.put('http://localhost:8080/notes/team/edit', teamNote,
      {
        headers,
        withCredentials: true
      });
  }
}
