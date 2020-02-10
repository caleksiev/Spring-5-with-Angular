import DateTimeFormat = Intl.DateTimeFormat;
import {Room} from '../rooms/room/room.interface';
import {User} from '../../authentication/register/user.interface';
import {Team} from '../teams/teams.interface';

export interface TeamEvent {
  id?: string;
  title: string;
  description: string;
  fromDate: Date;
  toDate: Date;
  room: Room;
  user?: User;
  team?: Team;
}
