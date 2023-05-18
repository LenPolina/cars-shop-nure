import { IOption, NewOption } from './option.model';

export const sampleWithRequiredData: IOption = {
  id: 71232,
};

export const sampleWithPartialData: IOption = {
  id: 96913,
};

export const sampleWithFullData: IOption = {
  id: 12023,
  optionName: 'Fresh hacking Fresh',
};

export const sampleWithNewData: NewOption = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
