import { Component, OnInit } from '@angular/core';
import { IOrder } from './order.model';
import { ICar } from '../../entities/car/car.model';
import { CarService } from '../../entities/car/service/car.service';
import { PriceService } from '../../entities/car/service/price.servic';
import { ActivatedRoute } from '@angular/router';
import { OrderService } from './order.service';

@Component({
  selector: 'jhi-payment',
  templateUrl: './payment.component.html',
  styleUrls: ['./payment.component.scss'],
})
export class PaymentComponent implements OnInit {
  public static order: IOrder;
  cars: ICar[] = [];
  constructor(
    protected carService: CarService,
    protected activatedRoute: ActivatedRoute,
    protected priceService: PriceService,
    private orderService: OrderService
  ) {}

  ngOnInit(): void {
    let id: number;
    id = parseInt(<string>this.activatedRoute.snapshot.paramMap.get('id'));

    this.orderService.get(id).subscribe(res => {
      PaymentComponent.order = res;
      console.log(res);
      PaymentComponent.order.cars.forEach(carId => {
        console.log(carId);
        this.carService.find(carId).subscribe(res => {
          if (res.body) {
            console.log('res.body ' + res.body);
            this.priceService.find(carId).subscribe(price => {
              if (res.body) {
                console.log('res.body ' + res.body);
                res.body.carPrice = price.body?.carPrice;
                this.cars.push(res.body);
              }
            });
          }
        });
        console.log('this.cars ' + this.cars);
      });
    });

    console.log('this.cars ' + this.cars);
  }
  public getOrder(): IOrder {
    return PaymentComponent.order;
  }
}
