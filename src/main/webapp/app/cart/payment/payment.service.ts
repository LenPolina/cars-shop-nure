import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { ApplicationConfigService } from '../../core/config/application-config.service';
import { ICar, NewCar } from '../../entities/car/car.model';
import { Observable } from 'rxjs';
import { EntityResponseType } from '../../entities/car/service/car.service';
import { IPayment } from './payment.mode';

@Injectable({ providedIn: 'root' })
export class PaymentService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('payment');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(payment: IPayment): Observable<HttpResponse<String>> {
    console.log(payment);
    return this.http.post<String>(this.resourceUrl + '/pay', payment, { observe: 'response' });
  }
}
