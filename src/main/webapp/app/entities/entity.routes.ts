import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'car',
    data: { pageTitle: 'shitApp.car.home.title' },
    loadChildren: () => import('./car/car.routes'),
  },
  {
    path: 'customer',
    data: { pageTitle: 'shitApp.customer.home.title' },
    loadChildren: () => import('./customer/customer.routes'),
  },
  {
    path: 'sale',
    data: { pageTitle: 'shitApp.sale.home.title' },
    loadChildren: () => import('./sale/sale.routes'),
  },
  {
    path: 'employee',
    data: { pageTitle: 'shitApp.employee.home.title' },
    loadChildren: () => import('./employee/employee.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
