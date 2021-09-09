import { Input } from '@angular/core';
import { Component, OnInit } from '@angular/core';

import { environment } from '@mbti-app-env/environment';

@Component({
  selector: 'app-personage-widget',
  templateUrl: './personage-widget.component.html',
  styleUrls: ['./personage-widget.component.scss'],
})
export class PersonageWidgetComponent implements OnInit {
  @Input() public name: string;
  @Input() public about: string;
  public imageUrl: string = `${environment.API_URL}/api/users/profile-image/`;

  constructor() {}

  ngOnInit(): void {}
}
