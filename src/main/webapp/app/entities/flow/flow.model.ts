import dayjs from 'dayjs/esm';
import { IBusinessUnit } from 'app/entities/business-unit/business-unit.model';
import { ISite } from 'app/entities/site/site.model';
import { FlowUseCase } from 'app/entities/enumerations/flow-use-case.model';
import { BuildState } from 'app/entities/enumerations/build-state.model';

export interface IFlow {
  id?: number;
  fileIdent?: string;
  flowUseCase?: FlowUseCase;
  description?: string | null;
  creationDate?: dayjs.Dayjs;
  lastUpdated?: dayjs.Dayjs;
  buildState?: BuildState | null;
  buildCount?: number | null;
  buildComment?: string | null;
  businessUnit?: IBusinessUnit;
  origin?: ISite;
  destination?: ISite;
}

export class Flow implements IFlow {
  constructor(
    public id?: number,
    public fileIdent?: string,
    public flowUseCase?: FlowUseCase,
    public description?: string | null,
    public creationDate?: dayjs.Dayjs,
    public lastUpdated?: dayjs.Dayjs,
    public buildState?: BuildState | null,
    public buildCount?: number | null,
    public buildComment?: string | null,
    public businessUnit?: IBusinessUnit,
    public origin?: ISite,
    public destination?: ISite
  ) {}
}

export function getFlowIdentifier(flow: IFlow): number | undefined {
  return flow.id;
}
