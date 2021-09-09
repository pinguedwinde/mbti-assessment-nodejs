import { NotificationMessage } from '@mbti-app/shared/models/notification.model';
import { User, UserForm } from '@mbti-app/shared/models/user.model';
import { createAction, props } from '@ngrx/store';

export const updatePage = createAction(
  '[User Profile Component] Update User',
  props<{ userForm: UserForm }>()
);

export const updateSuccess = createAction(
  '[User Effect] Update User Success',
  props<{ user: User; notificationMsg: NotificationMessage }>()
);

export const updateFailure = createAction(
  '[User Effect] Update User Failure',
  props<{ error: any }>()
);
