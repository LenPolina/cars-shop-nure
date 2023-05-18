import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICar, NewCar } from '../car.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICar for edit and NewCarFormGroupInput for create.
 */
type CarFormGroupInput = ICar | PartialWithRequiredKeyOf<NewCar>;

type CarFormDefaults = Pick<NewCar, 'id'>;

type CarFormGroupContent = {
  id: FormControl<ICar['id'] | NewCar['id']>;
  carBrand: FormControl<ICar['carBrand']>;
  carModel: FormControl<ICar['carModel']>;
  carBodyType: FormControl<ICar['carBodyType']>;
  carImageUrl: FormControl<ICar['carImageUrl']>;
  carYear: FormControl<ICar['carYear']>;
  carEngineVolume: FormControl<ICar['carEngineVolume']>;
  carGearboxType: FormControl<ICar['carGearboxType']>;
  carDescription: FormControl<ICar['carDescription']>;
};

export type CarFormGroup = FormGroup<CarFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CarFormService {
  createCarFormGroup(car: CarFormGroupInput = { id: null }): CarFormGroup {
    const carRawValue = {
      ...this.getFormDefaults(),
      ...car,
    };
    return new FormGroup<CarFormGroupContent>({
      id: new FormControl(
        { value: carRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      carBrand: new FormControl(carRawValue.carBrand, {
        validators: [Validators.required],
      }),
      carModel: new FormControl(carRawValue.carModel, {
        validators: [Validators.required, Validators.minLength(2), Validators.maxLength(40)],
      }),
      carBodyType: new FormControl(carRawValue.carBodyType),
      carImageUrl: new FormControl(carRawValue.carImageUrl),
      carYear: new FormControl(carRawValue.carYear, {
        validators: [Validators.required, Validators.min(1900), Validators.max(2100)],
      }),
      carEngineVolume: new FormControl(carRawValue.carEngineVolume, {
        validators: [Validators.min(0), Validators.max(30.0)],
      }),
      carGearboxType: new FormControl(carRawValue.carGearboxType),
      carDescription: new FormControl(carRawValue.carDescription, {
        validators: [Validators.minLength(20), Validators.maxLength(6000)],
      }),
    });
  }

  getCar(form: CarFormGroup): ICar | NewCar {
    return form.getRawValue() as ICar | NewCar;
  }

  resetForm(form: CarFormGroup, car: CarFormGroupInput): void {
    const carRawValue = { ...this.getFormDefaults(), ...car };
    form.reset(
      {
        ...carRawValue,
        id: { value: carRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CarFormDefaults {
    return {
      id: null,
    };
  }
}
