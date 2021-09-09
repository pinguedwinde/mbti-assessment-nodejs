import { Score } from '@mbti-app/shared/models/score.model';
import { Personality } from '@mbti-app/shared/models/personality.model';
import { createReducer, on } from '@ngrx/store';
import * as MbtiActions from './mbti.actions';
import * as AuthActions from '@mbti-app/features/auth/shared/state/auth.actions';
import { Personage } from '@mbti-app/shared/models/personage.model';

export const mbtiFeatureKey = 'mbti';

export interface MbtiState {
  personalities: Personality[];
  score: Score;
  takenDate: Date;
  personality: Personality;
  personages: Personage[];
  hasTakenTest: boolean;
  error: string;
}

export const initialState: MbtiState = {
  personalities: JSON.parse(localStorage.getItem('personalities')),
  score: JSON.parse(localStorage.getItem('score')),
  takenDate: JSON.parse(localStorage.getItem('takenDate')),
  personality: JSON.parse(localStorage.getItem('personality')),
  personages: JSON.parse(localStorage.getItem('personages')),
  hasTakenTest: false,
  error: null,
};

export const reducer = createReducer(
  initialState,

  on(MbtiActions.loadMbtiPersonalitiesSuccess, (state, action) => {
    return {
      ...state,
      personalities: action.personalities,
      error: null,
    };
  }),
  on(MbtiActions.loadMbtiPersonalitiesFailure, (state, action) => {
    return {
      ...state,
      error: action.error,
    };
  }),
  on(
    MbtiActions.processMbtiTestSuccess,
    MbtiActions.loadMbtiPersonalityTestSuccess,
    (state, action) => {
      return {
        ...state,
        personality: action.personalityInfo,
        score: action.score,
        takenDate: action.takenDate,
        error: null,
      };
    }
  ),
  on(MbtiActions.processMbtiTestSuccess, (state, action) => {
    return {
      ...state,
      hasTakenTest: true,
    };
  }),
  on(MbtiActions.blockCongratsPage, (state, action) => {
    return {
      ...state,
      hasTakenTest: false,
    };
  }),
  on(MbtiActions.loadMbtiTestResultPersonagesSuccess, (state, action) => {
    return {
      ...state,
      personages: action.personages,
      error: null,
    };
  }),
  on(
    MbtiActions.processMbtiTestFailure,
    MbtiActions.loadMbtiPersonalityTestFailure,
    MbtiActions.loadMbtiTestResultPersonagesFailure,
    (state, action) => {
      return {
        ...state,
        error: action.error,
      };
    }
  ),

  on(AuthActions.logout, (state) => {
    return {
      ...state,
      score: null,
      takenDate: null,
      personality: null,
      personages: null,
      error: null,
    };
  })
);
