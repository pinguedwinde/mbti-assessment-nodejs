import {
  HttpInterceptor,
  HttpRequest,
  HttpHandler,
  HttpEvent,
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { select, Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { AuthService } from './services/auth.service';
import { AuthState } from './state/auth.reducer';
import * as AuthSelectors from './state/auth.selectors';

import { environment } from '@mbti-app-env/environment';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  private authBaseUrl = `${environment.API_URL}/auth`;
  private token: string;

  constructor(
    private authService: AuthService,
    private store: Store<AuthState>
  ) {
    this.store
      .pipe(select(AuthSelectors.selectToken))
      .subscribe((token: string) => (this.token = token));
  }

  intercept(
    httpRequest: HttpRequest<any>,
    httpHandler: HttpHandler
  ): Observable<HttpEvent<any>> {
    if (httpRequest.url.includes(`${this.authBaseUrl}/login`)) {
      return httpHandler.handle(httpRequest);
    }
    if (httpRequest.url.includes(`${this.authBaseUrl}/register`)) {
      return httpHandler.handle(httpRequest);
    }
    const request = httpRequest.clone({
      setHeaders: { Authorization: `Bearer ${this.token}` },
    });
    return httpHandler.handle(request);
  }
}
