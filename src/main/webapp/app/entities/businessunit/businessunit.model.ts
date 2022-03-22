import { IUseraccount } from 'app/entities/useraccount/useraccount.model';

export interface IBusinessunit {
  id?: number;
  code?: string;
  name?: string;
  description?: string | null;
  useraccounts?: IUseraccount[] | null;
}

export class Businessunit implements IBusinessunit {
  constructor(
    public id?: number,
    public code?: string,
    public name?: string,
    public description?: string | null,
    public useraccounts?: IUseraccount[] | null
  ) {}
}

export function getBusinessunitIdentifier(businessunit: IBusinessunit): number | undefined {
  return businessunit.id;
}
