import { IUserAccount } from 'app/entities/user-account/user-account.model';

export interface IBusinessUnit {
  id?: number;
  code?: string;
  name?: string;
  description?: string | null;
  userAccounts?: IUserAccount[] | null;
}

export class BusinessUnit implements IBusinessUnit {
  constructor(
    public id?: number,
    public code?: string,
    public name?: string,
    public description?: string | null,
    public userAccounts?: IUserAccount[] | null
  ) {}
}

export function getBusinessUnitIdentifier(businessUnit: IBusinessUnit): number | undefined {
  return businessUnit.id;
}
