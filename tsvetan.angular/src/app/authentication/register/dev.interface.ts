import {Team} from '../../homepage/teams/teams.interface';
import {User} from './user.interface';

export interface Dev {
  id?: string;
  user: User;
  team?: Team;
}
