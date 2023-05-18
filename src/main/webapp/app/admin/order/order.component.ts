import { Component, OnInit } from '@angular/core';
import { CarService } from '../../entities/car/service/car.service';
import { ActivatedRoute } from '@angular/router';
import { PriceService } from '../../entities/car/service/price.servic';
import { OrderService } from '../../cart/payment/order.service';
import { IOrder } from '../../cart/payment/order.model';

@Component({
  selector: 'jhi-order',
  templateUrl: './order.component.html',
  styleUrls: ['./order.component.scss'],
})
export class OrderComponent implements OnInit {
  orders: IOrder[] | null = [];
  constructor(
    protected carService: CarService,
    protected activatedRoute: ActivatedRoute,
    protected priceService: PriceService,
    private orderService: OrderService
  ) {}

  ngOnInit(): void {
    this.loadInfo();
  }

  private loadInfo() {
    this.orderService.getAll().subscribe(res => {
      this.orders = res.body;
      console.log('this.orders ' + this.orders);
    });
  }

  refresh() {
    this.loadInfo();
  }

  getBadgeClass(up: string | null) {
    if (up === 'ORDERED') {
      return 'bg-warning';
    }
    if (up === 'ON_THE_WAY') {
      return 'bg-info';
    }
    if (up === 'DELIVERED') {
      return 'bg-success';
    }
    return 'bg-danger';
  }

  getDate(orderDate: Date | undefined) {
    if (orderDate) {
      console.log(orderDate);
      return orderDate;
    }
    return orderDate;
  }
}
