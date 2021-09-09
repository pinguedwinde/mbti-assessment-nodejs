import { map } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { Component, OnInit } from '@angular/core';

import { AuthService } from '@mbti-app/features/auth/shared/services/auth.service';
import * as AuthSelectors from '@mbti-app/features/auth/shared/state/auth.selectors';
import * as AuthActions from '@mbti-app/features/auth/shared/state/auth.actions';
import * as RouterSelectors from '@mbti-app/shared/store/selectors/router.selectors';

import { Store, select } from '@ngrx/store';
import { AuthState } from '@mbti-app/features/auth/shared/state/auth.reducer';
import { RouterStateUrl } from '@mbti-app/shared/store/router/router-state.config';

@Component({
  selector: 'app-topbar',
  templateUrl: './topbar.component.html',
  styleUrls: ['./topbar.component.scss'],
})
export class TopbarComponent implements OnInit {
  public isLoggedIn$: Observable<boolean>;
  public url$: Observable<string>;
  public isHomeUrl$: Observable<boolean>;
  public isLoginUrl$: Observable<boolean>;
  public isRegisterUrl$: Observable<boolean>;
  public isPersonalityTestUrl$: Observable<boolean>;
  public isPersonalitiesInfoUrl$: Observable<boolean>;
  public isProfileUrl$: Observable<boolean>;
  public isTestResultUrl$: Observable<boolean>;

  constructor(
    private storeAuth: Store<AuthState>,
    private storeRouter: Store<RouterStateUrl>,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.isLoggedIn$ = this.storeAuth.pipe(
      select(AuthSelectors.selectToken),
      map((token: string) => this.authService.isUserLoggedIn(token))
    );
    this.url$ = this.storeRouter.pipe(select(RouterSelectors.selectUrl));

    this.isHomeUrl$ = this.url$.pipe(
      map((url: string) => (url === '/home' ? true : false))
    );
    this.isLoginUrl$ = this.url$.pipe(
      map((url: string) => (url === '/auth/login' ? true : false))
    );
    this.isRegisterUrl$ = this.url$.pipe(
      map((url: string) => (url === '/auth/register' ? true : false))
    );
    this.isPersonalityTestUrl$ = this.url$.pipe(
      map((url: string) => (url === '/mbti/personality-test' ? true : false))
    );
    this.isPersonalitiesInfoUrl$ = this.url$.pipe(
      map((url: string) => (url === '/mbti/personalities-info' ? true : false))
    );
    this.isTestResultUrl$ = this.url$.pipe(
      map((url: string) => (url === '/mbti/test-result' ? true : false))
    );
    this.isProfileUrl$ = this.url$.pipe(
      map((url: string) => (url === '/user/profile' ? true : false))
    );
  }

  public onLogout(): void {
    const notificationMsg = {
      header: 'Logout Notification',
      body: `Successfully logged out.
          See you !`,
    };
    this.storeAuth.dispatch(AuthActions.logout({ notificationMsg }));
  }
}
