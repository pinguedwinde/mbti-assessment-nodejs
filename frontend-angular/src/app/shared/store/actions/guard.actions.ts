import { NotificationMessage } from '@mbti-app/shared/models/notification.model';
import { createAction, props } from '@ngrx/store';

export const cannotActivate = createAction(
  '[Guard Class] Cannot Activate Page',
  props<{ notificationMsg: NotificationMessage }>()
);
