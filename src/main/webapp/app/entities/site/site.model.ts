import dayjs from 'dayjs/esm';
import { IBusinessUnit } from 'app/entities/business-unit/business-unit.model';
import { IUserAccount } from 'app/entities/user-account/user-account.model';
import { SiteType } from 'app/entities/enumerations/site-type.model';
import { BuildState } from 'app/entities/enumerations/build-state.model';

export interface ISite {
  id?: number;
  name?: string;
  siteType?: SiteType;
  description?: string | null;
  creationDate?: dayjs.Dayjs;
  lastUpdated?: dayjs.Dayjs;
  buildState?: BuildState | null;
  buildCount?: number | null;
  buildComment?: string | null;
  businessUnit?: IBusinessUnit;
  accountedFors?: IUserAccount[] | null;
}

export class Site implements ISite {
  constructor(
    public id?: number,
    public name?: string,
    public siteType?: SiteType,
    public description?: string | null,
    public creationDate?: dayjs.Dayjs,
    public lastUpdated?: dayjs.Dayjs,
    public buildState?: BuildState | null,
    public buildCount?: number | null,
    public buildComment?: string | null,
    public businessUnit?: IBusinessUnit,
    public accountedFors?: IUserAccount[] | null
  ) {}
}

export function getSiteIdentifier(site: ISite): number | undefined {
  return site.id;
}
