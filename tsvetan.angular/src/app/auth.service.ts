import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {User} from './authentication/register/user.interface';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient) {
  }

  sendToken(token: string) {
    localStorage.setItem('LoggedInUser', token);
    this.http.get('http://localhost:8080/users/me', {
      headers: {Authorization: 'Basic ' + this.getToken()},
      withCredentials: true
    }).subscribe((data: User) => {
      localStorage.setItem('Username', data.username);
      localStorage.setItem('Role', data.role);
    });
  }

  getToken(): string {
    return localStorage.getItem('LoggedInUser');
  }

  getUsername(): string {
    return localStorage.getItem('Username');
  }

  getRole(): string {
    return localStorage.getItem('Role');
  }

  isLoggedIn(): boolean {
    return this.getToken() !== null;
  }


  logout(): void {

    // const headers = this.basicAuthorization(this.getToken());
    // this.http.post('http://localhost:8080/logout', null, {headers, withCredentials: true}).subscribe(()={});

    if (!this.isLoggedIn()) {
      return;
    }
    localStorage.removeItem('LoggedInUser');
    localStorage.removeItem('Username');
    localStorage.removeItem('Role');
  }

  private basicAuthorization(userDetails: string): any {
    return {
      Authorization: 'Basic ' + userDetails
    };
  }
}




