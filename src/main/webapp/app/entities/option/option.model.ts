import { ICar } from 'app/entities/car/car.model';

export interface IOption {
  id: number;
  optionName?: string | null;
  car?: Pick<ICar, 'id'> | null;
}

export type NewOption = Omit<IOption, 'id'> & { id: null };
