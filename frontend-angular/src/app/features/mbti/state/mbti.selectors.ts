import { Personage } from './../../../shared/models/personage.model';
import { Personality } from '@mbti-app/shared/models/personality.model';
import { Score } from '@mbti-app/shared/models/score.model';
import { createFeatureSelector, createSelector } from '@ngrx/store';
import * as fromMbti from './mbti.reducer';

export const selectMbtiState = createFeatureSelector<fromMbti.MbtiState>(
  fromMbti.mbtiFeatureKey
);

export const selectPersonalities = createSelector(
  selectMbtiState,
  (state: fromMbti.MbtiState): Personality[] => state.personalities
);

export const selectError = createSelector(
  selectMbtiState,
  (state: fromMbti.MbtiState): String => state.error
);

export const selectScore = createSelector(
  selectMbtiState,
  (state: fromMbti.MbtiState): Score => state.score
);

export const selectTakenDate = createSelector(
  selectMbtiState,
  (state: fromMbti.MbtiState): Date => state.takenDate
);

export const selectPersonality = createSelector(
  selectMbtiState,
  (state: fromMbti.MbtiState): Personality => state.personality
);

export const selectPersonages = createSelector(
  selectMbtiState,
  (state: fromMbti.MbtiState): Personage[] => state.personages
);

export const selectHasTakenTest = createSelector(
  selectMbtiState,
  (state: fromMbti.MbtiState): boolean => state.hasTakenTest
);
