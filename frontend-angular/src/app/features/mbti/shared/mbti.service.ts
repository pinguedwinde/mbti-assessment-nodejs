import { Personage } from './../../../shared/models/personage.model';
import { Score } from './../../../shared/models/score.model';
import { Answer } from './../../../shared/models/answer.model';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Personality } from '@mbti-app/shared/models/personality.model';
import { Observable } from 'rxjs';

import { environment } from '@mbti-app-env/environment';
import { Question } from '@mbti-app/shared/models/question.model';

@Injectable({
  providedIn: 'root',
})
export class MbtiService {
  private personalitiesInfoBaseUrl = `${environment.API_URL}/api/personality-infos`;
  private questionsInfoBaseUrl = `${environment.API_URL}/api/questions`;
  private personalityTestBaseUrl = `${environment.API_URL}/api/personality-test`;
  private personagesBaseUrl = `${environment.API_URL}/api/personages`;

  constructor(private http: HttpClient) {}

  public processMbtiTest(username: string, answers: Answer[]): Observable<any> {
    return this.http.post<any>(
      `${this.personalityTestBaseUrl}/${username}`,
      answers
    );
  }

  public getMbtiTest(username: string): Observable<any> {
    return this.http.get<any>(`${this.personalityTestBaseUrl}/${username}`);
  }

  public getQuestionnaire(): Observable<Question[]> {
    return this.http.get<Question[]>(
      `${this.questionsInfoBaseUrl}/questionnaire`
    );
  }

  public getPersonalities(): Observable<Personality[]> {
    return this.http.get<Personality[]>(this.personalitiesInfoBaseUrl);
  }

  public getPersonages(personalityType: string): Observable<Personage[]> {
    return this.http.get<Personage[]>(
      `${this.personagesBaseUrl}/personality-type/${personalityType}`
    );
  }

  public savePersonalitiesToLocalCache(personalities: Personality[]): void {
    localStorage.setItem('personalities', JSON.stringify(personalities));
  }

  public saveScoreToLocalCache(score: Score) {
    localStorage.setItem('score', JSON.stringify(score));
  }

  public saveTakenDateToLocalCache(takenDate: Date) {
    localStorage.setItem('takenDate', JSON.stringify(takenDate));
  }

  public savePersonalityToLocalCache(personality: Personality) {
    localStorage.setItem('personality', JSON.stringify(personality));
  }

  public savePersonagesToLocalCache(personages: Personage[]) {
    localStorage.setItem('personages', JSON.stringify(personages));
  }

  public getScoreFromLocalCache(): Score {
    return JSON.parse(localStorage.getItem('score'));
  }

  public getTakenDateFromLocalCache(takenDate: Date) {
    return JSON.parse(localStorage.getItem('takenDate'));
  }

  public getPersonalityFromLocalCache(): Personality {
    return JSON.parse(localStorage.getItem('personality'));
  }

  public getPersonalitiesFromLocalCache(): Personality[] {
    return JSON.parse(localStorage.getItem('personalities'));
  }

  public getPersonagesFromLocalCache(): Personage[] {
    return JSON.parse(localStorage.getItem('personages'));
  }
}
