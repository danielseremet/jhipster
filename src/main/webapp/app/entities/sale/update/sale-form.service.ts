import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ISale, NewSale } from '../sale.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISale for edit and NewSaleFormGroupInput for create.
 */
type SaleFormGroupInput = ISale | PartialWithRequiredKeyOf<NewSale>;

type SaleFormDefaults = Pick<NewSale, 'id'>;

type SaleFormGroupContent = {
  id: FormControl<ISale['id'] | NewSale['id']>;
  saleDate: FormControl<ISale['saleDate']>;
  salePrice: FormControl<ISale['salePrice']>;
  quantity: FormControl<ISale['quantity']>;
  car: FormControl<ISale['car']>;
  customer: FormControl<ISale['customer']>;
  employee: FormControl<ISale['employee']>;
};

export type SaleFormGroup = FormGroup<SaleFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SaleFormService {
  createSaleFormGroup(sale: SaleFormGroupInput = { id: null }): SaleFormGroup {
    const saleRawValue = {
      ...this.getFormDefaults(),
      ...sale,
    };
    return new FormGroup<SaleFormGroupContent>({
      id: new FormControl(
        { value: saleRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      saleDate: new FormControl(saleRawValue.saleDate, {
        validators: [Validators.required],
      }),
      salePrice: new FormControl(saleRawValue.salePrice, {
        validators: [Validators.required],
      }),
      quantity: new FormControl(saleRawValue.quantity, {
        validators: [Validators.required],
      }),
      car: new FormControl(saleRawValue.car),
      customer: new FormControl(saleRawValue.customer),
      employee: new FormControl(saleRawValue.employee),
    });
  }

  getSale(form: SaleFormGroup): ISale | NewSale {
    return form.getRawValue() as ISale | NewSale;
  }

  resetForm(form: SaleFormGroup, sale: SaleFormGroupInput): void {
    const saleRawValue = { ...this.getFormDefaults(), ...sale };
    form.reset(
      {
        ...saleRawValue,
        id: { value: saleRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): SaleFormDefaults {
    return {
      id: null,
    };
  }
}
