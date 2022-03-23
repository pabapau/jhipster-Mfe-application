import dayjs from 'dayjs/esm';
import { ISite } from 'app/entities/site/site.model';
import { XProtType } from 'app/entities/enumerations/x-prot-type.model';
import { XRole } from 'app/entities/enumerations/x-role.model';
import { BuildState } from 'app/entities/enumerations/build-state.model';

export interface IXProt {
  id?: number;
  xprotType?: XProtType;
  xRole?: XRole;
  comment?: string | null;
  accessAddress?: string | null;
  accessServicePoint?: number | null;
  creationDate?: dayjs.Dayjs;
  lastUpdated?: dayjs.Dayjs;
  buildState?: BuildState | null;
  buildCount?: number | null;
  buildComment?: string | null;
  onNode?: ISite | null;
}

export class XProt implements IXProt {
  constructor(
    public id?: number,
    public xprotType?: XProtType,
    public xRole?: XRole,
    public comment?: string | null,
    public accessAddress?: string | null,
    public accessServicePoint?: number | null,
    public creationDate?: dayjs.Dayjs,
    public lastUpdated?: dayjs.Dayjs,
    public buildState?: BuildState | null,
    public buildCount?: number | null,
    public buildComment?: string | null,
    public onNode?: ISite | null
  ) {}
}

export function getXProtIdentifier(xProt: IXProt): number | undefined {
  return xProt.id;
}
