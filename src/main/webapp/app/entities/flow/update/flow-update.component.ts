import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IFlow, Flow } from '../flow.model';
import { FlowService } from '../service/flow.service';
import { IBusinessunit } from 'app/entities/businessunit/businessunit.model';
import { BusinessunitService } from 'app/entities/businessunit/service/businessunit.service';
import { ISite } from 'app/entities/site/site.model';
import { SiteService } from 'app/entities/site/service/site.service';
import { Flowusecase } from 'app/entities/enumerations/flowusecase.model';
import { Buildstate } from 'app/entities/enumerations/buildstate.model';

@Component({
  selector: 'jhi-flow-update',
  templateUrl: './flow-update.component.html',
})
export class FlowUpdateComponent implements OnInit {
  isSaving = false;
  flowusecaseValues = Object.keys(Flowusecase);
  buildstateValues = Object.keys(Buildstate);

  businessunitsSharedCollection: IBusinessunit[] = [];
  sitesSharedCollection: ISite[] = [];

  editForm = this.fb.group({
    id: [],
    fileIdent: [null, [Validators.required]],
    flowusecase: [null, [Validators.required]],
    description: [],
    creationdate: [null, [Validators.required]],
    lastupdated: [null, [Validators.required]],
    buildstate: [],
    buildcount: [],
    buildcomment: [],
    businessunit: [null, Validators.required],
    origin: [null, Validators.required],
    destination: [null, Validators.required],
  });

  constructor(
    protected flowService: FlowService,
    protected businessunitService: BusinessunitService,
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

  trackBusinessunitById(index: number, item: IBusinessunit): number {
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
      flowusecase: flow.flowusecase,
      description: flow.description,
      creationdate: flow.creationdate,
      lastupdated: flow.lastupdated,
      buildstate: flow.buildstate,
      buildcount: flow.buildcount,
      buildcomment: flow.buildcomment,
      businessunit: flow.businessunit,
      origin: flow.origin,
      destination: flow.destination,
    });

    this.businessunitsSharedCollection = this.businessunitService.addBusinessunitToCollectionIfMissing(
      this.businessunitsSharedCollection,
      flow.businessunit
    );
    this.sitesSharedCollection = this.siteService.addSiteToCollectionIfMissing(this.sitesSharedCollection, flow.origin, flow.destination);
  }

  protected loadRelationshipsOptions(): void {
    this.businessunitService
      .query()
      .pipe(map((res: HttpResponse<IBusinessunit[]>) => res.body ?? []))
      .pipe(
        map((businessunits: IBusinessunit[]) =>
          this.businessunitService.addBusinessunitToCollectionIfMissing(businessunits, this.editForm.get('businessunit')!.value)
        )
      )
      .subscribe((businessunits: IBusinessunit[]) => (this.businessunitsSharedCollection = businessunits));

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
      flowusecase: this.editForm.get(['flowusecase'])!.value,
      description: this.editForm.get(['description'])!.value,
      creationdate: this.editForm.get(['creationdate'])!.value,
      lastupdated: this.editForm.get(['lastupdated'])!.value,
      buildstate: this.editForm.get(['buildstate'])!.value,
      buildcount: this.editForm.get(['buildcount'])!.value,
      buildcomment: this.editForm.get(['buildcomment'])!.value,
      businessunit: this.editForm.get(['businessunit'])!.value,
      origin: this.editForm.get(['origin'])!.value,
      destination: this.editForm.get(['destination'])!.value,
    };
  }
}
