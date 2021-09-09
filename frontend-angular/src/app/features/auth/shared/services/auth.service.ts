import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JwtHelperService } from '@auth0/angular-jwt';

import { User, UserForm } from '@mbti-app/shared/models/user.model';
import { UserCredentials } from '@mbti-app/shared/models/user-credentials.model';

import { environment } from '@mbti-app-env/environment';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private authBaseUrl = `${environment.API_URL}/auth`;
  private token: string;
  private loggedInUsername: string;
  private jwtHelper = new JwtHelperService();

  constructor(private http: HttpClient) {}

  public login(
    userCredentials: UserCredentials
  ): Observable<HttpResponse<User>> {
    return this.http.post<User>(`${this.authBaseUrl}/login`, userCredentials, {
      observe: 'response',
    });
  }

  public register(userForm: UserForm): Observable<User> {
    return this.http.post<User>(`${this.authBaseUrl}/register`, userForm);
  }

  public logOut(): void {
    this.token = null;
    this.loggedInUsername = null;
    localStorage.removeItem('user');
    localStorage.removeItem('token');
    localStorage.removeItem('personality');
    localStorage.removeItem('takenDate');
    localStorage.removeItem('score');
    localStorage.removeItem('personages');
  }

  public saveToken(token: string): void {
    this.token = token;
    localStorage.setItem('token', token);
  }

  public addUserToLocalCache(user: User): void {
    localStorage.setItem('user', JSON.stringify(user));
  }

  public getUserFromLocalCache(): User {
    return JSON.parse(localStorage.getItem('user'));
  }

  public loadToken(): void {
    this.token = localStorage.getItem('token');
  }

  public getToken(): string {
    return this.token;
  }

  public isUserLoggedIn(token: string): boolean {
    if (token != null && token !== '') {
      if (this.jwtHelper.decodeToken(token).sub != (null || '')) {
        if (!this.jwtHelper.isTokenExpired(token)) {
          this.loggedInUsername = this.jwtHelper.decodeToken(token).sub;
          return true;
        }
      }
    } else {
      this.logOut();
      return false;
    }
  }
}
