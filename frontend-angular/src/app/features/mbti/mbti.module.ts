import { ReactiveFormsModule } from '@angular/forms';
import { ComponentsModule } from './../../shared/modules/components.module';
import { NgModule } from '@angular/core';
import { StoreModule } from '@ngrx/store';
import { EffectsModule } from '@ngrx/effects';
import { MbtiRoutingModule } from './mbti-routing.module';
import { MdbModule } from '@mbti-app/shared/modules/mdb.module';

import { PersonalitiesInfoComponent } from './personalities-info/personalities-info.component';
import * as fromMbti from './state/mbti.reducer';
import { MbtiEffects } from './state/mbti.effects';
import { MbtiTestProcessComponent } from './mbti-test-process/mbti-test-process.component';
import { PersonalityInfoComponent } from './personality-info/personality-info.component';
import { ScoreComponent } from './score/score.component';
import { TestResultComponent } from './test-result/test-result.component';
import { MbtiTestProcessCongratsComponent } from './mbti-test-process-congrats/mbti-test-process-congrats.component';

@NgModule({
  declarations: [
    PersonalitiesInfoComponent,
    MbtiTestProcessComponent,
    ScoreComponent,
    PersonalityInfoComponent,
    TestResultComponent,
    MbtiTestProcessCongratsComponent,
  ],
  imports: [
    ReactiveFormsModule,
    MbtiRoutingModule,
    MdbModule,
    ComponentsModule,
    StoreModule.forFeature(fromMbti.mbtiFeatureKey, fromMbti.reducer),
    EffectsModule.forFeature([MbtiEffects]),
  ],
  exports: [ScoreComponent, PersonalityInfoComponent],
})
export class MbtiModule {}
