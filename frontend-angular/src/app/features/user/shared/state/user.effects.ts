import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { of } from 'rxjs';
import { concatMap, map, catchError } from 'rxjs/operators';

import * as UserActions from './user.actions';
import { UserService } from '../services/user.service';
import { User } from '@mbti-app/shared/models/user.model';

@Injectable()
export class UserEffects {
  constructor(private actions$: Actions, private userService: UserService) {}

  update$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(UserActions.updatePage),
      concatMap((action) =>
        this.userService.updateUser(action.userForm).pipe(
          map((user: User) => {
            const notificationMsg = {
              header: 'Profile Updating',
              body: `Your profile was successfully updated.`,
            };
            this.userService.updateUserInLocalCache(user);
            return UserActions.updateSuccess({ user, notificationMsg });
          }),
          catchError((errorResponse: HttpErrorResponse) => {
            if (errorResponse.error.message) {
              return of(
                UserActions.updateFailure({
                  error: errorResponse.error.message,
                })
              );
            } else {
              return of(
                UserActions.updateFailure({
                  error: 'An error occurred. Please try again.',
                })
              );
            }
          })
        )
      )
    );
  });
}
