import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {AuthService} from '../auth.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './homepage.component.html',
  styleUrls: ['./homepage.component.scss']
})
export class HomepageComponent implements OnInit {
  private username;


  constructor(private router: Router, private auth: AuthService) {
  }

  ngOnInit(): void {
    this.username = this.auth.getUsername();

  }


}
