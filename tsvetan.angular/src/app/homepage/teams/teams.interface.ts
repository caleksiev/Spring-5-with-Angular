import {Manager} from '../../authentication/register/manager.interface';

export interface Team {
  id?: string;
  name: string;
  manager?: Manager;
}
