import { MbtiState } from '@mbti-app/features/mbti/state/mbti.reducer';
import { Store } from '@ngrx/store';
import { Component, ElementRef, OnInit, Renderer2 } from '@angular/core';
import * as MbtiActions from '@mbti-app/features/mbti/state/mbti.actions';

import * as confetti from 'canvas-confetti';

@Component({
  selector: 'app-mbti-test-process-congrats',
  templateUrl: './mbti-test-process-congrats.component.html',
  styleUrls: ['./mbti-test-process-congrats.component.scss'],
})
export class MbtiTestProcessCongratsComponent implements OnInit {
  skew: number = 1;
  constructor(
    private store: Store<MbtiState>,
    private renderer2: Renderer2,
    private elementRef: ElementRef
  ) {}

  ngOnInit(): void {
    this.shoot();
  }

  public shoot(): void {
    const canvas = this.renderer2.createElement('canvas');
    this.renderer2.appendChild(this.elementRef.nativeElement, canvas);
    const myConfetti = confetti.create(canvas, {
      resize: true,
    });

    const duration = 30 * 1000;
    const end = Date.now() + duration;
    const timeLeft = end - Date.now();
    const ticks = this.randomInRange(200, 500 * (timeLeft / duration));
    this.skew = Math.max(0.8, this.skew - 0.001);
    const interval = setInterval(() => {
      if (Date.now() > end) {
        clearInterval(interval);
        this.store.dispatch(MbtiActions.blockCongratsPage());
        return this.renderer2.removeChild(
          this.elementRef.nativeElement,
          canvas
        );
      }

      myConfetti({
        particleCount: 5,
        startVelocity: 10,
        angle: 270,
        spread: 0,
        ticks: ticks,
        shapes: ['square'],
        origin: {
          x: Math.random(),
          y: Math.random() * this.skew - 0.2,
        },
        gravity: this.randomInRange(0.4, 0.6),
        scalar: this.randomInRange(0.4, 1),
        drift: this.randomInRange(-0.4, 0.4),
      });
    }, 20);
  }

  private randomInRange(min: number, max: number) {
    return Math.random() * (max - min) + min;
  }
}
