import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { map } from 'rxjs/operators';

import * as AuthActions from '@mbti-app/features/auth/shared/state/auth.actions';
import { AuthState } from '@mbti-app/features/auth/shared/state/auth.reducer';
import { Store } from '@ngrx/store';

@Injectable()
export class RouteEffects {
  constructor(
    private actions$: Actions,
    private route: Router,
    private store: Store<AuthState>
  ) {}

  gohome$ = createEffect(
    () =>
      this.actions$.pipe(
        ofType(AuthActions.logout, AuthActions.loginRoute),
        map(() => {
          setTimeout(() => {
            this.route.navigate(['/home']);
          }, 100);
        })
      ),
    { dispatch: false }
  );

  loginSuccessMessage$ = createEffect(
    () =>
      this.actions$.pipe(
        ofType(AuthActions.loginRoute),
        map(() => {
          setTimeout(() => {
            this.store.dispatch(AuthActions.loginNotification());
          }, 1000);
        })
      ),
    { dispatch: false }
  );

  goToLoginPage$ = createEffect(
    () =>
      this.actions$.pipe(
        ofType(AuthActions.registerRoute),
        map(() => {
          setTimeout(() => {
            this.route.navigate(['/auth/login']);
          }, 1000);
        })
      ),
    { dispatch: false }
  );

  registerSuccessMessage$ = createEffect(
    () =>
      this.actions$.pipe(
        ofType(AuthActions.registerRoute),
        map(() => {
          setTimeout(() => {
            this.store.dispatch(AuthActions.registerNotification());
          }, 1000);
        })
      ),
    { dispatch: false }
  );
}
