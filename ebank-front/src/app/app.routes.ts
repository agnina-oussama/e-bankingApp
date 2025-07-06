import { Routes } from '@angular/router';
import {Customers} from './pages/customers/customers';
import {Accounts} from './pages/accounts/accounts';

export const routes: Routes = [
  {path : "customers" , component : Customers},
  {path : "accounts" , component : Accounts},
];
