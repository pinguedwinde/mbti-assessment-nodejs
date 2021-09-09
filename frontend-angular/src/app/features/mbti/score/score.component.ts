import { select, Store } from '@ngrx/store';
import { Observable, of } from 'rxjs';
import { Component, OnInit } from '@angular/core';
import { Score } from '@mbti-app/shared/models/score.model';
import { MbtiState } from '@mbti-app/features/mbti/state/mbti.reducer';
import * as MbtiSelectors from '@mbti-app/features/mbti/state/mbti.selectors';

@Component({
  selector: 'app-score',
  templateUrl: './score.component.html',
  styleUrls: ['./score.component.scss'],
})
export class ScoreComponent implements OnInit {
  public score$: Observable<Score>;

  constructor(private store: Store<MbtiState>) {}

  ngOnInit(): void {
    this.score$ = this.store.pipe(select(MbtiSelectors.selectScore));
  }
}
