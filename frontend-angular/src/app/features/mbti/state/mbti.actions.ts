import { Score } from './../../../shared/models/score.model';
import { Answer } from './../../../shared/models/answer.model';
import { Personality } from '@mbti-app/shared/models/personality.model';
import { createAction, props } from '@ngrx/store';
import { Personage } from '@mbti-app/shared/models/personage.model';

export const loadMbtiPersonalities = createAction(
  '[Personalities Info] Load Personalities Info'
);

export const loadMbtiPersonalitiesSuccess = createAction(
  '[MBTI Effects] Load Personalities Info Success',
  props<{ personalities: Personality[] }>()
);

export const loadMbtiPersonalitiesFailure = createAction(
  '[MBTI Effects] Load Personalities Info Failure',
  props<{ error: any }>()
);

export const processMbtiTest = createAction(
  '[MBTI Test Process] Process MBTI Test',
  props<{ username: string; answers: Answer[] }>()
);

export const processMbtiTestSuccess = createAction(
  '[MBTI Effects] Process MBTI Test Sucess',
  props<{
    score: Score;
    takenDate: Date;
    personalityInfo: Personality;
  }>()
);

export const processMbtiTestFailure = createAction(
  '[MBTI Effects] Process MBTI Test Failure',
  props<{ error: any }>()
);

export const loadMbtiPersonalityTest = createAction(
  '[Personality Test Result] Load MBTI Result Test',
  props<{ username: string }>()
);

export const loadMbtiPersonalityTestFromUserProfile = createAction(
  '[User Profile] Load MBTI Result Test',
  props<{ username: string }>()
);

export const loadMbtiPersonalityTestSuccess = createAction(
  '[MBTI Effects] Load MBTI Test Result Success',
  props<{
    score: Score;
    takenDate: Date;
    personalityInfo: Personality;
  }>()
);

export const loadMbtiPersonalityTestFailure = createAction(
  '[MBTI Effects] Load MBTI Test Result Failure',
  props<{ error: any }>()
);

export const loadMbtiTestResultPersonages = createAction(
  '[Personality Test Result] Load MBTI Test Result Personages',
  props<{ personalityType: string }>()
);

export const loadMbtiTestResultPersonagesSuccess = createAction(
  '[MBTI Effects] Load MBTI Test Result Personages Success',
  props<{
    personages: Personage[];
  }>()
);

export const loadMbtiTestResultPersonagesFailure = createAction(
  '[MBTI Effects] Load MBTI Test Result Personages Failure',
  props<{ error: any }>()
);

export const blockCongratsPage = createAction(
  '[Congrats Component] Set HasTakenTest To False'
);
