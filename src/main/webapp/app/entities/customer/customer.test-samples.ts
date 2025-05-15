import { ICustomer, NewCustomer } from './customer.model';

export const sampleWithRequiredData: ICustomer = {
  id: 16447,
  firstName: 'Telly',
  lastName: 'Koss',
  email: 'Hailie44@hotmail.com',
};

export const sampleWithPartialData: ICustomer = {
  id: 11520,
  firstName: 'Tod',
  lastName: 'Bartell',
  email: 'Kris.Murphy@gmail.com',
};

export const sampleWithFullData: ICustomer = {
  id: 7862,
  firstName: 'Alaina',
  lastName: 'Christiansen',
  email: 'Fae41@gmail.com',
  phoneNumber: 'windy selfishly',
  address: 'duh integration functionality',
};

export const sampleWithNewData: NewCustomer = {
  firstName: 'Golda',
  lastName: 'Block',
  email: 'Andres.Robel@yahoo.com',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
