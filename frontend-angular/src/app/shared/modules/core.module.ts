import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { NgxSpinnerModule } from 'ngx-spinner';

import { MdbModule } from './mdb.module';
import { AppRoutingModule } from './app-routing.module';

import { CoreComponent } from '../components/core/core.component';
import { HeaderComponent } from '../components/header/header.component';
import { TopbarComponent } from '../components/topbar/topbar.component';
import { FooterComponent } from '../components/footer/footer.component';

const COMPONENTS = [
  CoreComponent,
  HeaderComponent,
  FooterComponent,
  TopbarComponent,
];

@NgModule({
  declarations: COMPONENTS,
  imports: [MdbModule, RouterModule, AppRoutingModule, NgxSpinnerModule],
  exports: COMPONENTS,
})
export class CoreModule {}
