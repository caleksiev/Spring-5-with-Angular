import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {User} from './register/user.interface';

@Injectable({
  providedIn: 'root'
})
export class ServerAuthenticationService {

  constructor(private http: HttpClient) {
  }

  register(user: User): Observable<any> {
    return this.http.post('http://localhost:8080/register', user);
  }

  login(username: string, password: string): Observable<any> {
    const headers = this.basicAuthorization(username, password);
    return this.http.post('http://localhost:8080/login', null, {headers, withCredentials: true});
  }

  private basicAuthorization(username: string, password: string) {
    return {
      Authorization: 'Basic ' + btoa(`${username}:${password}`),
      'X-Requested-With': 'XMLHttpRequest'
    };
  }
}
