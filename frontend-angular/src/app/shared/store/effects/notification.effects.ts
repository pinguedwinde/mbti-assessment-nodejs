import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { Store, select } from '@ngrx/store';
import { map, withLatestFrom } from 'rxjs/operators';
import { Observable } from 'rxjs';

import {
  NgxNotificationDirection,
  NgxNotificationMsgService,
  NgxNotificationStatusMsg,
} from 'ngx-notification-msg';

import * as AuthActions from '@mbti-app/features/auth/shared/state/auth.actions';
import * as UserActions from '@mbti-app/features/user/shared/state/user.actions';
import * as AuthSelectors from '@mbti-app/features/auth/shared/state/auth.selectors';
import { NotificationMessage } from '@mbti-app/shared/models/notification.model';
import { AuthState } from '@mbti-app/features/auth/shared/state/auth.reducer';
import * as GuardActions from '@mbti-app/shared/store/actions/guard.actions';
import { UserService } from '@mbti-app/features/user/shared/services/user.service';

@Injectable()
export class NotificationEffects {
  private notificationMsg$: Observable<NotificationMessage> = this.store.pipe(
    select(AuthSelectors.selectNotificationMsg)
  );

  constructor(
    private actions$: Actions,
    private store: Store<AuthState>,
    private userService: UserService,
    private readonly ngxNotificationMsgService: NgxNotificationMsgService
  ) {}

  authsuccessActions$ = createEffect(
    () =>
      this.actions$.pipe(
        ofType(
          AuthActions.loginNotification,
          AuthActions.registerNotification,
          AuthActions.logout,
          UserActions.updateSuccess
        ),
        withLatestFrom(
          this.store.pipe(select(AuthSelectors.selectNotificationMsg))
        ),
        map(([_, notificationMsg]) => {
          this.ngxNotificationMsgService.open({
            status: NgxNotificationStatusMsg.SUCCESS,
            header: notificationMsg.header,
            messages: [notificationMsg.body],
            direction: NgxNotificationDirection.TOP_RIGHT,
            delay: 10000,
            displayIcon: true,
          });
        })
      ),
    { dispatch: false }
  );

  guardActions$ = createEffect(
    () =>
      this.actions$.pipe(
        ofType(GuardActions.cannotActivate),
        map((action) => {
          this.ngxNotificationMsgService.open({
            status: NgxNotificationStatusMsg.FAILURE,
            header: action.notificationMsg.header,
            messages: [action.notificationMsg.body],
            direction: NgxNotificationDirection.TOP_RIGHT,
            delay: 10000,
            displayIcon: true,
          });
        })
      ),
    { dispatch: false }
  );
}
