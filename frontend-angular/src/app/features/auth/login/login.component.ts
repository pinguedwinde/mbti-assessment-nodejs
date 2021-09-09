import { Observable } from 'rxjs';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { select, Store } from '@ngrx/store';

import { UserCredentials } from '@mbti-app/shared/models/user-credentials.model';
import * as AuthActions from '@mbti-app/features/auth/shared/state/auth.actions';
import * as AuthSelectors from '@mbti-app/features/auth/shared/state/auth.selectors';
import { AuthState } from '../shared/state/auth.reducer';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent implements OnInit {
  public loginForm: FormGroup;
  public onLoginError$: Observable<string>;

  constructor(
    private formBuilder: FormBuilder,
    private store: Store<AuthState>
  ) {}

  ngOnInit(): void {
    this.loginForm = this.formBuilder.group({
      username: ['', [Validators.required, Validators.minLength(5)]],
      password: ['', Validators.required],
    });

    this.onLoginError$ = this.store.pipe(select(AuthSelectors.selectAuthError));
  }

  public get username() {
    return this.loginForm.get('username');
  }

  public get password() {
    return this.loginForm.get('password');
  }

  public get isUsernameInvalid() {
    return (
      this.username.invalid && (this.username.dirty || this.username.touched)
    );
  }

  public get isPasswordInvalid() {
    return (
      this.password.invalid && (this.password.dirty || this.password.touched)
    );
  }

  public onLogin(): void {
    const userCredentials: UserCredentials = {
      username: this.username.value,
      password: this.password.value,
    };
    this.store.dispatch(AuthActions.loginPage({ userCredentials }));
  }
}
