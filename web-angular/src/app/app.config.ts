import { ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { BASE_PATH } from '@monorepo-api';
import { environment } from '../environments/environment';
import { provideHttpClient } from '@angular/common/http';
import { ENVIRONMENT } from '../environments';

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    { provide: ENVIRONMENT, useValue: environment },

    provideHttpClient(),

    {
      provide: BASE_PATH,
      useValue: environment.serverBase,
    },
  ],
};
