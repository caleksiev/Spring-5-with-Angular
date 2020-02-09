import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {RouterModule, Routes} from '@angular/router';
import {AuthService} from './auth.service';
import {AuthGuard} from './auth.guard';
import {AuthenticationModule} from './authentication/authentication.module';
import {HomepageModule} from './homepage/homepage.module';
import {RoleGuard} from './role.guard';


const routes: Routes = [
  {
    path: 'authentication',
    loadChildren: './authentication/authentication.module#AuthenticationModule'
  },

  {
    path: 'homepage',
    loadChildren: './homepage/homepage.module#HomepageModule',
  },
  {
    path: '**',
    redirectTo: 'authentication/login'
  }
];

@NgModule({
  declarations: [
    AppComponent,
  ],
  imports: [
    BrowserModule,
    AuthenticationModule,
    HomepageModule,
    RouterModule.forRoot(routes),
  ],
  providers: [AuthService, AuthGuard, RoleGuard],
  bootstrap: [AppComponent]
})
export class AppModule {
}
