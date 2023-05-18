import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { Title } from '@angular/platform-browser';
import { ICar } from '../car.model';
import { CarService } from '../service/car.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { OptionService } from '../../option/service/option.service';
import { PriceService } from '../service/price.servic';

@Component({
  templateUrl: './delete.component.html',
  styleUrls: ['./delete.component.css'],
})
export class CarDeleteDialogComponent {
  car?: ICar;
  // @ts-ignore
  private mainTitle: string;
  constructor(
    protected carService: CarService,
    private priceService: PriceService,
    protected activeModal: NgbActiveModal,
    protected optionService: OptionService,
    private title: Title
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
    this.mainTitle = this.title.getTitle();
  }

  confirmDelete(id: number | undefined): void {
    if (id !== undefined)
      this.optionService.deleteAllCarOption(id).subscribe(() => {
        this.carService.delete(id).subscribe(() => {
          this.priceService.delete(id).subscribe(res => console.log('delete price'));
          this.activeModal.close(ITEM_DELETED_EVENT);
        });
      });
  }
}
