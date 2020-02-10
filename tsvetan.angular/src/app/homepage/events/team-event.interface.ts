import DateTimeFormat = Intl.DateTimeFormat;
import {Room} from '../rooms/room/room.interface';
import {User} from '../../authentication/register/user.interface';

export interface EventInterface {
  id?: string;
  title: string;
  description: string;
  fromDate: DateTimeFormat;
  to: DateTimeFormat;
  room: Room;
  user?: User;

}
