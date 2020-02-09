import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {AuthService} from '../../auth.service';
import {Observable} from 'rxjs';
import {User} from '../../authentication/register/user.interface';

@Injectable({
  providedIn: 'root'
})
export class UserProfileService {

  constructor(private http: HttpClient, private authService: AuthService) {
  }

  private basicAuthorization(userDetails: string): any {
    return {
      Authorization: 'Basic ' + userDetails
    };
  }

  getUsers() {
    if (this.authService.getRole() === 'ADMIN' || this.authService.getRole() === 'MANAGER') {
      return this.getAllUsers();
    } else {
      return this.getCurrentUser();
    }
  }

  getDevs() {
    const headers = this.basicAuthorization(this.authService.getToken());
    return this.http.get('http://localhost:8080/devs', {headers, withCredentials: true});
  }

  updateUser(user: User) {
    if (this.authService.getRole() === 'ADMIN') {
      return this.updateUsers(user);
    } else {
      return this.updateCurrentUser(user);
    }
  }

  changerRole(username: string, role: string) {
    const headers = this.basicAuthorization(this.authService.getToken());
    return this.http.patch(`http://localhost:8080/users/${username}`, null, {headers, withCredentials: true, params: {role}});
  }

  private getCurrentUser(): Observable<any> {
    const headers = this.basicAuthorization(this.authService.getToken());
    return this.http.get('http://localhost:8080/users/me', {headers, withCredentials: true});
  }

  private getAllUsers(): Observable<any> {
    const headers = this.basicAuthorization(this.authService.getToken());
    return this.http.get('http://localhost:8080/users', {headers, withCredentials: true});
  }

  private updateUsers(user: User): Observable<any> {
    const headers = this.basicAuthorization(this.authService.getToken());
    return this.http.put('http://localhost:8080/users', user, {headers, withCredentials: true});
  }

  private updateCurrentUser(user: User): Observable<any> {
    const headers = this.basicAuthorization(this.authService.getToken());
    return this.http.put('http://localhost:8080/users/me', user, {headers, withCredentials: true});
  }


}
