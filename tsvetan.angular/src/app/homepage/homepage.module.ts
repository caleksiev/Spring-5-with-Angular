import {NgModule} from '@angular/core';
import {CommonModule, DatePipe} from '@angular/common';
import {RouterModule, Routes} from '@angular/router';
import {HomepageComponent} from './homepage.component';
import {NgxPaginationModule} from 'ngx-pagination';
import {AuthGuard} from '../auth.guard';
import {CreateNotesComponent} from './create-note/create-notes.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {RoleGuard} from '../role.guard';
import {NotesComponent} from './notes/notes.component';
import {NoteComponent} from './notes/note/note.component';
import {DashboardComponent} from './dashboard/dashboard.component';
import {NoopAnimationsModule} from '@angular/platform-browser/animations';
import {MatSnackBar, MatSnackBarModule} from '@angular/material';
import {UserProfileComponent} from './user-profile/user-profile.component';
import {SingleUserComponent} from './user-profile/single-user/single-user.component';
import {TeamsComponent} from './teams/teams.component';
import { TeamComponent } from './teams/team/team.component';
import { RoomsComponent } from './rooms/rooms.component';
import { RoomComponent } from './rooms/room/room.component';


const appRoutes: Routes = [
  {
    path: 'homepage',
    component: HomepageComponent,
    canActivate: [AuthGuard],

    children: [
      {
        path: 'company/notes',
        component: NotesComponent
      },
      {
        path: 'manager',
        component: NotesComponent,
        canActivate: [RoleGuard],
        data: {
          expectedRole: ['MANAGER']
        },
      },
      {
        path: 'room',
        component: RoomsComponent,
        canActivate: [RoleGuard],
        data: {
          expectedRole: ['ADMIN']
        },
      },
      {
        path: 'teams',
        component: TeamsComponent,
        canActivate: [RoleGuard],
        data: {
          expectedRole: ['ADMIN', 'MANAGER']
        }
      },
      {
        path: 'user-profile',
        component: UserProfileComponent
      },
      {
        path: 'team/notes',
        component: NotesComponent
      },
      {
        path: 'create/notes',
        component: CreateNotesComponent
      },
      {
        path: 'dashboard',
        component: DashboardComponent
      }
    ]
  }
];

@NgModule({


  declarations: [HomepageComponent,
    CreateNotesComponent,
    NotesComponent,
    NoteComponent,
    DashboardComponent,
    UserProfileComponent,
    SingleUserComponent,
    TeamsComponent,
    TeamComponent,
    RoomsComponent,
    RoomComponent,
  ],
  imports: [
    CommonModule,
    RouterModule.forChild(appRoutes),
    NgxPaginationModule,
    ReactiveFormsModule,
    FormsModule,
    MatSnackBarModule,
    NoopAnimationsModule,
  ],
  providers: [DatePipe, MatSnackBar]
})
export class HomepageModule {
}
