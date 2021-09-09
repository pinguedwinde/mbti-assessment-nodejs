import { processMbtiTest } from './../state/mbti.actions';
import { Component, OnInit } from '@angular/core';
import { Store, select } from '@ngrx/store';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

import { Answer } from '@mbti-app/shared/models/answer.model';
import { MbtiService } from '@mbti-app/features/mbti/shared/mbti.service';
import { Question } from '@mbti-app/shared/models/question.model';
import { MbtiState } from '../state/mbti.reducer';
import * as MbtiActions from '@mbti-app/features/mbti/state/mbti.actions';
import * as AuthSelectors from '@mbti-app/features/auth/shared/state/auth.selectors';
import { Observable } from 'rxjs';
import { AuthState } from '@mbti-app/features/auth/shared/state/auth.reducer';

@Component({
  selector: 'app-mbti-test-process',
  templateUrl: './mbti-test-process.component.html',
  styleUrls: ['./mbti-test-process.component.scss'],
})
export class MbtiTestProcessComponent implements OnInit {
  public questionnaire: Question[];
  public displayedQuestions: Question[];
  public questionsPerPage: number = 5;
  public allPages: number;
  public currentPage: number;
  public display = 'd-block';
  public questionnaireForm: FormGroup;
  public controls = {};
  public onMbtiTestProcessError$: Observable<string>;
  public username$: Observable<string>;

  constructor(
    private formBuilder: FormBuilder,
    private mbtiService: MbtiService,
    private store: Store<MbtiState>,
    private storeAuth: Store<AuthState>
  ) {}

  ngOnInit(): void {
    this.fetchQuestions();
    this.questionnaireForm = this.formBuilder.group(this.controls);
    this.username$ = this.storeAuth.pipe(select(AuthSelectors.selectUsername));
  }

  private fetchQuestions(): void {
    this.mbtiService
      .getQuestionnaire()
      .subscribe((questionnaire: Question[]) => {
        this.questionnaire = questionnaire;
        this.initForm();
        this.allPages = this.allPages = Math.ceil(
          this.questionnaire.length / this.questionsPerPage
        );
        this.onPageChange();
      });
  }

  private initForm() {
    this.controls = {};
    this.questionnaire.forEach((question) => {
      this.controls[question.id] = this.formBuilder.control(
        null,
        Validators.required
      );
    });
    this.questionnaireForm = this.formBuilder.group(this.controls);
  }

  public onPageChange(page: number = 1): void {
    this.currentPage = page;
    const startItem = (page - 1) * this.questionsPerPage;
    const endItem = page * this.questionsPerPage;
    this.display = 'd-none';
    setTimeout(() => {
      this.display = 'd-block';
      this.displayedQuestions = this.questionnaire.slice(startItem, endItem);
    }, 100);
  }

  public onSubmitForm() {
    let answers: Answer[] = [];
    for (const [key, value] of Object.entries(this.questionnaireForm.value)) {
      answers.push({
        answer: value?.toString(),
        questionId: key,
      });
    }
    this.username$.subscribe((username: string) =>
      this.store.dispatch(MbtiActions.processMbtiTest({ username, answers }))
    );
  }
}
