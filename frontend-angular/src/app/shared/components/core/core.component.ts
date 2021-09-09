import { browserReload } from './../../../features/auth/shared/state/auth.actions';
import { User } from '@mbti-app/shared/models/user.model';
import { AuthService } from './../../../features/auth/shared/services/auth.service';
import { Store } from '@ngrx/store';
import { Component, OnInit } from '@angular/core';

import { AuthState } from '@mbti-app/features/auth/shared/state/auth.reducer';

@Component({
  selector: 'app-core',
  templateUrl: './core.component.html',
  styleUrls: ['./core.component.scss'],
})
export class CoreComponent implements OnInit {
  constructor(
    private store: Store<AuthState>,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    const user: User = this.authService.getUserFromLocalCache();
    if (user) {
      this.store.dispatch(browserReload({ user }));
    }
  }
}
