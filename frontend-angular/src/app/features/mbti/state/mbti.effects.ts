import { Store } from '@ngrx/store';
import { Personage } from './../../../shared/models/personage.model';
import { MbtiService } from './../shared/mbti.service';
import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { catchError, map, concatMap, withLatestFrom } from 'rxjs/operators';
import { Observable, EMPTY, of } from 'rxjs';

import * as MbtiActions from './mbti.actions';
import { Personality } from '@mbti-app/shared/models/personality.model';
import { Score } from '@mbti-app/shared/models/score.model';
import { HttpErrorResponse } from '@angular/common/http';
import { MbtiState } from './mbti.reducer';
import { Router } from '@angular/router';

@Injectable()
export class MbtiEffects {
  loadMbtiPersonalities$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(MbtiActions.loadMbtiPersonalities),
      concatMap(() =>
        this.mbtiService.getPersonalities().pipe(
          map((personalities: Personality[]) => {
            const filteredPersonalities: Personality[] = personalities.reduce(
              (accumulator, currentValue) => {
                return accumulator.find(
                  (p) => p.profile == currentValue.profile
                )
                  ? accumulator
                  : [...accumulator, currentValue];
              },
              []
            );
            this.mbtiService.savePersonalitiesToLocalCache(
              filteredPersonalities
            );
            return MbtiActions.loadMbtiPersonalitiesSuccess({
              personalities: filteredPersonalities,
            });
          }),
          catchError((errorResponse: HttpErrorResponse) => {
            let message: string;
            if (errorResponse.error.message) {
              message = errorResponse.error.message;
            } else if (errorResponse.error.messages) {
              message = errorResponse.error.messages.join('-');
            } else {
              message = 'An error occurred. Please try again.';
            }
            return of(
              MbtiActions.loadMbtiPersonalitiesFailure({
                error: message,
              })
            );
          })
        )
      )
    );
  });

  processMbtiTest$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(MbtiActions.processMbtiTest),
      concatMap((action) =>
        this.mbtiService.processMbtiTest(action.username, action.answers).pipe(
          map(this.processMbtiTestResult),
          catchError((errorResponse: HttpErrorResponse) => {
            let message: string;
            if (errorResponse.error.message) {
              message = errorResponse.error.message;
            } else if (errorResponse.error.messages) {
              message = errorResponse.error.messages.join('-');
            } else {
              message = 'An error occurred. Please try again.';
            }
            return of(
              MbtiActions.processMbtiTestFailure({
                error: message,
              })
            );
          })
        )
      )
    );
  });

  loadMbtiPersonalityTest$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(
        MbtiActions.loadMbtiPersonalityTest,
        MbtiActions.loadMbtiPersonalityTestFromUserProfile
      ),
      concatMap((action) =>
        this.mbtiService.getMbtiTest(action.username).pipe(
          map(this.loadMbtiPersonalityTest),
          catchError((errorResponse: HttpErrorResponse) => {
            console.log(errorResponse);

            let message: string;
            if (errorResponse.error) {
              if (errorResponse.error.message) {
                message = errorResponse.error.message;
              } else if (errorResponse.error.messages) {
                message = errorResponse.error.messages.join('-');
              }
            } else {
              message = 'An error occurred. Please try again.';
            }
            return of(
              MbtiActions.loadMbtiPersonalityTestFailure({
                error: message,
              })
            );
          })
        )
      )
    );
  });

  loadMbtiTestResultPersonages$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(MbtiActions.loadMbtiTestResultPersonages),
      concatMap((action) =>
        this.mbtiService.getPersonages(action.personalityType).pipe(
          map((personages: Personage[]) => {
            this.mbtiService.savePersonagesToLocalCache(personages);
            return MbtiActions.loadMbtiTestResultPersonagesSuccess({
              personages,
            });
          }),
          catchError((errorResponse: HttpErrorResponse) => {
            let message: string;
            if (errorResponse.error.message) {
              message = errorResponse.error.message;
            } else if (errorResponse.error.messages) {
              message = errorResponse.error.messages.join('-');
            } else {
              message = 'An error occurred. Please try again.';
            }
            return of(
              MbtiActions.loadMbtiTestResultPersonagesFailure({
                error: message,
              })
            );
          })
        )
      )
    );
  });

  constructor(
    private store: Store<MbtiState>,
    private actions$: Actions,
    private mbtiService: MbtiService,
    private route: Router
  ) {}

  private savePersonalityTestResult(personalityTestResult: any) {
    const score: Score = personalityTestResult.score;
    const takenDate: Date = personalityTestResult.takenDate;
    const personalityInfo: Personality =
      personalityTestResult.personalityInfoDTO;

    this.mbtiService.saveScoreToLocalCache(score);
    this.mbtiService.saveTakenDateToLocalCache(takenDate);
    this.mbtiService.savePersonalityToLocalCache(personalityInfo);

    this.store.dispatch(
      MbtiActions.loadMbtiTestResultPersonages({
        personalityType: personalityInfo.personalityType,
      })
    );
  }

  private loadMbtiPersonalityTest = (personalityTestResult: any) => {
    this.savePersonalityTestResult(personalityTestResult);
    const score: Score = personalityTestResult.score;
    const takenDate: Date = personalityTestResult.takenDate;
    const personalityInfo: Personality =
      personalityTestResult.personalityInfoDTO;
    return MbtiActions.loadMbtiPersonalityTestSuccess({
      score,
      takenDate,
      personalityInfo,
    });
  };

  private processMbtiTestResult = (personalityTestResult: any) => {
    this.savePersonalityTestResult(personalityTestResult);
    const score: Score = personalityTestResult.score;
    const takenDate: Date = personalityTestResult.takenDate;
    const personalityInfo: Personality =
      personalityTestResult.personalityInfoDTO;
    setTimeout(() => {
      this.route.navigate(['/mbti/congrats']);
    }, 500);
    return MbtiActions.processMbtiTestSuccess({
      score,
      takenDate,
      personalityInfo,
    });
  };
}
