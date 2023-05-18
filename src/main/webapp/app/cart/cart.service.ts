import { Injectable } from '@angular/core';
import { ICart } from './cart.model';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { ApplicationConfigService } from '../core/config/application-config.service';
import { Observable } from 'rxjs';
import jwt_decode from 'jwt-decode';
import { ICar } from '../entities/car/car.model';

@Injectable({ providedIn: 'root' })
export class CartService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('cart');
  cart: ICart = { id: null, username: 'user', carId: 1 };

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  // @ts-ignore
  public getItems(): Observable<List<ICart>> {
    let token = localStorage.getItem('jhi-authenticationToken');

    if (token) {
      let decodeToken = this.getDecodedAccessToken(token);
      let username = decodeToken.sub;
      return this.http.post<ICart>(`${this.resourceUrl}/getCars`, username);
    }
  }

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

  deleteCartNote(cart: ICart) {
    return this.http.post(`${this.resourceUrl}/delete/cart`, cart);
  }
  // @ts-ignore
  public add(carId: number) {
    let token = localStorage.getItem('jhi-authenticationToken');

    if (token) {
      let decodeToken = this.getDecodedAccessToken(token);
      let username = decodeToken.sub;
      this.cart = { id: null, username: decodeToken.sub, carId: carId };
      return this.http.post<ICart>(`${this.resourceUrl}/add`, this.cart);
    }
  }
}
