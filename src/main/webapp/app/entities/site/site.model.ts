import dayjs from 'dayjs/esm';
import { IBusinessunit } from 'app/entities/businessunit/businessunit.model';
import { IUseraccount } from 'app/entities/useraccount/useraccount.model';
import { Sitetype } from 'app/entities/enumerations/sitetype.model';
import { Buildstate } from 'app/entities/enumerations/buildstate.model';

export interface ISite {
  id?: number;
  name?: string;
  sitetype?: Sitetype;
  description?: string | null;
  sitenode?: string;
  creationdate?: dayjs.Dayjs;
  lastupdated?: dayjs.Dayjs;
  buildstate?: Buildstate | null;
  buildcount?: number | null;
  buildcomment?: string | null;
  businessunit?: IBusinessunit;
  accountedfors?: IUseraccount[] | null;
}

export class Site implements ISite {
  constructor(
    public id?: number,
    public name?: string,
    public sitetype?: Sitetype,
    public description?: string | null,
    public sitenode?: string,
    public creationdate?: dayjs.Dayjs,
    public lastupdated?: dayjs.Dayjs,
    public buildstate?: Buildstate | null,
    public buildcount?: number | null,
    public buildcomment?: string | null,
    public businessunit?: IBusinessunit,
    public accountedfors?: IUseraccount[] | null
  ) {}
}

export function getSiteIdentifier(site: ISite): number | undefined {
  return site.id;
}
