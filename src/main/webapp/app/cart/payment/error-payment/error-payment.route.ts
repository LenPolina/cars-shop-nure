import { Route } from '@angular/router';
import { ErrorPaymentComponent } from './error-payment.component';

export const ERROR_PAYMENT_ROUTE: Route = {
  path: '',
  component: ErrorPaymentComponent,
  data: {
    pageTitle: 'Order',
  },
};
