import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { NgxSpinnerService } from 'ngx-spinner';
import { map, tap } from 'rxjs/operators';

import * as AuthActions from '@mbti-app/features/auth/shared/state/auth.actions';
import * as UserActions from '@mbti-app/features/user/shared/state/user.actions';
import * as MbtiActions from '@mbti-app/features/mbti/state/mbti.actions';

import { AppState } from '..';
import { Store } from '@ngrx/store';

@Injectable()
export class SpinnerEffects {
  constructor(
    private actions$: Actions,
    private spinner: NgxSpinnerService,
    private store: Store<AppState>
  ) {}

  spinnerOn$ = createEffect(
    () =>
      this.actions$.pipe(
        ofType(
          AuthActions.loginPage,
          AuthActions.registerPage,
          UserActions.updatePage,
          MbtiActions.processMbtiTest,
          MbtiActions.loadMbtiPersonalities,
          MbtiActions.loadMbtiPersonalityTest,
          MbtiActions.loadMbtiPersonalityTestFromUserProfile
        ),
        tap(() => this.spinner.show())
      ),
    { dispatch: false }
  );

  spinnerOff$ = createEffect(
    () =>
      this.actions$.pipe(
        ofType(
          AuthActions.loginFailure,
          AuthActions.registerFailure,
          UserActions.updateFailure,
          MbtiActions.processMbtiTestFailure,
          MbtiActions.loadMbtiPersonalitiesFailure,
          MbtiActions.loadMbtiPersonalityTestFailure
        ),
        tap(() => {
          setTimeout(() => {
            this.spinner.hide();
          }, 300);
        })
      ),
    { dispatch: false }
  );

  spinnerOffLoginSuccess$ = createEffect(
    () =>
      this.actions$.pipe(
        ofType(AuthActions.loginSuccess),
        map(() => {
          setTimeout(() => {
            this.spinner.hide();
            this.store.dispatch(AuthActions.loginRoute());
          }, 500);
        })
      ),
    { dispatch: false }
  );

  spinnerOffRegisterSuccess$ = createEffect(
    () =>
      this.actions$.pipe(
        ofType(AuthActions.registerSuccess),
        map(() => {
          setTimeout(() => {
            this.spinner.hide();
            this.store.dispatch(AuthActions.registerRoute());
          }, 500);
        })
      ),
    { dispatch: false }
  );

  spinnerOffProcessMbtiTestSuccess$ = createEffect(
    () =>
      this.actions$.pipe(
        ofType(AuthActions.registerSuccess),
        map(() => {
          setTimeout(() => {
            this.spinner.hide();
            this.store.dispatch(AuthActions.registerRoute());
          }, 500);
        })
      ),
    { dispatch: false }
  );

  spinnerOffSuccess$ = createEffect(
    () =>
      this.actions$.pipe(
        ofType(
          UserActions.updateSuccess,
          MbtiActions.processMbtiTestSuccess,
          MbtiActions.loadMbtiPersonalitiesSuccess,
          MbtiActions.loadMbtiPersonalityTestSuccess
        ),
        map(() => {
          setTimeout(() => {
            this.spinner.hide();
          }, 500);
        })
      ),
    { dispatch: false }
  );
}
