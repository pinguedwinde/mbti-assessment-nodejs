import { Injectable } from '@angular/core';
import {
  HttpClient,
  HttpResponse,
  HttpErrorResponse,
  HttpEvent,
} from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from '@mbti-app/features/auth/shared/services/auth.service';
import { User, UserForm } from '@mbti-app/shared/models/user.model';
import { CustomHttpResponse } from '@mbti-app/shared/models/custom-http-reponse.model';

import { environment } from '@mbti-app-env/environment';

@Injectable({ providedIn: 'root' })
export class UserService {
  apiUserBaseUrl = `${environment.API_URL}/api/users`;
  constructor(private http: HttpClient, private authService: AuthService) {}

  public getUsers(): Observable<User[]> {
    return this.http.get<User[]>(`${this.apiUserBaseUrl}`);
  }

  public addUser(userForm: UserForm): Observable<User> {
    return this.http.post<User>(`${this.apiUserBaseUrl}/add`, userForm);
  }

  public updateUser(userForm: UserForm): Observable<User> {
    return this.http.put<User>(`${this.apiUserBaseUrl}/update`, userForm);
  }

  public resetPassword(email: string): Observable<CustomHttpResponse> {
    return this.http.get<CustomHttpResponse>(
      `${this.apiUserBaseUrl}/reset-password/${email}`
    );
  }

  public updateProfileImage(userForm: UserForm): Observable<HttpEvent<User>> {
    return this.http.put<User>(
      `${this.apiUserBaseUrl}/update/profile/image`,
      userForm,
      { reportProgress: true, observe: 'events' }
    );
  }

  public deleteUser(username: string): Observable<CustomHttpResponse> {
    return this.http.delete<CustomHttpResponse>(
      `${this.apiUserBaseUrl}/delete/${username}`
    );
  }

  public updateUserInLocalCache(user: User): void {
    localStorage.setItem('user', JSON.stringify(user));
  }
}
