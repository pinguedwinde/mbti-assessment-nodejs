import { Observable } from 'rxjs';
import { Component, OnInit } from '@angular/core';
import { select, Store } from '@ngrx/store';
import { Personage } from '@mbti-app/shared/models/personage.model';
import { MbtiState } from '@mbti-app/features/mbti/state/mbti.reducer';
import * as MbtiSelectors from '@mbti-app/features/mbti/state/mbti.selectors';
import * as MbtiActions from '@mbti-app/features/mbti/state/mbti.actions';

import { map } from 'rxjs/operators';
import { Score } from '@mbti-app/shared/models/score.model';

@Component({
  selector: 'app-test-result',
  templateUrl: './test-result.component.html',
  styleUrls: ['./test-result.component.scss'],
})
export class TestResultComponent implements OnInit {
  public personages$: Observable<Personage[]>;
  public score$: Observable<Score>;

  constructor(private store: Store<MbtiState>) {}

  ngOnInit(): void {
    this.score$ = this.store.pipe(
      select(MbtiSelectors.selectScore),
      map((score: Score) => {
        if (score === null) {
          const user = JSON.parse(localStorage.getItem('user'));
          if (user) {
            this.store.dispatch(
              MbtiActions.loadMbtiPersonalityTest({ username: user.username })
            );
          }
        }
        return score;
      })
    );
    this.personages$ = this.store.pipe(
      select(MbtiSelectors.selectPersonages),
      map((personages: Personage[]) => {
        if (personages) {
          let copyPersonages = [...personages];
          return copyPersonages.sort(() => 0.5 - Math.random()).slice(0, 5);
        }
        return null;
      })
    );
  }
}
