import { ICar, NewCar } from './car.model';

export const sampleWithRequiredData: ICar = {
  id: 30070,
  brand: 'exhausted',
  model: 'feline sweet',
  year: 21630,
  price: 10418.92,
};

export const sampleWithPartialData: ICar = {
  id: 8708,
  brand: 'sonnet',
  model: 'unlike',
  year: 18583,
  price: 17997.67,
  mileage: 14343,
};

export const sampleWithFullData: ICar = {
  id: 20895,
  brand: 'nor outgoing',
  model: 'ouch before obediently',
  year: 24737,
  price: 27374.43,
  mileage: 6004,
  color: 'black',
};

export const sampleWithNewData: NewCar = {
  brand: 'beyond',
  model: 'impure at',
  year: 6742,
  price: 26778.48,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
