import { MbtiModule } from './../mbti/mbti.module';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ComponentsModule } from '@mbti-app/shared/modules/components.module';
import { MdbModule } from '@mbti-app/shared/modules/mdb.module';
import { UserEffects } from '@mbti-app/features/user/shared/state/user.effects';
import { EffectsModule } from '@ngrx/effects';
import { ProfileComponent } from './profile/profile.component';
import { UserRoutingModule } from './user-routing.module';

@NgModule({
  declarations: [ProfileComponent],
  imports: [
    FormsModule,
    ReactiveFormsModule,
    MdbModule,
    MbtiModule,
    UserRoutingModule,
    ComponentsModule,
    EffectsModule.forFeature([UserEffects]),
  ],
})
export class UserModule {}
