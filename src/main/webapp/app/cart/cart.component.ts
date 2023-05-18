import { Component, OnInit } from '@angular/core';
import { CartService } from './cart.service';
import { CarService } from '../entities/car/service/car.service';
import { ICart } from './cart.model';
import { ICar } from '../entities/car/car.model';
import { PriceService } from '../entities/car/service/price.servic';
import { NgForm } from '@angular/forms';
import { OrderService } from './payment/order.service';
import { IOrder } from './payment/order.model';
import { PaymentComponent } from './payment/payment.component';
import { Router } from '@angular/router';
import { PaymentService } from './payment/payment.service';
import { IPayment } from './payment/payment.mode';
import { CarStorageService } from '../entities/car/service/storage.service';

@Component({
  selector: 'jhi-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.scss'],
})
export class CartComponent implements OnInit {
  cars: ICar[] = [];
  cart_notes: ICart[] = [];
  total_price: number = 0;

  constructor(
    protected cartServ: CartService,
    protected carServ: CarService,
    protected priceService: PriceService,
    private orderService: OrderService,
    private router: Router,
    private paymentService: PaymentService,
    private storageService: CarStorageService
  ) {}

  ngOnInit(): void {
    this.loadCartInfo();
  }

  deleteFromCart(id: number | undefined) {
    if (id) {
      let cart_id = this.findCartNotesByCarId(id);
      console.log('cart_id' + cart_id);
      this.cartServ.delete(id).subscribe(res => this.loadCartInfo());
    }
  }

  private findCartNotesByCarId(id: number) {
    console.log(id);
    // @ts-ignore
    return this.cart_notes.find(car => car.carId === id).id;
  }

  getImageUrl(imageUrl: string | undefined | null) {
    if (imageUrl === null) {
      return 'https://d24pzayae8hlxp.cloudfront.net/ghost.png';
    }
    return imageUrl;
  }

  private loadCartInfo() {
    this.cars = [];
    this.total_price = 0;
    this.cart_notes = [];
    this.cartServ.getItems().subscribe(items => {
      items.forEach((item: ICart) => {
        this.carServ.find(item.carId).subscribe(car => {
          if (car.body) {
            this.cars.push(car.body);
            this.priceService.find(car.body.id).subscribe(res => {
              if (car.body) {
                car.body.carPrice = res.body?.carPrice;
                if (car.body.carPrice) this.total_price += car.body.carPrice;
              }
            });
          }
        });
      });
      this.cart_notes = items;
      console.log(this.cart_notes);
    });
  }

  handlePayment(paymentForm: NgForm) {
    let carsId: number[] = [];
    this.cars.forEach(car => {
      carsId.push(car.id);
    });
    let order: IOrder = {
      orderTotalPrice: this.total_price,
      address: paymentForm.value.address,
      cars: carsId,
      id: null,
      user: null,
      orderDate: null,
      orderStatus: null,
    };
    // @ts-ignore
    this.orderService.add(order).subscribe(res => {
      PaymentComponent.order = res;
      this.router.navigate(['/order']);
      console.log('paymentForm.value ' + res);
    });

    console.log('paymentForm.value ' + PaymentComponent.order);
  }

  async handlePayPalPayment(paymentForm: NgForm) {
    let carsId: number[] = [];
    this.cars.forEach(car => {
      carsId.push(car.id);
    });
    let order: IOrder = {
      orderTotalPrice: this.total_price,
      address: paymentForm.value.address,
      cars: carsId,
      id: null,
      user: null,
      orderDate: null,
      orderStatus: null,
    };

    this.orderService.add(order).subscribe(async res => {
      PaymentComponent.order = res;
      let payment: IPayment = {
        id: null,
        orderId: PaymentComponent.order.id,
        paymentDate: null,
        paymentStatus: null,
        paymentTotalSum: PaymentComponent.order.orderTotalPrice,
      };

      let amountIsPositive: boolean = await this.checkCarsAmount(carsId);
      console.log('amountIsPositive' + amountIsPositive);
      if (amountIsPositive) {
        console.log('amountIsPositive');

        this.paymentService.create(payment).subscribe(res => {
          // @ts-ignore
          document.location.href = res.body?.substring(res.body?.indexOf(':') + 1);
        });
      } else {
        console.log('!amountIsPositive');
        this.onOpenAddModel();
        this.ngOnInit();
      }
    });
  }

  onOpenAddModel() {
    const container = document.getElementById('cart');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle', 'modal');
    button.setAttribute('data-target', '#amountCarModal');
    //this.title.setTitle("Add car");
    // @ts-ignore
    container.appendChild(button);
    button.click();
  }

  private async checkCarsAmount(carsId: number[]): Promise<boolean> {
    let amountIsPositive = true;
    await carsId.forEach(carId => {
      this.storageService.find(carId).subscribe(res => {
        if (res.body && !(res.body?.carNumber > 0)) {
          console.log('res.body?.carNumber > 0' + !(res.body?.carNumber > 0));
          amountIsPositive = false;
          this.cartServ.getItems().subscribe(items => {
            this.cart_notes = [];
            this.cart_notes = items;
            let cart = this.cart_notes.find(i => i.carId == res.body?.id);
            if (cart && cart.id)
              this.cartServ.delete(cart.id).subscribe(res => {
                this.ngOnInit();
              });
          });
        }
      });
      console.log('amountIsPositive' + amountIsPositive);
      return amountIsPositive;
    });
    return amountIsPositive;
  }
}
