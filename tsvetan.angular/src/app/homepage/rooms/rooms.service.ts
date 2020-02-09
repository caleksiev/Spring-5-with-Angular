import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {AuthService} from '../../auth.service';
import {Room} from './room/room.interface';

@Injectable({
  providedIn: 'root'
})
export class RoomsService {

  constructor(private http: HttpClient, private authService: AuthService) {
  }

  private basicAuthorization(userDetails: string): any {
    return {
      Authorization: 'Basic ' + userDetails
    };
  }

  getRooms() {
    const headers = this.basicAuthorization(this.authService.getToken());
    return this.http.get('http://localhost:8080/rooms', {headers, withCredentials: true});
  }

  createRooms(room: Room) {
    debugger
    const headers = this.basicAuthorization(this.authService.getToken());
    return this.http.post('http://localhost:8080/rooms', room, {headers, withCredentials: true});
  }

  updateRoom(room: Room) {
    const headers = this.basicAuthorization(this.authService.getToken());
    return this.http.put('http://localhost:8080/rooms', room, {headers, withCredentials: true});
  }
}
