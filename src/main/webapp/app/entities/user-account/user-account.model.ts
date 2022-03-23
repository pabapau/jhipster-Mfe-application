import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { IBusinessUnit } from 'app/entities/business-unit/business-unit.model';
import { ISite } from 'app/entities/site/site.model';
import { AccountType } from 'app/entities/enumerations/account-type.model';

export interface IUserAccount {
  id?: number;
  accountType?: AccountType;
  comment?: string | null;
  creationDate?: dayjs.Dayjs;
  lastUpdated?: dayjs.Dayjs;
  user?: IUser;
  businessUnits?: IBusinessUnit[] | null;
  sites?: ISite[] | null;
}

export class UserAccount implements IUserAccount {
  constructor(
    public id?: number,
    public accountType?: AccountType,
    public comment?: string | null,
    public creationDate?: dayjs.Dayjs,
    public lastUpdated?: dayjs.Dayjs,
    public user?: IUser,
    public businessUnits?: IBusinessUnit[] | null,
    public sites?: ISite[] | null
  ) {}
}

export function getUserAccountIdentifier(userAccount: IUserAccount): number | undefined {
  return userAccount.id;
}
