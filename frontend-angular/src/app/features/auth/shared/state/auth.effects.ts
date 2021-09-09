import { Injectable } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { catchError, map, concatMap, tap } from 'rxjs/operators';
import { of } from 'rxjs';

import * as AuthActions from './auth.actions';
import { AuthService } from '../services/auth.service';
import { User } from '@mbti-app/shared/models/user.model';
import { HeaderType } from '@mbti-app/shared/enums/header-type.enum';

@Injectable()
export class AuthEffects {
  constructor(private actions$: Actions, private authService: AuthService) {}

  login$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(AuthActions.loginPage),
      concatMap((action) =>
        this.authService.login(action.userCredentials).pipe(
          map((response: HttpResponse<User>) => {
            const token = response.headers.get(HeaderType.JWT_TOKEN);
            const user = response.body;
            const notificationMsg = {
              header: 'Login Notification',
              body: `Successfully logged in. Welcome again ${user.firstName}.`,
            };
            this.authService.saveToken(token);
            this.authService.addUserToLocalCache(response.body);
            return AuthActions.loginSuccess({
              user: user,
              token: token,
              notificationMsg: notificationMsg,
            });
          }),
          catchError((errorResponse: HttpErrorResponse) => {
            let message: string;
            if (errorResponse.error.message) {
              message = errorResponse.error.message;
            } else if (errorResponse.error.messages) {
              message = errorResponse.error.messages.join('-');
            } else {
              message = 'An error occurred. Please try again.';
            }
            return of(
              AuthActions.loginFailure({
                error: message,
              })
            );
          })
        )
      )
    );
  });

  register$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(AuthActions.registerPage),
      concatMap((action) =>
        this.authService.register(action.userForm).pipe(
          map((user: User) => {
            const notificationMsg = {
              header: 'Registration Notification',
              body: `A new account was created for ${user.firstName}.
          You can now log in.`,
            };
            return AuthActions.registerSuccess({ notificationMsg });
          }),
          catchError((errorResponse: HttpErrorResponse) => {
            if (errorResponse.error.message) {
              return of(
                AuthActions.registerFailure({
                  error: errorResponse.error.message,
                })
              );
            } else {
              return of(
                AuthActions.registerFailure({
                  error: 'An error occurred. Please try again.',
                })
              );
            }
          })
        )
      )
    );
  });

  logout$ = createEffect(
    () =>
      this.actions$.pipe(
        ofType(AuthActions.logout),
        tap(() => {
          this.authService.logOut();
        })
      ),
    { dispatch: false }
  );
}
