import {User} from '../../authentication/register/user.interface';

export interface Note {
  id?: string;
  title: string;
  description: string;
  deadline: Date;
  createdAt?: Date;
  user?: User;
  visibleForDetails?: boolean;
  visibleForEdit?: boolean;
  type?: string;
}
