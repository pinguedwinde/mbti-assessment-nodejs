import { NotificationMessage } from '@mbti-app/shared/models/notification.model';
import { User, UserForm } from '@mbti-app/shared/models/user.model';
import { UserCredentials } from '@mbti-app/shared/models/user-credentials.model';
import { createAction, props } from '@ngrx/store';

export const loginPage = createAction(
  '[Login Component] Login User',
  props<{ userCredentials: UserCredentials }>()
);

export const loginSuccess = createAction(
  '[Auth Effect] Login User Success',
  props<{ user: User; token: string; notificationMsg: NotificationMessage }>()
);

export const loginFailure = createAction(
  '[Auth Effect] Login User Failure',
  props<{ error: any }>()
);

export const loginRoute = createAction(
  '[Spinner Effect] Login User Success Route'
);

export const loginNotification = createAction(
  '[Route Effect] Login User Success Notification'
);

export const logout = createAction(
  '[Topbar Component] Logout User',
  props<{ notificationMsg: NotificationMessage }>()
);

export const browserReload = createAction(
  '[Core Component] Browser Reload',
  props<{ user: User }>()
);

export const registerPage = createAction(
  '[Register Component] Register User',
  props<{ userForm: UserForm }>()
);

export const registerSuccess = createAction(
  '[Auth Effect] Register User Success',
  props<{ notificationMsg: NotificationMessage }>()
);

export const registerFailure = createAction(
  '[Auth Effect] Register User Failure',
  props<{ error: any }>()
);

export const registerRoute = createAction(
  '[Spinner Effect] Register User Success Route'
);

export const registerNotification = createAction(
  '[Route Effect] Register User Success Notification'
);
