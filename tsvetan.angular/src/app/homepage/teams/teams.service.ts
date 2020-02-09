import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {AuthService} from '../../auth.service';
import {Team} from './teams.interface';

@Injectable({
  providedIn: 'root'
})
export class TeamsService {

  constructor(private http: HttpClient, private authService: AuthService) {
  }

  private basicAuthorization(userDetails: string): any {
    return {
      Authorization: 'Basic ' + userDetails
    };
  }


  getAllTeams() {
    const headers = this.basicAuthorization(this.authService.getToken());
    return this.http.get(`http://localhost:8080/teams`, {headers, withCredentials: true});
  }

  addTeam(team: Team) {
    const headers = this.basicAuthorization(this.authService.getToken());
    return this.http.post(`http://localhost:8080/teams`, team, {headers, withCredentials: true});
  }

  addManager(username: string, team: string) {
    const headers = this.basicAuthorization(this.authService.getToken());
    return this.http.patch(`http://localhost:8080/teams/manager/${username}`,
      null, {headers, withCredentials: true, params: {team}});
  }

  removeManager(team: string) {
    const headers = this.basicAuthorization(this.authService.getToken());
    return this.http.delete(`http://localhost:8080/teams/manager`,
      {headers, withCredentials: true, params: {team}});
  }

  addDev(username: string, team: string) {
    const headers = this.basicAuthorization(this.authService.getToken());
    return this.http.patch(`http://localhost:8080/teams/dev/${username}`,
      null, {headers, withCredentials: true, params: {team}});
  }

  removeDev(username: string, team: string) {
    const headers = this.basicAuthorization(this.authService.getToken());
    return this.http.delete(`http://localhost:8080/teams/dev/${username}`,
      {headers, withCredentials: true, params: {team}});
  }
}
