import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {User} from '../../authentication/register/user.interface';
import {Observable} from 'rxjs';
import {AuthService} from '../../auth.service';
import {TeamEvent} from './team-event.interface';
import {CompanyEvent} from './company-event.interface';

@Injectable({
  providedIn: 'root'
})
export class EventsService {

  constructor(private http: HttpClient,
              private authService: AuthService) {
  }

  createTeamEvent(team: string, teamEvent: TeamEvent): Observable<any> {
    const headers = this.basicAuthorization(this.authService.getToken());
    return this.http.post(`http://localhost:8080/events/${team}`, teamEvent, {headers, withCredentials: true});
  }

  createCompanyEvent(companyEvent: CompanyEvent): Observable<any> {
    const headers = this.basicAuthorization(this.authService.getToken());
    return this.http.post(`http://localhost:8080/events/`, companyEvent, {headers, withCredentials: true});
  }

  getAll

  private basicAuthorization(userDetails: string): any {
    return {
      Authorization: 'Basic ' + userDetails
    };
  }
}
