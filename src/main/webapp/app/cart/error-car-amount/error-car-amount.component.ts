import { Component, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'jhi-error-car-amount',
  templateUrl: './error-car-amount.component.html',
  styleUrls: ['./error-car-amount.component.scss'],
})
export class ErrorCarAmountComponent implements OnInit {
  constructor(protected activeModal: NgbActiveModal) {}

  ngOnInit(): void {}

  confirm() {
    this.activeModal.dismiss();
  }
}
