import { Component, OnInit } from '@angular/core';
import { Store, select } from '@ngrx/store';
import { Observable } from 'rxjs';
import { Personality } from '@mbti-app/shared/models/personality.model';
import { MbtiState } from '@mbti-app/features/mbti/state/mbti.reducer';
import * as MbtiSelectors from '@mbti-app/features/mbti/state/mbti.selectors';

@Component({
  selector: 'app-personality-info',
  templateUrl: './personality-info.component.html',
  styleUrls: ['./personality-info.component.scss'],
})
export class PersonalityInfoComponent implements OnInit {
  public personality$: Observable<Personality>;
  public takenDate$: Observable<Date>;

  constructor(private store: Store<MbtiState>) {}

  ngOnInit(): void {
    this.personality$ = this.store.pipe(
      select(MbtiSelectors.selectPersonality)
    );
    this.takenDate$ = this.store.pipe(select(MbtiSelectors.selectTakenDate));
  }
}
