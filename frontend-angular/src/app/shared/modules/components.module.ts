import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PersonageWidgetComponent } from '../components/personage-widget/personage-widget.component';
import { PaginationComponent } from '../components/pagination/pagination.component';

@NgModule({
  declarations: [PersonageWidgetComponent, PaginationComponent],
  exports: [PersonageWidgetComponent, PaginationComponent],
  imports: [CommonModule],
})
export class ComponentsModule {}
