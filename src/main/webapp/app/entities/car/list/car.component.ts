import { Component, Injectable, OnInit } from '@angular/core';
import { ActivatedRoute, Data, ParamMap, Router } from '@angular/router';
import { combineLatest, filter, Observable, switchMap, tap } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICar } from '../car.model';
import { ASC, DEFAULT_SORT_DATA, DESC, ITEM_DELETED_EVENT, SORT } from 'app/config/navigation.constants';
import { CarService, EntityArrayResponseType } from '../service/car.service';
import { CarDeleteDialogComponent } from '../delete/car-delete-dialog.component';
import { SortService } from 'app/shared/sort/sort.service';
import { Brand } from '../../enumerations/brand.model';
import { BodyType } from '../../enumerations/body-type.model';
import { GearBoxType } from '../../enumerations/gear-box-type.model';
import AOS from 'aos';
import { EditComponent } from '../edit/edit.component';
import { OptionService } from '../../option/service/option.service';
import { CartService } from '../../../cart/cart.service';
import { PriceService } from '../service/price.servic';
import { CarStorageService } from '../service/storage.service';

@Component({
  selector: 'jhi-car',
  templateUrl: './car.component.html',
  styleUrls: ['./car.component.css'],
})
@Injectable({
  providedIn: 'root',
})
export class CarComponent implements OnInit {
  cars?: ICar[];
  isLoading = false;
  static is = false;

  predicate = 'id';
  ascending = true;

  constructor(
    protected carService: CarService,
    protected activatedRoute: ActivatedRoute,
    public router: Router,
    protected sortService: SortService,
    protected modalService: NgbModal,
    private optionService: OptionService,
    protected cartServ: CartService,
    protected priceServ: PriceService,
    private carStorageService: CarStorageService
  ) {}

  trackId = (_index: number, item: ICar): number => this.carService.getCarIdentifier(item);

  ngOnInit(): void {
    let carBrand = <string>this.activatedRoute.snapshot.paramMap.get('carBrand');
    let carBody = <string>this.activatedRoute.snapshot.paramMap.get('carBody');
    let carGearBox = <string>this.activatedRoute.snapshot.paramMap.get('carGearBox');
    let minPrice = <string>this.activatedRoute.snapshot.paramMap.get('minPrice');
    let maxPrice = <string>this.activatedRoute.snapshot.paramMap.get('maxPrice');
    this.load(carBrand, carBody, carGearBox, minPrice, maxPrice);
    AOS.init();
  }

  delete(car: ICar): void {
    const modalRef = this.modalService.open(CarDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.car = car;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        switchMap(() => this.loadFromBackendWithRouteInformations(undefined, undefined, undefined, undefined, undefined))
      )
      .subscribe({
        next: (res: EntityArrayResponseType) => {
          this.onResponseSuccess(res);
        },
      });
  }

  load(carBrand: string, carBody: string, carGearBox: string, minPrice: string, maxPrice: string): void {
    this.loadFromBackendWithRouteInformations(carBrand, carBody, carGearBox, minPrice, maxPrice).subscribe({
      next: (res: EntityArrayResponseType) => {
        this.onResponseSuccess(res);
      },
    });
  }

  /*  navigateToWithComponentValues(): void {
      this.handleNavigation(this.predicate, this.ascending);
    }*/

  protected loadFromBackendWithRouteInformations(
    carBrand: string | undefined,
    carBody: string | undefined,
    carGearBox: string | undefined,
    minPrice: string | undefined,
    maxPrice: string | undefined
  ): Observable<EntityArrayResponseType> {
    return combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data]).pipe(
      tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
      switchMap(() => this.queryBackend(this.predicate, this.ascending, carBrand, carBody, carGearBox, minPrice, maxPrice))
    );
  }

  protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
    const sort = (params.get(SORT) ?? data[DEFAULT_SORT_DATA]).split(',');
    this.predicate = sort[0];
    this.ascending = sort[1] === ASC;
  }

  protected onResponseSuccess(response: EntityArrayResponseType): void {
    const dataFromBody = this.fillComponentAttributesFromResponseBody(response.body);
    this.cars = this.refineData(dataFromBody);
    this.cars.forEach(car => {
      this.priceServ.find(car.id).subscribe(res => {
        car.carPrice = res.body?.carPrice;
      });
      this.carStorageService.find(car.id).subscribe(res => {
        car.carNumber = res.body?.carNumber;
      });
    });
  }

  protected refineData(data: ICar[]): ICar[] {
    return data.sort(this.sortService.startSort(this.predicate, this.ascending ? -1 : 1));
  }

  protected getBrandValue(key: Brand | null | undefined): String | void {
    if (key !== null && key !== undefined) {
      const indexOfS = Object.keys(Brand).indexOf(key.toString());
      const s = Object.values(Brand)[indexOfS];
      return s;
    }
  }

  protected getBodyValue(key: BodyType | null | undefined): String | void {
    if (key !== null && key !== undefined) {
      const indexOfS = Object.keys(BodyType).indexOf(key.toString());
      const s = Object.values(BodyType)[indexOfS];
      return s;
    }
  }

  protected getGearBoxValue(key: GearBoxType | null | undefined): String | void {
    if (key !== null && key !== undefined) {
      const indexOfS = Object.keys(GearBoxType).indexOf(key.toString());
      const s = Object.values(GearBoxType)[indexOfS];
      return s;
    }
  }

  protected fillComponentAttributesFromResponseBody(data: ICar[] | null): ICar[] {
    return data ?? [];
  }

  protected queryBackend(
    predicate?: string,
    ascending?: boolean,
    carBrand?: string | undefined,
    carBody?: string | undefined,
    carGearBox?: string | undefined,
    minPrice?: string | undefined,
    maxPrice?: string | undefined
  ): Observable<EntityArrayResponseType> {
    this.isLoading = true;
    const queryObject = {
      sort: this.getSortQueryParam(predicate, ascending),
    };
    return this.carService.query(queryObject, carBrand, carBody, carGearBox, minPrice, maxPrice).pipe(tap(() => (this.isLoading = false)));
  }

  protected handleNavigation(predicate?: string, ascending?: boolean): void {
    const queryParamsObj = {
      sort: this.getSortQueryParam(predicate, ascending),
    };

    this.router.navigate(['./'], {
      relativeTo: this.activatedRoute,
      queryParams: queryParamsObj,
    });
  }

  protected getSortQueryParam(predicate = this.predicate, ascending = this.ascending): string[] {
    const ascendingQueryParam = ascending ? ASC : DESC;
    if (predicate === '') {
      return [];
    } else {
      return [predicate + ',' + ascendingQueryParam];
    }
  }

  onOpenAddModel() {
    const container = document.getElementById('catalog');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle', 'modal');
    button.setAttribute('data-target', '#addCarModal');
    //this.title.setTitle("Add car");
    // @ts-ignore
    container.appendChild(button);
    button.click();
    // @ts-ignore
    setTimeout(() => document.getElementById('carBrand').focus(), 500);
  }

  //my code
  getImageUrl(imageUrl: string | undefined | null) {
    if (imageUrl === null) {
      return 'https://d24pzayae8hlxp.cloudfront.net/ghost.png';
    }
    return imageUrl;
  }

  public onOpenModel(car: ICar): void {
    const container = document.getElementById('catalog');
    const button = document.createElement('button');

    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle', 'modal');

    button.setAttribute('data-target', '#updateCarModal');
    EditComponent.options = [];
    EditComponent.editCar = car;
    this.optionService.getCarOptions(EditComponent.editCar.id).subscribe(options => {
      let optionsFromCar = options;
      console.log('this.options ' + optionsFromCar);
      optionsFromCar?.forEach(option => {
        EditComponent.options.push({ name: option.toString() });
      });
    });

    //this.title.setTitle("Edit car");
    // @ts-ignore
    setTimeout(() => document.getElementById('edit-car-brand').focus(), 500);
    console.log(document.getElementById('edit-car-brand'));

    // @ts-ignore
    container.appendChild(button);
    button.click();
  }

  addToCart(carId: number) {
    if (carId) {
      // @ts-ignore
      this.cartServ.add(carId).subscribe(res => this.ngOnInit());
    }
  }

  checkCarAmount(carNumber: number | undefined | null) {
    if (carNumber) {
      console.log('carNumber ' + carNumber);
      return !(carNumber > 0);
    } else {
      return true;
    }
  }
}
