import { IEmployee, NewEmployee } from './employee.model';

export const sampleWithRequiredData: IEmployee = {
  id: 16889,
  firstName: 'Margaret',
  lastName: 'Keeling',
  email: 'Ahmed_King89@hotmail.com',
  jobTitle: 'Legacy Metrics Consultant',
};

export const sampleWithPartialData: IEmployee = {
  id: 13608,
  firstName: 'River',
  lastName: 'Zemlak',
  email: 'Mikel_Wilderman82@yahoo.com',
  jobTitle: 'Lead Program Associate',
};

export const sampleWithFullData: IEmployee = {
  id: 531,
  firstName: 'Gino',
  lastName: 'Ward',
  email: 'Ibrahim16@hotmail.com',
  jobTitle: 'National Functionality Supervisor',
};

export const sampleWithNewData: NewEmployee = {
  firstName: 'Daryl',
  lastName: 'Cummerata',
  email: 'Selmer_OKon84@yahoo.com',
  jobTitle: 'Corporate Web Coordinator',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
