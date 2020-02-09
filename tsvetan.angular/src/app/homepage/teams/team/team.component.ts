import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Team} from '../teams.interface';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Dev} from '../../../authentication/register/dev.interface';

@Component({
  selector: 'app-team',
  templateUrl: './team.component.html',
  styleUrls: ['./team.component.scss']
})
export class TeamComponent implements OnInit {

  @Input() team: Team;
  @Input() isAdmin;
  @Input() managers;
  @Input() isManager;
  @Input() devsWithouTeam: Dev[];
  @Input() devsInCurrentTeam: Dev[];

  @Output() addManagerEvent: EventEmitter<string[]> = new EventEmitter();
  @Output() removeManagerEvent: EventEmitter<string> = new EventEmitter();

  @Output() addDevEvent: EventEmitter<string[]> = new EventEmitter();
  @Output() removeDevEvent: EventEmitter<string[]> = new EventEmitter();

  showAddManagerFrom = false;
  showAddMDevFrom = false;

  addManagerForm: FormGroup;
  addDevForm: FormGroup;

  constructor(private formBuilder: FormBuilder) {
  }

  ngOnInit() {
    this.addManagerForm = this.formBuilder.group({
      name: ['', [Validators.required]],
    });

    this.addDevForm = this.formBuilder.group({
      name: ['', [Validators.required]],
    });
  }

  showManagerFrom() {
    this.showAddManagerFrom = !this.showAddManagerFrom;
  }

  showDevFrom() {
    this.showAddMDevFrom = !this.showAddMDevFrom;
  }

  addManager() {
    this.addManagerEvent.emit([this.addManagerForm.get('name').value, this.team.name]);
  }

  removeManager() {
    this.removeManagerEvent.emit(this.team.name);
  }

  addDev() {
    this.addDevEvent.emit([this.addDevForm.get('name').value, this.team.name]);
  }

  removeDev(dev: Dev) {
    this.removeDevEvent.emit([dev.user.username, this.team.name]);
  }
}
