import { Route } from '@angular/router';
import { PaymentComponent } from './payment.component';

export const ORDER_ROUTE: Route = {
  path: ':id',
  component: PaymentComponent,
  data: {
    pageTitle: 'Order',
  },
};
