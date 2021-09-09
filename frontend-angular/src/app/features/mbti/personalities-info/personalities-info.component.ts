import { MbtiState } from './../state/mbti.reducer';
import { Personality } from '@mbti-app/shared/models/personality.model';
import { Observable } from 'rxjs';
import { Component, OnInit } from '@angular/core';
import { Store, select } from '@ngrx/store';
import * as MbtiActions from '@mbti-app/features/mbti/state/mbti.actions';
import * as MbtiSelectors from '@mbti-app/features/mbti/state/mbti.selectors';
import { map, tap } from 'rxjs/operators';

@Component({
  selector: 'app-personalities-info',
  templateUrl: './personalities-info.component.html',
  styleUrls: ['./personalities-info.component.scss'],
})
export class PersonalitiesInfoComponent implements OnInit {
  array = new Array(16);
  personalities$ = new Observable<Personality[]>();

  constructor(private store: Store<MbtiState>) {}

  ngOnInit(): void {
    this.personalities$ = this.store.pipe(
      select(MbtiSelectors.selectPersonalities),
      map((personalities: Personality[]) => {
        if (personalities === null) {
          this.store.dispatch(MbtiActions.loadMbtiPersonalities());
        } else {
          return personalities;
        }
      })
    );
  }
}
