import {User} from '../../authentication/register/user.interface';
import {Room} from '../rooms/room/room.interface';
import DateTimeFormat = Intl.DateTimeFormat;


export interface CompanyEvent {
  id?: string;
  title: string;
  description: string;
  fromDate: DateTimeFormat;
  to: DateTimeFormat;
  room: Room;
  user?: User;
}
