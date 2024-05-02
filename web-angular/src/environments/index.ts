import { InjectionToken } from '@angular/core';

export interface IEnvironment {
  production: boolean;
  serverBase: string;
}

export const ENVIRONMENT = new InjectionToken<IEnvironment>('environment_token');
