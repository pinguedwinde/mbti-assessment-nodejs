import { Store, select } from '@ngrx/store';
import { Injectable } from '@angular/core';
import {
  CanActivate,
  Router,
  ActivatedRouteSnapshot,
  RouterStateSnapshot,
} from '@angular/router';

import { AuthState } from '@mbti-app/features/auth/shared/state/auth.reducer';
import * as AuthSelectors from '@mbti-app/features/auth/shared/state/auth.selectors';

@Injectable({ providedIn: 'root' })
export class AdminGuard implements CanActivate {
  constructor(private store: Store<AuthState>, private router: Router) {}

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): boolean {
    return this.isAdmin();
  }

  private isAdmin(): boolean {
    let isAdmin: boolean = false;
    this.store
      .pipe(select(AuthSelectors.selectIsAdmin))
      .subscribe((value: boolean) => (isAdmin = value));

    if (isAdmin) {
      return true;
    }
    return false;
  }
}
