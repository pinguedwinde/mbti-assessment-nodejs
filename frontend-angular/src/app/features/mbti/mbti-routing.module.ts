import { MbtiTestProcessCongratsComponent } from './mbti-test-process-congrats/mbti-test-process-congrats.component';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { PersonalitiesInfoComponent } from './personalities-info/personalities-info.component';
import { TestResultComponent } from './test-result/test-result.component';
import { MbtiTestProcessComponent } from './mbti-test-process/mbti-test-process.component';
import { CongratsGuard } from '@mbti-app/shared/guards/congrats.guard';

const ROUTES: Routes = [
  { path: 'personalities-info', component: PersonalitiesInfoComponent },
  { path: 'personality-test', component: MbtiTestProcessComponent },
  { path: 'test-result', component: TestResultComponent },
  {
    path: 'congrats',
    canActivate: [CongratsGuard],
    component: MbtiTestProcessCongratsComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(ROUTES)],
  exports: [RouterModule],
})
export class MbtiRoutingModule {}
