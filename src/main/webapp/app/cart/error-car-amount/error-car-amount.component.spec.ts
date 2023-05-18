import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ErrorCarAmountComponent } from './error-car-amount.component';

describe('ErrorCarAmountComponent', () => {
  let component: ErrorCarAmountComponent;
  let fixture: ComponentFixture<ErrorCarAmountComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ErrorCarAmountComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(ErrorCarAmountComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
