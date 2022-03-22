import dayjs from 'dayjs/esm';
import { IBusinessunit } from 'app/entities/businessunit/businessunit.model';
import { ISite } from 'app/entities/site/site.model';
import { Flowusecase } from 'app/entities/enumerations/flowusecase.model';
import { Buildstate } from 'app/entities/enumerations/buildstate.model';

export interface IFlow {
  id?: number;
  fileIdent?: string;
  flowusecase?: Flowusecase;
  description?: string | null;
  creationdate?: dayjs.Dayjs;
  lastupdated?: dayjs.Dayjs;
  buildstate?: Buildstate | null;
  buildcount?: number | null;
  buildcomment?: string | null;
  businessunit?: IBusinessunit;
  origin?: ISite;
  destination?: ISite;
}

export class Flow implements IFlow {
  constructor(
    public id?: number,
    public fileIdent?: string,
    public flowusecase?: Flowusecase,
    public description?: string | null,
    public creationdate?: dayjs.Dayjs,
    public lastupdated?: dayjs.Dayjs,
    public buildstate?: Buildstate | null,
    public buildcount?: number | null,
    public buildcomment?: string | null,
    public businessunit?: IBusinessunit,
    public origin?: ISite,
    public destination?: ISite
  ) {}
}

export function getFlowIdentifier(flow: IFlow): number | undefined {
  return flow.id;
}
