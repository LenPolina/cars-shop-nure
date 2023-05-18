import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { SharedModule } from 'app/shared/shared.module';
import { ordersRoute } from './orders.route';

@NgModule({
  imports: [SharedModule, RouterModule.forChild([ordersRoute])],
  declarations: [],
})
export class OrdersModule {}
