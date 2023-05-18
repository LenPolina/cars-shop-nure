import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOption, NewOption } from '../option.model';
import { Option } from '../option';
import { ICar } from '../../car/car.model';

export type PartialUpdateOption = Partial<IOption> & Pick<IOption, 'id'>;

export type EntityResponseType = HttpResponse<IOption>;
export type EntityArrayResponseType = HttpResponse<IOption[]>;

@Injectable({ providedIn: 'root' })
export class OptionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('car/options');

  public options: Option[] = [];

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(option: NewOption): Observable<EntityResponseType> {
    return this.http.post<IOption>(this.resourceUrl, option, { observe: 'response' });
  }

  update(option: IOption): Observable<EntityResponseType> {
    return this.http.put<IOption>(`${this.resourceUrl}/${this.getOptionIdentifier(option)}`, option, { observe: 'response' });
  }

  partialUpdate(option: PartialUpdateOption): Observable<EntityResponseType> {
    return this.http.patch<IOption>(`${this.resourceUrl}/${this.getOptionIdentifier(option)}`, option, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IOption>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  deleteAllCarOption(carId: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/car/${carId}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOption[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  getCarOptions(carId: number): Observable<String[]> {
    return this.http.get<String[]>(`${this.resourceUrl}/car/${carId}`);
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getOptionIdentifier(option: Pick<IOption, 'id'>): number {
    return option.id;
  }

  compareOption(o1: Pick<IOption, 'id'> | null, o2: Pick<IOption, 'id'> | null): boolean {
    return o1 && o2 ? this.getOptionIdentifier(o1) === this.getOptionIdentifier(o2) : o1 === o2;
  }

  addOptionToCollectionIfMissing<Type extends Pick<IOption, 'id'>>(
    optionCollection: Type[],
    ...optionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const options: Type[] = optionsToCheck.filter(isPresent);
    if (options.length > 0) {
      const optionCollectionIdentifiers = optionCollection.map(optionItem => this.getOptionIdentifier(optionItem)!);
      const optionsToAdd = options.filter(optionItem => {
        const optionIdentifier = this.getOptionIdentifier(optionItem);
        if (optionCollectionIdentifiers.includes(optionIdentifier)) {
          return false;
        }
        optionCollectionIdentifiers.push(optionIdentifier);
        return true;
      });
      return [...optionsToAdd, ...optionCollection];
    }
    return optionCollection;
  }

  saveAll(opt: string[], car: ICar | null): Observable<String[]> {
    this.options = [];
    if (car !== null)
      opt.forEach(option => {
        this.options.push({ optionName: option, car: car });
      });
    console.log(this.options);
    return this.http.post<String[]>(`${this.resourceUrl}/car`, this.options);
  }
}
