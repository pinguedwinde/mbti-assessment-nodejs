import { Params } from '@angular/router';
import {
  RouterReducerState,
  SerializedRouterStateSnapshot,
  StoreRouterConfig,
} from '@ngrx/router-store';
import { CustomSerializer } from './custom-serializer';

export const storeRouterConfig: StoreRouterConfig = {
  stateKey: 'router',
  serializer: CustomSerializer,
};

export const routerFeatureKey = 'router';

export interface RouterStateUrl {
  url: string;
  params: Params;
  queryParams: Params;
}

export const initialState: RouterReducerState<RouterStateUrl> = {
  state: {
    url: '/',
    params: {},
    queryParams: {},
  },
  navigationId: 0,
};
