import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {ServerAuthenticationService} from '../server-authentication.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {
  userForm: FormGroup;
  color: string;
  registrationResponse: string;

  constructor(private formBuilder: FormBuilder, private serverAuthenticationService: ServerAuthenticationService, private route: Router) {
  }

  ngOnInit() {
    this.userForm = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', Validators.required],
      firstName: ['', Validators.required],
      lastName: ['', Validators.required]
    });
  }

  register() {
    // if (!this.userForm.valid) {
    //   this.registrationResponse = 'Please fill all fields.';
    //   this.color = '#FF3333';
    //   this.userForm.reset();
    // }
    if (this.userForm.valid) {
      this.serverAuthenticationService.register(this.userForm.getRawValue()).subscribe(() => {
        this.registrationResponse = 'Successful registration. Go to login page.';
        this.color = '#47FF33';
      }, error => {
        this.color = '#FF3333';
        if (error.status === 409) {
          this.registrationResponse = 'This username already exists.';
          this.userForm.reset();
        } else {
          this.registrationResponse = 'Something went wrong! Check your connection.';
        }
      });
    } else {
      this.registrationResponse = 'Please fill all fields.';
      this.color = '#FF3333';
    }
  }
}
