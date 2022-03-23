import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IFlow, Flow } from '../flow.model';
import { FlowService } from '../service/flow.service';
import { IFileDescriptor } from 'app/entities/file-descriptor/file-descriptor.model';
import { FileDescriptorService } from 'app/entities/file-descriptor/service/file-descriptor.service';
import { IBusinessUnit } from 'app/entities/business-unit/business-unit.model';
import { BusinessUnitService } from 'app/entities/business-unit/service/business-unit.service';
import { ISite } from 'app/entities/site/site.model';
import { SiteService } from 'app/entities/site/service/site.service';
import { FlowUseCase } from 'app/entities/enumerations/flow-use-case.model';
import { BuildState } from 'app/entities/enumerations/build-state.model';

@Component({
  selector: 'jhi-flow-update',
  templateUrl: './flow-update.component.html',
})
export class FlowUpdateComponent implements OnInit {
  isSaving = false;
  flowUseCaseValues = Object.keys(FlowUseCase);
  buildStateValues = Object.keys(BuildState);

  originFileDescriptorsCollection: IFileDescriptor[] = [];
  destFileDescriptorsCollection: IFileDescriptor[] = [];
  businessUnitsSharedCollection: IBusinessUnit[] = [];
  sitesSharedCollection: ISite[] = [];

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
    originFileDescriptor: [],
    destFileDescriptor: [],
    businessUnit: [null, Validators.required],
    origin: [null, Validators.required],
    destination: [null, Validators.required],
  });

  constructor(
    protected flowService: FlowService,
    protected fileDescriptorService: FileDescriptorService,
    protected businessUnitService: BusinessUnitService,
    protected siteService: SiteService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ flow }) => {
      this.updateForm(flow);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const flow = this.createFromForm();
    if (flow.id !== undefined) {
      this.subscribeToSaveResponse(this.flowService.update(flow));
    } else {
      this.subscribeToSaveResponse(this.flowService.create(flow));
    }
  }

  trackFileDescriptorById(index: number, item: IFileDescriptor): number {
    return item.id!;
  }

  trackBusinessUnitById(index: number, item: IBusinessUnit): number {
    return item.id!;
  }

  trackSiteById(index: number, item: ISite): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFlow>>): void {
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

  protected updateForm(flow: IFlow): void {
    this.editForm.patchValue({
      id: flow.id,
      fileIdent: flow.fileIdent,
      flowUseCase: flow.flowUseCase,
      description: flow.description,
      creationDate: flow.creationDate,
      lastUpdated: flow.lastUpdated,
      buildState: flow.buildState,
      buildCount: flow.buildCount,
      buildComment: flow.buildComment,
      originFileDescriptor: flow.originFileDescriptor,
      destFileDescriptor: flow.destFileDescriptor,
      businessUnit: flow.businessUnit,
      origin: flow.origin,
      destination: flow.destination,
    });

    this.originFileDescriptorsCollection = this.fileDescriptorService.addFileDescriptorToCollectionIfMissing(
      this.originFileDescriptorsCollection,
      flow.originFileDescriptor
    );
    this.destFileDescriptorsCollection = this.fileDescriptorService.addFileDescriptorToCollectionIfMissing(
      this.destFileDescriptorsCollection,
      flow.destFileDescriptor
    );
    this.businessUnitsSharedCollection = this.businessUnitService.addBusinessUnitToCollectionIfMissing(
      this.businessUnitsSharedCollection,
      flow.businessUnit
    );
    this.sitesSharedCollection = this.siteService.addSiteToCollectionIfMissing(this.sitesSharedCollection, flow.origin, flow.destination);
  }

  protected loadRelationshipsOptions(): void {
    this.fileDescriptorService
      .query({ filter: 'issourcefor-is-null' })
      .pipe(map((res: HttpResponse<IFileDescriptor[]>) => res.body ?? []))
      .pipe(
        map((fileDescriptors: IFileDescriptor[]) =>
          this.fileDescriptorService.addFileDescriptorToCollectionIfMissing(
            fileDescriptors,
            this.editForm.get('originFileDescriptor')!.value
          )
        )
      )
      .subscribe((fileDescriptors: IFileDescriptor[]) => (this.originFileDescriptorsCollection = fileDescriptors));

    this.fileDescriptorService
      .query({ filter: 'isdestfor-is-null' })
      .pipe(map((res: HttpResponse<IFileDescriptor[]>) => res.body ?? []))
      .pipe(
        map((fileDescriptors: IFileDescriptor[]) =>
          this.fileDescriptorService.addFileDescriptorToCollectionIfMissing(fileDescriptors, this.editForm.get('destFileDescriptor')!.value)
        )
      )
      .subscribe((fileDescriptors: IFileDescriptor[]) => (this.destFileDescriptorsCollection = fileDescriptors));

    this.businessUnitService
      .query()
      .pipe(map((res: HttpResponse<IBusinessUnit[]>) => res.body ?? []))
      .pipe(
        map((businessUnits: IBusinessUnit[]) =>
          this.businessUnitService.addBusinessUnitToCollectionIfMissing(businessUnits, this.editForm.get('businessUnit')!.value)
        )
      )
      .subscribe((businessUnits: IBusinessUnit[]) => (this.businessUnitsSharedCollection = businessUnits));

    this.siteService
      .query()
      .pipe(map((res: HttpResponse<ISite[]>) => res.body ?? []))
      .pipe(
        map((sites: ISite[]) =>
          this.siteService.addSiteToCollectionIfMissing(sites, this.editForm.get('origin')!.value, this.editForm.get('destination')!.value)
        )
      )
      .subscribe((sites: ISite[]) => (this.sitesSharedCollection = sites));
  }

  protected createFromForm(): IFlow {
    return {
      ...new Flow(),
      id: this.editForm.get(['id'])!.value,
      fileIdent: this.editForm.get(['fileIdent'])!.value,
      flowUseCase: this.editForm.get(['flowUseCase'])!.value,
      description: this.editForm.get(['description'])!.value,
      creationDate: this.editForm.get(['creationDate'])!.value,
      lastUpdated: this.editForm.get(['lastUpdated'])!.value,
      buildState: this.editForm.get(['buildState'])!.value,
      buildCount: this.editForm.get(['buildCount'])!.value,
      buildComment: this.editForm.get(['buildComment'])!.value,
      originFileDescriptor: this.editForm.get(['originFileDescriptor'])!.value,
      destFileDescriptor: this.editForm.get(['destFileDescriptor'])!.value,
      businessUnit: this.editForm.get(['businessUnit'])!.value,
      origin: this.editForm.get(['origin'])!.value,
      destination: this.editForm.get(['destination'])!.value,
    };
  }
}
