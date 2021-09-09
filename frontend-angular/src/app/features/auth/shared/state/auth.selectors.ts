import { createFeatureSelector, createSelector } from '@ngrx/store';

import { NotificationMessage } from '@mbti-app/shared/models/notification.model';
import { User } from '@mbti-app/shared/models/user.model';
import * as fromAuth from './auth.reducer';

export const selectAuthState = createFeatureSelector<fromAuth.AuthState>(
  fromAuth.authFeatureKey
);

export const selectUser = createSelector(
  selectAuthState,
  (state: fromAuth.AuthState): User => state.user
);

export const selectUsername = createSelector(
  selectAuthState,
  (state: fromAuth.AuthState): string => state.user.username
);

export const selectToken = createSelector(
  selectAuthState,
  (state: fromAuth.AuthState): string => state.token
);

export const selectIsAdmin = createSelector(
  selectAuthState,
  (state: fromAuth.AuthState): boolean => state.isAdmin
);

export const selectAuthError = createSelector(
  selectAuthState,
  (state: fromAuth.AuthState): string => state.error
);

export const selectNotificationMsg = createSelector(
  selectAuthState,
  (state: fromAuth.AuthState): NotificationMessage => state.notificationMsg
);
