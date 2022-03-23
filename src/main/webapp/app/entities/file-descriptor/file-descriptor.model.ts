import dayjs from 'dayjs/esm';
import { IFlow } from 'app/entities/flow/flow.model';
import { FlowUseCase } from 'app/entities/enumerations/flow-use-case.model';
import { BuildState } from 'app/entities/enumerations/build-state.model';

export interface IFileDescriptor {
  id?: number;
  fileIdent?: string;
  flowUseCase?: FlowUseCase;
  description?: string | null;
  creationDate?: dayjs.Dayjs;
  lastUpdated?: dayjs.Dayjs;
  buildState?: BuildState | null;
  buildCount?: number | null;
  buildComment?: string | null;
  flow?: IFlow | null;
  isSourceFor?: IFlow | null;
  isDestFor?: IFlow | null;
}

export class FileDescriptor implements IFileDescriptor {
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
    public flow?: IFlow | null,
    public isSourceFor?: IFlow | null,
    public isDestFor?: IFlow | null
  ) {}
}

export function getFileDescriptorIdentifier(fileDescriptor: IFileDescriptor): number | undefined {
  return fileDescriptor.id;
}
