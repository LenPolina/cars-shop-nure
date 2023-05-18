import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { ICar, NewCar } from '../car.model';
import { IPrice } from '../price.model';

export type EntityResponseType = HttpResponse<ICar>;

@Injectable({ providedIn: 'root' })
export class PriceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('price');
  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(car: ICar): Observable<EntityResponseType> {
    return this.http.post<IPrice>(`${this.resourceUrl}/add`, car, { observe: 'response' });
  }

  update(car: ICar): Observable<EntityResponseType> {
    return this.http.put<IPrice>(`${this.resourceUrl}/edit`, car, { observe: 'response' });
  }

  find(id: number): Observable<HttpResponse<IPrice>> {
    console.log('id ' + id);
    return this.http.get<IPrice>(`${this.resourceUrl}/get/${id}`, { observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/delete/${id}`, { observe: 'response' });
  }
}
