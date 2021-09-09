import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CoreComponent } from '../components/core/core.component';
import { AuthGuard } from '../guards/auth.guard';

const ROUTES: Routes = [
  {
    path: '',
    component: CoreComponent,
    children: [
      {
        path: '',
        loadChildren: () =>
          import('@mbti-app/pages/pages.module').then((m) => m.PagesModule),
      },
      {
        path: 'user',
        canActivate: [AuthGuard],
        loadChildren: () =>
          import('@mbti-app/features/user/user.module').then(
            (m) => m.UserModule
          ),
      },
      {
        path: 'mbti',
        canActivate: [AuthGuard],
        loadChildren: () =>
          import('@mbti-app/features/mbti/mbti.module').then(
            (m) => m.MbtiModule
          ),
      },
      {
        path: 'auth',
        loadChildren: () =>
          import('@mbti-app/features/auth/auth.module').then(
            (m) => m.AuthModule
          ),
      },
    ],
  },
  { path: '**', redirectTo: 'error' },
];

@NgModule({
  imports: [RouterModule.forRoot(ROUTES)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
