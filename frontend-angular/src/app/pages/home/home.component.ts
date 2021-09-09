import { Component, OnInit } from '@angular/core';
import { AuthService } from '@mbti-app/features/auth/shared/services/auth.service';
import { AuthState } from '@mbti-app/features/auth/shared/state/auth.reducer';
import * as AuthSelectors from '@mbti-app/features/auth/shared/state/auth.selectors';
import { select, Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit {
  public isLoggedIn$: Observable<boolean>;

  constructor(
    private storeAuth: Store<AuthState>,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.isLoggedIn$ = this.storeAuth.pipe(
      select(AuthSelectors.selectToken),
      map((token: string) => this.authService.isUserLoggedIn(token))
    );
  }
}
