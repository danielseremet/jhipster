import dayjs from 'dayjs/esm';

import { ISale, NewSale } from './sale.model';

export const sampleWithRequiredData: ISale = {
  id: 18211,
  saleDate: dayjs('2025-05-14'),
  salePrice: 24877.46,
  quantity: 3118,
};

export const sampleWithPartialData: ISale = {
  id: 2121,
  saleDate: dayjs('2025-05-13'),
  salePrice: 18824.39,
  quantity: 15802,
};

export const sampleWithFullData: ISale = {
  id: 6389,
  saleDate: dayjs('2025-05-13'),
  salePrice: 7356.71,
  quantity: 10520,
};

export const sampleWithNewData: NewSale = {
  saleDate: dayjs('2025-05-13'),
  salePrice: 7017.12,
  quantity: 3559,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
