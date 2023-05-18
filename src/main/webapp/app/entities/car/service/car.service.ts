import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICar, NewCar } from '../car.model';
import { ICart } from '../../../cart/cart.model';

export type PartialUpdateCar = Partial<ICar> & Pick<ICar, 'id'>;

export type EntityResponseType = HttpResponse<ICar>;
export type EntityArrayResponseType = HttpResponse<ICar[]>;

@Injectable({ providedIn: 'root' })
export class CarService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('catalog/cars');
  protected resourceUrlFotCart = this.applicationConfigService.getEndpointFor('http://localhost:8090/cart/add');
  cart: ICart = { id: null, username: 'user', carId: 1 };
  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(car: NewCar): Observable<EntityResponseType> {
    if (car.carDescription === '') {
      car.carDescription = null;
    }
    // @ts-ignore
    if (car.carBodyType === undefined || car.carBodyType.toString() === '') {
      car.carBodyType = null;
    }
    // @ts-ignore
    if (car.carGearboxType === undefined || car.carGearboxType.toString() === '') {
      car.carGearboxType = null;
    }

    return this.http.post<ICar>(this.resourceUrl, car, { observe: 'response' });
  }

  public updateCarImage(imageFile: File, id: number): Observable<ICar> {
    console.log('file');
    console.log(imageFile);
    console.log(id);
    const formData = new FormData();
    formData.append('file', imageFile);
    return this.http.post<ICar>(`${this.resourceUrl}/update/image/${id}`, formData);
  }

  update(car: ICar): Observable<EntityResponseType> {
    if (car.carDescription === '') {
      car.carDescription = null;
    }

    if (!car.carBodyType) {
      car.carBodyType = null;
    }

    // @ts-ignore
    if (car.carGearboxType === undefined || car.carGearboxType.toString() === '') {
      car.carGearboxType = null;
    }
    console.log(car);
    return this.http.put<ICar>(`${this.resourceUrl}/${car.id}`, car, { observe: 'response' });
  }

  partialUpdate(car: PartialUpdateCar): Observable<EntityResponseType> {
    return this.http.patch<ICar>(`${this.resourceUrl}/${this.getCarIdentifier(car)}`, car, { observe: 'response' });
  }

  find(id: number): Observable<HttpResponse<ICar>> {
    console.log('id ' + id);
    return this.http.get<ICar>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(
    req?: any,
    carBrand?: string | undefined,
    carBody?: string | undefined,
    carGearBox?: string | undefined,
    minPrice?: string | undefined,
    maxPrice?: string | undefined
  ): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);

    let params = new HttpParams();
    if (carBrand) params = params.append('carBrand', carBrand);
    if (carBody) params = params.append('carBody', carBody);
    if (carGearBox) params = params.append('gearbox', carGearBox);
    if (minPrice) params = params.append('minPrice', minPrice);
    if (maxPrice) params = params.append('maxPrice', maxPrice);

    return this.http.get<ICar[]>(`${this.resourceUrl}/find`, { observe: 'response', params: params });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCarIdentifier(car: Pick<ICar, 'id'>): number {
    return car.id;
  }

  compareCar(o1: Pick<ICar, 'id'> | null, o2: Pick<ICar, 'id'> | null): boolean {
    return o1 && o2 ? this.getCarIdentifier(o1) === this.getCarIdentifier(o2) : o1 === o2;
  }

  addCarToCollectionIfMissing<Type extends Pick<ICar, 'id'>>(carCollection: Type[], ...carsToCheck: (Type | null | undefined)[]): Type[] {
    const cars: Type[] = carsToCheck.filter(isPresent);
    if (cars.length > 0) {
      const carCollectionIdentifiers = carCollection.map(carItem => this.getCarIdentifier(carItem)!);
      const carsToAdd = cars.filter(carItem => {
        const carIdentifier = this.getCarIdentifier(carItem);
        if (carCollectionIdentifiers.includes(carIdentifier)) {
          return false;
        }
        carCollectionIdentifiers.push(carIdentifier);
        return true;
      });
      return [...carsToAdd, ...carCollection];
    }
    return carCollection;
  }
}
