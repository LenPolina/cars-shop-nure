import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared/shared.module';
import { ErrorPaymentComponent } from './error-payment.component';
import { ERROR_PAYMENT_ROUTE } from './error-payment.route';

@NgModule({
  imports: [SharedModule, RouterModule.forChild([ERROR_PAYMENT_ROUTE])],
  declarations: [ErrorPaymentComponent],
})
export class ErrorPaymentModule {}
