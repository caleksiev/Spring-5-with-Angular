import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {AuthService} from '../../auth.service';
import {ServerAuthenticationService} from '../server-authentication.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;
  errorFormMessage: string;
  color;

  constructor(private formBuilder: FormBuilder, private myRoute: Router,
              private auth: AuthService, private serverAuthenticationService: ServerAuthenticationService) {
  }


  private encryptUserDetails(username, password): string {
    return btoa(`${username}:${password}`);
  }

  ngOnInit() {
    this.loginForm = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  login(): void {
    if (!this.loginForm.valid) {
      this.errorFormMessage = 'Please fill all fields';
      this.color = '#FF3333';
    }
    if (this.loginForm.valid) {
      this.serverAuthenticationService.login(this.loginForm.get('username').value, this.loginForm.get('password').value).subscribe(() => {
        this.auth.sendToken(this.encryptUserDetails(this.loginForm.get('username').value, this.loginForm.get('password').value));
        this.myRoute.navigate(['homepage']);
      }, (error) => {
        if (error.status === 401) {
          this.errorFormMessage = 'Wrong credentials';
          this.color = '#FF3333';
        }
      });
    }
  }
}

