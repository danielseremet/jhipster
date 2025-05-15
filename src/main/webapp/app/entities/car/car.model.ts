export interface ICar {
  id: number;
  brand?: string | null;
  model?: string | null;
  year?: number | null;
  price?: number | null;
  mileage?: number | null;
  color?: string | null;
}

export type NewCar = Omit<ICar, 'id'> & { id: null };
