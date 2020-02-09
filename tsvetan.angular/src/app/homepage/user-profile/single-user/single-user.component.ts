import {Component, EventEmitter, Input, OnChanges, OnInit, Output} from '@angular/core';
import {User} from '../../../authentication/register/user.interface';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-single-user',
  templateUrl: './single-user.component.html',
  styleUrls: ['./single-user.component.scss']
})
export class SingleUserComponent implements OnInit {
  @Input() user: User;
  @Input() isAdmin;
  @Output() private userEdit: EventEmitter<User> = new EventEmitter();
  private roles: string[] = ['ADMIN', 'MANAGER', 'DEV'];
  private userForm: FormGroup;
  showEdit = false;
  showRole = false;
  @Output() usersChangeRole: EventEmitter<string[]> = new EventEmitter();

  constructor(private formBuilder: FormBuilder) {
  }


  ngOnInit() {
    this.userForm = this.formBuilder.group({
      role: ['', [Validators.required]],
    });
  }

  showEditForm() {
    this.showEdit = !this.showEdit;
  }

  editNote() {
    this.showEdit = false;
    this.userEdit.emit(this.user);
  }

  changeRole() {
    if (!this.userForm.valid) {
      return;
    }
    if (confirm('Are you sure that you want to make ' + this.user.username + ' a ' + this.userForm.get('role').value)) {
      this.usersChangeRole.emit([this.user.username, this.userForm.get('role').value]);
    }
  }

  showChangeRoleForm() {
    this.showRole = !this.showRole;
  }
}
