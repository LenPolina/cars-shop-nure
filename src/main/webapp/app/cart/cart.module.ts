import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared/shared.module';

import { CartComponent } from './cart.component';
import { CART_ROUTE } from './cart.route';
import { ErrorCarAmountComponent } from './error-car-amount/error-car-amount.component';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@NgModule({
  imports: [SharedModule, RouterModule.forChild([CART_ROUTE])],
  declarations: [CartComponent, ErrorCarAmountComponent],
  providers: [NgbActiveModal],
})
export class CartModule {}
