import { MdbModule } from '@mbti-app/shared/modules/mdb.module';
import { NgModule } from '@angular/core';
import { ErrorComponent } from './error/error.component';
import { HomeComponent } from './home/home.component';
import { PagesRoutingModule } from './pages-routing.module';

@NgModule({
  declarations: [ErrorComponent, HomeComponent],
  imports: [MdbModule, PagesRoutingModule],
})
export class PagesModule {}
