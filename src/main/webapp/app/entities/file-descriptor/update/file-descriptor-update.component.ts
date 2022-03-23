import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IFileDescriptor, FileDescriptor } from '../file-descriptor.model';
import { FileDescriptorService } from '../service/file-descriptor.service';
import { IFlow } from 'app/entities/flow/flow.model';
import { FlowService } from 'app/entities/flow/service/flow.service';
import { FlowUseCase } from 'app/entities/enumerations/flow-use-case.model';
import { BuildState } from 'app/entities/enumerations/build-state.model';

@Component({
  selector: 'jhi-file-descriptor-update',
  templateUrl: './file-descriptor-update.component.html',
})
export class FileDescriptorUpdateComponent implements OnInit {
  isSaving = false;
  flowUseCaseValues = Object.keys(FlowUseCase);
  buildStateValues = Object.keys(BuildState);

  flowsCollection: IFlow[] = [];

  editForm = this.fb.group({
    id: [],
    fileIdent: [null, [Validators.required]],
    flowUseCase: [null, [Validators.required]],
    description: [],
    creationDate: [null, [Validators.required]],
    lastUpdated: [null, [Validators.required]],
    buildState: [],
    buildCount: [],
    buildComment: [],
    flow: [],
  });

  constructor(
    protected fileDescriptorService: FileDescriptorService,
    protected flowService: FlowService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fileDescriptor }) => {
      this.updateForm(fileDescriptor);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const fileDescriptor = this.createFromForm();
    if (fileDescriptor.id !== undefined) {
      this.subscribeToSaveResponse(this.fileDescriptorService.update(fileDescriptor));
    } else {
      this.subscribeToSaveResponse(this.fileDescriptorService.create(fileDescriptor));
    }
  }

  trackFlowById(index: number, item: IFlow): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFileDescriptor>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(fileDescriptor: IFileDescriptor): void {
    this.editForm.patchValue({
      id: fileDescriptor.id,
      fileIdent: fileDescriptor.fileIdent,
      flowUseCase: fileDescriptor.flowUseCase,
      description: fileDescriptor.description,
      creationDate: fileDescriptor.creationDate,
      lastUpdated: fileDescriptor.lastUpdated,
      buildState: fileDescriptor.buildState,
      buildCount: fileDescriptor.buildCount,
      buildComment: fileDescriptor.buildComment,
      flow: fileDescriptor.flow,
    });

    this.flowsCollection = this.flowService.addFlowToCollectionIfMissing(this.flowsCollection, fileDescriptor.flow);
  }

  protected loadRelationshipsOptions(): void {
    this.flowService
      .query({ filter: 'filedescriptor-is-null' })
      .pipe(map((res: HttpResponse<IFlow[]>) => res.body ?? []))
      .pipe(map((flows: IFlow[]) => this.flowService.addFlowToCollectionIfMissing(flows, this.editForm.get('flow')!.value)))
      .subscribe((flows: IFlow[]) => (this.flowsCollection = flows));
  }

  protected createFromForm(): IFileDescriptor {
    return {
      ...new FileDescriptor(),
      id: this.editForm.get(['id'])!.value,
      fileIdent: this.editForm.get(['fileIdent'])!.value,
      flowUseCase: this.editForm.get(['flowUseCase'])!.value,
      description: this.editForm.get(['description'])!.value,
      creationDate: this.editForm.get(['creationDate'])!.value,
      lastUpdated: this.editForm.get(['lastUpdated'])!.value,
      buildState: this.editForm.get(['buildState'])!.value,
      buildCount: this.editForm.get(['buildCount'])!.value,
      buildComment: this.editForm.get(['buildComment'])!.value,
      flow: this.editForm.get(['flow'])!.value,
    };
  }
}
