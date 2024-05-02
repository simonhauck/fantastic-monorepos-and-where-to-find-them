import {ApplicationConfig} from '@angular/core';
import {provideRouter} from '@angular/router';

import {routes} from './app.routes';
import {BASE_PATH} from '@monorepo-api';
import {environment} from "../environments/environment";

export const appConfig: ApplicationConfig = {
  providers: [provideRouter(routes),
    {
      provide: BASE_PATH,
      useValue: environment.serverBase,
    },]
};
