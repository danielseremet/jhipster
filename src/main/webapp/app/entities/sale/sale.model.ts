import dayjs from 'dayjs/esm';
import { ICar } from 'app/entities/car/car.model';
import { ICustomer } from 'app/entities/customer/customer.model';
import { IEmployee } from 'app/entities/employee/employee.model';

export interface ISale {
  id: number;
  saleDate?: dayjs.Dayjs | null;
  salePrice?: number | null;
  quantity?: number | null;
  car?: Pick<ICar, 'id'> | null;
  customer?: Pick<ICustomer, 'id'> | null;
  employee?: Pick<IEmployee, 'id'> | null;
}

export type NewSale = Omit<ISale, 'id'> & { id: null };
