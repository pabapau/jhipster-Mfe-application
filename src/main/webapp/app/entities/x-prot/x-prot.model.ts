import dayjs from 'dayjs/esm';
import { XProttype } from 'app/entities/enumerations/x-prottype.model';
import { Xrole } from 'app/entities/enumerations/xrole.model';
import { Buildstate } from 'app/entities/enumerations/buildstate.model';

export interface IXProt {
  id?: number;
  xprottype?: XProttype;
  xrole?: Xrole;
  comment?: string | null;
  accessAddress?: string | null;
  accessServicePoint?: number | null;
  creationdate?: dayjs.Dayjs;
  lastupdated?: dayjs.Dayjs;
  buildstate?: Buildstate | null;
  buildcount?: number | null;
  buildcomment?: string | null;
}

export class XProt implements IXProt {
  constructor(
    public id?: number,
    public xprottype?: XProttype,
    public xrole?: Xrole,
    public comment?: string | null,
    public accessAddress?: string | null,
    public accessServicePoint?: number | null,
    public creationdate?: dayjs.Dayjs,
    public lastupdated?: dayjs.Dayjs,
    public buildstate?: Buildstate | null,
    public buildcount?: number | null,
    public buildcomment?: string | null
  ) {}
}

export function getXProtIdentifier(xProt: IXProt): number | undefined {
  return xProt.id;
}
