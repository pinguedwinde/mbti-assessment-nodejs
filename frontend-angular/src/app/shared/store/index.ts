import { ActionReducerMap, MetaReducer } from '@ngrx/store';
import { environment } from '@mbti-app-env/environment';
import * as fromAuth from '@mbti-app/features/auth/shared/state/auth.reducer';
import * as fromRouterStore from '@ngrx/router-store';
import * as fromRouterConfig from './router/router-state.config';
import { RouterReducerState } from '@ngrx/router-store';
import * as fromMbti from '@mbti-app/features/mbti/state/mbti.reducer';

export interface AppState {
  [fromAuth.authFeatureKey]: fromAuth.AuthState;
  [fromRouterConfig.routerFeatureKey]: RouterReducerState<fromRouterConfig.RouterStateUrl>;
  [fromMbti.mbtiFeatureKey]: fromMbti.MbtiState;
}

export const initialState: AppState = {
  [fromAuth.authFeatureKey]: fromAuth.initialState,
  [fromRouterConfig.routerFeatureKey]: fromRouterConfig.initialState,
  [fromMbti.mbtiFeatureKey]: fromMbti.initialState,
};

export const reducers: ActionReducerMap<AppState> = {
  [fromAuth.authFeatureKey]: fromAuth.reducer,
  [fromRouterConfig.routerFeatureKey]: fromRouterStore.routerReducer,
  [fromMbti.mbtiFeatureKey]: fromMbti.reducer,
};

export const metaReducers: MetaReducer<AppState>[] = !environment.production
  ? []
  : [];
