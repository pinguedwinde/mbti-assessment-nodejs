import { createReducer, on } from '@ngrx/store';

import { NotificationMessage } from '@mbti-app/shared/models/notification.model';
import { User } from '@mbti-app/shared/models/user.model';
import * as AuthActions from './auth.actions';
import * as UserActions from '@mbti-app/features/user/shared/state/user.actions';
import { Role } from '@mbti-app/shared/enums/role.enum';

export const authFeatureKey = 'auth';

export interface AuthState {
  user: User;
  token: string;
  isAdmin: boolean;
  notificationMsg: NotificationMessage;
  error: string;
}

export const initialState: AuthState = {
  user: null,
  token: localStorage.getItem('token'),
  isAdmin: null,
  notificationMsg: null,
  error: null,
};

export const reducer = createReducer(
  initialState,
  on(AuthActions.loginSuccess, (state, action) => {
    return {
      ...state,
      user: action.user,
      token: action.token,
      isAdmin:
        action.user.role === Role.ADMIN ||
        action.user.role === Role.SUPER_ADMIN,
      notificationMsg: action.notificationMsg,
      error: null,
    };
  }),
  on(AuthActions.browserReload, (state, action) => {
    return {
      ...state,
      user: action.user,
      isAdmin:
        action.user.role === Role.ADMIN ||
        action.user.role === Role.SUPER_ADMIN,
      error: null,
    };
  }),
  on(
    AuthActions.loginFailure,
    AuthActions.registerFailure,
    UserActions.updateFailure,
    (state, action) => {
      return {
        ...state,
        error: action.error,
      };
    }
  ),
  on(AuthActions.registerSuccess, (state, action) => {
    return {
      ...state,
      notificationMsg: action.notificationMsg,
      error: null,
    };
  }),
  on(UserActions.updateSuccess, (state, action) => {
    return {
      ...state,
      user: action.user,
      isAdmin:
        action.user.role === Role.ADMIN ||
        action.user.role === Role.SUPER_ADMIN,
      notificationMsg: action.notificationMsg,
      error: null,
    };
  }),
  on(AuthActions.logout, (state, action) => {
    return {
      ...state,
      notificationMsg: action.notificationMsg,
      user: null,
      token: null,
      isAdmin: null,
      error: null,
    };
  })
);
