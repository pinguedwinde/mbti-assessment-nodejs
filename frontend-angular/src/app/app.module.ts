import { RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';

import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { StoreModule } from '@ngrx/store';
import { StoreRouterConnectingModule } from '@ngrx/router-store';
import { EffectsModule } from '@ngrx/effects';
import { StoreDevtoolsModule } from '@ngrx/store-devtools';

import { AppComponent } from './app.component';
import { CoreModule } from './shared/modules/core.module';

import { environment } from '@mbti-app-env/environment';
import { reducers, metaReducers } from './shared/store';
import { SpinnerEffects } from './shared/store/effects/spinner.effects';
import { RouteEffects } from './shared/store/effects/route.effects';
import { NotificationEffects } from './shared/store/effects/notification.effects';

import * as fromRouterConfig from './shared/store/router/router-state.config';
import * as fromAppState from './shared/store/index';
import { AuthInterceptor } from './features/auth/shared/auth.interceptor';

const storeRuntimeChecks = {
  strictStateImmutability: true,
  strictActionImmutability: true,
  strictStateSerializability: true,
  strictActionSerializability: true,
  strictActionWithinNgZone: true,
  strictActionTypeUniqueness: true,
};

const EFFECTS = [SpinnerEffects, RouteEffects, NotificationEffects];

@NgModule({
  declarations: [AppComponent],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    RouterModule,
    CoreModule,
    EffectsModule.forRoot(EFFECTS),
    StoreRouterConnectingModule.forRoot(fromRouterConfig.storeRouterConfig),
    StoreModule.forRoot(reducers, {
      metaReducers,
      runtimeChecks: storeRuntimeChecks,
      initialState: fromAppState.initialState,
    }),
    !environment.production
      ? StoreDevtoolsModule.instrument({
          maxAge: 25,
          logOnly: environment.production,
        })
      : [],
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
