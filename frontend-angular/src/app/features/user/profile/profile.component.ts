import { Component, OnInit } from '@angular/core';
import { Store, select } from '@ngrx/store';
import { Observable } from 'rxjs';

import { AuthState } from '@mbti-app/features/auth/shared/state/auth.reducer';
import * as AuthActions from '@mbti-app/features/auth/shared/state/auth.actions';
import * as UserActions from '@mbti-app/features/user/shared/state/user.actions';
import * as AuthSelectors from '@mbti-app/features/auth/shared/state/auth.selectors';
import * as MbtiSelectors from '@mbti-app/features/mbti/state/mbti.selectors';
import * as MbtiActions from '@mbti-app/features/mbti/state/mbti.actions';

import { User, UserForm } from '@mbti-app/shared/models/user.model';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Role } from '@mbti-app/shared/enums/role.enum';
import { Score } from '@mbti-app/shared/models/score.model';
import { MbtiState } from '@mbti-app/features/mbti/state/mbti.reducer';
import { defaultIfEmpty, map } from 'rxjs/operators';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss'],
})
export class ProfileComponent implements OnInit {
  public user$: Observable<User>;
  public currentUsername: string;
  public refreshing: boolean;
  public fileName: string;
  public profileImage: File;
  public isAdmin$: Observable<boolean>;
  public profileUserForm: FormGroup;
  public onProfileUserFormError$: Observable<string>;
  public score$: Observable<Score>;

  namePattern = "^[a-zA-Z]+(([' -][a-zA-Z ])?[a-zA-Z]*)*$";
  usernamePattern = '^[a-z0-9_-]{5,15}$';
  passwordPattern = '^(?=.*d)(?=.*[a-z])(?=.*[A-Z]).{8,15}$';
  emailPattern = '^[a-z0-9._%+-]+@[a-z0-9.-]+.[a-z]{2,4}$';

  roleOptions = [
    { value: 'USER', label: 'USER' },
    { value: 'ADMIN', label: 'ADMIN' },
    { value: 'SUPER_ADMIN', label: 'SUPER_ADMIN' },
  ];

  constructor(
    private formBuilder: FormBuilder,
    private storeAuth: Store<AuthState>,
    private storeMbti: Store<MbtiState>
  ) {}

  ngOnInit(): void {
    this.user$ = this.storeAuth.pipe(select(AuthSelectors.selectUser));
    this.isAdmin$ = this.storeAuth.pipe(select(AuthSelectors.selectIsAdmin));
    this.onProfileUserFormError$ = this.storeAuth.pipe(
      select(AuthSelectors.selectAuthError)
    );
    this.score$ = this.storeMbti.pipe(select(MbtiSelectors.selectScore));
    this.score$.subscribe((score: Score) => {
      if (score === null) {
        const user = JSON.parse(localStorage.getItem('user'));
        if (user) {
          this.storeMbti.dispatch(
            MbtiActions.loadMbtiPersonalityTestFromUserProfile({
              username: user.username,
            })
          );
        }
      }
      return score;
    });
    this.user$.subscribe((user: User) => {
      this.profileUserForm = this.formBuilder.group({
        firstName: [
          user?.firstName,
          [
            Validators.required,
            Validators.minLength(3),
            Validators.pattern(this.namePattern),
          ],
        ],
        lastName: [
          user?.lastName,
          [
            Validators.required,
            Validators.minLength(3),
            Validators.pattern(this.namePattern),
          ],
        ],
        username: [
          user?.username,
          [
            Validators.required,
            Validators.minLength(5),
            Validators.pattern(this.usernamePattern),
          ],
        ],
        email: [
          user?.email,
          [Validators.required, Validators.pattern(this.emailPattern)],
        ],
        role: [''],
        enabled: [
          { value: user?.enabled, disabled: this.isAdmin(user) ? false : true },
        ],
        nonLocked: [
          {
            value: user?.nonLocked,
            disabled: this.isAdmin(user) ? false : true,
          },
        ],
      });
    });
  }

  public onLogout(): void {
    const notificationMsg = {
      header: 'Logout Notification',
      body: `Successfully logged out.
          See you !`,
    };
    this.storeAuth.dispatch(AuthActions.logout({ notificationMsg }));
  }

  public onSave(): void {
    this.user$.subscribe(
      (user: User) => (this.currentUsername = user.username)
    );
    const userForm: UserForm = {
      lastName: this.lastName.value,
      firstName: this.firstName.value,
      email: this.email.value,
      username: this.username.value,
      currentUsername: this.currentUsername,
      role: this.role.value,
      isEnabled: this.enabled.value,
      isNonLocked: this.nonLocked.value,
    };
    this.storeAuth.dispatch(UserActions.updatePage({ userForm }));
  }

  public get firstName() {
    return this.profileUserForm.get('firstName');
  }

  public get lastName() {
    return this.profileUserForm.get('lastName');
  }

  public get username() {
    return this.profileUserForm.get('username');
  }

  public get email() {
    return this.profileUserForm.get('email');
  }

  public get role() {
    return this.profileUserForm.get('role');
  }

  public get enabled() {
    return this.profileUserForm.get('enabled');
  }

  public get nonLocked() {
    return this.profileUserForm.get('nonLocked');
  }

  public get isFirstNameInvalid() {
    return (
      this.firstName.invalid && (this.firstName.dirty || this.firstName.touched)
    );
  }

  public get isFirstNameValid() {
    return (
      this.firstName.valid && (this.firstName.dirty || this.firstName.touched)
    );
  }

  public get isLastNameInvalid() {
    return (
      this.lastName.invalid && (this.lastName.dirty || this.lastName.touched)
    );
  }

  public get isLastNameValid() {
    return (
      this.lastName.valid && (this.lastName.dirty || this.lastName.touched)
    );
  }

  public get isUsernameInvalid() {
    return (
      this.username.invalid && (this.username.dirty || this.username.touched)
    );
  }

  public get isUsernameValid() {
    return (
      this.username.valid && (this.username.dirty || this.username.touched)
    );
  }

  public get isEmailInvalid() {
    return this.email.invalid && (this.email.dirty || this.email.touched);
  }

  public get isEmailValid() {
    return this.email.valid && (this.email.dirty || this.email.touched);
  }

  private isAdmin(user: User): boolean {
    if (user) {
      return user.role === Role.ADMIN || user.role === Role.SUPER_ADMIN;
    } else {
      return null;
    }
  }
}
