import { Store, select } from '@ngrx/store';
import { Injectable } from '@angular/core';
import {
  CanActivate,
  Router,
  ActivatedRouteSnapshot,
  RouterStateSnapshot,
} from '@angular/router';
import { map } from 'rxjs/operators';

import { AuthState } from '@mbti-app/features/auth/shared/state/auth.reducer';
import { AuthService } from '@mbti-app/features/auth/shared/services/auth.service';
import * as AuthSelectors from '@mbti-app/features/auth/shared/state/auth.selectors';
import * as GuardActions from '@mbti-app/shared/store/actions/guard.actions';

@Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate {
  constructor(
    private store: Store<AuthState>,
    private authService: AuthService,
    private router: Router
  ) {}

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): boolean {
    return this.isUserLoggedIn();
  }

  private isUserLoggedIn(): boolean {
    let isLoggedIn: boolean = false;
    this.store
      .pipe(
        select(AuthSelectors.selectToken),
        map((token: string) => this.authService.isUserLoggedIn(token))
      )
      .subscribe((value: boolean) => (isLoggedIn = value));

    if (isLoggedIn) {
      return true;
    }
    this.router.navigate(['/auth/login']);

    const notificationMsg = {
      header: 'Access Denied',
      body: `You need to log in to access this page`,
    };
    this.store.dispatch(GuardActions.cannotActivate({ notificationMsg }));
    return false;
  }
}
