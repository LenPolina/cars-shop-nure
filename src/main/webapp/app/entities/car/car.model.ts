import { Brand } from 'app/entities/enumerations/brand.model';
import { BodyType } from 'app/entities/enumerations/body-type.model';
import { GearBoxType } from 'app/entities/enumerations/gear-box-type.model';

export interface ICar {
  id: number;
  carBrand?: Brand | null;
  carModel?: string | null;
  carBodyType?: BodyType | null;
  carImageUrl?: string | null;
  carYear?: number | null;
  carEngineVolume?: number | null;
  carGearboxType?: GearBoxType | null;
  carDescription?: string | null;
  carPrice?: number | null;
  carNumber?: number | null;
}

export type NewCar = Omit<ICar, 'id'> & { id: null };
