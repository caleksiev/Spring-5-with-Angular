import {Component, OnChanges, OnInit} from '@angular/core';
import {User} from '../../authentication/register/user.interface';
import {UserProfileService} from './user-profile.service';
import {AuthService} from '../../auth.service';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.scss']
})
export class UserProfileComponent implements OnInit {


  private users: User[];
  message: string;
  isAdmin = this.authService.getRole() === 'ADMIN';

  constructor(private userProfileService: UserProfileService, private authService: AuthService) {
  }


  ngOnInit(): void {
    this.getUser();
  }

  updateUser(user: User) {
    this.userProfileService.updateUser(user).subscribe(() => {
      this.message = 'Updated!';
    }, (error) => {
      this.message = error.error;
    });
  }

  changeRole(data: string[]) {
    this.userProfileService.changerRole(data[0], data[1]).subscribe(() => {
      this.message = 'Role changed!';
      this.getUser();
    }, error => {
      this.message = error.error;
    });
  }

  private getUser() {
    if (this.authService.getRole() === 'ADMIN') {
      this.userProfileService.getUsers().subscribe((users: User[]) => {
          this.users = users;
        }, error => {
        }
      );
    } else {
      this.userProfileService.getUsers().subscribe((users: User) => {
          this.users = [users];
        }, error => {
        }
      );
    }
  }
}
