import { Params, RouterStateSnapshot } from '@angular/router';
import { RouterStateSerializer } from '@ngrx/router-store';
import { RouterStateUrl } from './router-state.config';

export class CustomSerializer implements RouterStateSerializer<RouterStateUrl> {
  serialize(routerState: RouterStateSnapshot): RouterStateUrl {
    const {
      url,
      root: { queryParams },
    } = routerState;

    let route = routerState.root;

    while (route.firstChild) {
      route = route.firstChild;
    }
    const { params } = route;

    return { url, params, queryParams };
  }
}
