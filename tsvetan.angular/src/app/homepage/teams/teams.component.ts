import {Component, OnInit} from '@angular/core';
import {TeamsService} from './teams.service';
import {Team} from './teams.interface';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {AuthService} from '../../auth.service';
import {UserProfileService} from '../user-profile/user-profile.service';
import {User} from '../../authentication/register/user.interface';
import {Dev} from '../../authentication/register/dev.interface';

@Component({
  selector: 'app-teams',
  templateUrl: './teams.component.html',
  styleUrls: ['./teams.component.scss']
})
export class TeamsComponent implements OnInit {

  teams: Team[];
  managers: string[];
  devsWithoutTeam: Dev[];
  devsWithTeam: Dev[];
  createTeamForm: FormGroup;
  message;
  showAddTeamForm = false;
  showAddManagerFrom = false;
  isAdmin = this.authService.getRole() === 'ADMIN';
  isManager = this.authService.getRole() === 'MANAGER';

  constructor(private userProfileService: UserProfileService,
              private formBuilder: FormBuilder,
              private teamsService: TeamsService,
              private authService: AuthService) {
  }

  ngOnInit() {
    this.createTeamForm = this.formBuilder.group({
      name: ['', [Validators.required]],
    });


    this.userProfileService.getUsers().subscribe((users: User[]) => {
      this.teamsService.getAllTeams().subscribe((teams: Team[]) => {
        this.teams = teams;
        this.managers = users.filter(u => u.role === 'MANAGER').map((m) => {
          return m.username;
        });
      }, error => {
      });

    });

    this.userProfileService.getDevs().subscribe((devs: Dev[]) => {
      this.devsWithoutTeam = devs.filter(d => !d.team);
      this.devsWithTeam = devs.filter(d => d.team);
    }, error => {
    });


  }


  createTeam() {
    this.teamsService.addTeam(this.createTeamForm.getRawValue()).subscribe(() => {
      this.message = 'The team was added';
      this.teamsService.getAllTeams().subscribe((teams: Team[]) => {
        this.teams = teams;
      }, error => {
      });
    }, (error) => {
      this.message = error.error.message;
    });
  }

  showTeamForm() {
    this.showAddTeamForm = !this.showAddTeamForm;
  }

  showManagerFrom() {
    this.showAddManagerFrom = !this.showAddManagerFrom;
  }

  addManager(data: string[]) {
    this.teamsService.addManager(data[0], data[1]).subscribe(() => {
      this.message = 'The manager was added!';
      this.teamsService.getAllTeams().subscribe((teams: Team[]) => {
        this.teams = teams;
      });
    }, error => {
      this.message = error.error.message;
    });
  }

  removeManager(teamName: string) {
    this.teamsService.removeManager(teamName).subscribe(() => {
      this.message = 'The manager was removed!';
      this.teamsService.getAllTeams().subscribe((teams: Team[]) => {
        this.teams = teams;
      });
    }, error => {
      this.message = error.error.message;
    });
  }

  filterDevInTeam(team: Team) {
    return this.devsWithTeam.filter((d) => d.team.name === team.name);
  }

  addDev(data: string[]) {
    this.teamsService.addDev(data[0], data[1]).subscribe(() => {
      this.message = 'The dev was added!';
      this.userProfileService.getUsers().subscribe((users: User[]) => {
        this.teamsService.getAllTeams().subscribe((teams: Team[]) => {
          this.teams = teams;
          this.managers = users.filter(u => u.role === 'MANAGER').map((m) => {
            return m.username;
          });
        }, error => {
        });

      });

      this.userProfileService.getDevs().subscribe((devs: Dev[]) => {
        this.devsWithoutTeam = devs.filter(d => !d.team);
        this.devsWithTeam = devs.filter(d => d.team);
      }, error => {
      });

    }, error => {
      this.message = error.error.message;
    });
  }

  removeDev(data: string[]) {
    this.teamsService.removeDev(data[0], data[1]).subscribe(() => {
      this.message = 'The dev was removed!';

      this.userProfileService.getUsers().subscribe((users: User[]) => {
        this.teamsService.getAllTeams().subscribe((teams: Team[]) => {
          this.teams = teams;
          this.managers = users.filter(u => u.role === 'MANAGER').map((m) => {
            return m.username;
          });
        }, error => {
        });

      });

      this.userProfileService.getDevs().subscribe((devs: Dev[]) => {
        this.devsWithoutTeam = devs.filter(d => !d.team);
        this.devsWithTeam = devs.filter(d => d.team);
      }, error => {
      });

    }, error => {
      this.message = error.error.message;

    });
  }
}
