import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { IBusinessunit } from 'app/entities/businessunit/businessunit.model';
import { ISite } from 'app/entities/site/site.model';
import { Accounttype } from 'app/entities/enumerations/accounttype.model';

export interface IUseraccount {
  id?: number;
  accounttype?: Accounttype;
  comment?: string | null;
  creationdate?: dayjs.Dayjs;
  lastupdated?: dayjs.Dayjs;
  user?: IUser;
  businessunits?: IBusinessunit[] | null;
  sites?: ISite[] | null;
}

export class Useraccount implements IUseraccount {
  constructor(
    public id?: number,
    public accounttype?: Accounttype,
    public comment?: string | null,
    public creationdate?: dayjs.Dayjs,
    public lastupdated?: dayjs.Dayjs,
    public user?: IUser,
    public businessunits?: IBusinessunit[] | null,
    public sites?: ISite[] | null
  ) {}
}

export function getUseraccountIdentifier(useraccount: IUseraccount): number | undefined {
  return useraccount.id;
}
