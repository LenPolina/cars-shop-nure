import { Injectable } from '@angular/core';

import { HttpClient, HttpResponse } from '@angular/common/http';

import { Observable } from 'rxjs';
import jwt_decode from 'jwt-decode';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { IOrder } from './order.model';

@Injectable({ providedIn: 'root' })
export class OrderService {
  protected resourceUrl = 'http://localhost:9000/order';

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  // @ts-ignore
  /* public getItems(): Observable<List<ICart>> {
    let token = localStorage.getItem("jhi-authenticationToken")

    if (token) {
      let decodeToken = this.getDecodedAccessToken(token)
      let username = decodeToken.sub
      return this.http.post<ICart>(`${this.resourceUrl}/getCars`, username);
    }
  }*/

  getDecodedAccessToken(token: string): any {
    try {
      return jwt_decode(token);
    } catch (Error) {
      return null;
    }
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/delete/${id}`, { observe: 'response' });
  }

  public add(order: IOrder): Observable<IOrder> {
    let token = localStorage.getItem('jhi-authenticationToken');

    if (token) {
      let decodeToken = this.getDecodedAccessToken(token);
      let username = decodeToken.sub;
      console.log('username' + username);
      order.user = username;
      return this.http.post<IOrder>(`${this.resourceUrl}/add`, order);
    } else return new Observable<IOrder>();
  }

  public get(orderId: number): Observable<IOrder> {
    let token = localStorage.getItem('jhi-authenticationToken');

    if (token) {
      let decodeToken = this.getDecodedAccessToken(token);
      let username = decodeToken.sub;
      console.log('username' + username);
      return this.http.post<IOrder>(`${this.resourceUrl}/get/${orderId}`, username);
    } else return new Observable<IOrder>();
  }

  public getAll(): Observable<HttpResponse<IOrder[]>> {
    return this.http.get<IOrder[]>(`${this.resourceUrl}/get`, { observe: 'response' });
  }
}
