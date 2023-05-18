import { Route } from '@angular/router';

import { OrderComponent } from './order.component';

export const ordersRoute: Route = {
  path: '',
  component: OrderComponent,
  data: {
    pageTitle: 'Orders',
  },
};
