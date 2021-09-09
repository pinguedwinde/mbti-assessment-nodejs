import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { select, Store } from '@ngrx/store';
import { Observable } from 'rxjs';

import { CustomValidators } from '../shared/custom-validators';
import { AuthState } from '../shared/state/auth.reducer';
import * as AuthActions from '@mbti-app/features/auth/shared/state/auth.actions';
import * as AuthSelectors from '@mbti-app/features/auth/shared/state/auth.selectors';
import { User, UserForm } from '@mbti-app/shared/models/user.model';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss'],
})
export class RegisterComponent implements OnInit {
  public registrationForm: FormGroup;
  public onRegisterError$: Observable<string>;

  namePattern = "^[a-zA-Z]+(([' -][a-zA-Z ])?[a-zA-Z]*)*$";
  usernamePattern = '^[a-z0-9_-]{5,15}$';
  passwordPattern = '^(?=.*d)(?=.*[a-z])(?=.*[A-Z]).{8,15}$';
  emailPattern = '^[a-z0-9._%+-]+@[a-z0-9.-]+.[a-z]{2,4}$';

  constructor(
    private formBuilder: FormBuilder,
    private store: Store<AuthState>
  ) {}

  ngOnInit(): void {
    this.registrationForm = this.formBuilder.group(
      {
        firstName: [
          '',
          [
            Validators.required,
            Validators.minLength(3),
            Validators.pattern(this.namePattern),
          ],
        ],
        lastName: [
          '',
          [
            Validators.required,
            Validators.minLength(3),
            Validators.pattern(this.namePattern),
          ],
        ],
        username: [
          '',
          [
            Validators.required,
            Validators.minLength(5),
            Validators.pattern(this.usernamePattern),
          ],
        ],
        email: [
          '',
          [Validators.required, Validators.pattern(this.emailPattern)],
        ],
        password: [
          '',
          [
            Validators.required,
            CustomValidators.patternValidator(/\d/, { hasNumber: true }),
            CustomValidators.patternValidator(/[A-Z]/, { hasUpperCase: true }),
            CustomValidators.patternValidator(/[a-z]/, { hasLowerCase: true }),
            CustomValidators.patternValidator(/[!?@#$%^&%*:,.;'"_=\-\+<>]/, {
              hasSpecialCharacters: true,
            }),
            Validators.minLength(8),
            Validators.maxLength(20),
          ],
        ],
        confirmPassword: ['', [Validators.required]],
      },
      {
        validator: CustomValidators.passwordMatchValidator,
      }
    );
    this.onRegisterError$ = this.store.pipe(
      select(AuthSelectors.selectAuthError)
    );
  }

  public get firstName() {
    return this.registrationForm.get('firstName');
  }

  public get lastName() {
    return this.registrationForm.get('lastName');
  }

  public get username() {
    return this.registrationForm.get('username');
  }

  public get email() {
    return this.registrationForm.get('email');
  }

  public get password() {
    return this.registrationForm.get('password');
  }

  public get confirmPassword() {
    return this.registrationForm.get('confirmPassword');
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

  public get isPasswordInvalid() {
    return (
      this.password.invalid && (this.password.dirty || this.password.touched)
    );
  }

  public get isPasswordValid() {
    return (
      this.password.valid && (this.password.dirty || this.password.touched)
    );
  }

  public get isConfirmPasswordInvalid() {
    return (
      this.confirmPassword.invalid &&
      (this.confirmPassword.dirty || this.confirmPassword.touched)
    );
  }

  public get isConfirmPasswordValid() {
    return (
      this.confirmPassword.valid &&
      (this.confirmPassword.dirty || this.confirmPassword.touched)
    );
  }

  public onRegister(): void {
    const userForm: UserForm = {
      lastName: this.lastName.value,
      firstName: this.firstName.value,
      email: this.email.value,
      username: this.username.value,
      password: this.password.value,
    };
    this.store.dispatch(AuthActions.registerPage({ userForm }));
  }
}
