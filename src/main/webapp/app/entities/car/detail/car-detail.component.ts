import { Component, Injectable, OnChanges, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICar } from '../car.model';
import { OptionService } from '../../option/service/option.service';
import { EditComponent } from '../edit/edit.component';
import { CarService } from '../service/car.service';
import { PriceService } from '../service/price.servic';
import { CarStorageService } from '../service/storage.service';

@Component({
  selector: 'jhi-car-detail',
  templateUrl: './car-detail.component.html',
  styleUrls: ['.//car-detail.component.css'],
})
@Injectable({
  providedIn: 'root',
})
export class CarDetailComponent implements OnInit {
  car: ICar | null = null;
  options: String[] | null = null;

  constructor(
    protected activatedRoute: ActivatedRoute,
    private carService: CarService,
    protected optionService: OptionService,
    private priceService: PriceService,
    private storageService: CarStorageService
  ) {}

  ngOnInit(): void {
    let id: number;

    id = parseInt(<string>this.activatedRoute.snapshot.paramMap.get('id'));
    if (id)
      this.carService.find(id).subscribe(car => {
        this.car = car.body;
        if (car.body != null) {
          this.optionService.getCarOptions(car.body.id).subscribe(options => {
            this.options = options;
            console.log('this.options ' + this.options);
          });
          this.storageService.find(car.body.id).subscribe(number => {
            // @ts-ignore
            this.car.carNumber = number.body?.carNumber;
            console.log('number.body?.carNumber ' + number.body?.carNumber);
          });
          this.priceService.find(car.body.id).subscribe(price => {
            // @ts-ignore
            this.car.carPrice = price.body?.carPrice;
            console.log('price.body?.carPrice ' + price.body?.carPrice);
          });
        }
      });
  }

  getImageUrl(imageUrl: string | undefined | null) {
    if (imageUrl === null) {
      return 'https://d24pzayae8hlxp.cloudfront.net/ghost.png';
    }
    return imageUrl;
  }
  previousState(): void {
    window.history.back();
  }
  getEngineVolume() {
    if (this.car?.carEngineVolume == 0) {
      return 'electric';
    }
    return this.car?.carEngineVolume + 'L';
  }
  isPresent(description: any | undefined) {
    return description.toString() === '';
  }
  public onOpenModel(car: ICar, mode: string): void {
    const container = document.getElementById('card');
    const button = document.createElement('button');

    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle', 'modal');

    EditComponent.options = [];
    EditComponent.editCar = car;
    this.optionService.getCarOptions(EditComponent.editCar.id).subscribe(options => {
      let optionsFromCar = options;
      console.log('this.options ' + optionsFromCar);
      optionsFromCar?.forEach(option => {
        EditComponent.options.push({ name: option.toString() });
      });
    });
    button.setAttribute('data-target', '#updateCarModal');

    // @ts-ignore
    container.appendChild(button);
    button.click();
  }
}
