import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { Brand } from '../entities/enumerations/brand.model';
import { BodyType } from '../entities/enumerations/body-type.model';
import { GearBoxType } from '../entities/enumerations/gear-box-type.model';
import { NgForm } from '@angular/forms';

@Component({
  selector: 'jhi-home',
  templateUrl: './homenew.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit, OnDestroy {
  brandValues = Object.keys(Brand);
  bodyTypeValues = Object.keys(BodyType);
  gearBoxTypeValues = Object.keys(GearBoxType);
  account: Account | null = null;

  private readonly destroy$ = new Subject<void>();

  constructor(private accountService: AccountService, private router: Router) {}

  ngOnInit(): void {
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => (this.account = account));
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  protected getBrandValue(key: string): String | void {
    if (key !== null && key !== undefined) {
      const indexOfS = Object.keys(Brand).indexOf(key.toString());
      const s = Object.values(Brand)[indexOfS];
      return s;
    }
  }
  protected getBodyValue(key: string): String | void {
    if (key !== null && key !== undefined) {
      const indexOfS = Object.keys(BodyType).indexOf(key.toString());
      const s = Object.values(BodyType)[indexOfS];
      return s;
    }
  }
  protected getGearBoxValue(key: string): String | void {
    if (key !== null && key !== undefined) {
      const indexOfS = Object.keys(GearBoxType).indexOf(key.toString());
      const s = Object.values(GearBoxType)[indexOfS];
      return s;
    }
  }

  onFindCar(findForm: NgForm) {
    console.log(findForm.value);
    //[`/all/?carBrand=${findForm.value.carBrand}&carBody=${findForm.value.carBody}&&minPrice=${findForm.value.minPrice}&&maxPrice=${findForm.value.maxPrice}`]
    //this.router.navigate('all', findForm.value.carBrand);
  }
}
