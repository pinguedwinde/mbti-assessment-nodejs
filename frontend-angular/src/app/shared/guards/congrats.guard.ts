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
import * as MbtiSelectors from '@mbti-app/features/mbti/state/mbti.selectors';
import * as GuardActions from '@mbti-app/shared/store/actions/guard.actions';
import { MbtiState } from '@mbti-app/features/mbti/state/mbti.reducer';

@Injectable({ providedIn: 'root' })
export class CongratsGuard implements CanActivate {
  constructor(private store: Store<MbtiState>, private router: Router) {}

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): boolean {
    return this.hasTakenTest();
  }

  private hasTakenTest(): boolean {
    let hasTakenTest: boolean = false;
    this.store
      .pipe(select(MbtiSelectors.selectHasTakenTest))
      .subscribe((value: boolean) => (hasTakenTest = value));
    console.log(hasTakenTest);

    if (hasTakenTest) {
      return true;
    }
    this.router.navigate(['/home']);
    return false;
  }
}
