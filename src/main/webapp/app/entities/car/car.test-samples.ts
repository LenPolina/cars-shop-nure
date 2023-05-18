import { Brand } from 'app/entities/enumerations/brand.model';
import { BodyType } from 'app/entities/enumerations/body-type.model';
import { GearBoxType } from 'app/entities/enumerations/gear-box-type.model';

import { ICar, NewCar } from './car.model';

export const sampleWithRequiredData: ICar = {
  id: 96848,
  // @ts-ignore
  carBrand: Brand['SMART'],
  carModel: 'Indiana',
  carYear: 1971,
};

export const sampleWithPartialData: ICar = {
  id: 60391,
  carBrand: Brand['TOYOTA'],
  carModel: 'e-tailers web-enabled',
  carImageUrl: 'connecting',
  carYear: 1910,
  carGearboxType: GearBoxType['ROBOTIC'],
};

export const sampleWithFullData: ICar = {
  id: 80533,
  carBrand: Brand['TATA'],
  carModel: 'payment grey',
  carBodyType: BodyType['SUV'],
  carImageUrl: 'Configuration National schemas',
  carYear: 2068,
  carEngineVolume: 14,
  carGearboxType: GearBoxType['AUTOMATIC'],
  carDescription: 'infrastructureXXXXXX',
};

export const sampleWithNewData: NewCar = {
  // @ts-ignore
  carBrand: Brand['SAAB'],
  carModel: 'Illinois real-time',
  carYear: 1938,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
