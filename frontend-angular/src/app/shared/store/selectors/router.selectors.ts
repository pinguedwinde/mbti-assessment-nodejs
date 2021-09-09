import { Params } from '@angular/router';
import { RouterReducerState } from '@ngrx/router-store';
import { createFeatureSelector, createSelector } from '@ngrx/store';
import {
  routerFeatureKey,
  RouterStateUrl,
} from '../router/router-state.config';

export const selectRouterState =
  createFeatureSelector<RouterReducerState<RouterStateUrl>>(routerFeatureKey);

export const selectState = createSelector(
  selectRouterState,
  (routerState: RouterReducerState<RouterStateUrl>) => routerState.state
);

export const selectUrl = createSelector(
  selectState,
  (state: RouterStateUrl): string => state.url
);

export const selectRouteParams = createSelector(
  selectState,
  (state: RouterStateUrl): Params => state.params
);

export const selectQueryParams = createSelector(
  selectState,
  (state: RouterStateUrl): Params => state.queryParams
);
